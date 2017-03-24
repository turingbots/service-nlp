package com.citi.hackathon;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/**
 * Created by GK82893 on 12/20/2016.
 */


@RestController
public class ApiController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getApiInfo() {
        return new ModelAndView("redirect:/swagger-ui.html");
    }
}
