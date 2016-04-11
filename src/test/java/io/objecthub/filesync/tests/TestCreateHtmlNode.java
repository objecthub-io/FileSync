package io.objecthub.filesync.tests;

import de.mxro.file.FileItem;
import de.oehme.xtend.junit.Hamcrest;
import de.oehme.xtend.junit.JUnit;
import io.objecthub.filesync.internal.engine.N;
import io.objecthub.filesync.tests.CheckFilesToNodesTemplate;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.internal.ArrayComparisonFailure;
import org.junit.rules.ErrorCollector;

@JUnit
@Hamcrest
@SuppressWarnings("all")
public class TestCreateHtmlNode extends CheckFilesToNodesTemplate {
  @Override
  protected void step1_defineFiles() {
    FileItem _createFile = this.source.createFile("My Document.html");
    _createFile.setText("<html></html>");
  }
  
  @Override
  protected void step2_assertNodes() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method LABEL is undefined for the type TestCreateHtmlNode");
  }
  
  @Extension
  private N n = new N();
  
  private static void assertArrayEquals(final Object[] expecteds, final Object[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final byte[] expecteds, final byte[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final char[] expecteds, final char[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final short[] expecteds, final short[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final int[] expecteds, final int[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final long[] expecteds, final long[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final Object[] expecteds, final Object[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final byte[] expecteds, final byte[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final char[] expecteds, final char[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final short[] expecteds, final short[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final int[] expecteds, final int[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final long[] expecteds, final long[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final double[] expecteds, final double[] actuals, final double delta) {
    Assert.assertArrayEquals(expecteds, actuals, delta);
  }
  
  private static void assertArrayEquals(final float[] expecteds, final float[] actuals, final float delta) {
    Assert.assertArrayEquals(expecteds, actuals, delta);
  }
  
  private static void assertArrayEquals(final String message, final double[] expecteds, final double[] actuals, final double delta) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals, delta);
  }
  
  private static void assertArrayEquals(final String message, final float[] expecteds, final float[] actuals, final float delta) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals, delta);
  }
  
  private static void assertEquals(final Object expected, final Object actual) {
    Assert.assertEquals(expected, actual);
  }
  
  private static void assertEquals(final long expected, final long actual) {
    Assert.assertEquals(expected, actual);
  }
  
  private static void assertEquals(final String message, final Object expected, final Object actual) {
    Assert.assertEquals(message, expected, actual);
  }
  
  private static void assertEquals(final String message, final long expected, final long actual) {
    Assert.assertEquals(message, expected, actual);
  }
  
  private static void assertEquals(final double expected, final double actual, final double delta) {
    Assert.assertEquals(expected, actual, delta);
  }
  
  private static void assertEquals(final float expected, final float actual, final float delta) {
    Assert.assertEquals(expected, actual, delta);
  }
  
  private static void assertEquals(final String message, final double expected, final double actual, final double delta) {
    Assert.assertEquals(message, expected, actual, delta);
  }
  
  private static void assertEquals(final String message, final float expected, final float actual, final float delta) {
    Assert.assertEquals(message, expected, actual, delta);
  }
  
  private static void assertFalse(final boolean condition) {
    Assert.assertFalse(condition);
  }
  
  private static void assertFalse(final String message, final boolean condition) {
    Assert.assertFalse(message, condition);
  }
  
  private static void assertNotEquals(final Object first, final Object second) {
    Assert.assertNotEquals(first, second);
  }
  
  private static void assertNotEquals(final long first, final long second) {
    Assert.assertNotEquals(first, second);
  }
  
  private static void assertNotEquals(final String message, final Object first, final Object second) {
    Assert.assertNotEquals(message, first, second);
  }
  
  private static void assertNotEquals(final String message, final long first, final long second) {
    Assert.assertNotEquals(message, first, second);
  }
  
  private static void assertNotEquals(final double first, final double second, final double delta) {
    Assert.assertNotEquals(first, second, delta);
  }
  
  private static void assertNotEquals(final String message, final double first, final double second, final double delta) {
    Assert.assertNotEquals(message, first, second, delta);
  }
  
  private static void assertNotNull(final Object object) {
    Assert.assertNotNull(object);
  }
  
  private static void assertNotNull(final String message, final Object object) {
    Assert.assertNotNull(message, object);
  }
  
  private static void assertNotSame(final Object unexpected, final Object actual) {
    Assert.assertNotSame(unexpected, actual);
  }
  
  private static void assertNotSame(final String message, final Object unexpected, final Object actual) {
    Assert.assertNotSame(message, unexpected, actual);
  }
  
  private static void assertNull(final Object object) {
    Assert.assertNull(object);
  }
  
  private static void assertNull(final String message, final Object object) {
    Assert.assertNull(message, object);
  }
  
  private static void assertSame(final Object expected, final Object actual) {
    Assert.assertSame(expected, actual);
  }
  
  private static void assertSame(final String message, final Object expected, final Object actual) {
    Assert.assertSame(message, expected, actual);
  }
  
  private static <T extends Object> void assertThat(final T actual, final Matcher<? super T> matcher) {
    Assert.<T>assertThat(actual, matcher);
  }
  
  private static <T extends Object> void assertThat(final String reason, final T actual, final Matcher<? super T> matcher) {
    Assert.<T>assertThat(reason, actual, matcher);
  }
  
  private static void assertTrue(final boolean condition) {
    Assert.assertTrue(condition);
  }
  
  private static void assertTrue(final String message, final boolean condition) {
    Assert.assertTrue(message, condition);
  }
  
  private static void fail() {
    Assert.fail();
  }
  
  private static void fail(final String message) {
    Assert.fail(message);
  }
  
  private static <T extends Object, U extends T> void operator_doubleArrow(final T actual, final U expected) {
    Assert.assertEquals(expected, actual);
  }
  
  private static <T extends Exception> void isThrownBy(final Class<T> expected, final Procedure0 block) {
    try {
    	block.apply();
    	Assert.fail("Expected a " + expected.getName());
    } catch (Exception e) {
    	Class<?> actual = e.getClass();
    	Assert.assertTrue(
    		"Expected a " + expected.getName() + " but got " + actual.getName(), 
    		expected.isAssignableFrom(actual)
    	);
    }
  }
  
  @Rule
  @Extension
  public final ErrorCollector _errorCollector = new ErrorCollector();
  
  private <T extends Object> void operator_doubleArrow(final T object, final Matcher<? super T> matcher) {
    Assert.assertThat(object, matcher);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Iterable<Matcher<? super T>> matchers) {
    return CoreMatchers.<T>allOf(matchers);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Matcher<? super T>... matchers) {
    return CoreMatchers.<T>allOf(matchers);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second) {
    return CoreMatchers.<T>allOf(first, second);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
    return CoreMatchers.<T>allOf(first, second, third);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth) {
    return CoreMatchers.<T>allOf(first, second, third, fourth);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return CoreMatchers.<T>allOf(first, second, third, fourth, fifth);
  }
  
  private static <T extends Object> Matcher<T> allOf(final Matcher<? super T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
    return CoreMatchers.<T>allOf(first, second, third, fourth, fifth, sixth);
  }
  
  private static <T extends Object> Matcher<T> any(final Class<T> type) {
    return CoreMatchers.<T>any(type);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Iterable<Matcher<? super T>> matchers) {
    return CoreMatchers.<T>anyOf(matchers);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Matcher<? super T>... matchers) {
    return CoreMatchers.<T>anyOf(matchers);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second) {
    return CoreMatchers.<T>anyOf(first, second);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third) {
    return CoreMatchers.<T>anyOf(first, second, third);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth) {
    return CoreMatchers.<T>anyOf(first, second, third, fourth);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth) {
    return CoreMatchers.<T>anyOf(first, second, third, fourth, fifth);
  }
  
  private static <T extends Object> AnyOf<T> anyOf(final Matcher<T> first, final Matcher<? super T> second, final Matcher<? super T> third, final Matcher<? super T> fourth, final Matcher<? super T> fifth, final Matcher<? super T> sixth) {
    return CoreMatchers.<T>anyOf(first, second, third, fourth, fifth, sixth);
  }
  
  private static Matcher<Object> anything() {
    return CoreMatchers.anything();
  }
  
  private static Matcher<Object> anything(final String description) {
    return CoreMatchers.anything(description);
  }
  
  private static <LHS extends Object> CombinableMatcher.CombinableBothMatcher<LHS> both(final Matcher<? super LHS> matcher) {
    return CoreMatchers.<LHS>both(matcher);
  }
  
  private static Matcher<String> containsString(final String substring) {
    return CoreMatchers.containsString(substring);
  }
  
  private static <T extends Object> Matcher<T> describedAs(final String description, final Matcher<T> matcher, final Object... values) {
    return CoreMatchers.<T>describedAs(description, matcher, values);
  }
  
  private static <LHS extends Object> CombinableMatcher.CombinableEitherMatcher<LHS> either(final Matcher<? super LHS> matcher) {
    return CoreMatchers.<LHS>either(matcher);
  }
  
  private static Matcher<String> endsWith(final String suffix) {
    return CoreMatchers.endsWith(suffix);
  }
  
  private static <T extends Object> Matcher<T> equalTo(final T operand) {
    return CoreMatchers.<T>equalTo(operand);
  }
  
  private static <U extends Object> Matcher<Iterable<U>> everyItem(final Matcher<U> itemMatcher) {
    return CoreMatchers.<U>everyItem(itemMatcher);
  }
  
  private static <T extends Object> Matcher<Iterable<? super T>> hasItem(final T item) {
    return CoreMatchers.<T>hasItem(item);
  }
  
  private static <T extends Object> Matcher<Iterable<? super T>> hasItem(final Matcher<? super T> itemMatcher) {
    return CoreMatchers.<T>hasItem(itemMatcher);
  }
  
  private static <T extends Object> Matcher<Iterable<T>> hasItems(final T... items) {
    return CoreMatchers.<T>hasItems(items);
  }
  
  private static <T extends Object> Matcher<Iterable<T>> hasItems(final Matcher<? super T>... itemMatchers) {
    return CoreMatchers.<T>hasItems(itemMatchers);
  }
  
  private static <T extends Object> Matcher<T> instanceOf(final Class<?> type) {
    return CoreMatchers.<T>instanceOf(type);
  }
  
  private static <T extends Object> Matcher<T> is(final T value) {
    return CoreMatchers.<T>is(value);
  }
  
  private static <T extends Object> Matcher<T> is(final Matcher<T> matcher) {
    return CoreMatchers.<T>is(matcher);
  }
  
  private static <T extends Object> Matcher<T> isA(final Class<T> type) {
    return CoreMatchers.<T>isA(type);
  }
  
  private static <T extends Object> Matcher<T> not(final Matcher<T> matcher) {
    return CoreMatchers.<T>not(matcher);
  }
  
  private static <T extends Object> Matcher<T> not(final T value) {
    return CoreMatchers.<T>not(value);
  }
  
  private static Matcher<Object> notNullValue() {
    return CoreMatchers.notNullValue();
  }
  
  private static <T extends Object> Matcher<T> notNullValue(final Class<T> type) {
    return CoreMatchers.<T>notNullValue(type);
  }
  
  private static Matcher<Object> nullValue() {
    return CoreMatchers.nullValue();
  }
  
  private static <T extends Object> Matcher<T> nullValue(final Class<T> type) {
    return CoreMatchers.<T>nullValue(type);
  }
  
  private static <T extends Object> Matcher<T> sameInstance(final T target) {
    return CoreMatchers.<T>sameInstance(target);
  }
  
  private static Matcher<String> startsWith(final String prefix) {
    return CoreMatchers.startsWith(prefix);
  }
  
  private static <T extends Object> Matcher<T> theInstance(final T target) {
    return CoreMatchers.<T>theInstance(target);
  }
}
