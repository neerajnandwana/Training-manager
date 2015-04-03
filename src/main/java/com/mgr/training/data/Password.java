package com.mgr.training.data;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * A simple, secure, immutable password class.
 *
 * Passwords are stored using a salted SHA-512 digest. To persist a password
 * object, use Java's serialization interface or save the salt and digest, and
 * recreate the password object using Password.from(salt, digest).
 *
 * Character arrays are used instead of strings so the contents can be cleared
 * before they are garbage collected. (Java's strings are immutable). Passwords
 * should never be stored as strings at any intermediate stage.
 *
 * To serialize a digest to disk / database, either use Java's Serialization or
 * store the byte arrays returned by both getSalt() and getDigest().
 *
 * To deserialize a digest from disk / database, use
 * {@code Password.from(salt, digest);}
 *
 */
public class Password implements Serializable {
	
	public static final int DEFAULT_SALT_LENGTH = 16;
	public static final int MINIMUM_SALT_LENGTH = 10;
	public static final HashFunction DIGEST_HASHING_ALGORITHM = Hashing.sha512();

	private final byte[] salt;
	private final byte[] digest;

	// The random number generator is reused, as allocating the RNG each time we
	// need one requires entropy, and is thus quite expensive.
	private static ThreadLocal<SecureRandom> rng = new ThreadLocal<SecureRandom>() {
		@Override
		protected SecureRandom initialValue() {
			return new SecureRandom();
		}
	};

	/**
	 * Create a password from the specified salt and digest.
	 */
	public static Password from(byte[] salt, byte[] digest) {
		return new Password(salt.clone(), digest.clone());
	}

	/**
	 * Create a new password object using the password provided.
	 *
	 * Callers should clear the bytes in the password array when they're done
	 * with it.
	 */
	public Password(char[] password) {
		Preconditions.checkNotNull(password, "Password is null");

		salt = generateSalt();
		digest = createPassword(password, salt);
	}

	/**
	 * Helper for deserializing passwords. Use from().
	 */
	private Password(byte[] salt, byte[] digest) {
		Preconditions.checkNotNull(salt, "Salt is null");
		Preconditions.checkNotNull(digest, "Digest is null");
		// A short salt makes passwords susceptible to rainbow tables.
		Preconditions.checkArgument(salt.length >= 10, 
				"Salt length %d is too short. It must be at least %d to avoid rainbow table attacks.", salt.length,
				MINIMUM_SALT_LENGTH);
		// Note: We don't need to check the digest length, as if the digest is
		// invalid, verify() will always fail anyway.

		this.salt = salt;
		this.digest = digest;
	}

	private static byte[] createPassword(char[] password, byte[] salt) {
		return DIGEST_HASHING_ALGORITHM
				.newHasher()
				.putBytes(salt)
				.putString(CharBuffer.wrap(password), Charsets.UTF_8)
				.hash()
				.asBytes();
	}

	private static byte[] generateSalt(int length) {
		byte[] new_salt = new byte[length];
		rng.get().nextBytes(new_salt);
		return new_salt;
	}

	private static byte[] generateSalt() {
		return generateSalt(DEFAULT_SALT_LENGTH);
	}

	/**
	 * Verify that the stored password matches the password provided.
	 *
	 * @param providedPassword
	 *            Password to compare to the password stored
	 * @return true if the passwords match, false otherwise.
	 */
	public boolean verify(char[] providedPassword) {
		Preconditions.checkNotNull(providedPassword, "Provided password is null");
		return digest != null && Arrays.equals(digest, createPassword(providedPassword, salt));
	}

	/**
	 * Get the salt bytes.
	 *
	 * @return A copy of the password's salt
	 */
	public byte[] getSalt() {
		return salt.clone();
	}

	/**
	 * Get the password digest.
	 *
	 * @return A copy of the password's digest
	 */
	public byte[] getDigest() {
		return digest.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Password))
			return false;
		Password other = (Password) obj;
		if (!Arrays.equals(this.salt, other.salt))
			return false;
		if (!Arrays.equals(this.digest, other.digest))
			return false;
		return true;
	}
}
