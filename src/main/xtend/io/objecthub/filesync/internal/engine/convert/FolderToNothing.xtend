package io.objecthub.filesync.internal.engine.convert

import com.appjangle.api.Node
import io.objecthub.filesync.Converter
import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import de.mxro.file.FileItem
import delight.async.callbacks.ValueCallback
import delight.functional.Function
import java.util.List

class FolderToNothing implements Converter {
	
	val Function<FileItem, Boolean> test;
	
	new(Function<FileItem, Boolean> test) {
		this.test = test
	}
	
	override worksOn(FileItem source) {
		test.apply(source)
	}
	
	override worksOn(Node node, ValueCallback<Boolean> cb) {
		cb.onSuccess(false)
	}
	
	override createNodes(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {
		cb.onSuccess(newArrayList)
	}
	
	override update(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {
		throw new IllegalStateException("This operation should never be triggered for this converter.")
	}
	
	override deleteNodes(Metadata metadata, ItemMetadata cachedFile, ValueCallback<List<NetworkOperation>> cb) {
		throw new IllegalStateException("This operation should never be triggered for this converter.")
	}
	
	override createFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {
		throw new IllegalStateException("This operation should never be triggered for this converter.")
	}
	
	override updateFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {
		throw new IllegalStateException("This operation should never be triggered for this converter.")
	}
	
	override removeFiles(FileItem folder, Metadata metadata, ItemMetadata item, ValueCallback<List<FileOperation>> cb) {
		throw new IllegalStateException("This operation should never be triggered for this converter.")
	}
	
	override id() {
		"folder-to-nothing"
	}
	
}