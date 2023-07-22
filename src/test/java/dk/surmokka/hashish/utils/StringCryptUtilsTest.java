package dk.surmokka.hashish.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringCryptUtilsTest {
    @Test
    void givenString_whenEncrypt_thenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        String input = "baeldung";
        SecretKey key = AESUtil.generateKey(128);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";
        String cipherText = StringCryptUtils.encrypt(algorithm, input, key, ivParameterSpec);
        log.info("input : {}", input);
        log.info("cipher {}", cipherText);
        String plainText = StringCryptUtils.decrypt(algorithm, cipherText, key, ivParameterSpec);
        log.info("plainText : {}", plainText);
        Assertions.assertEquals(input, plainText);
    }
}
