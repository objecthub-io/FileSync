package io.objecthub.filesync.internal.engine.metadata.v01

import io.objecthub.filesync.ItemMetadata
import java.io.Serializable
import java.util.Date
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode

@EqualsHashCode
class ItemXml implements ItemMetadata, Serializable {
	
	@Accessors public String name
	@Accessors public Date lastModified
	@Accessors public String uri
	@Accessors public String hash
	@Accessors public String converter
	
	override name() {
		name
	}
	
	override lastModified() {
		lastModified
	}
	
	override uri() {
		uri
	}
	
	override hash() {
		hash
	}
	
	override converter() {
		converter
	}
	
	
	
}