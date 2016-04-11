package io.objecthub.filesync.tests

import io.objecthub.filesync.internal.engine.N
import de.oehme.xtend.junit.Hamcrest
import de.oehme.xtend.junit.JUnit


@JUnit
@Hamcrest
class TestCreateNode extends CheckFilesToNodesTemplate {
	
	override protected step1_defineFiles() {
		source.assertFolder('Oh my test')
	}
	
	override protected step2_assertNodes() {
		
		result.selectAll.get.size => 1
		
		val node = result.select("./Oh_my_test").get()
		
		node => notNullValue
		
		node.select(node.client().LABEL).get().value() => equalTo("Oh my test")
		
	}
	
	extension N n = new N
	
}