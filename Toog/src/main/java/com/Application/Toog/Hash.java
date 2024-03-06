package com.Application.Toog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Hash {
    // Method to hash a password
    public static String hashPassword(String password) {
        try {
            // Generate a salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Hash the password along with the salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(concatenateByteArrays(password.getBytes(),
                    salt));

            // Convert hashed password and salt to hexadecimal format
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            String hashedPassword = hexStringBuilder.toString();

            // Concatenate salt with hashed password and return
            return hashedPassword + ":" + bytesToHex(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to concatenate two byte arrays
    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    // Method to check if a plaintext password matches its hashed version
    public static boolean checkPassword(String plaintextPassword, String hashedPassword) {
        // Split hashed password into hash and salt
        String[] parts = hashedPassword.split(":");
        if (parts.length != 2) {
            return false; // Invalid format
        }
        String storedHashedPassword = parts[0];
        String storedSalt = parts[1];

        // Hash the plaintext password along with the stored salt
        String hashedPlaintextPassword = hashPassword(plaintextPassword, storedSalt);

        // Compare hashed passwords
        return storedHashedPassword.equals(hashedPlaintextPassword);
    }

    // Method to hash a password with a provided salt
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(concatenateByteArrays(password.getBytes(), hexStringToBytes(salt)));

            // Convert hashed password to hexadecimal format
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert a byte array to a hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Method to convert a hexadecimal string to a byte array
    private static byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
