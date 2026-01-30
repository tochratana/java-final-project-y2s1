package model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    private Long id;
    private Long userId;
    private String skillName;
    private String skillLevel;
    private Integer experienceYears;
    private String description;
    private Boolean availability;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}