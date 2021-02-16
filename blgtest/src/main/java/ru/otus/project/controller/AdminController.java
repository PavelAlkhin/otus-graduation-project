package ru.otus.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.project.controller.dto.SetRolesDto;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;
import ru.otus.project.service.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping(value="/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private UserService userService;
    private PostRecordService postRecordService;
    private RoleService roleService;

    @GetMapping( {"/", ""} )
    public String indexPage(Model model) {
        List<User> recordList = userService.listAllUsers();
        model.addAttribute("users", recordList);
        return "admin/index";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        List<User> recordList = userService.listAllUsers();
        model.addAttribute("users", recordList);
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String editUserProfilePage(@PathVariable("id") long id, Model model) throws UnsupportedEncodingException {
        User user = userService.getUserById(id);
        List<SetRolesDto> setRoles = new ArrayList<>();
        roleService.getAll().forEach(role -> {
            SetRolesDto setRolesDto = new SetRolesDto(role, false);
            if(user.getRoles().contains(role)){
                setRolesDto.setSet(true);
            }
            setRoles.add(setRolesDto);
        });
        model.addAttribute("roles", setRoles);
        model.addAttribute("user", user);
        String qr = userService.generateQRUrl(user);
        model.addAttribute("qr", qr);
        return "admin/edituser";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, Model model){
        User userFromDB = userService.getUserById(user.getId());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setName(user.getName());
        userFromDB.setUsername( user.getUsername() );
        userFromDB.setLastname( user.getLastname() );
        userFromDB.setActive( user.getActive() );
        userFromDB.setAccountNonExpired( user.getAccountNonExpired() );
        userFromDB.setCredentialsNonExpired( user.getCredentialsNonExpired() );
        userFromDB.setAccountNonLocked( user.getAccountNonLocked() );
        userFromDB.setRoles( user.getRoles() );
        userFromDB.setUsing2FA( user.isUsing2FA() );
        userFromDB.setSecret( user.getSecret() );
        User savedUser = userService.updateUser( userFromDB, false );
        return "redirect:/admin/users/"+savedUser.getId();

    }

    @GetMapping("/posts")
    public String postsPage(Model model) {
        List<PostRecord> posts = postRecordService.listRecords();
        model.addAttribute("posts", posts);
        return "admin/postlist";
    }

    @GetMapping("/posts/{id}")
    public String editPostPage(@PathVariable("id") long id,Model model) {
        PostRecord post = postRecordService.getPostById(id);
        model.addAttribute("post", post);
        return "admin/editpost";
    }

    @PostMapping("/posts/savepost")
    public String savePost( PostRecord postRecord, Model model ){

        PostRecord postFromDB = postRecordService.getPostById(postRecord.getId());
        postRecord.setUser(postFromDB.getUser());
        PostRecord savedPost = postRecordService.savePost(postRecord);

        model.addAttribute("post", savedPost);
        return "redirect:/admin/posts";
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost( @PathVariable("id") long id, Model model ) {
        postRecordService.deletePostRecord(id);
        return "redirect:/admin/posts";
    }


}
