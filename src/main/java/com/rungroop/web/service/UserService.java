package com.rungroop.web.service;

import com.rungroop.web.dto.RegistrationDto;
import com.rungroop.web.model.User;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    void saveUserAdmin(RegistrationDto registrationDto);
    User findByEmail(String email);
    User findByUsername(String username);
}
