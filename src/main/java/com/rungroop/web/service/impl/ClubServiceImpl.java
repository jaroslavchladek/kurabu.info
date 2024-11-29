package com.rungroop.web.service.impl;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.model.Club;
import com.rungroop.web.model.User;
import com.rungroop.web.repository.ClubRepository;
import com.rungroop.web.repository.UserRepository;
import com.rungroop.web.security.SecurityUtil;
import com.rungroop.web.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rungroop.web.mapper.ClubMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.rungroop.web.mapper.ClubMapper.mapToClub;
import static com.rungroop.web.mapper.ClubMapper.mapToClubDto;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ClubDto> findAllClubs() {
        List<Club> clubs = clubRepository.findAll();
        return clubs.stream().map(ClubMapper::mapToClubDto).collect(Collectors.toList());
    }

    @Override
    public Club saveClub(ClubDto clubDto) {
        String userEmail = SecurityUtil.getSessionUser();
        User user = userRepository.findByEmail(userEmail);
        Club club = mapToClub(clubDto);
        club.setCreatedBy(user);
        club.setLocationMapLink(
                extractBetweenQuotes(club.getLocationMapLink())
        );
        club.setCreatedOn(LocalDateTime.now());
        return clubRepository.save(club);
    }

    @Override
    public ClubDto findClubById(long clubId) {
        Club club = clubRepository.findById(clubId).get();
        return mapToClubDto(club);
    }

    @Override
    public void updateClub(ClubDto clubDto) {
        String userEmail = SecurityUtil.getSessionUser();
        User user = userRepository.findByEmail(userEmail);
        Club club = mapToClub(clubDto);
        club.setCreatedBy(user);
        club.setLocationMapLink(
                extractBetweenQuotes(club.getLocationMapLink())
        );
        club.setUpdatedOn(LocalDateTime.now());
        clubRepository.save(club);
    }

    @Override
    public void delete(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Override
    public List<ClubDto> searchClubs(String query) {
        List<Club> clubs = clubRepository.searchClubs(query);
        return clubs.stream().map(ClubMapper::mapToClubDto).collect(Collectors.toList());
    }

    public static String extractBetweenQuotes(String input) {
        int firstQuoteIndex = input.indexOf("\"");
        int secondQuoteIndex = input.indexOf("\"", firstQuoteIndex + 1);

        if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
            return input.substring(firstQuoteIndex + 1, secondQuoteIndex);
        }

        return input;  // Return the input String unchanged if quotation marks are not found properly.
    }

}
