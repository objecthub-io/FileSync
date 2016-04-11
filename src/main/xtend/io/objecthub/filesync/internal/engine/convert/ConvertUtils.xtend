package io.objecthub.filesync.internal.engine.convert

import com.appjangle.api.Link
import com.appjangle.api.Node
import com.appjangle.api.Query
import com.appjangle.api.operations.OperationsExtension
import com.appjangle.api.queries.QueriesExtension
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import io.objecthub.filesync.internal.engine.FileUtils
import io.objecthub.filesync.internal.engine.N
import de.mxro.file.FileItem
import delight.async.AsyncCommon
import delight.async.callbacks.ValueCallback
import java.util.ArrayList
import java.util.LinkedList
import java.util.List

import static extension delight.async.AsyncCommon.*
import io.nextweb.promise.utils.CallbackUtils
import io.nextweb.promise.DataOperation

class ConvertUtils {

	

	val textValueExtensions = #{
		N.HTML_VALUE -> '.html',
		N.ATTRIBUTE -> '.attribute',
		N.CLASS -> '.class',
		N.CSS -> '.css',
		N.JAVASCRIPT -> '.js',
		N.COFFEESCRIPT -> '.coffee',
		N.RICHTEXT -> '.htm'	
	}

	def isTextValue(String fileName) {
		val ext = fileName.getExtension

		textValueExtensions.containsValue('.'+ext)
	}

	def isTextType(Link link) {
		
		
		textValueExtensions.keySet.contains(link.uri())
	}

	def getFileExtension(Node forNode, ValueCallback<String> cb) {

		val qry = forNode.selectAllLinks()

		qry.catchExceptions([er|cb.onFailure(er.exception())])

		qry.get [ links |
			for (mapping : textValueExtensions.entrySet) {
				
				if (links.contains(mapping.key)) {
					cb.onSuccess(mapping.value)
					return
				}
				
			}
		]

	}

	def deleteNodes(Metadata metadata, ItemMetadata cachedFile, ValueCallback<List<NetworkOperation>> cb) {
		val address = cachedFile.uri

		val ops = new LinkedList<NetworkOperation>

		ops.add(
			[ ctx, opscb |
				metadata.remove(cachedFile.name)
				val nodeToBeRemoved = ctx.session.link(address)
				val parent = ctx.parent
				
				val list = new ArrayList<DataOperation<?>>
				
				if (parent.client().link(parent).hasDirectChild(nodeToBeRemoved)) {
					val innercb = CallbackUtils.asDataCallback(nodeToBeRemoved.exceptionManager, opscb.embed [
							opscb.onSuccess(list)
						])
					parent.removeRecursive(nodeToBeRemoved,innercb)

				} else {
					
					list.add(parent.remove(nodeToBeRemoved));
					opscb.onSuccess(list)
					
				}
			])

		cb.onSuccess(ops)
	}

	def appendLabel(Query toNode, String label) {
		throw new RuntimeException('Not supported!')
		//toNode.appendSafe(label, "./.label").appendSafe(toNode.client().LABEL)
	}

	def List<DataOperation<?>> appendTypesAndIcon(Query toNode, FileItem source) {
		val res = newArrayList
		
		val session = toNode.client()

		var ext = source.extension
		
		ext = '.'+ext
		
		if (ext == ".html") {
			
			res.add(toNode.appendSafe(session.HTML_VALUE))
			
			
			
				
		} else if (ext == ".htm") {
			res.add(toNode.appendSafe(session.RICHTEXT))
			
			
		
			
		} else if (ext == ".js") {
			
			
			res.add(toNode.appendSafe(session.JAVASCRIPT))
			
			
			
			
		} else if (ext == ".coffee") {
			
			res.add(toNode.appendSafe(session.COFFEESCRIPT))
			
			
			
			
		} else if (ext == ".css") {
			
			res.add(toNode.appendSafe(session.CSS))
			
			
			
			
			
		} else if (ext == ".type") {
			
			res.add(toNode.appendSafe(session.ATTRIBUTE))
			
			
			
		}
		
		
		
	 	// res
	 	
	 	throw new RuntimeException('Not supported!')
		
	}
	
	
	

	public static val NO_VALUE = new Object()

	def getFileName(Node forNode, FileItem inFolder, String fileExtension, ValueCallback<String> cb) {
		getFileName(forNode,
			cb.embed(
				[ fileNameFromNode |
					var fileName = fileNameFromNode + fileExtension
					var idx = 1
					while (inFolder.get(fileName).exists) {
						fileName = fileNameFromNode + idx + fileExtension
						idx++
					}
					cb.onSuccess(fileName)
				]))
	}

	def getFileName(Node fromNode, ValueCallback<String> cb) {

		val qry = fromNode.select('./.meta/title')

		qry.catchExceptions(CallbackUtils.asExceptionListener(cb))

		qry.catchUndefined [
			cb.onSuccess(getNameFromUri(fromNode.uri()))
		]
		
		qry.get [ title |
			cb.onSuccess(title.value(String))
		]

		

	}

	def static getNameFromUri(String uri) {
		uri.substring(uri.lastIndexOf("/") + 1)
	}
	
	
	extension N n = new N
	extension OperationsExtension ext = new OperationsExtension
	extension QueriesExtension qxt = new QueriesExtension
	extension FileUtils futils = new FileUtils
}
