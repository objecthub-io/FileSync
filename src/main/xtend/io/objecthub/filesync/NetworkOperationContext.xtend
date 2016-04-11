package io.objecthub.filesync

import com.appjangle.api.Client
import com.appjangle.api.Node

interface NetworkOperationContext {
	def Client session()
	def Node parent()
}