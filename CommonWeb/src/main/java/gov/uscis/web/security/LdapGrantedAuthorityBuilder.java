package gov.uscis.web.security;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml.SAMLCredential;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import java.util.ArrayList;
import java.util.List;

public class LdapGrantedAuthorityBuilder implements GrantedAuthorityBuilder {
    private Logger log = LoggerFactory.getLogger(LdapGrantedAuthorityBuilder.class);

    private String groupsAttribute = "IsMemberOf";

    public String getGroupsAttribute() {
        return groupsAttribute;
    }

    public void setGroupsAttribute(String groupsAttribute) {
        this.groupsAttribute = groupsAttribute;
    }

    @Override
    public List<GrantedAuthority> buildAuthorities(Object credential) {
        SAMLCredential samlCredential = (SAMLCredential) credential;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Attribute attribute = samlCredential.getAttributeByName(groupsAttribute);
        if(attribute != null) {
            for(XMLObject attrObj : attribute.getAttributeValues()) {
                String attrValue = attrObj.getDOM().getTextContent();
                try {
                    LdapName dn = new LdapName(attrValue);
                    String dnFragment = dn.get(dn.size() - 1);
                    String groupName = dnFragment.substring(dnFragment.indexOf('=') + 1);
                    grantedAuthorities.add(new SimpleGrantedAuthority(groupName));
                }
                catch(IndexOutOfBoundsException e) {
                    log.error("Unrecognized LDAP DN: " + attrValue, e);
                }
                catch(InvalidNameException e) {
                    log.error("Failed to parse LDAP group: " + attrValue, e);
                }
            }
        }
        return grantedAuthorities;
    }
}
