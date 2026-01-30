package model;

import lombok.*;
import java.time.LocalDateTime;

//@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ExchangeRequest {
    private Long id;
    private Long requesterId;
    private Long providerId;
    private String requestedSkill;
    private String offeredSkill;
    private String requestMessage;
    private String status;
    private Boolean requesterConfirmed;
    private Boolean providerConfirmed;
    private LocalDateTime createdAt;
}