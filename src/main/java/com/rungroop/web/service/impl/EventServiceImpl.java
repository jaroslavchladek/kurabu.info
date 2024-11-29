package com.rungroop.web.service.impl;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.dto.EventDto;
import com.rungroop.web.mapper.EventMapper;
import com.rungroop.web.model.Club;
import com.rungroop.web.model.Event;
import com.rungroop.web.repository.ClubRepository;
import com.rungroop.web.repository.EventRepository;
import com.rungroop.web.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.rungroop.web.mapper.ClubMapper.mapToClub;
import static com.rungroop.web.mapper.EventMapper.mapToEvent;
import static com.rungroop.web.mapper.EventMapper.mapToEventDto;

@Service
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private ClubRepository clubRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ClubRepository clubRepository) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
    }

    @Override
    public void saveEvent(Long clubId, EventDto eventDto) {
        Club club = clubRepository.findById(clubId).get();
        Event event = mapToEvent(eventDto);
        event.setClub(club);
        event.setLocationMapLink(
                extractBetweenQuotes(event.getLocationMapLink())
        );
        event.setCreatedOn(LocalDateTime.now());
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public List<EventDto> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(EventMapper::mapToEventDto).collect(Collectors.toList());
    }

    @Override
    public EventDto findEventById(Long id) {
        Event event = eventRepository.findById(id).get();
        return mapToEventDto(event);
    }

    @Override
    public void updateEvent(EventDto eventDto) {
        Event event = mapToEvent(eventDto);
        event.setLocationMapLink(
                extractBetweenQuotes(event.getLocationMapLink())
        );
        event.setUpdatedOn(LocalDateTime.now());
        eventRepository.save(event);
    }

    public static String extractBetweenQuotes(String input) {
        int firstQuoteIndex = input.indexOf("\"");
        int secondQuoteIndex = input.indexOf("\"", firstQuoteIndex + 1);

        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return input.substring(firstQuoteIndex + 1, secondQuoteIndex);
        }

        return input;  // Return the input String unchanged if quotation marks are not found properly.
    }

//    @Override
//    public List<ClubDto> findAllEvents() {
//        List<Club> clubs = clubRepository.findAll();
//        return clubs.stream().map(Club::getEvents).map(EventMapper::mapToEventDto).collect(Collectors.toList());
//    }

}
