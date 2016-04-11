package io.objecthub.filesync

import org.eclipse.xtend.lib.annotations.Accessors

/**
 * Settings for FileSync synchronization.
 */
@Accessors
class SynchronizationSettings {
	
	var boolean upload = true
	var boolean download = true
	
	
}