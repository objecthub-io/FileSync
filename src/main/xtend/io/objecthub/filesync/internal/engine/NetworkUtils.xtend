package io.objecthub.filesync.internal.engine

import com.appjangle.api.Node
import com.appjangle.api.Query
import io.objecthub.filesync.NetworkOperation
import io.objecthub.filesync.NetworkOperationContext
import delight.async.AsyncCommon
import delight.async.callbacks.ValueCallback
import delight.functional.Success
import java.util.List

import static extension delight.async.AsyncCommon.*
import io.nextweb.promise.DataPromise

class NetworkUtils {

	def execute(List<NetworkOperation> ops, Node onNode, ValueCallback<Success> cb) {

		val ctx = new NetworkOperationContext() {

			override session() {
				onNode.client
			}

			override parent() {
				onNode
			}

		};

		val opscbs = AsyncCommon.collect(ops.size,
			cb.embed(
				[
					cb.onSuccess(Success.INSTANCE)
				]))

		for (NetworkOperation op : ops) {

			op.apply(ctx,
				cb.embed [ qries |
					//println('exec '+qries)
					val opscbsitem = opscbs.createCallback
					
					val cbs = AsyncCommon.collect(qries.size,
						cb.embed(
							[
								opscbsitem.onSuccess(Success.INSTANCE)
							]))
					
					for (qry : qries) {
						val itmcb = cbs.createCallback

						if (qry instanceof Query) {
							
							qry.catchExceptions [er|itmcb.onFailure(er.exception)]
							qry.get([succ|
								
								itmcb.onSuccess(Success.INSTANCE)
							])

						} else if (qry instanceof DataPromise<?>) {
							
							val safeQry = qry as DataPromise<Object>
							//val res = onNode.session().promise(safeQry)
							safeQry.catchExceptions [er|itmcb.onFailure(er.exception)]
							safeQry.get([succ|itmcb.onSuccess(Success.INSTANCE)])
						} else {
							
							throw new RuntimeException('Unsupported pending query: '+qry.class)
							
						}
					}
				])

		}

		onNode.client().commit

	}

}
