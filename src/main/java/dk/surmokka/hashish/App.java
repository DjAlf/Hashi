package dk.surmokka.hashish;

import java.io.Console;
import java.io.File;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import dk.surmokka.hashish.utils.FileCryptUtils;

@SpringBootApplication
public class App {
    public static void main(String[] args) throws Exception {
        
        if ( args.length > 0 ) {
            Console c = System.console();
            char[] pass =  c.readPassword("enter password : ");
            try {
            if ( args[0].equals("-d") || args[0].equals("-e")) {
               if ( args[0].equals("-d") ) {
                    File encryptedFile = new File(args[1]);
                    File decryptedFile = new File(args[1] + ".decrypted");       
                    FileCryptUtils.decryptFileByPassword(encryptedFile, decryptedFile, new String(pass));
               } else {
                    File inputFile = new File(args[1]);
                    File encryptedFile = new File(args[1] + ".encrypted");       
                    FileCryptUtils.encryptFileByPassword(inputFile, encryptedFile, new String(pass));    
               }
            }    
        } catch ( RuntimeException | javax.crypto.BadPaddingException ex ) {
            System.out.println("hrmmmmm");
        }

        } else {
            System.out.println("Usage : option filename ");
            System.out.println("-d decrypt");
            System.out.println("-e decrypt");
        }
        



    }
}
