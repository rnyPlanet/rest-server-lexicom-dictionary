package com.grin.lexicom.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grin.lexicom.model.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String userName;
    private String surName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;


    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setSurName(surName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setSurName(user.getSurName());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());

        return userDto;
    }
}

