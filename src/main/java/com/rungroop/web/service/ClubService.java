package com.rungroop.web.service;

import com.rungroop.web.dto.ClubDto;
import com.rungroop.web.model.Club;

import java.util.List;

public interface ClubService {
    List<ClubDto> findAllClubs();

    Club saveClub(ClubDto clubDto);

    ClubDto findClubById(long clubId);

    void updateClub(ClubDto clubDto);

    void delete(Long clubId);

    List<ClubDto> searchClubs(String query);
}
