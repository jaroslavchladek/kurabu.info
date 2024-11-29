package com.rungroop.web.controller;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.model.Club;
import com.rungroop.web.model.Role;
import com.rungroop.web.model.User;
import com.rungroop.web.security.SecurityUtil;
import com.rungroop.web.service.ClubService;
import com.rungroop.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClubController {

    private ClubService clubService;
    private UserService userService;

    @Autowired
    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
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

        return (clubDto.getCreatedBy() == null
                || currentUser == clubDto.getCreatedBy()
                || isAdmin);
    }

    @GetMapping("/")
    public String getHome(Model model){
        return "redirect:/clubs";
    }

    @GetMapping("/clubs")
    public String listClubs(Model model) {
        User user = new User();
        List<ClubDto> clubs = clubService.findAllClubs();

        String userEmail = SecurityUtil.getSessionUser();
        if (userEmail != null) {
            user = userService.findByEmail(userEmail);
            model.addAttribute("user", user);
        }

        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }

    @GetMapping("/clubs/new")
    public String createClubForm(Model model) {
        // Check whether User is authorized to save the Club.
        if (getCurrentUser() == null)
            return "redirect:/clubs?unauthorized";

        Club club = new Club();
        model.addAttribute("club", club);
        return "clubs-create";
    }

    @PostMapping("/clubs/new")
    public String saveClub(@Valid @ModelAttribute("club") ClubDto clubDto, BindingResult result, Model model) {
        // Check whether User is authorized to save the Club.
        User currentUser = getCurrentUser();
        if (!userIsTheCreationUserOrAnAdmin(clubDto))
            return "redirect:/clubs/" + clubDto.getId() + "/?unauthorized";

        if (result.hasErrors()) {
            model.addAttribute("club", clubDto);
            return "clubs-create";
        }
        clubService.saveClub(clubDto);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/{clubId}/edit")
    public String editClub(@PathVariable("clubId") Long clubId, Model model) {
        if (!userIsTheCreationUserOrAnAdmin(clubService.findClubById(clubId)))
            return "redirect:/clubs/" + clubId + "?unauthorized";

        ClubDto club = clubService.findClubById(clubId);
        model.addAttribute("club", club);
        return "clubs-edit";
    }

    @GetMapping("/clubs/{clubId}")
    public String clubDetail(@PathVariable("clubId") long clubId, Model model) {
        User user = new User();
        ClubDto clubDto = clubService.findClubById(clubId);

        String userEmail = SecurityUtil.getSessionUser();
        if (userEmail != null) {
            user = userService.findByEmail(userEmail);
            model.addAttribute("user", user);
        }

        model.addAttribute("user", user);
        model.addAttribute("creationUser", clubDto.getCreatedBy());
        model.addAttribute("club", clubDto);
        return "clubs-detail";
    }

    @GetMapping("/clubs/{clubId}/delete")
    public String deleteClub(@PathVariable("clubId") Long clubId) {
        if (!userIsTheCreationUserOrAnAdmin(clubService.findClubById(clubId)))
            return "redirect:/clubs/" + clubId + "?unauthorized";

        clubService.delete(clubId);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/search")
    public String searchClub(@RequestParam(value = "query") String query, Model model) {
        List<ClubDto> clubs = clubService.searchClubs(query);
        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }

    @PostMapping("/clubs/{clubId}/edit")
    public String updateClub(@PathVariable("clubId") Long clubId, @Valid @ModelAttribute("club") ClubDto clubDto,
                             BindingResult result) {
        if (!userIsTheCreationUserOrAnAdmin(clubService.findClubById(clubId)))
            return "redirect:/clubs/" + clubId + "?unauthorized";

        if (result.hasErrors()) {
            return "clubs-edit";
        }
        clubDto.setId(clubId);
        clubService.updateClub(clubDto);
        return "redirect:/clubs";
    }
}
