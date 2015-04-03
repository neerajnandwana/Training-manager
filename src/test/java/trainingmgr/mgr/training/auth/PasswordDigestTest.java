package trainingmgr.mgr.training.auth;

import org.junit.Assert;
import org.junit.Test;

import com.mgr.training.data.Password;

public class PasswordDigestTest {

	@Test
	public final void passwordDigestTest() {
		String password = "test";
		String wrongPassword = "wrong password";
		Password passwordObj = new Password(password.toCharArray());
		Assert.assertTrue(passwordObj.verify(password.toCharArray()));
		Assert.assertFalse(passwordObj.verify(wrongPassword.toCharArray()));
	}

	@Test
	public final void emptyPasswordDigestTest() {
		String password = "";
		String wrongPassword = "wrong password";
		Password passwordObj = new Password(password.toCharArray());
		Assert.assertTrue(passwordObj.verify(password.toCharArray()));
		Assert.assertFalse(passwordObj.verify(wrongPassword.toCharArray()));
	}

	@Test
	public final void longPasswordDigestTest() {
		String password = "my very long password with number : 1234567890";
		String wrongPassword = "wrong password";
		Password passwordObj = new Password(password.toCharArray());
		Assert.assertTrue(passwordObj.verify(password.toCharArray()));
		Assert.assertFalse(passwordObj.verify(wrongPassword.toCharArray()));
	}

	@Test
	public final void passwordWithSpecialCharDigestTest() {
		String password = "password with special char: ~!@#$%^&*()_+}|:\"<?";
		String wrongPassword = "wrong password";
		Password passwordObj = new Password(password.toCharArray());
		Assert.assertTrue(passwordObj.verify(password.toCharArray()));
		Assert.assertFalse(passwordObj.verify(wrongPassword.toCharArray()));
	}
}
