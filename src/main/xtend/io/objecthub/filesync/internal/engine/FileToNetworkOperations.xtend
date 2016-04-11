package io.objecthub.filesync.internal.engine

import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import io.objecthub.filesync.SyncParams
import de.mxro.file.FileItem
import delight.async.AsyncCommon
import delight.async.callbacks.ValueCallback
import delight.functional.collections.CollectionsUtils
import java.util.ArrayList
import java.util.List

/**
 * Determines operations performed on local files which need to be uploaded to the cloud.
 */
class FileToNetworkOperations {

	val SyncParams params
	val Metadata metadata;
	

	new(SyncParams params, Metadata metadata) {
		this.params = params
		this.metadata = metadata
		

	}

	def determineOps(ValueCallback<List<NetworkOperation>> cb) {

		if (!params.folder.directory)
			throw new Exception('File passed and not directory. ' + params.folder)

		if (!params.folder.exists)
			throw new Exception('File passed does not exist. ' + params.folder)

		var Iterable<String> locallyAddedFiles = determineLocallyAddedFiles(metadata, params.folder)

		val locallyRemovedFiles = determineLocallyRemovedFiles(metadata, params.folder)

		val locallyChangedFiles = determineLocallyChangedFiles(metadata, params.folder)

		/*
		 * Don't add hidden files.
		 */
		/*locallyAddedFiles = locallyAddedFiles.filter [ fileName |
			if (fileName.startsWith(".")) {
				return false;
			}
			if (!params.folder.get(fileName).visible) {
				return false;
			}
			return true
		]*/

		

		val agg = AsyncCommon.collect(3,
			AsyncCommon.embed(cb,
				[ res |
					val ops = CollectionsUtils.flatten(res)
					cb.onSuccess(ops)
				]));

		createOperationsFromRemovedFiles(locallyRemovedFiles, agg.createCallback)
		createOperationsFromChangedFiles(locallyChangedFiles, agg.createCallback)
		createOperationsFromCreatedFiles(locallyAddedFiles, agg.createCallback)

	}

	def createOperationsFromChangedFiles(List<String> fileNames, ValueCallback<List<NetworkOperation>> cb) {

		val agg = AsyncCommon.collect(fileNames.size,
			AsyncCommon.embed(cb,
				[ res |
					cb.onSuccess(CollectionsUtils.flatten(res))
				]))

		fileNames.forEach [ fileName |
			params.converter.update(metadata, params.folder.get(fileName), agg.createCallback());
		]

	}

	def createOperationsFromRemovedFiles(List<String> fileNames, ValueCallback<List<NetworkOperation>> cb) {

		val agg = AsyncCommon.collect(fileNames.size,
			AsyncCommon.embed(cb,
				[ res |
					cb.onSuccess(CollectionsUtils.flatten(res))
				]))

		fileNames.forEach [ fileName |
			params.converter.deleteNodes(metadata, metadata.get(fileName), agg.createCallback)
		]

	}

	def createOperationsFromCreatedFiles(Iterable<String> fileNames, ValueCallback<List<NetworkOperation>> cb) {

		val agg = AsyncCommon.collect(fileNames.size,
			AsyncCommon.embed(cb,
				[ res |
					cb.onSuccess(CollectionsUtils.flatten(res))
				]))

		fileNames.forEach [ fileName |
			params.converter.createNodes(metadata, params.folder.get(fileName), agg.createCallback());
		]

	}

	static def determineLocallyChangedFiles(Metadata metadata, FileItem folder) {

		val res = new ArrayList<String>(0)

		for (ItemMetadata fileMetadata : metadata.children) {

			val itemNow = folder.get(fileMetadata.name)

			if (itemNow.exists) {

				//println(itemNow+" now:"+itemNow.lastModified.time+" cache:"+fileMetadata.lastModified.time)
				if (itemNow.lastModified.time > fileMetadata.lastModified.time) {
					res.add(itemNow.name)
				}
			}

		}

		res

	}

	static def determineLocallyAddedFiles(Metadata metadata, FileItem folder) {

		val previousNames = getNamesFromCache(metadata.children)

		val currentNames = getNames(folder.children)

		currentNames.removeAll(previousNames)
		return currentNames

	}

	static def determineLocallyRemovedFiles(Metadata metadata, FileItem folder) {

		val previousNames = getNamesFromCache(metadata.children)

		val currentNames = getNames(folder.children)

		previousNames.removeAll(currentNames)
		return previousNames

	}

	static def getNamesFromCache(List<ItemMetadata> cachedChildren) {

		val res = new ArrayList<String>(cachedChildren.size)

		for (ItemMetadata fileItemMetaData : cachedChildren) {
			res.add(fileItemMetaData.name)
		}

		return res

	}

	static def getNames(List<FileItem> cachedChildren) {

		val res = new ArrayList<String>(cachedChildren.size)

		for (FileItem fileItem : cachedChildren) {
			res.add(fileItem.name)
		}

		return res

	}

}
