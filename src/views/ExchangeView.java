package views;

import controller.ExchangeController;
import controller.AuthController;
import java.util.Scanner;

/**
 * ExchangeView - Handles exchange request UI
 */
public class ExchangeView extends ConsoleView {
    private ExchangeController exchangeController;
    private AuthController authController;

    public ExchangeView(Scanner scanner, ExchangeController exchangeController,
                        AuthController authController) {
        super(scanner);
        this.exchangeController = exchangeController;
        this.authController = authController;
    }

    public void showMyRequests() {
        // clearScreen();
        printHeader("MY EXCHANGE REQUESTS");

        Long userId = authController.getCurrentUser().getId();
        exchangeController.viewMyRequests(userId);

        System.out.println("\n1. Confirm Completion of Request");
        System.out.println("2. Back to Main Menu");
        printSeparator();

        int choice = readInt("Choose option: ");

        if (choice == 1) {
            handleConfirmCompletion();
        }
        pause();
    }

    public void showIncomingRequests() {
        //clearScreen();
        printHeader("INCOMING REQUESTS");

        Long userId = authController.getCurrentUser().getId();
        exchangeController.viewRequestsForMe(userId);

        System.out.println("\n1. Accept Request");
        System.out.println("2. Reject Request");
        System.out.println("3. Confirm Completion");
        System.out.println("4. Back to Main Menu");
        printSeparator();

        int choice = readInt("Choose option: ");

        switch (choice) {
            case 1:
                handleAcceptRequest();
                break;
            case 2:
                handleRejectRequest();
                break;
            case 3:
                handleConfirmCompletion();
                break;
            case 4:
                return;
        }
        pause();
    }

    private void handleAcceptRequest() {
        Long requestId = readLong("Enter Request ID to accept: ");
        Long userId = authController.getCurrentUser().getId();
        exchangeController.acceptRequest(requestId, userId);
    }

    private void handleRejectRequest() {
        Long requestId = readLong("Enter Request ID to reject: ");
        Long userId = authController.getCurrentUser().getId();
        exchangeController.rejectRequest(requestId, userId);
    }

    private void handleConfirmCompletion() {
        Long requestId = readLong("Enter Request ID to confirm completion: ");
        Long userId = authController.getCurrentUser().getId();
        exchangeController.confirmCompletion(requestId, userId);
    }
}