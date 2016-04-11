package io.objecthub.filesync.internal.engine

import io.objecthub.filesync.Metadata
import io.objecthub.filesync.SyncParams
import delight.async.callbacks.ValueCallback
import delight.functional.Success

import static extension delight.async.AsyncCommon.*

class SyncFolder {

	val SyncParams params

	new(SyncParams params) {
		this.params = params
	}

	var Metadata metadata

	def doIt(ValueCallback<Success> cb) {

		params.notifications.onStartSynchronizing(params.folder, params.node)

		if (!params.folder.hasMetadata) {

			metadata = params.folder.assertMetadata

			download(cb)
			return;
		}

		metadata = params.folder.assertMetadata

		if (!params.settings.upload) {
			download(cb)
			return
		}

		new SyncValueOperations().uploadValue(params.node, metadata, params.folder,
			cb.embed [
				new FileToNetworkOperations(params, metadata).determineOps(
					cb.embed [ ops |
						ops.execute(params.node,
							cb.embed [
								download(cb)
							])
					])
			])
	}

	def download(ValueCallback<Success> cb) {
		if (!params.settings.download) {
			params.notifications.onFinishedSynchronizing(params.folder, params.node)
			cb.onSuccess(Success.INSTANCE)
			return;
		}

		new SyncValueOperations().downloadValue(params.node, metadata, params.folder)

		new NetworkToFileOperations(params, metadata).determineOps(
			cb.embed(
				[ ops |
					ops.execute(params.folder, metadata)
					params.folder.saveMetadata(metadata)
					params.notifications.onFinishedSynchronizing(params.folder, params.node)
					cb.onSuccess(Success.INSTANCE)
				]))
	}

	extension FileUtils fileUtils = new FileUtils()
	extension NetworkUtils networkUtils = new NetworkUtils()

}
