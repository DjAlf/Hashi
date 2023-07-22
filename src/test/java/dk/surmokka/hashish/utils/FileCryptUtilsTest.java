package dk.surmokka.hashish.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileCryptUtilsTest {
    @Test
    void givenFile_whenEncrypt_thenSuccess() 
        throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, 
        InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, 
        NoSuchPaddingException {
        
        SecretKey key = AESUtil.generateKey(128);
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        File whereAmI = new File(".");
        log.info("im here {}", whereAmI.getAbsolutePath());    
        File inputFile = new File("inputFile/baeldung.txt");

        File encryptedFile = new File("baeldung.encrypted");
        File decryptedFile = new File("document.decrypted");

        FileCryptUtils.encryptFile(algorithm, key, ivParameterSpec, inputFile, encryptedFile);
        ivParameterSpec = AESUtil.generateIv();
        FileCryptUtils.decryptFile(algorithm, key, ivParameterSpec, encryptedFile, decryptedFile);
            assertTrue( filesEqual(inputFile, decryptedFile));
    }
    
    @Test
    public void testFileEncryptionByPassword () throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException {
        File inputFile = new File("inputFile/baeldung.txt");

        File encryptedFile = new File("baeldung.password.encrypted");
        File decryptedFile = new File("document.password.decrypted");

        FileCryptUtils.encryptFileByPassword(inputFile, encryptedFile, "vestbanevej");
        FileCryptUtils.decryptFileByPassword(encryptedFile, decryptedFile, "vestbanevej");
                assertTrue( filesEqual(inputFile, decryptedFile));
    }
    
    @Test
    public void testFileDeEncryptionByPassword () throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException {
        
        File inputFile = new File("inputFile/baeldung.txt");
        File encryptedFile = new File("baeldung.password.encrypted");
        File decryptedFile = new File("document.password2.decrypted");

       
        FileCryptUtils.decryptFileByPassword(encryptedFile, decryptedFile, "vestbanevej");
        assertTrue( filesEqual(inputFile, decryptedFile));
    }

    protected Boolean filesEqual( File file1 , File file2  ) throws FileNotFoundException
    {
        Scanner scanner1 = new Scanner(file1);
        Scanner scanner2 = new Scanner(file2);
    
        boolean filesAreEqual = true;
        while (scanner1.hasNextLine() && scanner2.hasNextLine()) {
          String line1 = scanner1.nextLine();
          String line2 = scanner2.nextLine();
    
          if (!line1.equals(line2)) {
            filesAreEqual = false;
            break;
          }
        }
        scanner1.close();
        scanner2.close();
        return filesAreEqual;
    }

}
