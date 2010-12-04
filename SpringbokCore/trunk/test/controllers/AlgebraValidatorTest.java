package controllers;

import utils.AlgebraConstants;
import utils.MessageBundle;
import junit.framework.TestCase;

public class AlgebraValidatorTest extends TestCase {

	public void testValidate(){
		String algebra = AlgebraConstants.SELECT + " id=5 table1";
		String message = AlgebraValidator.validate(algebra);
		System.out.println(message);
		assertEquals(MessageBundle.getMessage(MessageBundle.SELECT_MISSING_TABLE_NAME), message);
	}
}
