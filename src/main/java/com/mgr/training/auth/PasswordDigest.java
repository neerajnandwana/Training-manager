package com.mgr.training.auth;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.inject.Singleton;
import com.mgr.training.util.Const;
import com.mgr.training.util.Prop;

@Singleton
public class PasswordDigest {
	public static final int MINIMUM_SALT_LENGTH = 10;
	public static final HashFunction SHA_512 = Hashing.sha512();

	public boolean verify(final byte[] digestedPassword, final String userPassword) {
		checkNotNull(digestedPassword, "Digested Password is null");
		checkNotNull(userPassword, "User Password is null");
		return Arrays.equals(digestedPassword, digest(userPassword));
	}

	/**
	 * This function will salt the password and hash it N times
	 * 
	 * Always hash string password in byte array and persist it as blob because,
	 * SHA produces raw byte array and there is no guarantee that arbitrary
	 * byte[] will produce valid String irrespective to the encoding
	 **/
	public byte[] digest(final String userPass) {
		int repeat = Prop.applicationConfig.getInt(Const.HASH_REPEAT_KEY);
		String salt = Prop.applicationConfig.getString(Const.HASH_SALT_KEY);

		checkNotNull(salt, "Salt is null");
		checkNotNull(userPass, "Pass is null");
		// A short salt makes passwords susceptible to rainbow tables.
		checkArgument(salt.length() >= 10, "Salt length %d is too short. It must be at least %d to avoid rainbow table attacks.", salt.length(),
				MINIMUM_SALT_LENGTH);

		byte[] hashedBytes = hash(salt, userPass); // salt and hash the password
		for (int count = 0; count < repeat; count++) { // repeat hashing
			hashedBytes = hash(hashedBytes);
		}
		return hashedBytes;
	}

	private byte[] hash(byte[]... bytes) {
		checkNotNull(bytes, "Bytes is null");
		Hasher hasher = SHA_512.newHasher();
		for (byte[] b : bytes) {
			hasher.putBytes(b);
		}
		return hasher.hash().asBytes();
	}

	private byte[] hash(String... strings) {
		checkNotNull(strings, "String is null");
		Hasher hasher = SHA_512.newHasher();
		for (String string : strings) {
			hasher.putString(string, Const.CHAR_SET);
		}
		return hasher.hash().asBytes();
	}
}
