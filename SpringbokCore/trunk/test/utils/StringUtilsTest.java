package utils;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {

	public void testIsEmpty() {
		String string = null;

		assertTrue("String should be empty", StringUtils.isEmpty(string));

		string = "";
		assertTrue("String should be empty", StringUtils.isEmpty(string));

		string = "  ";
		assertTrue("String should be empty", StringUtils.isEmpty(string));

		string = " AA ";
		assertFalse("String should not be empty", StringUtils.isEmpty(string));

	}

	public void testIsNotEmpty() {
		String string = null;
		assertFalse("String should not be empty", StringUtils
				.isNotEmpty(string));

		string = "";
		assertFalse("String should not be empty", StringUtils
				.isNotEmpty(string));

		string = "  ";
		assertFalse("String should not be empty", StringUtils
				.isNotEmpty(string));

		string = " AA ";
		assertTrue("String should not be empty", StringUtils.isNotEmpty(string));
	}

	public void testNormalize() {
		String string = null;
		assertEquals("Should be zero lenght ", "", StringUtils.normalize(string));
		
		string = "";
		assertEquals("Should be zero lenght ", "", StringUtils.normalize(string));
		
		string = " My Name  is Khan ";
		assertEquals("Should be My Name is Khan ", "My Name is Khan", StringUtils.normalize(string));
	}
}
