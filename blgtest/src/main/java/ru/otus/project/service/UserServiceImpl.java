package ru.otus.project.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.model.PostRecord;
import ru.otus.project.model.Role;
import ru.otus.project.model.User;
import ru.otus.project.model.VerificationToken;
import ru.otus.project.repository.PostRecordRepository;
import ru.otus.project.repository.RoleRepository;
import ru.otus.project.repository.UserRepository;
import ru.otus.project.repository.VerificationTokenRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PostRecordRepository postRecordRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    private VerificationTokenRepository tokenRepository;

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "BLOG";

    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Transactional
    public List<PostRecord> getUserPostsByUserName(String userName){
        User user = findUserByUserName(userName);
        return user.getRecords();
    }

    @Transactional
    public PostRecord getUserPostByUserNameAndId(String userName, long id){
        User user = findUserByUserName(userName);
        List<PostRecord> posts = postRecordRepository.findByIdAndUser(id, user);

        try{
            return posts.get(0);
        }catch (NullPointerException e){
            return new PostRecord();
        }

   }

    @Transactional
    public List<User> listAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(User user, boolean newPass) {
        if (newPass){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public User saveNewUser(User user, String verificationToken) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);

        VerificationToken newToken = new VerificationToken();
        newToken.updateToken(verificationToken);

        User savedUser = userRepository.save(user);
        newToken.setUser(savedUser);

        tokenRepository.save(newToken);
        return savedUser;
    }

    @Transactional
    public User saveAdmin(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("ROLE_ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(long id){
        return userRepository.findById(id).orElse(new User());
    }

    public String getUserNameFromSecurityContext(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getUserFromSecurityContext(){
        try{
            return findUserByUserName(getUserNameFromSecurityContext());
        }catch (NullPointerException e){
            return new User();
        }
    }

    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                "email", user.getEmail(), user.getSecret(), APP_NAME),
                "UTF-8");
    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setActive(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    public User updateUser2FA(boolean use2FA) {
        Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) curAuth.getPrincipal();
        currentUser.setUsing2FA(use2FA);
        currentUser = userRepository.save(currentUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                currentUser, currentUser.getPassword(), curAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return currentUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}