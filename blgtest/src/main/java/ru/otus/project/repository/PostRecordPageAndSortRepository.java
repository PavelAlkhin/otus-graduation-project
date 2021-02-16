package ru.otus.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;

import java.util.List;

@Repository
public interface PostRecordPageAndSortRepository extends PagingAndSortingRepository<PostRecord, Long> {

    List<PostRecord> findAllByVisibleAllIsTrueAndUserIs(User user);
    List<PostRecord> findByIdAndUser(Long id, User user);

    //Pageble and sorting
    Page<PostRecord> findAllByVisibleAllIsTrue(Pageable pageable);
//    List<PostRecord> findSortAllByVisibleAllIsTrue(Sort sort);
//    List<PostRecord> findSortAndPageableAllByVisibleAllIsTrue(Sort sort, Pageable pageable);

}
