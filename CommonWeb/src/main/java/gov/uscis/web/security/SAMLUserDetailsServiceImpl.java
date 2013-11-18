package gov.uscis.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {
    private Logger log = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);

    private GrantedAuthorityBuilder grantedAuthorityBuilder;

    public GrantedAuthorityBuilder getGrantedAuthorityBuilder() {
        return grantedAuthorityBuilder;
    }

    public void setGrantedAuthorityBuilder(GrantedAuthorityBuilder grantedAuthorityBuilder) {
        this.grantedAuthorityBuilder = grantedAuthorityBuilder;
    }

    public Object loadUserBySAML(SAMLCredential samlCredential) throws UsernameNotFoundException {
        log.info("----------------------------------------");
        log.info("Building UserObject from SAML Credential: " + samlCredential);

        if(samlCredential != null) {
            List<GrantedAuthority> grantedAuthorities = grantedAuthorityBuilder.buildAuthorities(samlCredential);

            // Spit out the granted auths
            StringBuffer sb = new StringBuffer();
            for(GrantedAuthority grantedAuthority : grantedAuthorities) {
                sb.append(grantedAuthority.getAuthority() + ", ");
            }
            log.info("User " + samlCredential.getNameID().getValue() + " has authorities: " + sb);

            // TODO: Make this formal.
            Map<String, String> attributes = new HashMap<>();
            attributes.put("email", SAMLUtil.getAttributeValue(samlCredential, "EmailAddress"));
            attributes.put("name", SAMLUtil.getAttributeValue(samlCredential, "FullName"));

            return new CustomUserDetails(samlCredential.getNameID().getValue(), "", grantedAuthorities, attributes);
        }

        log.error("The SAMLCredential was null!");
        return null;
    }
}
