package ru.otus.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.project.model.Role;

@AllArgsConstructor
@Setter
@Getter
public class SetRolesDto {
    private Role role;
    private boolean isSet;
}
