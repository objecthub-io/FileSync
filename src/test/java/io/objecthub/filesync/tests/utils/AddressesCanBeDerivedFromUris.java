package io.objecthub.filesync.tests.utils;

import io.objecthub.filesync.internal.engine.convert.ConvertUtils;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class AddressesCanBeDerivedFromUris {
  @Test
  public void test() {
    String _nameFromUri = ConvertUtils.getNameFromUri("https://myuri.com/just/for/testing/name.xml");
    Assert.assertEquals("name.xml", _nameFromUri);
  }
}
