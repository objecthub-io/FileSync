package io.objecthub.filesync.internal.engine

import com.appjangle.api.Node
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import com.google.common.base.Preconditions
import de.mxro.file.FileItem
import delight.async.callbacks.ValueCallback
import delight.functional.Success

class SyncValueOperations {
	
	def static createMetadata(Node node, FileItem forFile) {
		new ItemMetadata() {
				
				override name() {
					forFile.name
				}
				
				override lastModified() {
					forFile.lastModified
				}
				
				override uri() {
					node.uri()
				}
				
				override hash() {
					forFile.hash
				}
				
				override converter() {
					""
				}
				
			}
	}
	
	def downloadValue(Node node, Metadata metadata, FileItem folder) {
		
		if (!folder.get("value.txt").exists) {
			folder.createFile("value.txt").text = node.value().toString()
			
			metadata.value = createMetadata(node, folder.get("value.txt"))
			
			return;
		}
		
		val oldText = folder.get("value.txt").text
		
		if (oldText != node.value().toString()) {
			folder.get("value.txt").text = node.value().toString()
			metadata.value = createMetadata(node, folder.get("value.txt"))
		}
		
	}
	
	def uploadValue(Node node, Metadata metadata, FileItem folder, ValueCallback<Success> cb) {
		
		val file = folder.get("value.txt")
		
		Preconditions.checkState(file.exists)
		
		if (metadata.value.lastModified.time > file.lastModified.time) {
			
			val qry = node.setValueSafe(file.text)
			
			qry.catchExceptions [cb.onFailure(exception) ]
			
			qry.get [
				metadata.value = createMetadata(node, folder.get('value.txt'))
				cb.onSuccess(Success.INSTANCE)
			]
			
			
			
		}
		
	}
	
}