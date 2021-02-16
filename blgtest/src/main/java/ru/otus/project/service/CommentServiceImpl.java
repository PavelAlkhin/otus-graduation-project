package ru.otus.project.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.model.Comment;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;
import ru.otus.project.repository.CommentRepository;

import java.util.List;


@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    CommentRepository commentRepository;
    UserService userService;
    PostRecordService postRecordService;

    @Transactional
    public List<Comment> getPostComments(PostRecord post) {
        return (List<Comment>) commentRepository.findAllByPost(post);
    }

    @Transactional
    public List<Comment> getCommentsByUser(User user){
        return commentRepository.findAllByUser(user);
    }

    @Transactional
    public Comment saveNewComment(String username, String comment, long postId){
        User user = userService.findUserByUserName(username);
        PostRecord post = postRecordService.getPostById(postId);
        return commentRepository.save(new Comment(comment, user, post));
    }
}
