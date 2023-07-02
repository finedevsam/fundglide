package com.savitech.fintab.util;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

@Component
public class Encryption {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    // Encrypts the given plaintext with the provided key
    public static byte[] encrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    // Decrypts the given ciphertext with the provided key
    public static byte[] decrypt(byte[] key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }


    private static byte[] generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private static byte[] serializedData(Map<Object, Object> originalMap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(originalMap);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static Map<Object, Object> deserializeData(byte[] decryptedData) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Map<Object, Object> decryptedMap = (Map<Object, Object>) objectInputStream.readObject();
        objectInputStream.close();
        return decryptedMap;
    }

    public Pair<byte[], String> encryptData(Map<Object, Object> dataToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] key = generateKey();
        byte[] serializedData = serializedData(dataToEncrypt);
        byte[] encryptedData = encrypt(key, serializedData);
        String encryptedMessage = Base64.getEncoder().encodeToString(encryptedData);

        return Pair.of(key, encryptedMessage);
    }

    public Map<?, ?> decryptData(String encryptedData, byte[] key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException, ClassNotFoundException {
        byte[] decryptedData = decrypt(key, Base64.getDecoder().decode(encryptedData));

        // Deserialize the decrypted data back into a Map
        return deserializeData(decryptedData);
    }
//    public static void main(String[] args) {
//        try {
//            // Create a sample Map
//            Map<Object, Object> originalMap = new HashMap<>();
//            originalMap.put("Key1", "Value1");
//            originalMap.put("Key2", "Value2");
////            originalMap.put("Key3", "Value3");
//
//            // Encrypt the serialized data
//            Pair<byte[], String> encryptedData = encryptData(originalMap);
//            String encryptedMessage = encryptedData.getSecond();
//            byte[] key = encryptedData.getFirst();
//            System.out.println(key);
//
//
//            // Decrypt the encrypted data
//            Map<?, ?> decryptedMap = decryptData(encryptedMessage, key);
//
//
//            // Output results
//            System.out.println("Original Map: " + originalMap);
//            System.out.println("Encrypted message: " + encryptedMessage);
//            System.out.println("Length: " + encryptedMessage.length());
//            System.out.println("Decrypted Map: " + decryptedMap);
//            System.out.println("Code: " + encryptedMessage.substring(15, 30));
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
