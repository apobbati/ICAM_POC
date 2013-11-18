package gov.uscis.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class AppWebSecurityConfig extends AbstractAppWebSecurityConfigurer {
    @Override
    public String successTargetUrl() {
        return "/";
    }

    @Override
    public String failureTargetUrl() {
        return "/";
    }

    @Override
    public String logoutSuccessUrl() {
        return "/";
    }

    @Override
    public String idpMetadataUrl() {
        return "http://localhost:8080/openam/saml2/jsp/exportmetadata.jsp?entityid=myidp";
    }

    @Override
    public String idpAlias() {
        return "myidp";
    }

    @Override
    public String spMetadataUrl() {
        return "http://localhost:8080/openam/saml2/jsp/exportmetadata.jsp?entityid=externalapp";
    }

    @Override
    public String spAlias() {
        return "externalapp";
    }

    @Override
    public String encryptionKey() {
        return "test";
    }

    @Override
    public String encryptionKeyPassword() {
        return "changeit";
    }

    @Override
    public String signingKey() {
        return "test";
    }

    @Override
    public String signingKeyPassword() {
        return "changeit";
    }

    @Override
    public String keyStorePassword() {
        return "changeit";
    }

    @Override
    public Resource keyStore() {
        return new ClassPathResource("keystore.jks");
    }
}
