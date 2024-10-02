package com.rungroop.web.dto;

import com.rungroop.web.model.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    @DateTimeFormat(pattern="HH:mm:ss YYYY/MM/DD")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern="HH:mm:ss'T'YYYY/MM/DD")
    private LocalDateTime endTime;
    private String photoUrl;
    private String type;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Club club;
}
