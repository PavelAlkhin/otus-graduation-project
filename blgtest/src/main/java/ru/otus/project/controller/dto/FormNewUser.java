package ru.otus.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FormNewUser {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String name;

    private String lastname;

    private boolean using2FA;
}
