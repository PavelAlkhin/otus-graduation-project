package ru.otus.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.project.model.Comment;
import ru.otus.project.model.PostRecord;
import ru.otus.project.service.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainController {

    UserService userService;
    PostRecordService postRecordService;
    CommentService commentService;

    @GetMapping({"/",""})
    public String indexPage(Model model) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("date").descending());
        Page<PostRecord> recordList = postRecordService.findAllByVisibleAllIsTrue(pageable);
        long totalElements = recordList.getTotalElements();
        long totalPages = recordList.getTotalPages();

        int nextPage = 1;
        int prevPage = 1;

        if( (int)totalPages > 1) {
            nextPage = 2;
        }

        model.addAttribute("nextPage", nextPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("posts", recordList.toList());
        return "index";
    }

    @GetMapping("/{page}")
    public String indexPageByPage(@PathVariable(value = "page", required = true) int page, Model model) {

        if(page==0){
            page=1;
        }

        Pageable pageable = PageRequest.of(page-1, 5, Sort.by("date").descending());
        Page<PostRecord> recordList = postRecordService.findAllByVisibleAllIsTrue(pageable);
        long totalElements = recordList.getTotalElements();
        long totalPages = recordList.getTotalPages();

        int nextPage = 0;
        int prevPage = 0;

        if(page>=1 && page < (int)totalPages) {
            nextPage = page+1;
        }
        if(page>=1 && page == (int)totalPages) {
            nextPage = (int)totalPages;
        }

        if(page>1) {
            prevPage = page-1;
        }

        if(page==1) {
            prevPage = 1;
        }

        model.addAttribute("nextPage", nextPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("posts", recordList.toList());

        return "index";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/user/{username}")
    public String getAllRecordsByUser(@PathVariable String username, Model model ) {
        List<PostRecord> posts = userService.getUserPostsByUserName(username);
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/posts/{id}")
    public String getPost( @PathVariable("id") long id, Model model ) {
        PostRecord post = postRecordService.getPostById(id);
        List<Comment> commentList = commentService.getPostComments(post);
        model.addAttribute("post", post);
        model.addAttribute("comments", commentList);
        return "post";
    }

}
