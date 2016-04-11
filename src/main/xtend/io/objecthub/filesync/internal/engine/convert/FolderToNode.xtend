package io.objecthub.filesync.internal.engine.convert

import com.appjangle.api.Node
import io.objecthub.filesync.Converter
import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import io.objecthub.filesync.internal.engine.FileUtils
import io.objecthub.filesync.internal.engine.N
import de.mxro.file.FileItem
import delight.async.callbacks.ValueCallback
import java.util.Date
import java.util.LinkedList
import java.util.List

import static extension delight.async.AsyncCommon.*

class FolderToNode implements Converter {

	override worksOn(FileItem source) {
		source.directory
	}

	override worksOn(Node node, ValueCallback<Boolean> cb) {
		cb.onSuccess(true)
	}


	override createNodes(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {
		val simpleName = source.name.getSimpleName
		val ops = new LinkedList<NetworkOperation>

		ops.add(
			[ ctx, opscb |
				val baseNode = ctx.parent.appendSafe(source.name, "./" + simpleName)
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
							simpleName.hashCode.toString
						}

						override converter() {
							FolderToNode.this.class.toString
						}

					})
				opscb.onSuccess(
					newArrayList(
						baseNode,
						baseNode.appendSafe(source.name, "./.label").appendSafe(baseNode.client.LABEL),
						baseNode.appendSafe("https://appjangle.com/files/img/20141020/List.png", "./.icon").
							appendSafe(baseNode.client().ICON)
					))
			])

		cb.onSuccess(ops)
	}

	override update(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {

		// folders must not be updated		
		cb.onSuccess(newArrayList)
	}

	override deleteNodes(Metadata metadata, ItemMetadata cachedFile, ValueCallback<List<NetworkOperation>> cb) {
		utils.deleteNodes(metadata, cachedFile, cb)
	}

	override createFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {

		source.getFileName(
			cb.embed [ rawFolderName |
				val ops = new LinkedList<FileOperation>

				ops.add(
					[ ctx |
						
						
						val folderName = rawFolderName.toFileSystemSafeName(false, 100)
						ctx.folder.assertFolder(folderName)
						
						
						ctx.metadata.add(
							new ItemMetadata() {

								override name() {
									folderName
								}

								override lastModified() {
									new Date() // TODO replace with last modified if available from node !!
								}

								override uri() {
									source.uri()
								}

								override hash() {
									folderName.hashCode.toString
								}

								override converter() {
									FolderToNode.this.class.toString
								}

							})
					]
				)
				cb.onSuccess(ops)
			])
	}

	override updateFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {

		// folder must not be updated
		cb.onSuccess(newArrayList)
	}

	override removeFiles(FileItem folder, Metadata metadata, ItemMetadata item, ValueCallback<List<FileOperation>> cb) {
		val folderName = item.name

		val ops = new LinkedList<FileOperation>
		ops.add(
			[ ctx |
				ctx.folder.deleteFolder(folderName)
				ctx.metadata.remove(folderName)
			])

		cb.onSuccess(ops)
	}

	extension ConvertUtils utils = new ConvertUtils
	extension FileUtils futils = new FileUtils
	extension N n = new N
	

}
