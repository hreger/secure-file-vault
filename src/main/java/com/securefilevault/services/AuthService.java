package com.securefilevault.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AuthService {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    
    private final DatabaseService databaseService;

    public AuthService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public boolean authenticate(String username, String password) {
        try {
            String storedHash = databaseService.getUserPasswordHash(username);
            if (storedHash == null) {
                return false;
            }

            String[] parts = storedHash.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String hash = parts[1];

            String computedHash = hashPassword(password, salt);
            return hash.equals(computedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String hashPassword(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public byte[] generateSalt() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
} 