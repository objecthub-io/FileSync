package io.objecthub.filesync

import com.appjangle.api.Node
import com.google.common.base.Preconditions
import java.util.HashSet
import java.util.Set

/**
 * Must be synchronized.
 */
class SynchronizationState {
	
	/**
	 * All nodes already synchronized
	 */
	val Set<String> synced;
	
	def synchronized addSynced(Node n) {
		Preconditions.checkState(!synced.contains(n.uri()), "Node was already synced [%s]", n.uri())
		
		synced.add(n.uri())
	}
	
	
	def synchronized wasSynced(Node n) {
		synced.contains(n.uri())
	}
	
	new() {
		synced = new HashSet<String>
	}
	
}