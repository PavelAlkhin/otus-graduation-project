package ru.otus.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class FormNewPost {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String title;

    private String text;

    private Boolean visibleAll;
}
