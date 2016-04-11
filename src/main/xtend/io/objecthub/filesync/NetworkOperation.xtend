package io.objecthub.filesync

import delight.async.callbacks.ValueCallback
import delight.functional.Closure2
import java.util.List
import io.nextweb.promise.DataOperation

interface NetworkOperation extends Closure2<NetworkOperationContext, ValueCallback<List<DataOperation<?>>>> {
	
	
	
	
}