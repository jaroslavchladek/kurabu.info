package com.rungroop.web.controller;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.dto.EventDto;
import com.rungroop.web.mapper.EventMapper;
import com.rungroop.web.model.Club;
import com.rungroop.web.model.Event;
import com.rungroop.web.model.Role;
import com.rungroop.web.model.User;
import com.rungroop.web.security.SecurityUtil;
import com.rungroop.web.service.ClubService;
import com.rungroop.web.service.EventService;
import com.rungroop.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EventController {

    private EventService eventService;
    private UserService userService;
    private ClubService clubService;

    @Autowired
    public EventController(EventService eventService, UserService userService, ClubService clubService) {
        this.eventService = eventService;
        this.userService = userService;
        this.clubService = clubService;
    }

    public User getCurrentUser() {
        return userService.findByEmail(
                SecurityUtil.getSessionUser()
        );
    }

    public boolean userIsTheCreationUserOrAnAdmin(ClubDto clubDto) {
        // Check whether User is authorized to save the Club.
        User currentUser = getCurrentUser();
        if (currentUser == null)
            return false;

        List<Role> userRoles = currentUser.getRoles();
        boolean isAdmin = userRoles
                .stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()));

        // Club is never null for a given Event.
        return (clubDto.getCreatedBy() == null
                || currentUser == clubDto.getCreatedBy()
                || isAdmin);
    }

    @GetMapping("/events/{clubId}/new")
    public String createEventForm(@PathVariable("clubId") Long clubId, Model model) {
        if (!userIsTheCreationUserOrAnAdmin(
                clubService.findClubById(clubId)
        ))
            return "redirect:/clubs/" + clubId + "?unauthorized";

        Event event = new Event();
        model.addAttribute("clubId", clubId);
        model.addAttribute("event", event);

        return "events-create";
    }

    @PostMapping("/events/{clubId}")
    public String createEvent(@PathVariable("clubId")  Long clubId,
                              @ModelAttribute("event") EventDto eventDto,
                              BindingResult result,
                              Model model) {
        if (!userIsTheCreationUserOrAnAdmin(
                clubService.findClubById(clubId)
        ))
            return "redirect:/clubs/" + clubId + "?unauthorized";

        if (result.hasErrors()) {
            model.addAttribute("event", eventDto);
            return "clubs-create";
        }
        eventService.createEvent(clubId, eventDto);
        return "redirect:/clubs/" + clubId;
    }

    @GetMapping("/events/{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") Long eventId) {
        if (!userIsTheCreationUserOrAnAdmin(
                clubService.findClubById(eventService.findEventById(eventId).getClub().getId())
        ))
            return "redirect:/clubs/" + eventService.findEventById(eventId).getClub().getId() + "?unauthorized";

        eventService.deleteEvent(eventId);
        return "redirect:/events";
    }

    @GetMapping("/events")
    public String eventList(Model model) {
        User user = new User();
        List<EventDto> events = eventService.findAllEvents();

        String userEmail = SecurityUtil.getSessionUser();
        if (userEmail != null) {
            user = userService.findByEmail(userEmail);
            model.addAttribute("user", user);
        }

        model.addAttribute("events", events);
        return "events-list";
    }

    @GetMapping("/events/{eventId}")
    public String viewEvent(@PathVariable("eventId") Long eventId, Model model) {
        User user = new User();
        EventDto eventDto = eventService.findEventById(eventId);

        String userEmail = SecurityUtil.getSessionUser();
        if (userEmail != null) {
            user = userService.findByEmail(userEmail);
            model.addAttribute("user", user);
        }

        model.addAttribute("club", eventDto);
        model.addAttribute("event", eventDto);
        return "events-detail";
    }

    @GetMapping("/events/{eventId}/edit")
    public String editEvent(@PathVariable("eventId") Long eventId, Model model) {
        if (!userIsTheCreationUserOrAnAdmin(
                clubService.findClubById(eventService.findEventById(eventId).getClub().getId())
        ))
            return "redirect:/clubs/" + eventService.findEventById(eventId).getClub().getId() + "?unauthorized";

        EventDto event = eventService.findEventById(eventId);
        model.addAttribute("event", event);
        return "events-edit";
    }

    @PostMapping("/events/{eventId}/edit")
    public String updateEvent(@PathVariable("eventId") Long eventId,
                              @Valid @ModelAttribute("event") EventDto eventDto,
                             BindingResult result, Model model) {
        if (!userIsTheCreationUserOrAnAdmin(
                clubService.findClubById(eventService.findEventById(eventId).getClub().getId())
        ))
            return "redirect:/clubs/" + eventService.findEventById(eventId).getClub().getId() + "?unauthorized";

        if (result.hasErrors()) {
            model.addAttribute("event", eventDto);
            return "events-edit";
        }
        EventDto eventDto1 = eventService.findEventById(eventId);

        eventDto.setId(eventId);
        eventDto.setClub(eventDto1.getClub());

        eventService.updateEvent(eventDto);
        return "redirect:/events";
    }
}
