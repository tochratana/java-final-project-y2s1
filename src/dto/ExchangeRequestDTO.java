package dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRequestDTO {
    private Long id; // id user that we want request
    private String requesterName; // who learn
    private String providerName; // who teach
    private String requestedSkill; //
    private String offeredSkill;
    private String requestMessage;
    private String status;
    private LocalDateTime createdAt;
}