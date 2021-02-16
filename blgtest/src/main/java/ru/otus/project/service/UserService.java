package ru.otus.project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.otus.project.model.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    User findUserByEmail(String email);
    List<PostRecord> getUserPostsByUserName(String userName);
    PostRecord getUserPostByUserNameAndId(String userName, long id);
    List<User> listAllUsers();
    User updateUser(User user, boolean newPass);
    User saveNewUser(User user, String verificationToken);
    User saveAdmin(User user);
    User getUserById(long id);
    VerificationToken getVerificationToken(final String VerificationToken);
    String validateVerificationToken(String token);
    User getUser(final String verificationToken);
    User updateUser2FA(boolean use2FA);
    UserDetails loadUserByUsername(String username);
    User findUserByUserName(String userName);
    String generateQRUrl(User user) throws UnsupportedEncodingException;
}
