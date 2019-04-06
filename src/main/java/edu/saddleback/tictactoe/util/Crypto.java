package edu.saddleback.tictactoe.util;

import java.math.BigInteger;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Crypto {
    private static final String ALGO = "AES";

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data, BigInteger sharedSecret) throws Exception {
        Key key = generateKey(sharedSecret.toByteArray());
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param ciphertext is a string
     * @return the decrypted string
     */
    public static String decrypt(String ciphertext, BigInteger sharedSecret) throws Exception {
        Key key = generateKey(sharedSecret.toByteArray());
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(ciphertext);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    private static Key generateKey(byte[] key) {
        return new SecretKeySpec(key, ALGO);
    }
}