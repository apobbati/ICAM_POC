package gov.uscis.web.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

abstract class CustomSecurityExpressionRoot extends SecurityExpressionRoot {
    private AuthorizationAttributesProvider authorizationAttributesProvider;
    
    public void setAuthorizationAttributesProvider(
			AuthorizationAttributesProvider authorizationAttributesProvider) {
		this.authorizationAttributesProvider = authorizationAttributesProvider;
	}
    
    public AuthorizationAttributesProvider getAuthorizationAttributesProvider() {
		return authorizationAttributesProvider;
	}

    public CustomSecurityExpressionRoot(Authentication a) {
        super(a);
    }

    public boolean hasAttribute(String attribute) {
    	return authorizationAttributesProvider.hasAttribute(getAuthentication(), attribute);
    }
}
