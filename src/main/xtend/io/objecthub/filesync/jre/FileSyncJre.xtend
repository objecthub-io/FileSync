package io.objecthub.filesync.jre

import com.appjangle.api.Node
import io.objecthub.filesync.FileSync
import de.mxro.file.Jre.FilesJre
import delight.async.jre.Async
import java.io.File

class FileSyncJre {
	
	def static void sync(File folder, Node node) {
		Async.waitFor([cb |
			FileSync.sync(FilesJre.wrap(folder), node, cb)
		])
		
	}
	
}