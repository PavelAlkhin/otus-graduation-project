package ru.otus.project.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.User;
import ru.otus.project.repository.PostRecordPageAndSortRepository;
import ru.otus.project.repository.PostRecordRepository;
import ru.otus.project.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PostRecordServiceImpl implements PostRecordService{

    private UserRepository userRepository;
    private PostRecordRepository postRecordRepository;
    private PostRecordPageAndSortRepository postRecordPageAndSortRepository;
    private UserService userServiceImpl;

    @Transactional(readOnly = true)
    public List<PostRecord> listRecords(){
        List<PostRecord> records = postRecordRepository.findAll();
        return records;
    }

    @Transactional
    public PostRecord saveNewPostRecord( long userId, Date date, String title, String text, Boolean visibleAll ){
        User user = userRepository.findById(userId).orElse(new User());
        return postRecordRepository.save(new PostRecord(user, date, title, text, visibleAll));
    }

    @Transactional
    public void deletePostRecord(long id){
        postRecordRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserPostRecord(long id, User user){
        user.getRecords().forEach(p -> {
            if(id == p.getId()){
                postRecordRepository.deleteById(id);
            }
        });
    }

    @Transactional
    public PostRecord updatePostRecord(PostRecord record){
        return postRecordRepository.save(record);
    }

    @Transactional
    public PostRecord savePost(PostRecord record){
        return postRecordRepository.save(record);
    }

    @Transactional
    public PostRecord getPostById(long id){
        return postRecordRepository.findById(id).orElse(new PostRecord());
    }

    @Transactional
    public List<PostRecord> findAllByVisibleAllIsTrue(){
      return postRecordRepository.findAllByVisibleAllIsTrue();
    }

    @Transactional
    public Page<PostRecord> findAllByVisibleAllIsTrue(Pageable pageable){
        Page<PostRecord> postRecordPage = postRecordPageAndSortRepository.findAllByVisibleAllIsTrue( pageable );

        return postRecordPage;

    }

    @Transactional
    public List<PostRecord> getVisibleRecordsByUsername(String username){
        return postRecordRepository.findAllByVisibleAllIsTrueAndUserIs(userRepository.findByUsername(username));
    }

    @Transactional
    public PostRecord getPostByIdAndUserName(long id, String userName){
        User user = userServiceImpl.findUserByUserName(userName);
        List<PostRecord> recordList = postRecordRepository.findByIdAndUser(id, user);
        if(recordList.size() == 1){
            return recordList.get(0);
        }
        return new PostRecord();
    }

}
