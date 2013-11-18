package gov.uscis.web.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    
    private AuthorizationAttributesProvider authorizationAttributesProvider;
    
    public AuthorizationAttributesProvider getAuthorizationAttributesProvider() {
		return authorizationAttributesProvider;
	}
    
    public void setAuthorizationAttributesProvider(AuthorizationAttributesProvider authorizationAttributesProvider) {
		this.authorizationAttributesProvider = authorizationAttributesProvider;
	}

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        
        root.setAuthorizationAttributesProvider(authorizationAttributesProvider);
        return root;
    }
    
    public void setReturnObject(Object returnObject, EvaluationContext ctx) {
        ((CustomMethodSecurityExpressionRoot) ctx.getRootObject().getValue()).setReturnObject(returnObject);
    }
}
