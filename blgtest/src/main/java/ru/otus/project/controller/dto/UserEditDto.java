package ru.otus.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.Role;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class UserEditDto {

    private long id;

    private String username;

    private String email;

    private String password;

    private String passwordConfirm;

    private String name;

    private String lastname;

    private Boolean active;

    private List<PostRecord> records;

    private Set<Role> roles;
}
