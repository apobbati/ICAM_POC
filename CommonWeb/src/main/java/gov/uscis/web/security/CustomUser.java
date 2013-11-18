package gov.uscis.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class CustomUser extends User {
	private static final long serialVersionUID = -5197295789604797286L;
	
	private final String email;
	
	private Set<String> attributes;
	
	public Set<String> getAttributes() {
		return attributes;
	}
	
	protected CustomUser(String user, String password, String email, Collection<? extends GrantedAuthority> authorities, Set<String> attrs) {
		super(user, password, authorities);
		this.email = email;
		this.attributes = Collections.unmodifiableSet(attrs);
	}
	
	public String getEmail() {
		return email;
	}
}
