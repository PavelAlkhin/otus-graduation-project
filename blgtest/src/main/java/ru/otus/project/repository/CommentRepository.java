package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.model.Comment;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);
    List<Comment> findAllByPost(PostRecord post);

}
