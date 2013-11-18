package gov.uscis.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {
    private Logger logger = LoggerFactory.getLogger(Test.class);

    @PreAuthorize("hasRole('NOT_THERE')")
    @RequestMapping(value = "/somedata", method = RequestMethod.GET)
    public @ResponseBody String foo() {
        return "Hello!";
    }

    @PreAuthorize("hasRole('ACCESS_LEVEL1')")
    @RequestMapping(value = "/blah", method = RequestMethod.GET)
    public @ResponseBody String bar() {
        return "World!";
    }
}
