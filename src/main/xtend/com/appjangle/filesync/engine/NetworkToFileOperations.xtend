package com.appjangle.filesync.engine

import com.appjangle.filesync.Converter
import com.appjangle.filesync.FileOperation
import com.appjangle.filesync.engine.metadata.ItemMetadata
import com.appjangle.filesync.engine.metadata.Metadata
import de.mxro.async.Async
import de.mxro.async.callbacks.ValueCallback
import de.mxro.file.FileItem
import de.mxro.fn.collections.CollectionsUtils
import io.nextweb.Node
import io.nextweb.NodeList
import java.util.ArrayList
import java.util.List

/**
 * Determines operations to be performed on local files based on remote changes made in the cloud.
 */
class NetworkToFileOperations {

	val Node node;
	val FileItem folder;
	val Metadata metadata;
	val Converter converter;

	new(Node node, FileItem folder, Metadata metadata, Converter converter) {
		this.node = node
		this.folder = folder
		this.metadata = metadata
		this.converter = converter

	}

	def determineOps(ValueCallback<List<FileOperation>> cb) {

		val qry = node.selectAll

		qry.catchExceptions[er|cb.onFailure(er.exception)]

		qry.get [ children |
			val remotelyAdded = children.determineRemotelyAddedNodes
			val remotelyRemoved = children.determineRemotelyRemovedNodes
			val remotelyUpdated = children.determineRemotelyUpdatedNodes
			
			val agg = Async.collect(3,
				Async.embed(cb,
					[ res |
						cb.onSuccess(CollectionsUtils.flatten(res))
					]))
					
			remotelyAdded.deduceCreateOperations(agg.createCallback)
			remotelyRemoved.deduceRemoveOperations(agg.createCallback)
			remotelyUpdated.deduceUpdateOperations(agg.createCallback)
					
		]

	}


	def deduceUpdateOperations(List<NodeList> remotelyUpdated, ValueCallback<List<FileOperation>> cb ) {
		
	}

	def deduceCreateOperations(List<Node> remotelyAdded, ValueCallback<List<FileOperation>> cb) {

		val agg = Async.collect(remotelyAdded.size,
			Async.embed(cb,
				[ res |
					cb.onSuccess(CollectionsUtils.flatten(res))
				]))

		for (newNode : remotelyAdded) {

			converter.createFiles(folder, metadata, newNode, agg.createCallback)

		}

	}
	
	def deduceRemoveOperations(List<ItemMetadata> remotelyRemoved, ValueCallback<List<FileOperation>> cb) {
		
		val agg = Async.collect(remotelyRemoved.size,
			Async.embed(cb,
				[ res |
					cb.onSuccess(CollectionsUtils.flatten(res))
				]))
				
		for (removedNode : remotelyRemoved) {

			converter.removeFiles(folder, metadata, removedNode, agg.createCallback)

		}
		
	}

	def determineRemotelyAddedNodes(NodeList children) {

		val res = new ArrayList<Node>(0)

		for (child : children) {
			if (metadata.get(child) == null) {
				res.add(child)
			}
		}

		res
	}

	def determineRemotelyRemovedNodes(NodeList children) {

		val res = new ArrayList<ItemMetadata>(0)

		for (item : metadata.children) {

			if (!children.uris.contains(item.uri)) {
				res.add(item)
			}

		}

		res

	}

	def determineRemotelyUpdatedNodes(NodeList children) {
		val res = new ArrayList<NodeList>(0)

		for (item : metadata.children) {
			// TODO: not yet supported
		}

		res

	}

}
