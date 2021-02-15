package com.capstone.tmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String index() {
        return "board/read";
    }

    @GetMapping("/main")
    public String main() {
        return "board/main";
    }
}
