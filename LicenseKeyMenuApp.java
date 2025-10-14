import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LicenseKeyMenuApp {

    final static LicenseManager licenseManager = new LicenseManager();;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println(" ███████████  █████   ███   █████  █████████   █████████     █████        ███                                                   █████   ████                    \r\n" + //
                        "░░███░░░░░███░░███   ░███  ░░███  ███░░░░░███ ███░░░░░███   ░░███        ░░░                                                   ░░███   ███░                     \r\n" + //
                        " ░███    ░███ ░███   ░███   ░███ ░███    ░░░ ░███    ░░░     ░███        ████   ██████   ██████  ████████    █████   ██████     ░███  ███     ██████  █████ ████\r\n" + //
                        " ░██████████  ░███   ░███   ░███ ░░█████████ ░░█████████     ░███       ░░███  ███░░███ ███░░███░░███░░███  ███░░   ███░░███    ░███████     ███░░███░░███ ░███ \r\n" + //
                        " ░███░░░░░░   ░░███  █████  ███   ░░░░░░░░███ ░░░░░░░░███    ░███        ░███ ░███ ░░░ ░███████  ░███ ░███ ░░█████ ░███████     ░███░░███   ░███████  ░███ ░███ \r\n" + //
                        " ░███          ░░░█████░█████░    ███    ░███ ███    ░███    ░███      █ ░███ ░███  ███░███░░░   ░███ ░███  ░░░░███░███░░░      ░███ ░░███  ░███░░░   ░███ ░███ \r\n" + //
                        " █████           ░░███ ░░███     ░░█████████ ░░█████████     ███████████ █████░░██████ ░░██████  ████ █████ ██████ ░░██████     █████ ░░████░░██████  ░░███████ \r\n" + //
                        "░░░░░             ░░░   ░░░       ░░░░░░░░░   ░░░░░░░░░     ░░░░░░░░░░░ ░░░░░  ░░░░░░   ░░░░░░  ░░░░ ░░░░░ ░░░░░░   ░░░░░░     ░░░░░   ░░░░  ░░░░░░    ░░░░░███ \r\n" + //
                        "                                                                                                                                                       ███ ░███ \r\n" + //
                        "                                                                                                                                                      ░░██████  \r\n" + //
                        "                                                                                                                                                       ░░░░░░  ");

        while (running) {
            System.out.println("\n--- PWSS License Key Generator ---");
            System.out.println("1. Create new License Key (human readable)");
            System.out.println("2. Create Finalized License Key");
            System.out.println("3. Convert Finalized Key Into License Key");
            System.out.println("4. Create new Secret key");
            System.out.println("5. Exit");

            System.out.print("Please select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createNewLicenseKey();
                    break;
                case 2:
                    createFinalizedLicenseKey(scanner);
                    break;
                case 3:
                    convertFinalizedKeyToLicenseKey(scanner);
                    break;
                case 4:
                    createNewSecretKey();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            // Pause execution before showing menu again
            pause(scanner);

        }

        scanner.close();
    }

    private static void createNewLicenseKey() throws NoSuchAlgorithmException {
        System.out.println("Creating new License Key (human readable)...");

        LicenseKeyGenerator licenseKeyGenerator = new LicenseKeyGenerator();
        licenseKeyGenerator.generateLicenseKey();
    }

    private static void createFinalizedLicenseKey(Scanner scanner) throws Exception {
        System.out.print("Please enter the input string: ");
        String input = scanner.nextLine();

        System.out.println("Creating finalized License Key with input: " + input);
        LicenseManager licenseManager = new LicenseManager();
        licenseManager.createFinalizedLicenceKeyFromHumanReadAbleKey(input);
    }

    private static void convertFinalizedKeyToLicenseKey(Scanner scanner) throws Exception {
        System.out.print("Please enter the Finalized Key to convert: ");
        String input = scanner.nextLine();

        System.out.println("Converting Finalized Key to License Key...");
         
         licenseManager.convertFinalizedKeyIntoHumanReadAbleLicenceKey(input);
    }

     /**
     * Pauses execution until the user presses Enter.
     */
    private static void pause(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine(); // Wait for user input
    }

    private static final void createNewSecretKey() {
            System.out.println("Creating new Secret Key...");
            licenseManager.createNewSecretKeyFile();
    }
}