package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;

import java.util.List;

@Repository
public interface PostRecordRepository extends JpaRepository<PostRecord, Long> {
    List<PostRecord> findAllByVisibleAllIsTrue();
    List<PostRecord> findAllByVisibleAllIsTrueAndUserIs(User user);
    List<PostRecord> findByIdAndUser(Long id, User user);

}
