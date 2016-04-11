package io.objecthub.filesync.tests

import com.appjangle.api.Client
import com.appjangle.api.Node
import com.appjangle.api.common.LocalServer
import com.appjangle.api.servers.jre.Servers
import io.objecthub.filesync.FileSync
import com.appjangle.jre.Clients
import de.mxro.file.FileItem
import de.mxro.file.Jre.FilesJre
import delight.async.jre.Async
import java.io.File
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

abstract class CheckNodesToFilesTemplate {

	protected LocalServer server
	protected Client session
	protected Node source
	protected File target
	protected FileItem result

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Before
	def void setUp() {
		server = Servers.createAndStart()

		session = Clients.create(server)

		source = session.seed(server).get

		target = tempFolder.newFolder("sync1")

		result = FilesJre.wrap(target)
	}

	def protected doRecursiveSync() {
		false
	}

	def protected abstract void step1_defineData()

	def protected abstract void step2_assertFiles()

	@Test
	def void test() {
		step1_defineData
		session.commit.get


		Async.waitFor [ cb |
			if (!doRecursiveSync) {
				FileSync.syncSingleFolder(result, source, cb)
			} else {
				FileSync.sync(result, source, cb);
			}
		]

		step2_assertFiles

	}

	@After
	def void tearDown() {
		session.close.get

		server.shutdown.get
	}

}
