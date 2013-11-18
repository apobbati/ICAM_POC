package gov.uscis.web.security;

import org.springframework.security.core.Authentication;

import java.util.Set;

public class CustomUserAuthAttrProviderImpl implements AuthorizationAttributesProvider {
	@Override
	public Set<String> getAttributes(Authentication authentication) {
		CustomUser user = (CustomUser) authentication.getPrincipal();
		return user.getAttributes();
	}

	@Override
	public boolean hasAttribute(Authentication authentication, String attribute) {
		return getAttributes(authentication).contains(attribute);
	}
}
