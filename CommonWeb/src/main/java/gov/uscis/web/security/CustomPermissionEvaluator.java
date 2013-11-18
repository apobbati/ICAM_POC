package gov.uscis.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Map;

public class CustomPermissionEvaluator implements PermissionEvaluator {
	private Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);
	
	private Map<String, Permission> permissionObjects;
	
	private AuthorizationAttributesProvider authorizationAttributesProvider;

    public AuthorizationAttributesProvider getAuthorizationAttributesProvider() {
        return authorizationAttributesProvider;
    }
    
    public void setAuthorizationAttributesProvider(AuthorizationAttributesProvider authorizationAttributesProvider) {
        this.authorizationAttributesProvider = authorizationAttributesProvider;
    }
	
	public Map<String, Permission> getPermissionObjects() {
        return permissionObjects;
    }

    public void setPermissionObjects(Map<String, Permission> permissionObjects) {
        this.permissionObjects = permissionObjects;
    }
    
    protected Permission lookupPermission(Authentication authentication, Object permission) {
    	Permission permissionObject = null;

        if(authorizationAttributesProvider.hasAttribute(authentication, permission.toString())) {
            permissionObject = permissionObjects.get(permission);
        }

        if(permissionObject == null) {
            logger.error("Permission " + permission + " not found for user!");
        }
        return permissionObject;
    }

	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		logger.debug("Inside hasPermission()");
		
		if(targetDomainObject == null) {
			throw new IllegalStateException("Target domain object cannot be null for permission: " + permission);
        }
		
		Permission permissionObject = lookupPermission(authentication, permission);
        if(permissionObject == null) {
        	logger.error("Target domain object is null!");
            return false;
        }

        logger.debug("Evaluating permission " + permission);
        
        return permissionObject.isAllowed((CustomUser) authentication.getPrincipal(), targetDomainObject);
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		throw new UnsupportedOperationException();
	}
}
