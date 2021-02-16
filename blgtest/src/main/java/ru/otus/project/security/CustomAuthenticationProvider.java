package ru.otus.project.security;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import ru.otus.project.model.User;
import ru.otus.project.service.UserService;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserService userServiceImpl;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String verificationCode = "";
        try {
            Object detailsSource =  auth.getDetails();
            CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) detailsSource;
            verificationCode = details.getVerificationCode();

        }catch (ClassCastException e){
            verificationCode = "";
        }

        User user = userServiceImpl.findUserByUserName( auth.getPrincipal().toString() );
        if ( (user == null) ) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if ( user.isUsing2FA() ) {
            Totp totp = new Totp(user.getSecret());
            try {
                if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                    throw new BadCredentialsException("Invalid verfication code");
                }
            }
            catch (AuthenticationException e) {
               throw new AuthenticationException("Invalid verfication code") {
                   @Override
                   public String getMessage() {
                       return super.getMessage();
                   }
               };
            }
        }

        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }


    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
