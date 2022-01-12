package com.luxcampus.web.controller;
import com.luxcampus.entity.User;
import com.luxcampus.service.SecurityService;
import com.luxcampus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    //return login page
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    protected String getLogin() {
        return "login";
    }

    //auth
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    protected String login(@RequestParam String password,
                           @RequestParam String email, HttpServletResponse response) {
        String hashPassword = securityService.getHash(password);
        User user = User.builder().email(email).password(hashPassword).build();
        if (userService.isNew(user)) {
            System.out.println("Is new!");
            return "redirect:registration";
        } else {
            System.out.println("Not new!");
            String userToken = UUID.randomUUID().toString();
            securityService.getUserTokens().add(userToken);
            Cookie cookie = new Cookie("user-token", userToken);
            response.addCookie(cookie);
            return "redirect:/products";
        }
    }

    //get registration
    @RequestMapping(path = "/registration", method = RequestMethod.GET)
    protected String getRegistration(){
        return "registration";
    }

    //post registration
    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    protected String registration(@RequestParam String name,
                                @RequestParam String lastName,
                                @RequestParam String password,
                                @RequestParam String email){
            String hashPassword = securityService.getHash(password);
            User user = User.builder()
                    .name(name)
                    .lastName(lastName)
                    .email(email)
                    .password(hashPassword)
                    .build();
            userService.save(user);
            return "redirect:/login";
    }

    //logOut
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    protected String getLogOut(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("user-token")){
                System.out.println(cookie.getValue());
                securityService.logOut(cookie);
            }
        }
        return "redirect:/login";
    }
}
