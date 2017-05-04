package io.objecthub.filesync

import com.appjangle.api.Link
import com.appjangle.api.Node
import com.appjangle.api.nodes.Token
import de.mxro.file.FileItem
import de.mxro.file.Jre.FilesJre
import delight.async.AsyncCommon
import delight.async.callbacks.ValueCallback
import delight.functional.Success
import io.objecthub.filesync.internal.engine.FileUtils
import io.objecthub.filesync.internal.engine.N
import io.objecthub.filesync.internal.engine.SyncFolder
import io.objecthub.filesync.internal.engine.convert.ConvertUtils
import io.objecthub.filesync.internal.engine.convert.ConverterCollection
import io.objecthub.filesync.internal.engine.convert.FileToTextNode
import io.objecthub.filesync.internal.engine.convert.FolderToNode
import io.objecthub.filesync.internal.engine.convert.FolderToNothing
import io.objecthub.filesync.internal.engine.convert.NodeToNothing
import java.io.File
import java.util.LinkedList

import static extension delight.async.AsyncCommon.*

class FileSync {

	def static syncSingleFolder(SyncParams params, ValueCallback<Success> cb) {
		new SyncFolder(params).doIt(cb)
	}

	def static defaultSyncParams() {
		val params = new SyncParams

		params.converter = createDefaultConverter
		params.settings = new SynchronizationSettings
		params.state = new SynchronizationState() {
		}
		params.notifications = new SyncNotifications
		params.syncRoots = new LinkedList
		
		params.dontFollow = new LinkedList

		return params
	}

	/**
	 * <p>Synchronized the contents of a folder and a node without synchronizing sub-folders.
	 */
	def static syncSingleFolder(File folder, Node node, ValueCallback<Success> cb) {
		syncSingleFolder(FilesJre.wrap(folder), node, cb)
	}

	/**
	 * <p>Synchronized the contents of a folder and a node without synchronizing sub-folders.
	 */
	def static syncSingleFolder(FileItem folder, Node node, ValueCallback<Success> cb) {
		val params = defaultSyncParams

		params.folder = folder
		params.node = node

		syncSingleFolder(params, cb)

	}

	private def static void syncInt(SyncParams params, ValueCallback<Success> cb) {
		if (params.state.wasSynced(params.node)) {
			params.notifications.onNodeSkippedBecauseItWasAlreadySynced(params.folder, params.node);
			cb.onSuccess(Success.INSTANCE);
			return;
		}

		params.state.addSynced(params.node);

		syncSingleFolder(params,
			cb.embed [
				val toSync = params.folder.children.filter[isDirectory && visible && !name.startsWith('.')]
				AsyncCommon.forEach(toSync.toList,
					[ childFolder, itmcb |
						val metadata = params.folder.loadMetadata
						val itmmetadata = metadata.get(childFolder.name)
						val isChild = itmmetadata.uri.startsWith(params.node.uri())
						var withinSyncRoots = false
						var Link matchedSyncRoot = null
						for (syncRoot : params.syncRoots) {
							if (itmmetadata.uri.startsWith(syncRoot.uri())) {
								withinSyncRoots = true
								matchedSyncRoot = syncRoot
							}
						}
						
						if (!isChild && !withinSyncRoots) {
							itmcb.onSuccess(Success.INSTANCE)
							return;

						}
						
						var inDontFollow = false;
						for (dontFollow : params.dontFollow) {
							if (itmmetadata.uri.equals(dontFollow.uri())) {
								inDontFollow = true
							}
						}
						
						if (inDontFollow) {
							itmcb.onSuccess(Success.INSTANCE)
							return;
						}
						
						
						var Link qry
						if (withinSyncRoots && matchedSyncRoot.secret() !== null && matchedSyncRoot.secret().length > 0) {
							qry = params.node.client().link(itmmetadata.uri, matchedSyncRoot.secret())
						} else {
							qry = params.node.client().link(itmmetadata.uri)
						}
						qry.catchExceptions[er|itmcb.onFailure(er.exception)]
						qry.get [ childNode |
							val childParams = new SyncParams(params)
							childParams.folder = childFolder
							childParams.node = childNode
							
							//println("Processing "+childNode.uri())
							
							if (childNode.uri().startsWith("http://localhost")) {
								println("ERROR: Illegal node "+childNode.uri()+" with parent "+params.node.uri())
								itmcb.onSuccess(Success.INSTANCE)
								
								return;
							}
							
							syncInt(childParams, itmcb)
						]
					],
					cb.embed [
						cb.onSuccess(Success.INSTANCE)
					])
			])
	}

	static def void sync(SyncParams params, ValueCallback<Success> cb) {
		if (params.syncRoots.size() == 0) {
			params.syncRoots.add(params.node.client().link(params.node))
		}

		syncInt(params, cb)
	}

	/**
	 * <p>Synchronized the contents of the specified folder with the specified nodes and does the same for all sub-folders and child nodes.
	 */
	def static void sync(FileItem folder, Node node, ValueCallback<Success> cb) {
		val params = defaultSyncParams

		params.folder = folder
		params.node = node

		sync(params, cb)
	}

	def static createDefaultConverter() {

		val coll = new ConverterCollection

		coll.addConverter(new FileToTextNode("html", ".html", N.HTML_VALUE, "./baseHtml"))
		coll.addConverter(new FileToTextNode("js", ".js", N.MICRO_LIBRARY, "./baseJs"))
		coll.addConverter(new FileToTextNode("coffee", ".coffee", N.COFFEESCRIPT, "./baseCS"))
		coll.addConverter(new FileToTextNode("css", ".css", N.CSS, "./baseCss"))
		coll.addConverter(new FileToTextNode("clazz", ".clazz", N.CLASS, null))
		coll.addConverter(new FileToTextNode("attribute", ".attribute", N.ATTRIBUTE, null))
		coll.addConverter(new FileToTextNode("plain-js", ".js", N.PLAIN_JS, null))
		
		coll.addConverter(
			new NodeToNothing [ node, cb |
				cb.onSuccess(node.value() instanceof Token)
			])

		coll.addConverter(
			new NodeToNothing [ node, cb |
				cb.onSuccess(ConvertUtils.getNameFromUri(node.uri()).startsWith('.'))
			])

		coll.addConverter(
			new FolderToNothing [ file |
				file.name.startsWith(".") || !file.visible
			])

		coll.addConverter(new FolderToNode)

		coll

	}

	static extension FileUtils fileUtils = new FileUtils

}
