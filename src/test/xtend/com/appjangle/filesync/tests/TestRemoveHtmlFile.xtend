package io.objecthub.filesync.tests

import io.objecthub.filesync.internal.engine.N
import de.oehme.xtend.junit.JUnit
import delight.async.jre.Async
import delight.functional.Success

@JUnit
class TestRemoveHtmlFile extends CheckUpdatesTemplate {
	
	override protected step1_defineData() {
		val file1 = source.append("<p>file1</p>", "./file1")
		file1.append("html1", "./label").append(session.LABEL)
		file1.append(session.HTML_VALUE)
		
		val file2 = source.append("file2", "./file2")
		file2.append("html2").append(session.LABEL)
		file2.append(session.HTML_VALUE)
		
		val file3 =source.append("file3", "./file3")
		file3.append("html3").append(session.LABEL)
		file3.append(session.HTML_VALUE)
	}
	
	override protected step2_assertFiles() {
		result.children.size => 4
		result.get("html1.html").exists => true
	}
	
	override protected step3_updateNodes() {
		val html = source.select("./file1")
		html.select("./label").remove(session.LABEL)
		html.remove(html.select("./label"))
		html.remove(session.HTML_VALUE)
		source.remove(html)
	}
	
	override protected step4_assertFilesAfterUpdate() {
		result.get("html1.html").exists => false
		
		source.select("./file2").get() // asserts that it exists
	}
	
	override protected step5_updateFiles() {
		result.deleteFile("html2.html")
	}
	
	
	override protected step6_assertNodesAfterUpdate() {
		Async.waitFor([cb |
			val qry = source.select("./file2")
			
			qry.catchExceptions(er|cb.onFailure(er.exception))
			
			qry.catchUndefined([ cb.onSuccess(Success.INSTANCE)])
			
			qry.get([ cb.onFailure(new Exception("Node should have been removed.")) ])

		])
	}
	
	extension N n = new N()
	
}