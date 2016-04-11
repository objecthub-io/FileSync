package io.objecthub.filesync.tests

import de.oehme.xtend.junit.JUnit
import delight.async.jre.Async
import delight.functional.Success

@JUnit
class TestRemoveFolder extends CheckUpdatesTemplate {
	
	override protected step1_defineData() {
		source.append("folder1", "./folder1")
		source.append("folder2", "./folder2")
		source.append("folder3", "./folder3")
	}
	
	override protected step2_assertFiles() {
		result.children.size => 4
		result.get("folder1").exists => true
	}
	
	override protected step3_updateNodes() {
		source.remove(source.select("./folder1"))
	}
	
	override protected step4_assertFilesAfterUpdate() {
		result.get("folder1").exists => false
	}
	
	override protected step5_updateFiles() {
		result.deleteFolder("folder2")
	}
	
	
	override protected step6_assertNodesAfterUpdate() {
		
		Async.waitFor([cb |
			val qry = source.select("./folder2")
			
			qry.catchExceptions(er|cb.onFailure(er.exception))
			
			qry.catchUndefined([cb.onSuccess(Success.INSTANCE)])
			
			qry.get([ cb.onFailure(new Exception("Node should have been removed.")) ])

		])
	}
	
}