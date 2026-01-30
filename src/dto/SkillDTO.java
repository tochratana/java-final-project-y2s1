package dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO {
    private Long id;
    private String skillName;
    private String skillLevel;
    private Integer experienceYears;
    private String description;
    private Boolean availability;
    private String ownerName;
}