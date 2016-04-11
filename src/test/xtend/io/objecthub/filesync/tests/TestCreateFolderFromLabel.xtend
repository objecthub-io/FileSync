package io.objecthub.filesync.tests

import de.oehme.xtend.junit.JUnit

@JUnit
class TestCreateFolderFromLabel extends CheckNodesToFilesTemplate {
	protected override step1_defineData() {
		source.append("No value", "./value").append("Labelled Node").append(session.link('https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/shortLabel'))
	}
	
	protected override step2_assertFiles() {

		result.children.size => 2
		
		result.contains(".filesync-meta") => true
		
		result.contains("Labelled Node") => true
		
	}
}