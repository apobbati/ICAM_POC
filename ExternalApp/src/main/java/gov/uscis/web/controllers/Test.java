package gov.uscis.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {
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

    @PreAuthorize("isFullyAuthenticated()")
    @RequestMapping(value = "/endpoint", method = RequestMethod.GET)
    public @ResponseBody Person blah() {
        Person person = new Person();
        person.setName("Foobar");
        person.setAddress("Baker Street");
        return person;
    }

    class Person {
        private String name;

        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
