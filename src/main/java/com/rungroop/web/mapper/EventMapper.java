package com.rungroop.web.mapper;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.dto.EventDto;
import com.rungroop.web.model.Club;
import com.rungroop.web.model.Event;

public class EventMapper {
//    public static EventDto mapToEventDto(Event event) {
//        ClubDto eventDto = EventDto.builder()
//                .id(event.getId())
//                .photoUrl(event.getPhotoUrl())
//                .createdOn(event.getCreatedOn())
//                .updatedOn(event.getUpdatedOn())
//                .
//                .build();
//        return eventDto;
//    }

    public static EventDto mapToEventDto(Event event) {
        EventDto eventDto = EventDto.builder()
                .id(event.getId())
                .createdOn(event.getCreatedOn())
                .updatedOn(event.getUpdatedOn())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .photoUrl(event.getPhotoUrl())
                .type(event.getType())
                .club(event.getClub())
                .build();
        return eventDto;
    }

    public static Event mapToEvent(EventDto eventDto) {
        Event event = Event.builder()
                .id(eventDto.getId())
                .createdOn(eventDto.getCreatedOn())
                .updatedOn(eventDto.getUpdatedOn())
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .photoUrl(eventDto.getPhotoUrl())
                .type(eventDto.getType())
                .club(eventDto.getClub())
                .build();
        return event;
    }
}
