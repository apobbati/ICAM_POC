package gov.uscis.web.security;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.springframework.security.saml.SAMLCredential;

class SAMLUtil {
    public static String getAttributeValue(SAMLCredential samlCredential, String attributeName) {
        Attribute attribute = samlCredential.getAttributeByName(attributeName);
        if(attribute != null) {
            for(XMLObject attrObj : attribute.getAttributeValues()) {
                String attrValue = attrObj.getDOM().getTextContent();
                return attrValue;
            }
        }

        // TODO: Handler error here
        return null;
    }
}
