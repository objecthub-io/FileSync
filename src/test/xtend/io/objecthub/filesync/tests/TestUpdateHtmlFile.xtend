package io.objecthub.filesync.tests

import de.oehme.xtend.junit.JUnit

@JUnit
class TestUpdateHtmlFile extends CheckUpdatesTemplate {
	
	override protected step1_defineData() {
		val html = source.append("<p>Hello 1</p>", "./html")
		html.append("doc").append(session.link('https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/shortLabel'))
		html.append(session.link("https://admin1.linnk.it/types/v01/isHtmlValue"))
	}
	
	override protected step2_assertFiles() {
		result.get("doc.html").text => "<p>Hello 1</p>"
	}
	
	override protected step3_updateNodes() {
		source.select("./html").setValue("<p>Hello 1 and Hello 2 are an amazing team.</p>")
	}
	
	override protected step4_assertFilesAfterUpdate() {
		result.get("doc.html").text => "<p>Hello 1 and Hello 2 are an amazing team.</p>"
	}
	
	override protected step5_updateFiles() {
		result.get("doc.html").text = "And now for something different"
	}
	
	override protected step6_assertNodesAfterUpdate() {
		source.select("./html").get.value() => "And now for something different"
	}
	
}