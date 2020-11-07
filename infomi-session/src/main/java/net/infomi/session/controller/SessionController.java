package net.infomi.session.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会话控制器
 */
@RestController
public class SessionController {

    @RequestMapping("/checkLogin")
    public String checkLogin() {
        return "ok";
    }

}
