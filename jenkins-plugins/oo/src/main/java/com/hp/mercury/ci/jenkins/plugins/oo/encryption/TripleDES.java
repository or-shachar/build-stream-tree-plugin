package com.hp.mercury.ci.jenkins.plugins.oo.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * this class is taken from the OO client jar, which very slight modifications
 * to fit our needs.
 * since i am not overly familiar with it, we use it as a black box, and the
 * contents is not documented.
 *
 * it provides 3xDES encryption and decryption functionality.
 * if the password used is known, the messages can be decrypted.
 * this class contains a hardcoded password so it's not secure.
 *
 * DES is an old encryption method that is currently regarded as insecure, due
 * to modern computer strength, and can be overcome by brute-force attack.
 * it should be sufficient for non - critical commercial use.
 * i wouldn't encrypt credit cards with it.
 * http://en.wikipedia.org/wiki/Data_Encryption_Standard
 *
 * Triple DES is a more secure alternative that is based on applying DES 3 times,
 * it should be safe until around 2030
 * http://en.wikipedia.org/wiki/Triple_DES
 *
 * TODO TRIPLE DES encryption with dynamnic keys can be moved to commons module, and hardcoded keys should remain in this class and wrap that logic
 *
 */
public class TripleDES {

	private static String DEFAULT_CODEPAGE = "windows-1252";
	private static String ENCRYPTION_MODE = "DESede/ECB/PKCS5Padding";
	private static String ENCRYPTION_KEYSPECTYPE = "DESede";

    /**
     *
     * @param encPass encrypted data to decrypt
     * @return the decrypted data
     */
	public static String decryptPassword(String encPass) {
		try {
			return decryptString(DatatypeConverter.parseBase64Binary(encPass));
		}catch (Exception e) {
			return "";
		}
	}

    /**
     *
     * @param toHash data to md5-hash
     * @return the md5 function value when applied to the input string
     */
	private static byte[] md5Hash(String toHash) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(toHash.getBytes());
		byte[] key = new byte[24];
		System.arraycopy(digest, 0, key, 0, 16);
		System.arraycopy(digest, 0, key, 16, 8);

		return key;
	}

    //hardcoded password - bad...
	private static SecretKeySpec secretKeySpec() throws Exception {
		return new SecretKeySpec(TripleDES.md5Hash("p7%^&5p1#$%87245$pn8pw!@e\\arp0swe7fa-\\##0sz7fdgzgt$^%^$"), ENCRYPTION_KEYSPECTYPE);
	}

    //internal function that decrypts the string after it has been modified to
    //base64 encoding, making it a byte sequence
	private static String decryptString(byte[] text) throws Exception{
		SecretKey key = secretKeySpec();

		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] data = cipher.doFinal(text);

			return new String(data, 0, data.length);
		}catch(Exception e) {
			return null;
		}
	}

    //internal function that encrypts the string after it has been modified to
    //base64 encoding, making it a byte sequence
	static byte[] encryptString(byte[] text) throws Exception{
		SecretKey key = secretKeySpec();

		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] data = cipher.doFinal(text);

			return data;
		}catch(Exception e) {
			return null;
		}
	}

    /**
     *
     * @param password string to encrypt
     * @return encrypted password
     */
    public static String encryptPassword(String password) {

        byte [] encString = new byte[0];
        try {
            encString = TripleDES.encryptString(password.getBytes(DEFAULT_CODEPAGE));
        } catch (Exception e) {
            //never happens
        }
        return DatatypeConverter.printBase64Binary(encString);
    }

}