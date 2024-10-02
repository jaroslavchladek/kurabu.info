package com.rungroop.web.service;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.dto.EventDto;

import java.util.List;

public interface EventService {
    void createEvent(Long clubId, EventDto eventDto);
    List<EventDto> findAllEvents();
    void deleteEvent(Long eventId);
    EventDto findEventById(Long eventId);
    void updateEvent(EventDto eventDto);
//    List<ClubDto> findAllEvents();
}
