package core.utilities;

import org.apache.commons.codec.binary.Base64;

/**
 * The Encrypt class contains encryption and decryption methods using the Apache
 * Base64 codec. Use these methods to mask or encrypt sensitive data such as
 * plain text passwords that are store in properties files, etc.
 */
public class Encrypt {
	/**
	 * Encrypts a given String and returns an encoded version of it. For
	 * example, if you enter encryptPassword("MyPassword123") as the string to
	 * be encoded this method will return the encrypted string
	 * "TXlQYXNzd29yZDEyMw==". The encrypted password can be decrypted by using
	 * the decryptPassword() method.
	 *
	 * @param password
	 *            string to be encrypted
	 * @return encrypted version of given string password
	 */
	public static String encryptPassword(String password) {

		// Ecrypt a password
		byte[] encodedPwdBytes = Base64.encodeBase64(password.getBytes());
		return new String(encodedPwdBytes);
	}

	/**
	 * Decrypts a given String and returns the original version of it. For
	 * example, if you encrypt a password using encryptPassword("MyPassword123")
	 * and "MyPassword123" is the string to be encoded the encryptPassword()
	 * method will return the encrypted string "TXlQYXNzd29yZDEyMw==". The
	 * encrypted password can be decrypted by using the decryptPassword()
	 * method: For example, decryptPassword("TXlQYXNzd29yZDEyMw==") will return
	 * "MyPassword123"
	 *
	 * @param encryptedPassword
	 *            the encrypted password to be decrypted
	 * @return decrypted or original version of given encrypted password
	 */
	public static String decryptPassword(String encryptedPassword) {

		// Decrypt an encrypted password
		byte[] decodedPwdBytes = Base64.decodeBase64(encryptedPassword);
		return new String(decodedPwdBytes);

	}
}
