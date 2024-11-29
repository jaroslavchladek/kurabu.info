package com.rungroop.web.dto;

import com.rungroop.web.model.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ClubDto {
    private Long id;
    @NotEmpty(message = "Club title should not be empty")
    private String title;
    @NotEmpty(message = "Photo link should not be empty")
    private String photoUrl;
    @Column(name = "description", columnDefinition = "TEXT", length = 50000)
    @Size(max = 50000)  // Apply validation at the DTO level
    private String content;
    private User createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private List<EventDto> events;

    @Size(max = 5000)  // Apply validation at the DTO level
    private String locationMapLink;
}
