package io.objecthub.filesync

import java.util.Date

interface ItemMetadata {
	
	def String name()
	
	def Date lastModified()
	
	def String uri()
	
	def String hash()
	
	def String converter()
	
	
}