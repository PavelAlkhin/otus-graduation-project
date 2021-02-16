package ru.otus.project.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.Role;
import ru.otus.project.model.User;
import ru.otus.project.repository.PostRecordRepository;
import ru.otus.project.repository.RoleRepository;
import ru.otus.project.repository.UserRepository;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
@NoArgsConstructor
public class FillData {

    @Autowired
    private UserRepository userRep;

    @Autowired
    private RoleRepository roleRep;

    @Autowired
    private PostRecordRepository postRecordRepository;

    public void fillUsersAndPosts() {

        User userUser = new User("user");
        userUser.setPassword("111");
        User userFromDB2 = userRep.findByUsername(userUser.getUsername());

        if (userFromDB2 != null) {
            return;
        }

        Role roleUser = roleRep.findByRole("ROLE_USER");
        if (roleUser == null) {
            roleUser = roleRep.save(new Role("ROLE_USER"));
        }

        userUser.setPassword("111");
        userUser.setActive(true);
        userUser.setName("userovich");
        userUser.setEmail("aaa@qqq");
        userUser.setLastname("userov");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userUser.setPassword(bCryptPasswordEncoder.encode(userUser.getPassword()));
        userUser.setAccountNonLocked(true);
        userUser.setCredentialsNonExpired(true);
        userUser.setAccountNonExpired(true);

        userUser.setSecret("NY4A5CPJZ46LXZCP");
        userUser.setUsing2FA(true);

        userUser.setRoles(Collections.singleton(roleUser));
        userRep.save(userUser);

        User userAdmin = new User("admin");
        userAdmin.setPassword("111");
        userFromDB2 = userRep.findByUsername(userAdmin.getUsername());

        if (userFromDB2 != null) {
            return;
        }

        Role roleAdmin = roleRep.findByRole("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = roleRep.save(new Role("ROLE_ADMIN"));
        }

        Role roleEditor = roleRep.findByRole("ROLE_EDITOR");
        if (roleEditor == null) {
            roleEditor = roleRep.save(new Role("ROLE_EDITOR"));
        }

        Set<Role> roleList = new HashSet<>();

        roleList.add(roleAdmin);
        roleList.add(roleEditor);
        roleList.add(roleUser);

        userAdmin.setActive(true);
        userAdmin.setName("adminych");
        userAdmin.setEmail("adm@qqq");
        userAdmin.setLastname("adminov");

        userAdmin.setRoles(roleList);
        userAdmin.setPassword(bCryptPasswordEncoder.encode(userAdmin.getPassword()));
        userAdmin.setUsing2FA(false);
        userUser.setAccountNonLocked(true);
        userUser.setCredentialsNonExpired(true);
        userUser.setAccountNonExpired(true);

        userAdmin.setSecret("NY4A5CPJZ46LXZCP");

        userRep.save(userAdmin);

        fillPosts(userUser, userAdmin);

    }

    public void fillPosts(User userUser, User userAdmin){

        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 20).getTime(), "some title1 from user", "some text1 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 21).getTime(), "some title2 from user", "some text2 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 23).getTime(), "some title3 from user", "some text3 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 24).getTime(), "some title4 from user", "some text4 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 25).getTime(), "some title5 from user", "some text5 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 26).getTime(), "some title6 from user", "some text6 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 27).getTime(), "some title7 from user", "some text7 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 28).getTime(), "some title8 from user", "some text8 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 29).getTime(), "some title9 from user", "some text9 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 30).getTime(), "some title10 from user", "some text10 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 01, 31).getTime(), "some title11 from user", "some text11 from user", true));
        postRecordRepository.save(new PostRecord(userUser, new GregorianCalendar(2021, 02, 01).getTime(), "some title12 from user", "some text12 from user", true));

        postRecordRepository.save(new PostRecord(userAdmin, new GregorianCalendar(2019, 02, 20).getTime(), "some title admin", "some text admin", true));
    }

}
