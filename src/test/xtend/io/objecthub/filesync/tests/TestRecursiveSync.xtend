package io.objecthub.filesync.tests

import io.objecthub.filesync.internal.engine.N
import de.oehme.xtend.junit.JUnit

@JUnit
class TestRecursiveSync extends CheckNodesToFilesTemplate{
	
	override protected doRecursiveSync() {
		true
	}
	
	override protected step1_defineData() {
		source.append("oh my", "./node1").append("And in the subfolder", "./sub")
		source.append('Hello').append("Any another foder")
		
		val node3 = source.append("node3", "./node3")
		node3.append("child1", "./child1").append("b", "./inThere").get
		node3.append("child2").append("c")
		
		
		val html = node3.append("<html></html>", "./html")
		html.append("My Html Document", "./.label").append(source.client().LABEL)
		html.append(source.client().HTML_VALUE)
		
		source.append("node4", "./node4").append(source.client().link('http://slicnet.com/mxrogm/mxrogm/data/stream/2013/12/3/n1'))
		
	}
	
	override protected step2_assertFiles() {
		
		// checking directories
		result.get("node1").exists => true
		
		result.get("node1").get("sub").exists => true
		
		result.get("node3").exists => true
		
		result.get("node3").get("child1").exists => true
		
		result.get("node3").get("child1").get("inThere").exists => true
		
		result.get("node3").get("child1").get("inThere").isDirectory => true
		
		// checking files
		result.get("node3").get("My Html Document.html").text => "<html></html>"
		
		// checking exclusions
		
		// assure that of external node only one node is created.
		
		result.get("node4").get("Plain Text Editor").children.size => 0
	}
	
	extension N n = new N
	
}