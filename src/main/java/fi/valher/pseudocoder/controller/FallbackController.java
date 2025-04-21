package fi.valher.pseudocoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FallbackController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward any unmapped paths (except those with a period) to index.html
        return "forward:/index.html";
    }
}