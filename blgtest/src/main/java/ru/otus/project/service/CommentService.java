package ru.otus.project.service;

import ru.otus.project.model.Comment;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;

import java.util.List;

public interface CommentService {
    List<Comment> getPostComments(PostRecord post);
    List<Comment> getCommentsByUser(User user);
    Comment saveNewComment(String username, String comment, long postId);
}
