package io.objecthub.filesync.tests

import de.oehme.xtend.junit.JUnit

@JUnit
class TestCreateFolder extends CheckNodesToFilesTemplate {

	protected override step1_defineData() {
		source.append("A Folder")
	}
	
	protected override step2_assertFiles() {

		result.children.size => 2
		
		result.contains(".filesync-meta") => true
		
		result.children.toString.contains("Folder") => true
		
	}

}
