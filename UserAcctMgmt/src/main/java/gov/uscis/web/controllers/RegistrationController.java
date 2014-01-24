package gov.uscis.web.controllers;

import com.sun.jersey.api.client.Client;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {
    class UserRegistration {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @RequestMapping("/register")
    public String register() {
        // Invoke Rest API on Form submission..

        return "register";
    }
}
