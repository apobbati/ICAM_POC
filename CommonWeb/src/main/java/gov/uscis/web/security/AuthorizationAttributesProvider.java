package gov.uscis.web.security;

import org.springframework.security.core.Authentication;

import java.util.Set;

/**
 * This interface provides a mechanism for callers to query the current
 * principal's authorization attributes.
 */
public interface AuthorizationAttributesProvider {
    /**
     * Provides the set of attributes for current authenticated user.
     */
    public Set<String> getAttributes(Authentication authentication);

    /**
     * Checks whether the specified user has a particular attribute.
     */
    public boolean hasAttribute(Authentication authentication, String attribute);
}
