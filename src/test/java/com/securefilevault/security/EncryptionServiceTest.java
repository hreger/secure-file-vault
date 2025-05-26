package com.securefilevault.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Files;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Encryption Service Tests")
class EncryptionServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(EncryptionServiceTest.class);

    private EncryptionService encryptionService;
    
    @Mock
    private SecureRandom secureRandom;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encryptionService = new EncryptionService(secureRandom);
        logger.debug("Test setup completed");
    }

    @Nested
    @DisplayName("Key Generation Tests")
    class KeyGenerationTests {
        
        @Test
        @DisplayName("Should generate key with correct specifications")
        void generateKeyTest() {
            // Given
            when(secureRandom.generateSeed(16)).thenReturn(new byte[16]);
            logger.debug("Testing key generation with mocked secure random");

            // When
            SecretKey key = encryptionService.generateKey();

            // Then
            assertNotNull(key, "Generated key should not be null");
            assertEquals(256, key.getEncoded().length * 8, "Key should be 256 bits");
            verify(secureRandom).generateSeed(16);
            logger.debug("Key generation test completed successfully");
        }

        @Test
        @DisplayName("Should generate unique keys")
        void generateUniqueKeysTest() {
            // When
            SecretKey key1 = encryptionService.generateKey();
            SecretKey key2 = encryptionService.generateKey();

            // Then
            assertNotEquals(
                key1.getEncoded(),
                key2.getEncoded(),
                "Generated keys should be unique"
            );
        }
    }

    @Nested
    @DisplayName("File Encryption and Decryption Tests")
    class FileEncryptionTests {

        @Test
        @DisplayName("Should successfully encrypt and decrypt file")
        void encryptAndDecryptTest() throws IOException {
            // Given
            String originalContent = "Test content for encryption";
            Path testFile = tempDir.resolve("test.txt");
            Path encryptedFile = tempDir.resolve("test.encrypted");
            Path decryptedFile = tempDir.resolve("test.decrypted");
            
            Files.writeString(testFile, originalContent);
            logger.debug("Created test file with content");

            // When
            SecretKey key = encryptionService.generateKey();
            logger.debug("Encrypting file");
            encryptionService.encryptFile(testFile, encryptedFile, key);
            
            logger.debug("Decrypting file");
            encryptionService.decryptFile(encryptedFile, decryptedFile, key);

            // Then
            assertTrue(encryptedFile.toFile().exists(), "Encrypted file should exist");
            assertTrue(decryptedFile.toFile().exists(), "Decrypted file should exist");
            assertNotEquals(
                Files.size(testFile),
                Files.size(encryptedFile),
                "Encrypted file size should differ from original"
            );
            
            String decryptedContent = Files.readString(decryptedFile);
            assertEquals(originalContent, decryptedContent, "Decrypted content should match original");
            logger.debug("Encryption and decryption test completed successfully");
        }
    }

        @Test
        @DisplayName("Should handle empty file encryption")
        void emptyFileEncryptionTest() throws IOException {
            // Given
            Path emptyFile = tempDir.resolve("empty.txt");
            Path encryptedFile = tempDir.resolve("empty.encrypted");
            Path decryptedFile = tempDir.resolve("empty.decrypted");
            
            Files.createFile(emptyFile);
            SecretKey key = encryptionService.generateKey();

            // When/Then
            assertDoesNotThrow(() -> {
                encryptionService.encryptFile(emptyFile, encryptedFile, key);
                encryptionService.decryptFile(encryptedFile, decryptedFile, key);
            }, "Should handle empty file encryption/decryption");
            
            assertEquals(0, Files.size(decryptedFile), "Decrypted empty file should be empty");
        }

        @Test
        @DisplayName("Should fail encryption with invalid key")
        void encryptionFailsWithInvalidKey() throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Path encryptedFile = tempDir.resolve("test.encrypted");
            Files.writeString(testFile, "test content");
            SecretKey invalidKey = mock(SecretKey.class);

            // When/Then
            assertThrows(EncryptionException.class, () -> {
                encryptionService.encryptFile(testFile, encryptedFile, invalidKey);
            }, "Encryption should fail with invalid key");
            logger.debug("Invalid key encryption test completed");
    }

        @Test
        @DisplayName("Should fail decryption with wrong key")
        void decryptionFailsWithWrongKey() throws IOException {
            // Given
            Path testFile = tempDir.resolve("test.txt");
            Path encryptedFile = tempDir.resolve("test.encrypted");
            Path decryptedFile = tempDir.resolve("test.decrypted");
            Files.writeString(testFile, "test content");

            SecretKey correctKey = encryptionService.generateKey();
            SecretKey wrongKey = encryptionService.generateKey();

            // When
            encryptionService.encryptFile(testFile, encryptedFile, correctKey);
            logger.debug("Attempting decryption with wrong key");

            // Then
            assertThrows(DecryptionException.class, () -> {
                encryptionService.decryptFile(encryptedFile, decryptedFile, wrongKey);
            }, "Decryption should fail with wrong key");
            logger.debug("Wrong key decryption test completed");
    }

    @Nested
    @DisplayName("IV Generation Tests")
    class IvGenerationTests {
        
        @Test
        @DisplayName("Should generate IV with correct specifications")
        void generateIvTest() {
            // When
            byte[] iv = encryptionService.generateIv();
            logger.debug("Generated IV for testing");

            // Then
            assertNotNull(iv, "Generated IV should not be null");
            assertEquals(12, iv.length, "IV should be 12 bytes for GCM mode");
        }

        @Test
        @DisplayName("Should generate unique IVs")
        void generateUniqueIvsTest() {
            // When
            byte[] iv1 = encryptionService.generateIv();
            byte[] iv2 = encryptionService.generateIv();

            // Then
            assertFalse(
                java.util.Arrays.equals(iv1, iv2),
                "Generated IVs should be unique"
            );
            logger.debug("Unique IVs test completed");
        }
    }
}