package ru.otus.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import ru.otus.project.service.FillData;

@SpringBootApplication
@Configuration
public class GraduationProjectApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GraduationProjectApplication.class, args);

        FillData fillData = context.getBean("fillData", FillData.class);

        fillData.fillUsersAndPosts();

    }

}