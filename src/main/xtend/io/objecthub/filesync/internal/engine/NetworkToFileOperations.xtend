package io.objecthub.filesync.internal.engine

import com.appjangle.api.Client
import com.appjangle.api.Node
import delight.async.AsyncCommon
import delight.async.Value
import delight.async.callbacks.ValueCallback
import delight.functional.Success
import delight.functional.collections.CollectionsUtils
import io.nextweb.promise.utils.CallbackUtils
import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.SyncParams
import java.util.ArrayList
import java.util.List

import static extension delight.async.AsyncCommon.*

/**
 * Determines operations to be performed on local files based on remote changes made in the cloud.
 */
class NetworkToFileOperations {

	val SyncParams params
	val Metadata metadata;

	new(SyncParams params, Metadata metadata) {
		this.params = params
		this.metadata = metadata

	}

	val Value<Integer> counter = new Value<Integer>(0)

	def performCommitIfRequired(Client client, ValueCallback<Success> cb) {
		counter.set(counter.get + 1)
		if (counter.get % 20 !== 0) {
			cb.onSuccess(Success.INSTANCE)

		} else {
			//println('FORCE COMMIT')
			val op = client.commit
			op.catchExceptions(CallbackUtils.asExceptionListener(cb))
			op.get [
				cb.onSuccess(Success.INSTANCE)
			]
		}
	}

	def determineOps(ValueCallback<List<FileOperation>> cb) {

		val qry = params.node.selectAllLinks

		qry.catchExceptions[er|cb.onFailure(er.exception)]

		qry.get [ children |
			AsyncCommon.forEach(children.links, [ link, ValueCallback<Value<Object>> itmcb |

				performCommitIfRequired(
					link.client,
					AsyncCommon.embed(itmcb, [
						link.catchUnauthorized [
							params.notifications.onInsufficientAuthorization(params.folder, link)
							itmcb.onSuccess(new Value<Object>(link))
						]
						link.catchUndefined [
							params.notifications.onNodeNotDefined(params.node, link)
							itmcb.onSuccess(new Value<Object>(link))
						]
						link.catchExceptions[itmcb.onFailure(exception)]

						link.get [
							itmcb.onSuccess(new Value<Object>(it))
						]
					])
				)

			], AsyncCommon.embed(cb, [ List<Value<Object>> values |
				val nodes = new ArrayList<Node>(values.size())
				for (value : values) {
					if (value.get instanceof Node) {
						nodes.add(value.get as Node)
					} else {
					}
				}
				val Iterable<Node> remotelyAdded = nodes.determineRemotelyAddedNodes
				val remotelyRemoved = nodes.determineRemotelyRemovedNodes
				val remotelyUpdated = nodes.determineRemotelyUpdatedNodes
				val agg = AsyncCommon.collect(3, AsyncCommon.embed(cb, [ res |
					cb.onSuccess(CollectionsUtils.flatten(res))
				]))
				remotelyAdded.deduceCreateOperations(agg.createCallback)
				remotelyRemoved.deduceRemoveOperations(agg.createCallback)
				remotelyUpdated.deduceUpdateOperations(agg.createCallback)
			]))
		]

	}

	def deduceUpdateOperations(Iterable<Node> remotelyUpdated, ValueCallback<List<FileOperation>> cb) {

		val agg = AsyncCommon.collect(remotelyUpdated.size, AsyncCommon.embed(cb, [ res |
			cb.onSuccess(CollectionsUtils.flatten(res))
		]))

		for (updatedNode : remotelyUpdated) {

			params.converter.updateFiles(params.folder, metadata, updatedNode, agg.createCallback)

		}

	}

	def deduceCreateOperations(Iterable<Node> remotelyAdded, ValueCallback<List<FileOperation>> cb) {

		val agg = AsyncCommon.collect(remotelyAdded.size, AsyncCommon.embed(cb, [ res |
			cb.onSuccess(CollectionsUtils.flatten(res))
		]))

		for (newNode : remotelyAdded) {

			params.converter.createFiles(params.folder, metadata, newNode, agg.createCallback)

		}

	}

	def deduceRemoveOperations(List<ItemMetadata> remotelyRemoved, ValueCallback<List<FileOperation>> cb) {

		val agg = AsyncCommon.collect(remotelyRemoved.size, AsyncCommon.embed(cb, [ res |
			cb.onSuccess(CollectionsUtils.flatten(res))
		]))

		for (removedNode : remotelyRemoved) {

			params.converter.removeFiles(params.folder, metadata, removedNode, agg.createCallback)

		}

	}

	def determineRemotelyAddedNodes(List<Node> children) {

		val res = new ArrayList<Node>(0)

		for (child : children) {
			if (metadata.get(child) === null) {
				res.add(child)
			}
		}

		res
	}

	def determineRemotelyRemovedNodes(List<Node> children) {

		val res = new ArrayList<ItemMetadata>(0)

		val uris = new ArrayList<String>(children.size)
		for (node : children) {
			uris.add(node.uri())
		}

		for (item : metadata.children) {
			if (!uris.contains(item.uri)) {
				res.add(item)
			}

		}

		res

	}

	def determineRemotelyUpdatedNodes(List<Node> children) {
		val res = new ArrayList<Node>(children.size)

		for (node : children) {

			// TODO: not yet supported, just update all
			if (metadata.get(node) !== null) {
				res.add(node)
			}

		}

		res

	}

}
