package com.securefilevault.services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionService {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65536;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;

    public void encryptFile(File inputFile, File outputFile, String password) throws Exception {
        // Generate salt
        byte[] salt = generateSalt();
        
        // Generate key from password
        SecretKey key = generateKey(password, salt);
        
        // Generate IV
        byte[] iv = generateIV();
        
        // Initialize cipher
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        
        // Write salt and IV to output file
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(salt);
            fos.write(iv);
            
            // Encrypt file content
            try (FileInputStream fis = new FileInputStream(inputFile)) {
                byte[] inputBytes = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(inputBytes)) != -1) {
                    byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                    if (outputBytes != null) {
                        fos.write(outputBytes);
                    }
                }
                byte[] finalBytes = cipher.doFinal();
                if (finalBytes != null) {
                    fos.write(finalBytes);
                }
            }
        }
    }

    public void decryptFile(File inputFile, File outputFile, String password) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            // Read salt and IV
            byte[] salt = new byte[SALT_LENGTH];
            byte[] iv = new byte[IV_LENGTH];
            fis.read(salt);
            fis.read(iv);
            
            // Generate key from password
            SecretKey key = generateKey(password, salt);
            
            // Initialize cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            
            // Decrypt file content
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                byte[] inputBytes = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(inputBytes)) != -1) {
                    byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                    if (outputBytes != null) {
                        fos.write(outputBytes);
                    }
                }
                byte[] finalBytes = cipher.doFinal();
                if (finalBytes != null) {
                    fos.write(finalBytes);
                }
            }
        }
    }

    private SecretKey generateKey(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);
        return iv;
    }
} 