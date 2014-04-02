package trainingmgr.mgr.training.auth;

import org.junit.Assert;
import org.junit.Test;

import com.mgr.training.auth.PasswordDigest;

public class PasswordDigestTest {
	public static final PasswordDigest passwordDigest = new PasswordDigest();

	@Test
	public final void passwordDigestTest() {
		String password = "test";
		String wrongPassword = "wrong password";
		byte[] passwordHash = passwordDigest.digest(password);
		Assert.assertTrue(passwordDigest.verify(passwordHash, password));
		Assert.assertFalse(passwordDigest.verify(passwordHash, wrongPassword));
	}

	@Test
	public final void emptyPasswordDigestTest() {
		String password = "";
		String wrongPassword = "wrong password";
		byte[] passwordHash = passwordDigest.digest(password);
		Assert.assertTrue(passwordDigest.verify(passwordHash, password));
		Assert.assertFalse(passwordDigest.verify(passwordHash, wrongPassword));
	}

	@Test
	public final void longPasswordDigestTest() {
		String password = "my very long password with number : 1234567890";
		String wrongPassword = "wrong password";
		byte[] passwordHash = passwordDigest.digest(password);
		Assert.assertTrue(passwordDigest.verify(passwordHash, password));
		Assert.assertFalse(passwordDigest.verify(passwordHash, wrongPassword));
	}

	@Test
	public final void passwordWithSpecialCharDigestTest() {
		String password = "password with special char: ~!@#$%^&*()_+}|:\"<?";
		String wrongPassword = "wrong password";
		byte[] passwordHash = passwordDigest.digest(password);
		Assert.assertTrue(passwordDigest.verify(passwordHash, password));
		Assert.assertFalse(passwordDigest.verify(passwordHash, wrongPassword));
	}
}
