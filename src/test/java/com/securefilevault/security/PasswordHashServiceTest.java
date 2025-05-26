package com.securefilevault.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Password Hash Service Tests")
class PasswordHashServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PasswordHashServiceTest.class);

    private PasswordHashService passwordHashService;

    @Mock
    private SecureRandom secureRandom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordHashService = new PasswordHashService(secureRandom);
        logger.debug("Test setup completed");
    }

    @Nested
    @DisplayName("Hash Generation Tests")
    class HashGenerationTests {

        @Test
        @DisplayName("Should generate hash with correct format")
        void generateHashTest() {
            // Given
            String password = "TestPassword123!";
            byte[] mockSalt = new byte[32];
            when(secureRandom.generateSeed(32)).thenReturn(mockSalt);
            logger.debug("Testing hash generation with mock salt");

            // When
            String hash = passwordHashService.hashPassword(password);

            // Then
            assertNotNull(hash, "Generated hash should not be null");
            assertTrue(hash.contains("$"), "Hash should contain delimiter");
            String[] parts = hash.split("\\$");
            assertEquals(3, parts.length, "Hash should have three parts");
            assertEquals("PBKDF2", parts[0], "Algorithm should be PBKDF2");
            
            // Verify salt and hash are Base64 encoded
            assertDoesNotThrow(() -> Base64.getDecoder().decode(parts[1]),
                "Salt should be Base64 encoded");
            assertDoesNotThrow(() -> Base64.getDecoder().decode(parts[2]),
                "Hash should be Base64 encoded");
            
            logger.debug("Hash generation test completed successfully");
        }

        @Test
        @DisplayName("Should generate different hashes for same password")
        void generateUniqueHashesTest() {
            // Given
            String password = "TestPassword123!";
            when(secureRandom.generateSeed(32))
                .thenReturn(new byte[32])
                .thenReturn(new byte[32]);

            // When
            String hash1 = passwordHashService.hashPassword(password);
            String hash2 = passwordHashService.hashPassword(password);

            // Then
            assertNotEquals(hash1, hash2, "Hashes should be different due to different salts");
            logger.debug("Unique hashes test completed");
        }
    }

    @Nested
    @DisplayName("Password Verification Tests")
    class PasswordVerificationTests {

        @Test
        @DisplayName("Should verify correct password")
        void verifyCorrectPasswordTest() {
            // Given
            String password = "TestPassword123!";
            String hash = passwordHashService.hashPassword(password);
            logger.debug("Testing password verification with correct password");

            // When
            boolean result = passwordHashService.verifyPassword(password, hash);

            // Then
            assertTrue(result, "Password verification should succeed with correct password");
            logger.debug("Correct password verification test completed");
        }

        @Test
        @DisplayName("Should reject incorrect password")
        void rejectIncorrectPasswordTest() {
            // Given
            String correctPassword = "TestPassword123!";
            String wrongPassword = "WrongPassword123!";
            String hash = passwordHashService.hashPassword(correctPassword);
            logger.debug("Testing password verification with incorrect password");

            // When
            boolean result = passwordHashService.verifyPassword(wrongPassword, hash);

            // Then
            assertFalse(result, "Password verification should fail with incorrect password");
            logger.debug("Incorrect password verification test completed");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty password")
        void handleEmptyPasswordTest() {
            // Given
            String emptyPassword = "";
            logger.debug("Testing hash generation with empty password");

            // When/Then
            assertThrows(IllegalArgumentException.class,
                () -> passwordHashService.hashPassword(emptyPassword),
                "Should throw exception for empty password");
            logger.debug("Empty password test completed");
        }

        @Test
        @DisplayName("Should handle null password")
        void handleNullPasswordTest() {
            // When/Then
            assertThrows(IllegalArgumentException.class,
                () -> passwordHashService.hashPassword(null),
                "Should throw exception for null password");
            logger.debug("Null password test completed");
        }

        @Test
        @DisplayName("Should handle invalid hash format")
        void handleInvalidHashFormatTest() {
            // Given
            String password = "TestPassword123!";
            String invalidHash = "InvalidHashFormat";
            logger.debug("Testing password verification with invalid hash format");

            // When/Then
            assertThrows(IllegalArgumentException.class,
                () -> passwordHashService.verifyPassword(password, invalidHash),
                "Should throw exception for invalid hash format");
            logger.debug("Invalid hash format test completed");
        }

        @Test
        @DisplayName("Should handle very long password")
        void handleLongPasswordTest() {
            // Given
            String longPassword = "a".repeat(1000);
            logger.debug("Testing hash generation with very long password");

            // When
            String hash = passwordHashService.hashPassword(longPassword);

            // Then
            assertNotNull(hash, "Should handle long password");
            assertTrue(passwordHashService.verifyPassword(longPassword, hash),
                "Should verify long password correctly");
            logger.debug("Long password test completed");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should complete hash generation within time limit")
        void hashingPerformanceTest() {
            // Given
            String password = "TestPassword123!";
            long startTime = System.currentTimeMillis();
            logger.debug("Starting hash generation performance test");

            // When
            passwordHashService.hashPassword(password);
            long endTime = System.currentTimeMillis();

            // Then
            long duration = endTime - startTime;
            assertTrue(duration < 1000,
                "Hash generation should complete within 1 second");
            logger.debug("Hash generation completed in {} ms", duration);
        }

        @Test
        @DisplayName("Should complete verification within time limit")
        void verificationPerformanceTest() {
            // Given
            String password = "TestPassword123!";
            String hash = passwordHashService.hashPassword(password);
            long startTime = System.currentTimeMillis();
            logger.debug("Starting password verification performance test");

            // When
            passwordHashService.verifyPassword(password, hash);
            long endTime = System.currentTimeMillis();

            // Then
            long duration = endTime - startTime;
            assertTrue(duration < 1000,
                "Password verification should complete within 1 second");
            logger.debug("Password verification completed in {} ms", duration);
        }
    }
}