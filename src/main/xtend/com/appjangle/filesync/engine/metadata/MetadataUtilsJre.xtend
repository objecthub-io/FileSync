package com.appjangle.filesync.engine.metadata

import com.thoughtworks.xstream.XStream
import de.mxro.file.FileItem
import de.mxro.file.Jre.JreFiles

class MetadataUtilsJre {

	def static NodesMetadata readFromFile(FileItem file) {

		if (!file.exists) {
			
			return null
			
			} 

		val xstream = new XStream

		val NodesMetadata nodesMetadata = xstream.fromXML(JreFiles.getInputStream(file)) as NodesMetadata

		nodesMetadata

	}

}
