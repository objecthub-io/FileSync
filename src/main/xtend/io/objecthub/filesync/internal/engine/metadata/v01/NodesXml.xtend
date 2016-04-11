package io.objecthub.filesync.internal.engine.metadata.v01

import java.io.Serializable
import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors

@Accessors
class NodesXml implements Serializable {
	
	public val List<ItemXml> items
	
	public var ItemXml value
	
	new() {
		items = newArrayList
		value = null
	}
	
}