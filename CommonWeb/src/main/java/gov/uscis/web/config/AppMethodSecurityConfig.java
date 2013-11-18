package gov.uscis.web.config;

import gov.uscis.web.security.AuthorizationAttributesProvider;
import gov.uscis.web.security.CustomMethodSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppMethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	@Autowired
	private PermissionEvaluator permissionEvaluator;
	
	@Autowired
	private AuthorizationAttributesProvider authorizationAttributesProvider;

	@Override
	protected MethodSecurityExpressionHandler expressionHandler() {
		CustomMethodSecurityExpressionHandler handler = new CustomMethodSecurityExpressionHandler();
		handler.setPermissionEvaluator(permissionEvaluator);
		handler.setAuthorizationAttributesProvider(authorizationAttributesProvider);
		return handler;
	}
}
