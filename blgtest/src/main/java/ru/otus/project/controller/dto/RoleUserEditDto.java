package ru.otus.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.project.model.Role;
import ru.otus.project.model.User;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class RoleUserEditDto {
    private User user;
    private Set<Role> activeRoles;
}
