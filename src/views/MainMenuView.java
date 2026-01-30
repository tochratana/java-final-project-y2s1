package views;

import controller.AuthController;
import model.User;
import java.util.Scanner;

/**
 * MainMenuView - Main application menu after login
 */
public class MainMenuView extends ConsoleView {
    private AuthController authController;
    private SkillView skillView;
    private ExchangeView exchangeView;

    public MainMenuView(Scanner scanner, AuthController authController,
                        SkillView skillView, ExchangeView exchangeView) {
        super(scanner);
        this.authController = authController;
        this.skillView = skillView;
        this.exchangeView = exchangeView;
    }

    public void showMainMenu() {
        while (authController.isLoggedIn()) {
            // clearScreen();
            User user = authController.getCurrentUser();

            printHeader("MAIN MENU");
            System.out.println("\nWelcome, " + user.getFullName() + "!");
            printSeparator();
            System.out.println("\n1. My Skills");
            System.out.println("2. Browse Available Skills");
            System.out.println("3. My Exchange Requests");
            System.out.println("4. Incoming Requests");
            System.out.println("5. Logout");
            printSeparator();

            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1:
                    skillView.showMySkillsMenu();
                    break;
                case 2:
                    skillView.showBrowseSkillsMenu();
                    break;
                case 3:
                    exchangeView.showMyRequests();
                    break;
                case 4:
                    exchangeView.showIncomingRequests();
                    break;
                case 5:
                    authController.logout();
                    return;
                default:
                    System.out.println("✗ Invalid option!");
                    pause();
            }
        }
    }
}