package gov.uscis.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserDetails extends User {
    private Map<String, String> readonlyAttributes;

    public CustomUserDetails(String userName, String password, Collection<? extends GrantedAuthority> grantedAuthorities, Map<String, String> attributes) {
        super(userName, password, grantedAuthorities);

        this.readonlyAttributes = Collections.unmodifiableMap(attributes);
    }

    public Map<String, String> getAttributes() {
        return readonlyAttributes;
    }
}
