package io.objecthub.filesync

import de.mxro.file.FileItem

interface FileOperationContext {
	def FileItem folder()	
	def Metadata metadata()
}