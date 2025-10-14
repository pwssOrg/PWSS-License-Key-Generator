import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.UUID;

final class LicenseKeyGenerator {
    private static final String ALGORITHM = "SHA-256";

    LicenseKeyGenerator() {
    }

    void generateLicenseKeyForUser() throws NoSuchAlgorithmException {

        LocalDate expirationDate = LocalDate.of(2027, 10, 14);
        String featureFlags = "USER,SUPPORT=YES";

        generateLicenseKey(expirationDate, featureFlags);
    }

    void generateLicenseKeyForProUser() throws NoSuchAlgorithmException {

        LocalDate expirationDate = LocalDate.of(2030, 10, 14);
        String featureFlags = "PRO-USER,SUPPORT=YES";

        generateLicenseKey(expirationDate, featureFlags);
    }
    

    void generateLicenseKey(LocalDate expirationDate, String featureFlags) throws NoSuchAlgorithmException {

        final String uniqueId = getRandomtUuid().toString();

        // Combine the components
        StringBuilder licenseKey = new StringBuilder();
        licenseKey.append(uniqueId).append("-");

        licenseKey.append(expirationDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .append("-");
        licenseKey.append(featureFlags);

        // Generate a checksum/hash
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] hashBytes = digest.digest(licenseKey.toString().getBytes());
        String checksum = bytesToHex(hashBytes);

        final String output = licenseKey.append("-").append(checksum).toString();
        System.out.println("Generated License Key: " + output);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private UUID getRandomtUuid() {

        UUID uuid = UUID.randomUUID();
        System.out.println("Generated UUID: " + uuid.toString());
        return uuid;
    }

}
