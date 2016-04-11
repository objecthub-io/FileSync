package io.objecthub.filesync.internal.engine.metadata

import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.internal.engine.metadata.v01.ItemXml
import io.objecthub.filesync.internal.engine.metadata.v01.NodesXml
import com.thoughtworks.xstream.XStream
import de.mxro.file.FileItem

class MetadataUtilsJre {

	def static Metadata readFromFile(FileItem file) {

		if (!file.exists) {

			return null

		}

		val xstream = new XStream

		val NodesXml nodesXml = xstream.fromXML(file.text) as NodesXml

		nodesXml.toMetadata

	}

	def static Metadata toMetadata(NodesXml nodesXml) {
		
		val metadata = new MetadataImpl
		
		for ( item:nodesXml.items) {
			metadata.add(item)
		}
		
		metadata.value = nodesXml.value
		
		metadata
		
	}
	
	def static void saveToFile(Metadata metadata, FileItem file) {
		if (!file.exists) {
			throw new RuntimeException("File doesn't exist.")
		}
		
		val xstream = new XStream
		
		val nodesXml = metadata.toNodesXml
		
		file.text = xstream.toXML(nodesXml)

		
	}

	def static NodesXml toNodesXml(Metadata metadata) {
		var nodesXml = new NodesXml
		
		for (item: metadata.children) {
			
			nodesXml.items.add(item.toItemXml)
			
		}
		
		
		nodesXml.value = toItemXml(metadata.value)
		
		nodesXml
		
	}
	
	def static ItemXml toItemXml(ItemMetadata item) {
		if (item == null) {
			return null
		}
		
		val itemXml = new ItemXml
		
		itemXml.converter = item.converter
		itemXml.lastModified = item.lastModified
		itemXml.hash = item.hash
		itemXml.name = item.name
		itemXml.uri = item.uri
		
		itemXml
	}

}
