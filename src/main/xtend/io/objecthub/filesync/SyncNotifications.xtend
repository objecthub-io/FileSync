package io.objecthub.filesync

import com.appjangle.api.Link
import com.appjangle.api.Node
import de.mxro.file.FileItem
import org.eclipse.xtend.lib.annotations.Accessors

@Accessors
class SyncNotifications {
	
	var boolean printToStdOut = false
	
	def void onInsufficientAuthorization(FileItem inFolder, Link forNode) {
		if (printToStdOut) {
			println("Insufficient authorization for node ["+forNode+"] in folder ["+inFolder+"]")
		}
	}
	
	def void onNodeNotDefined(Node parent, Link node) {
		if (printToStdOut) {
			println("Folder is not defined  ["+node+"] in parent ["+parent+"]")
		}
	}
	
	def void onStartSynchronizing(FileItem folder, Node node) {
		if (printToStdOut) {
			//println("["+node+"]->Start in ["+folder+"]")
		}
	}
	
	def void onFinishedSynchronizing(FileItem folder, Node node) {
		if (printToStdOut) {
			//println("["+node+"]->Finish in ["+folder+"]")
		}
	}
	
	def void onNodeSkippedBecauseItWasAlreadySynced(FileItem folder, Node node) {
		if (printToStdOut) {
			println("Node was already synchronized ["+node+"] in folder ["+folder+"]. Synchronziation skipped.")
		}
	}
	
	
}