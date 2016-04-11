package io.objecthub.filesync.tests

import io.objecthub.filesync.internal.engine.N
import de.oehme.xtend.junit.JUnit

@JUnit
class TestCreateHtmlFile extends CheckNodesToFilesTemplate {
	
	protected override step1_defineData() {
		val html = source.append("<html></html>", "./html")
		html.append("Html Document").append(session.link(N.LABEL))
		html.append(session.link(N.HTML_VALUE))
	}
	
	protected override step2_assertFiles() {

		result.children.size => 2
		
		result.contains(".filesync-meta") => true
		
		result.contains("Html Document.html") => true
		
		result.get("Html Document.html").text => "<html></html>"
		
	}
	
}