package fi.valher.pseudocoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallbackController {

    // Matches all routes that do not start with "api", do not contain a period, and are not "/login"
    @RequestMapping(value = "/{path:^(?!api)(?!.*\\.).*(?<!login)$}")
    public String redirect() {
        // API routes, static resources, and "/login" are excluded from fallback.
        return "forward:/index.html";
    }
}