package io.objecthub.filesync.tests.utils

import io.objecthub.filesync.internal.engine.convert.ConvertUtils
import org.junit.Assert
import org.junit.Test

class AddressesCanBeDerivedFromUris {
	
	@Test
	def void test() {
		Assert.assertEquals("name.xml", ConvertUtils.getNameFromUri("https://myuri.com/just/for/testing/name.xml"))

	}
	
}