package io.objecthub.filesync.internal.engine.convert

import com.appjangle.api.Node
import com.appjangle.api.nodes.Bytes
import com.appjangle.api.nodes.Values
import de.mxro.file.FileItem
import delight.async.AsyncCommon
import delight.async.callbacks.ValueCallback
import io.nextweb.promise.utils.CallbackUtils
import io.objecthub.filesync.Converter
import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import io.objecthub.filesync.internal.engine.FileUtils
import java.util.Base64
import java.util.LinkedList
import java.util.List

import static extension delight.async.AsyncCommon.*

class FileToTextNode implements Converter {
	
	val String id
	val String fileExtension
	val String markerClass
	val String valueReference

	override worksOn(FileItem source) {
		val ext = source.name.getExtension
		println(ext+' == '+fileExtension)
		return ext == fileExtension
	}

	override worksOn(Node node, ValueCallback<Boolean> cb) {

		val qry = node.selectAllLinks

		qry.catchExceptions([er|cb.onFailure(er.exception)])

		qry.get [ links |
			for (link : links) {
				
				if (link.uri() == markerClass) {
					
					cb.onSuccess(true)
					return
				}
			}
			cb.onSuccess(false)
		]

	}

	override createNodes(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {
		// FIXME not working yet
		val nameWithoutExtension = source.name.removeExtension

		val simpleName = nameWithoutExtension.getSimpleName

		val ops = new LinkedList<NetworkOperation>

		ops.add(
			[ ctx, opscb |
			val baseNode = ctx.parent.appendSafe(source.text, "./" + simpleName)
			metadata.add(new ItemMetadata() {

				override name() {
					source.name
				}

				override lastModified() {
					source.lastModified
				}

				override uri() {
					ctx.parent.uri() + "/" + simpleName
				}

				override hash() {
					source.hash
				}

				override converter() {
					id
				}

			})

			val res = newArrayList
			res.add(baseNode)
			// res.add(baseNode.appendLabel(nameWithoutExtension))
			// res.addAll(baseNode.appendTypesAndIcon(source))
			opscb.onSuccess(res)
		])

		cb.onSuccess(ops)
	}

	override update(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {

		val sourceText = source.text

		var Object contentVar 
		
		if (sourceText.startsWith("//BASE64//")) {
			val base64End = "//BASE64//".length
			val mimeTypeEnd = sourceText.indexOf("/", base64End)-1
			val mimeType = sourceText.substring(base64End, mimeTypeEnd)
			val data = sourceText.substring(base64End+mimeType.length+1)
			contentVar = Values.bytes(Base64.decoder.decode(data), mimeType)
		} else {
			contentVar = source.text
		}
		
		val content = contentVar
		val address = metadata.get(source.getName).uri

		val ops = new LinkedList<NetworkOperation>

		ops.add(
			[ ctx, opscb |

			if (valueReference === null) {
				opscb.onSuccess(newArrayList(ctx.session.link(address).setValueSafe(content)))
			} else {
				opscb.onSuccess(
					newArrayList(ctx.session.link(address).selectAsLink(valueReference).setValueSafe(content)))
			}

		])

		cb.onSuccess(ops)

	}

	override deleteNodes(Metadata metadata, ItemMetadata cachedFile, ValueCallback<List<NetworkOperation>> cb) {
		cutils.deleteNodes(metadata, cachedFile, cb)
	}

	override createFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {
		source.getFileExtension(cb.embed(
				[ ext |
			source.getFileName(folder, ext, cb.embed(
							[ rawFileName |
				val fileName = rawFileName.toFileSystemSafeName(false, 100)

				obtainValueNode(source, AsyncCommon.embed(cb, [ node |
					val ops = new LinkedList<FileOperation>
					ops.add(
						[ ctx |
							val file = ctx.folder.createFile(fileName)
							file.text = node.value(String)
							ctx.metadata.add(new ItemMetadata() {

								override name() {
									fileName
								}

								override lastModified() {
									file.lastModified // TODO replace with last modified if available from node !!
								}

								override uri() {
									source.uri()
								}

								override hash() {
									file.hash
								}

								override converter() {
									id
								}

							})
						]
					)
					cb.onSuccess(ops)
				]))

			]))
		]))

	}

	def obtainValueNode(Node source, ValueCallback<Node> cb) {
		if (valueReference === null) {
			cb.onSuccess(source)
			return
		}

		val qry = source.selectAsLink(valueReference)

		qry.catchExceptions(CallbackUtils.asExceptionListener(cb))

		qry.get [ node |
			cb.onSuccess(node)
		]
	}

	override updateFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {

		val fileName = metadata.get(source).name
		
		//println('update file '+fileName)
		
		obtainValueNode(source, AsyncCommon.embed(cb, [ node |
			
			var String contentVar 
			if (node.value() instanceof Bytes) {
				// FIXME This does not yet work for storing byte data - text converter not triggered for such nodes.
				val bytes = (node.value() as Bytes)
				contentVar = "//BASE64//"+bytes.mimeType+"/"+new String(Base64.encoder.encode(bytes.bytes), "UTF-8")
			} else {
				contentVar= node.value(String)
			}
			val content = contentVar
			val ops = new LinkedList<FileOperation>

			ops.add(
			[ ctx |
				val file = ctx.folder.get(fileName)
				if (file.text != content) {

					file.text = content

					ctx.metadata.update(new ItemMetadata() {

						override name() {
							fileName
						}

						override lastModified() {
							file.lastModified // TODO replace with last modified if available from node !!
						}

						override uri() {
							source.uri()
						}

						override hash() {
							file.hash
						}

						override converter() {
							id
						}

					})

				}
			])

			cb.onSuccess(ops)
		]));

	}

	override removeFiles(FileItem folder, Metadata metadata, ItemMetadata item, ValueCallback<List<FileOperation>> cb) {
		val fileName = item.name

		val ops = new LinkedList<FileOperation>

		ops.add(
			[ ctx |
			ctx.folder.deleteFile(fileName)
			ctx.metadata.remove(fileName)
		])

		cb.onSuccess(ops)
	}

	extension ConvertUtils cutils = new ConvertUtils
	extension FileUtils futils = new FileUtils

	new(String id, String fileExtension, String markerClass, String valueReference) {
		this.id = id
		this.fileExtension = fileExtension
		this.markerClass = markerClass
		this.valueReference = valueReference
	}
	
	override id() {
		id
	}

}
