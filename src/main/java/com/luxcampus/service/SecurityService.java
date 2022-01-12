package com.luxcampus.service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SecurityService {
    private List<String> userTokens = Collections.synchronizedList(new ArrayList<>());
    public List<String> getUserTokens() {
        return userTokens;
    }

    public boolean isAuth(HttpServletRequest req) {
        boolean isAuth = false;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    if (userTokens.contains(cookie.getValue())) {
                        isAuth = true;
                    }
                }
            }
        }
        return isAuth;
    }

    public String getHash(String password) {
        String md5Hex = DigestUtils.md5Hex(password);
        return md5Hex;
    }

    public void logOut(Cookie cookie) {
        userTokens.remove(cookie.getValue());
    }
}
