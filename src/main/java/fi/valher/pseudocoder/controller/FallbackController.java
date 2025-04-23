package fi.valher.pseudocoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallbackController {

    // Matches all routes that do not start with "api" and do not contain a period
    @RequestMapping(value = "/{path:^(?!api).*$}")
    public String redirect() {
        // API routes are excluded from fallback.
        return "forward:/index.html";
    }
}