package views;

import controller.AuthController;
import dto.UserLoginDTO;
import dto.UserRegistrationDTO;
import java.util.Scanner;

/**
 * AuthView - Handles authentication UI (Login, Register)
 */
public class AuthView extends ConsoleView {
    private AuthController authController;

    public AuthView(Scanner scanner, AuthController authController) {
        super(scanner);
        this.authController = authController;
    }

    public void showAuthMenu() {
        while (true) {
            // clearScreen();
            printHeader("SKILL EXCHANGE PLATFORM");
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            printSeparator();

            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1:
                    handleLogin();
                    if (authController.isLoggedIn()) {
                        return; // Exit to main menu
                    }
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    System.out.println("\nThank you for using Skill Exchange Platform!");
                    System.exit(0);
                default:
                    System.out.println("✗ Invalid option!");
                    pause();
            }
        }
    }

    private void handleLogin() {
        // clearScreen();
        printHeader("LOGIN");

        String username = readString("Username: ");
        String password = readString("Password: ");

        UserLoginDTO dto = new UserLoginDTO(username, password);
        boolean success = authController.login(dto);

        if (!success) {
            pause();
        }
    }

    private void handleRegister() {
        // clearScreen();
        printHeader("REGISTER NEW ACCOUNT");

        String username = readString("Username: ");
        String password = readString("Password (min 6 chars): ");
        String fullName = readString("Full Name: ");
        String email = readString("Email: ");
        String phone = readString("Phone: ");

        UserRegistrationDTO dto = new UserRegistrationDTO(username, password, fullName, email, phone);
        authController.register(dto);

        pause();
    }
}