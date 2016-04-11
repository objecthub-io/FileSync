package io.objecthub.filesync

import com.appjangle.api.Node
import java.util.List

interface Metadata {
	
	def List<ItemMetadata> getChildren()
	
	def ItemMetadata get(String name)
	
	def ItemMetadata get(Node forNode)
	
	def Metadata add(ItemMetadata itemMetadata)
	
	def Metadata update(ItemMetadata itemMetadata)
	
	def Metadata remove(String name)
	
	def ItemMetadata value()
	
	def Metadata setValue(ItemMetadata item)
	
}