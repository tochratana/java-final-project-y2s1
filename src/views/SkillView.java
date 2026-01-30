package views;

import controller.SkillController;
import controller.AuthController;
import controller.ExchangeController;
import dto.SkillDTO;
import dto.CreateExchangeRequestDTO;
import model.Skill;
import repository.SkillRepository;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * SkillView - Handles all skill-related UI
 */
public class SkillView extends ConsoleView {
    private SkillController skillController;
    private AuthController authController;
    private ExchangeController exchangeController;
    private SkillRepository skillRepository;

    public SkillView(Scanner scanner, SkillController skillController,
                     AuthController authController, ExchangeController exchangeController, SkillRepository skillRepository) {
        super(scanner);
        this.skillController = skillController;
        this.authController = authController;
        this.exchangeController = exchangeController;
        this.skillRepository = skillRepository;
    }

    public void showMySkillsMenu() {
        while (true) {
            // clearScreen();
            printHeader("MY SKILLS");

            Long userId = authController.getCurrentUser().getId();
            skillController.viewMySkills(userId);

            System.out.println("\n1. Add New Skill");
            System.out.println("2. Update Skill");
            System.out.println("3. Delete Skill");
            System.out.println("4. Back to Main Menu");
            printSeparator();

            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1:
                    handleAddSkill();
                    pause();
                    break;
                case 2:
                    handleUpdateSkill();
                    pause();
                    break;
                case 3:
                    handleDeleteSkill();
                    pause();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("✗ Invalid option!");
                    pause();
            }
        }
    }

    private void handleAddSkill() {
        // clearScreen();
        printHeader("ADD NEW SKILL");

        String skillName = readString("Skill Name: ");

        System.out.println("\nSkill Levels:");
        System.out.println("1. BEGINNER");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");
        System.out.println("4. EXPERT");
        int levelChoice = readInt("Choose level (1-4): ");

        String[] levels = {"BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"};
        String skillLevel = (levelChoice >= 1 && levelChoice <= 4) ? levels[levelChoice - 1] : "BEGINNER";

        int experienceYears = readInt("Years of Experience: ");
        String description = readString("Description: ");
        boolean availability = readBoolean("Available for exchange?");

        SkillDTO dto = SkillDTO.builder()
                .skillName(skillName)
                .skillLevel(skillLevel)
                .experienceYears(experienceYears)
                .description(description)
                .availability(availability)
                .build();

        Long userId = authController.getCurrentUser().getId();
        skillController.addSkill(userId, dto);
    }

    private void handleUpdateSkill() {
        printHeader("UPDATE SKILL");

        Long skillId = readLong("Enter Skill ID to update: ");
        Long userId = authController.getCurrentUser().getId();

        // Validate skill exists and belongs to user FIRST
        try {
            Skill existingSkill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new IllegalArgumentException("Skill with ID " + skillId + " not found"));

            if (!existingSkill.getUserId().equals(userId)) {
                System.out.println("✗ Unauthorized: You can only update your own skills");
                return;
            }

            // Show current skill information
            System.out.println("\n--- Current Skill Information ---");
            System.out.println("Name: " + existingSkill.getSkillName());
            System.out.println("Level: " + existingSkill.getSkillLevel());
            System.out.println("Experience: " + existingSkill.getExperienceYears() + " years");
            System.out.println("\n--- Enter New Information ---\n");

            // NOW collect new information
            String skillName = readString("New Skill Name: ");

            System.out.println("\nSkill Levels:");
            System.out.println("1. BEGINNER");
            System.out.println("2. INTERMEDIATE");
            System.out.println("3. ADVANCED");
            System.out.println("4. EXPERT");
            int levelChoice = readInt("Choose level (1-4): ");

            String[] levels = {"BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"};
            String skillLevel = (levelChoice >= 1 && levelChoice <= 4) ? levels[levelChoice - 1] : "BEGINNER";

            int experienceYears = readInt("Years of Experience: ");
            String description = readString("Description: ");
            boolean availability = readBoolean("Available for exchange?");

            SkillDTO dto = SkillDTO.builder()
                    .skillName(skillName)
                    .skillLevel(skillLevel)
                    .experienceYears(experienceYears)
                    .description(description)
                    .availability(availability)
                    .build();

            skillController.updateSkill(skillId, userId, dto);

        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Failed to update skill: " + e.getMessage());
        }
    }

    private void handleDeleteSkill() {
        // clearScreen();
        printHeader("DELETE SKILL");

        Long skillId = readLong("Enter Skill ID to delete: ");

        if (readBoolean("Are you sure you want to delete this skill?")) {
            Long userId = authController.getCurrentUser().getId();
            skillController.deleteSkill(skillId, userId);
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    public void showBrowseSkillsMenu() {
        // clearScreen();
        printHeader("BROWSE AVAILABLE SKILLS");

        Long userId = authController.getCurrentUser().getId();
        skillController.viewAvailableSkills(userId);

        System.out.println("\n1. Request Skill Exchange");
        System.out.println("2. Back to Main Menu");
        printSeparator();

        int choice = readInt("Choose option: ");

        if (choice == 1) {
            handleCreateExchangeRequest();
            pause();
        }
    }

    private void handleCreateExchangeRequest() {
        // clearScreen();
        printHeader("CREATE EXCHANGE REQUEST");

        Long providerId = readLong("Enter Provider User ID: ");
        String requestedSkill = readString("Skill you want to learn: ");
        String offeredSkill = readString("Skill you offer in exchange: ");
        String requestMessage = readString("Message to provider: ");

        CreateExchangeRequestDTO dto = new CreateExchangeRequestDTO(
                providerId, requestedSkill, offeredSkill, requestMessage
        );

        Long requesterId = authController.getCurrentUser().getId();
        exchangeController.createExchangeRequest(requesterId, dto);
    }
}