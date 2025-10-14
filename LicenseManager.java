import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The LicenseManager class handles license key generation, encryption, and
 * decryption using AES.
 */
final class LicenseManager {

    /**
     * The cryptographic algorithm used for key operations.
     */
    private final String ALGORITHM = "AES";

    /**
     * The size of the secret key in bits.
     */
    private final int KEY_SIZE = 256;

    /**
     * The SecretKey object used for encryption and decryption.
     */
    private SecretKey mSecretKey;

    /**
     * The file path where the secret key is stored.
     */
    private static final String SECRET_KEY_FILE_PATH = "secret.key";

    LicenseManager() {
    }

    /**
     * Generates a new AES secret key with the specified key size.
     *
     * @return A newly generated SecretKey object.
     * @throws Exception If an error occurs during key generation.
     */
    private final SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        return keyGen.generateKey();
    }

    /**
     * Retrieves the existing secret key from a file, or generates a new one if it
     * doesn't exist.
     *
     * @return The SecretKey object
     * @throws Exception If an error occurs during key generation or reading from
     *                   file.
     */
    private final void readSecretKeyFromFile() throws Exception {
        // Try to read the secret key from the file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SECRET_KEY_FILE_PATH))) {
            this.mSecretKey = (SecretKey) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, so generate a new key and save it to the file
            throw new Exception("Error reading the secret key from file");
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error reading the secret key from file", e);
        }
    }

    /**
     * Generates a new secret key and saves it to a file. If the file already
     * exists, an error message is printed.
     * <p>
     * This method attempts to generate a secret key using {@link #generateKey()}.
     * If successful,
     * it checks if a file at {@code SECRET_KEY_FILE_PATH} exists. If not, it
     * creates the file and
     * writes the secret key to it. If an error occurs during file creation or
     * writing, it prints
     * the stack trace of the exception.
     * </p>
     */
    final void createNewSecretKeyFile() {

        SecretKey secretKey = null;
        try {
            secretKey = generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Files.exists(new File(SECRET_KEY_FILE_PATH).toPath())) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SECRET_KEY_FILE_PATH))) {
                oos.writeObject(secretKey);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {

            System.err.println(
                    "ERROR: Secret key file already exists.\nPlease make sure to backup your old key first and put it in another folder named with date and time");

        }

    }

    /**
     * Encrypts the provided data using the specified secret key.
     *
     * @param key  The SecretKey used for encryption.
     * @param data The plaintext string to be encrypted.
     * @return The Base64-encoded encrypted data as a string.
     * @throws Exception If an error occurs during encryption.
     */
    private final String encrypt(SecretKey key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * Decrypts the provided data using the specified secret key.
     *
     * @param key  The SecretKey used for decryption.
     * @param data The Base64-encoded encrypted string to be decrypted.
     * @return The plaintext string after decryption.
     * @throws Exception If an error occurs during decryption.
     */
    private final String decrypt(SecretKey key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decryptedData);
    }

    /**
     * Creates a finalized (encrypted) license key from a human-readable key.
     *
     * @param humanReadableKey The plaintext license key to be encrypted.
     * @throws Exception If an error occurs during encryption or reading the secret
     *                   key.
     */
    final void createFinalizedLicenceKeyFromHumanReadAbleKey(String humanReadableKey) throws Exception {

        this.readSecretKeyFromFile();

        String encryptedLicenseKey = this.encrypt(this.mSecretKey, humanReadableKey);

        System.out.println("Encrypted License Key: " + encryptedLicenseKey);
        printExceptedHash(encryptedLicenseKey);

    }

    /**
     * Converts a finalized (encrypted) license key back into its human-readable
     * form.
     *
     * @param finalizedLicenseKey The Base64-encoded encrypted license key to be
     *                            decrypted.
     * @throws Exception If an error occurs during decryption or reading the secret
     *                   key.
     */
    final void convertFinalizedKeyIntoHumanReadAbleLicenceKey(String finalizedLicenseKey) throws Exception {

        this.readSecretKeyFromFile();
        final String output = this.decrypt(mSecretKey, finalizedLicenseKey);

        System.out.println(output);

    }

    /**
     * Calculates the SHA-256 hash for the given input string and prints it.
     *
     * @param input The plaintext string to be hashed.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    private void printExceptedHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        String calculatedHash = bytesToHex(hashBytes);
        System.out.println("Use this for DB: " + calculatedHash);

    }

    /**
     * Converts a byte array to its hexadecimal string representation.
     *
     * @param bytes The byte array to be converted.
     * @return A string representing the hexadecimal value of the input byte array.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
