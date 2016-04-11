package io.objecthub.filesync.tests

import io.objecthub.filesync.FileSync
import delight.async.jre.Async
import org.junit.Test

abstract class CheckUpdatesTemplate extends CheckNodesToFilesTemplate {
	
	protected abstract def void step3_updateNodes()
	
	protected abstract def void step4_assertFilesAfterUpdate()
	
	protected abstract def void step5_updateFiles()
	
	protected abstract def void step6_assertNodesAfterUpdate()
	
	@Test
	override test() {
		step1_defineData
		session.commit.get


		Async.waitFor [cb |
			FileSync.syncSingleFolder(target, source, cb)
		]
		
		step2_assertFiles
		
		step3_updateNodes
		
		session.commit.get
		
		Async.waitFor [cb |
			FileSync.syncSingleFolder(target, source, cb)
		]
		
		step4_assertFilesAfterUpdate
		
		// required because of file lastModified impreciseness
		Thread.sleep(2000)
		
		step5_updateFiles
		
		Async.waitFor [cb |
			FileSync.syncSingleFolder(target, source, cb)
		]
		
		step6_assertNodesAfterUpdate
	}
	
}