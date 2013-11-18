package gov.uscis.web.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface GrantedAuthorityBuilder {
    public List<GrantedAuthority> buildAuthorities(Object credential);
}
