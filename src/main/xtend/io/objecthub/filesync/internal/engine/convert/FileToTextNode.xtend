package io.objecthub.filesync.internal.engine.convert

import com.appjangle.api.Node
import io.objecthub.filesync.Converter
import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import io.objecthub.filesync.internal.engine.FileUtils
import de.mxro.file.FileItem
import delight.async.callbacks.ValueCallback
import java.util.LinkedList
import java.util.List

import static extension delight.async.AsyncCommon.*

class FileToTextNode implements Converter {

	override worksOn(FileItem source) {

		source.name.isTextValue
	}

	override worksOn(Node node, ValueCallback<Boolean> cb) {

		val qry = node.selectAllLinks

		qry.catchExceptions([er|cb.onFailure(er.exception)])

		qry.get [ links |
			for (link : links) {
				if (link.isTextType) {
					cb.onSuccess(true)
					return
				}
			}
			cb.onSuccess(false)
		]

	}

	override createNodes(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {

		val nameWithoutExtension = source.name.removeExtension

		val simpleName = nameWithoutExtension.getSimpleName

		val ops = new LinkedList<NetworkOperation>

		ops.add(
			[ ctx, opscb |
				val baseNode = ctx.parent.appendSafe(source.text, "./" + simpleName)
				metadata.add(
					new ItemMetadata() {

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
							FileToTextNode.this.class.toString
						}

					})
					
					
				val res = newArrayList
				res.add(baseNode)
				res.add(baseNode.appendLabel(nameWithoutExtension))
				res.addAll(baseNode.appendTypesAndIcon(source))
				
				opscb.onSuccess(res )
			])

		cb.onSuccess(ops)
	}

	override update(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {

		val content = source.text

		val address = metadata.get(source.getName).uri

		val ops = new LinkedList<NetworkOperation>

		ops.add(
			[ ctx, opscb |
				opscb.onSuccess(newArrayList(ctx.session.link(address).setValueSafe(content)))
			])

		cb.onSuccess(ops)

	}

	override deleteNodes(Metadata metadata, ItemMetadata cachedFile, ValueCallback<List<NetworkOperation>> cb) {
		cutils.deleteNodes(metadata, cachedFile, cb)
	}

	override createFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {
		source.getFileExtension(
			cb.embed(
				[ ext |
					source.getFileName(folder, ext,
						cb.embed(
							[ rawFileName |
								val fileName = rawFileName.toFileSystemSafeName(false, 100)
								val ops = new LinkedList<FileOperation>
								ops.add(
									[ ctx |
										val file = ctx.folder.createFile(fileName)
										file.text = source.value(String)
										ctx.metadata.add(
											new ItemMetadata() {

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
													FileToTextNode.this.class.toString
												}

											})
									]
								)
								cb.onSuccess(ops)
							]))
				]))

	}

	override updateFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {

		val fileName = metadata.get(source).name

		val content = source.value(String)

		val ops = new LinkedList<FileOperation>

		ops.add(
			[ ctx |
				val file = ctx.folder.get(fileName)
				if (file.text != content) {

					file.text = content

					ctx.metadata.update(
						new ItemMetadata() {

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
								FileToTextNode.this.class.toString
							}

						})

				}
			])

		cb.onSuccess(ops)

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
	

}
