package ru.otus.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;

import java.util.Date;
import java.util.List;

public interface PostRecordService {
    List<PostRecord> listRecords();
    PostRecord saveNewPostRecord(long userId, Date date, String title, String text, Boolean visibleAll );
    void deletePostRecord(long id);
    void deleteUserPostRecord(long id, User user);
    PostRecord updatePostRecord(PostRecord record);
    PostRecord savePost(PostRecord record);
    PostRecord getPostById(long id);
    List<PostRecord> findAllByVisibleAllIsTrue();
    Page<PostRecord> findAllByVisibleAllIsTrue(Pageable pageable);
    List<PostRecord> getVisibleRecordsByUsername(String username);
    PostRecord getPostByIdAndUserName(long id, String userName);
}
