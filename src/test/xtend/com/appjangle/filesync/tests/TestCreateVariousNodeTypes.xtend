package com.appjangle.filesync.tests

import com.appjangle.filesync.internal.engine.N
import de.oehme.xtend.junit.Hamcrest
import de.oehme.xtend.junit.JUnit

@JUnit
@Hamcrest
class TestCreateVariousNodeTypes extends CheckFilesToNodesTemplate {
	
	override protected step1_defineFiles() {
		source.createFile("My Document.css").text = ".class {}"
		
		source.createFile("My Script.js").text = "window.alert('nothing much');"
		
		source.createFile("My Script.coffee").text = "window.alert 'nothing much'"
		
		
	}
	
	override protected step2_assertNodes() {
		
		result.selectAll(session.CSS).get.size => equalTo(1)
		
		result.select(session.CSS).get.value() => equalTo(".class {}")
		
		result.selectAll(session.JAVASCRIPT).get.size => equalTo(1)
		
		result.select(session.JAVASCRIPT).get.value() => equalTo("window.alert('nothing much');")
		
		result.selectAll(session.COFFEESCRIPT).get.size => equalTo(1)
		
		result.select(session.COFFEESCRIPT).get.value() => equalTo("window.alert 'nothing much'")
		
		
	}
	
	extension N n = new N
	
}