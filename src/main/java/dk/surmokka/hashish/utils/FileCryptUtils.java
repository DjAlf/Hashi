package dk.surmokka.hashish.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileCryptUtils {

    private static final String SALT = "1234456";
    public static void encryptFileByPassword ( File input, File output, String password ) 
   throws IOException, NoSuchPaddingException,
        NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException
    {
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword( password, SALT );
      log.info("key:{}", key);
        encryptFile( algorithm, key , ivParameterSpec, input, output );

    }

    public static void decryptFileByPassword( File input, File output, String password ) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, IOException, InvalidKeySpecException
    {
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword( password, SALT );
        log.info("key:{}", key);
        decryptFile(algorithm, key, ivParameterSpec, input, output);
    }

    public static void encryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
        File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
        NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
        
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        outputStream.write(iv.getIV());
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void decryptFile(String algorithm, SecretKey key, 
        IvParameterSpec iv, 
        File inputFile,
            File outputFile) throws IOException, NoSuchPaddingException,
        NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
        BadPaddingException, IllegalBlockSizeException {
        
            byte[] ivInFile = new byte[16];
            FileInputStream inputStream = new FileInputStream(inputFile);
            inputStream.read(ivInFile);


        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivInFile));

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();    
    
    
    }    
}
