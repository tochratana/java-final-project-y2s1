import config.DatabaseConfig;
import controller.*;
import repository.impl.ExchangeRequestRepositoryImpl;
import service.*;
import service.impl.*;
import repository.*;
import repository.impl.*;
import views.*;

import java.sql.Connection;
import java.util.Scanner;

public class PlatformApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // init database
            System.out.println("Initializing Skill Exchange Platform...");
            DatabaseConfig.init();
            Connection conn = DatabaseConfig.getConn();

            // init repository
            UserRepository userRepository = new UserRepositoryImpl(conn);
            SkillRepository skillRepository = new SkillRepositoryImpl(conn);
            ExchangeRequestRepository exchangeRepository = new ExchangeRequestRepositoryImpl(conn);

            // init service
            AuthService authService = new AuthServiceImpl(userRepository);
            SkillService skillService = new SkillServiceImpl(skillRepository, userRepository);
            ExchangeService exchangeService = new ExchangeServiceImpl(exchangeRepository, userRepository);

            // init controller
            AuthController authController = new AuthController(authService);
            SkillController skillController = new SkillController(skillService);
            ExchangeController exchangeController = new ExchangeController(exchangeService);

            // Init view
            AuthView authView = new AuthView(scanner, authController);
            SkillView skillView = new SkillView(scanner, skillController, authController, exchangeController);
            ExchangeView exchangeView = new ExchangeView(scanner, exchangeController, authController);
            MainMenuView mainMenuView = new MainMenuView(scanner, authController, skillView, exchangeView);
            System.out.println("✓ Application initialized successfully!\n");

            // Authentication loop
            while (true) {
                authView.showAuthMenu();

                // If logged in, show main menu
                if (authController.isLoggedIn()) {
                    mainMenuView.showMainMenu();
                    // After logout, loop back to auth menu
                }
            }

        } catch (Exception e) {
            System.err.println("✗ Application failed to start:");
            e.printStackTrace();
        } finally {
            // Cleanup
            scanner.close();
            DatabaseConfig.close();
        }
    }
}