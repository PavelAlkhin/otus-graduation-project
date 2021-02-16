package ru.otus.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.project.controller.dto.FormNewPost;
import ru.otus.project.model.Comment;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;
import ru.otus.project.service.CommentService;
import ru.otus.project.service.PostRecordService;
import ru.otus.project.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="/profile")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private PostRecordService postRecordService;
    private CommentService commentService;


    @GetMapping({"/", ""})
    public String profilePage(HttpServletRequest httpServletRequest, Model model) {
        final String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUserName(currentUser);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/save")
    public String saveUserProfile(User user, Model model ){

        final String currentUser = getUserNameFromSecurityContext();
        User userFromContext = userService.findUserByUserName(currentUser);
        if(user.getId() == userFromContext.getId()){
            userFromContext.setUsername(user.getUsername());
            userFromContext.setName(user.getName());
            userFromContext.setEmail(user.getEmail());
            userFromContext.setLastname(user.getLastname());
            User savedUser = userService.updateUser(userFromContext, false);
        }

//        userService.updateUser2FA(true);

        model.addAttribute("user", userFromContext);
        return "user/profile";
    }

    @GetMapping("/posts")
    public String getPost(Model model) {
        final String currentUser = getUserNameFromSecurityContext();
        List<PostRecord> recordList = userService.getUserPostsByUserName(currentUser);
        model.addAttribute("posts", recordList);
        return "user/postlist";
    }

    @GetMapping("/posts/{id}")
    public String editPost(@PathVariable("id") long id, Model model) {
        final String currentUser = getUserNameFromSecurityContext();
        PostRecord post = postRecordService.getPostByIdAndUserName(id, currentUser);
        model.addAttribute("post", post);
        if(post.getId()==0){
            return "/user/notfoundpost";
        }
        return "user/editpost";
    }

    @GetMapping("/posts/newpost")
    public String newPostForm(Model model) {
        return "user/newpost";
    }

    @PostMapping("/posts/savenewpost")
    public String newPost(FormNewPost formNewPost, Model model ){

        final String currentUser = getUserNameFromSecurityContext();
        User user = userService.findUserByUserName(currentUser);

        PostRecord post = postRecordService.saveNewPostRecord(
                user.getId(),
                formNewPost.getDate(),
                formNewPost.getTitle(),
                formNewPost.getText(),
                formNewPost.getVisibleAll()
        );

        model.addAttribute("post", post);
        return "redirect:/profile/posts/"+post.getId();
    }

    @PostMapping("/posts/savepost")
    public String savePost( PostRecord postRecord, Model model ){

        final String currentUser = getUserNameFromSecurityContext();
        User user = userService.findUserByUserName(currentUser);

        PostRecord postFromBD = userService.getUserPostByUserNameAndId( currentUser, postRecord.getId() );

        if(postFromBD.getId() == postRecord.getId()){
            postRecord.setUser(user);
            PostRecord savedPost = postRecordService.savePost(postRecord);
            model.addAttribute("post", savedPost);
            return "redirect:/profile/posts/"+savedPost.getId();
        }

        return "user/notfoundpost";

    }

    @PostMapping("/posts/delete")
    public String deletePost( @RequestParam("id") long id, Model model ){

        final String currentUser = getUserNameFromSecurityContext();
        User user = userService.findUserByUserName(currentUser);

        postRecordService.deleteUserPostRecord(id, user);

        return "redirect:/profile/posts/";
    }

    @PostMapping("/posts/newcomment")
    public String addNewPostComment(@RequestParam("postid") long postid, @RequestParam("textcomment") String commentText, Model model) {
        final String currentUser = getUserNameFromSecurityContext();
        Comment savedComment = commentService.saveNewComment(currentUser, commentText, postid);

        model.addAttribute("post", savedComment.getPost());
        return "redirect:/posts/"+postid;
    }

    public String getUserNameFromSecurityContext(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getUserFromSecurityContext(){
        try{
            User user = userService.findUserByUserName(getUserNameFromSecurityContext());
            return user;
        }catch (NullPointerException e){
            return new User();
        }
    }
}
