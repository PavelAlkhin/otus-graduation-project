package ru.otus.project.controller;

import lombok.AllArgsConstructor;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.project.model.User;
import ru.otus.project.service.UserService;

import java.io.UnsupportedEncodingException;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private static final String APP_NAME = "BLOG";
    UserService userServiceImpl;

    @GetMapping({"/registration/{error}", "/registration"})
    public String registration(@PathVariable(value="error", required = false) String error, Model model) {
        model.addAttribute("token", java.util.UUID.randomUUID().toString());
        model.addAttribute("tokenotp", Base32.random());
        model.addAttribute("error", error);
        return "registration";
    }

    @PostMapping("/registration/confirm")
    public String saveNewUser(@RequestParam(value = "token") String token,
                              User formNewUser,
                              @RequestParam("tokenotp") String tokenotp,
                              Model model) throws UnsupportedEncodingException {

        User userTestU = userServiceImpl.findUserByUserName(formNewUser.getUsername());
        if(userTestU != null){
            String error = "User with username " + userTestU.getUsername() + " already axist. Choose another username";
            return "redirect:/registration/"+error;
        }

        User userTestE = userServiceImpl.findUserByEmail(formNewUser.getEmail());
        if(userTestE != null){
            String error = "User with email " + userTestE.getUsername() + " already axist. Choose another email";
            return "redirect:/registration/"+error;
        }

        User user = new User();
        user.setUsername(formNewUser.getUsername());
        user.setPassword(formNewUser.getPassword());
        user.setEmail(formNewUser.getEmail());
        user.setName(formNewUser.getName());
        user.setLastname(formNewUser.getLastname());
        user.setUsing2FA(formNewUser.isUsing2FA());
        user.setSecret(tokenotp);

        User saved = userServiceImpl.saveNewUser(user, token);
        model.addAttribute("user", saved);

        model.addAttribute("error","");

        if(saved.isUsing2FA()) {
            return "redirect:/qrcode/"+saved.getId()+"/"+token;
        }

        return "redirect:/login";
    }

    @GetMapping("/qrcode/{id}/{token}")
    public String showQrCode(@PathVariable long id, @PathVariable String token,  Model model) throws UnsupportedEncodingException {

        String result = userServiceImpl.validateVerificationToken(token);
        if(result.equals("valid")) {
            User user = userServiceImpl.getUser(token);
            if (user.isUsing2FA()) {
                String qr = userServiceImpl.generateQRUrl(user);
                model.addAttribute("secret", user.getSecret());
                model.addAttribute("qr", qr);
                return "qrcode";
            }

            model.addAttribute("message", "accountVerified");
            return "redirect:/login";
        }

        return "redirect:/registration";

    }


}
