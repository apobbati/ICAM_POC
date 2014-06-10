package gov.uscis.web.config;

import gov.uscis.web.security.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.xml.parse.BasicParserPool;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.*;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.*;
import org.springframework.security.saml.processor.*;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.AntPathRequestMatcher;

import java.util.*;

public abstract class AbstractAppWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    public Map<String, Permission> permissionObjects() {
        return new HashMap<>();
    }

    public abstract String successTargetUrl();

    public abstract String failureTargetUrl();

    public abstract String logoutSuccessUrl();

    public abstract String idpMetadataUrl();

    public abstract String idpAlias();

    public abstract String spMetadataUrl();

    public abstract String spAlias();

    public abstract String encryptionKey();

    public abstract String encryptionKeyPassword();

    public abstract String signingKey();

    public abstract String signingKeyPassword();

    public abstract String keyStorePassword();

    public abstract Resource keyStore();

    protected ExtendedMetadataDelegate spMetadataDelegate() throws Exception {
        HTTPMetadataProvider spMetadataProvider = new HTTPMetadataProvider(spMetadataUrl(), 5000);
        spMetadataProvider.setParserPool(parserPool());

        ExtendedMetadata spExtendedMetadataProvider = new ExtendedMetadata();
        spExtendedMetadataProvider.setLocal(true);
        spExtendedMetadataProvider.setAlias(spAlias());
        spExtendedMetadataProvider.setEncryptionKey(encryptionKey());
        spExtendedMetadataProvider.setSigningKey(signingKey());

        ExtendedMetadataDelegate spDelegate = new ExtendedMetadataDelegate(spMetadataProvider, spExtendedMetadataProvider);
        return spDelegate;
    }

    protected ExtendedMetadataDelegate idpMetadataDelegate() throws Exception {
        HTTPMetadataProvider idpMetadataProvider = new HTTPMetadataProvider(idpMetadataUrl(), 5000);
        idpMetadataProvider.setParserPool(parserPool());

        ExtendedMetadata idpExtendedMetadataProvider = new ExtendedMetadata();
        idpExtendedMetadataProvider.setAlias(idpAlias());
        idpExtendedMetadataProvider.setSigningKey(signingKey());
        idpExtendedMetadataProvider.setEncryptionKey(encryptionKey());

        ExtendedMetadataDelegate idpDelegate = new ExtendedMetadataDelegate(idpMetadataProvider, idpExtendedMetadataProvider);
        return idpDelegate;
    }

    @Bean(name = "metadata")
    public CachingMetadataManager metadata() throws Exception {
        List<MetadataProvider> providers = new ArrayList<>();

        providers.add(idpMetadataDelegate());
        providers.add(spMetadataDelegate());

        CachingMetadataManager manager = new CachingMetadataManager(providers);
        manager.setHostedSPName(spAlias());
        manager.setDefaultIDP(idpAlias());

        return manager;
    }

    @Bean(name = "metadataDisplayFilter")
    public MetadataDisplayFilter metadataDisplayFilter() {
        return new MetadataDisplayFilter();
    }

    @Bean(name = "metadataGeneratorFilter")
    public MetadataGeneratorFilter metadataGeneratorFilter() {
        MetadataGenerator metadataGenerator = new MetadataGenerator();

        // Explicitly set the signing algorithm here
        // In most cases, the signing algorithm is determined by the Credential
        // object, which in most cases is extracted from public-private keypair
        // living in the keystore

        // metadataGenerator.setSigningAlgorithm(...);

        return new MetadataGeneratorFilter(metadataGenerator);
    }

    @Bean(name = "samlEntryPoint")
    public SAMLEntryPoint samlEntryPoint() {
        WebSSOProfileOptions options = new WebSSOProfileOptions();
        options.setIncludeScoping(false);

        SAMLEntryPoint entryPoint = new SAMLEntryPoint();
        entryPoint.setDefaultProfileOptions(options);

        return entryPoint;
    }

    @Bean(name = "keyManager")
    public JKSKeyManager keyManager() {
        Map<String, String> keys = new HashMap<>();
        keys.put(signingKey(), signingKeyPassword());
        keys.put(encryptionKey(), encryptionKeyPassword());

        JKSKeyManager keyManager = new JKSKeyManager(keyStore(), keyStorePassword(), keys, null);
        return keyManager;
    }

    @Bean(name = "samlFilter")
    public FilterChainProxy samlFilter() throws Exception {
        List<SecurityFilterChain> filterChains = new ArrayList<>();
        filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"), samlEntryPoint()));
        filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"), samlLogoutFilter()));
        filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"), metadataDisplayFilter()));
        filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"), samlWebSSOProcessingFilter()));
        filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSOHoK/**"), samlWebSSOHoKProcessingFilter()));
        filterChains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"), samlLogoutProcessingFilter()));

        FilterChainProxy filterChainProxy = new FilterChainProxy(filterChains);
        return filterChainProxy;
    }

    @Bean(name = "samlLogger")
    public SAMLDefaultLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    @Bean
    public GrantedAuthorityBuilder grantedAuthorityBuilder() {
        LdapGrantedAuthorityBuilder builder = new LdapGrantedAuthorityBuilder();
        builder.setGroupsAttribute("IsMemberOf");
        return builder;
    }

    @Bean
    public SAMLUserDetailsService samlUserDetailsService() {
        SAMLUserDetailsServiceImpl userDetailsService = new SAMLUserDetailsServiceImpl();
        userDetailsService.setGrantedAuthorityBuilder(grantedAuthorityBuilder());
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        SAMLAuthenticationProvider provider = new SAMLAuthenticationProvider();
        provider.setUserDetails(samlUserDetailsService());
        return provider;
    }
    
    @Bean
    public AuthorizationAttributesProvider authorizationAttributesProvider() {
    	return new CustomUserAuthAttrProviderImpl();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
    
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return authenticationProvider().authenticate(authentication);
            }
        };
    }

    @Bean(name = "parserPool")
    public BasicParserPool parserPool() {
        return new BasicParserPool();
    }

    @Bean(name = "velocityEngine")
    public VelocityEngine velocityEngine() {
        return VelocityFactory.getEngine();
    }

    @Bean
    public static SAMLBootstrap samlBootstrap() {
        return new SAMLBootstrap();
    }

    @Bean(name = "paosBinding")
    public HTTPPAOS11Binding paosBinding() {
        return new HTTPPAOS11Binding(parserPool());
    }

    @Bean(name = "soapBinding")
    public HTTPSOAP11Binding soapBinding() {
        return new HTTPSOAP11Binding(parserPool());
    }

    @Bean(name = "artifactBinding")
    public HTTPArtifactBinding artifactBinding() {
        return new HTTPArtifactBinding(parserPool(), velocityEngine(), artifactResolutionProfile());
    }

    @Bean(name = "redirectBinding")
    public HTTPRedirectDeflateBinding redirectBinding() {
        return new HTTPRedirectDeflateBinding(parserPool());
    }

    @Bean(name = "postBinding")
    public HTTPPostBinding postBinding() {
        return new HTTPPostBinding(parserPool(), velocityEngine());
    }

    @Bean(name = "logoutprofile")
    public SingleLogoutProfileImpl logoutprofile() {
        return new SingleLogoutProfileImpl();
    }

    @Bean(name = "ecpprofile")
    public WebSSOProfileECPImpl ecpprofile() {
        return new WebSSOProfileECPImpl();
    }

    @Bean(name = "hokWebSSOProfile")
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean(name = "webSSOprofile")
    public WebSSOProfileImpl webSSOProfile() {
        return new WebSSOProfileImpl();
    }

    @Bean(name = "hokWebSSOprofileConsumer")
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean(name = "webSSOprofileConsumer")
    public WebSSOProfileConsumerImpl webSSOprofileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }

    @Bean(name = "processor")
    public SAMLProcessorImpl samlProcessor() {
        Collection<SAMLBinding> bindingList = new ArrayList<>();
        bindingList.add(redirectBinding());
        bindingList.add(postBinding());
        bindingList.add(artifactBinding());
        bindingList.add(soapBinding());
        bindingList.add(paosBinding());
        return new SAMLProcessorImpl(bindingList);
    }

    @Bean(name = "logoutHandler")
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.setInvalidateHttpSession(true);
        return handler;
    }

    @Bean(name = "successLogoutHandler")
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setDefaultTargetUrl(logoutSuccessUrl());
        return handler;
    }

    @Bean(name = "samlLogoutProcessingFilter")
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
    }

    @Bean(name = "samlLogoutFilter")
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(
                successLogoutHandler(),
                new LogoutHandler[] { logoutHandler() },
                new LogoutHandler[] { logoutHandler() }
        );
    }

    @Bean(name = "samlWebSSOHoKProcessingFilter")
    public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
        SAMLWebSSOHoKProcessingFilter filter = new SAMLWebSSOHoKProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successRedirectHandler());
        filter.setAuthenticationFailureHandler(failureRedirectHandler());
        return filter;
    }

    @Bean(name = "samlWebSSOProcessingFilter")
    public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
        SAMLProcessingFilter filter = new SAMLProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successRedirectHandler());
        filter.setAuthenticationFailureHandler(failureRedirectHandler());
        return filter;
    }

    @Bean(name = "contextProvider")
    public SAMLContextProviderImpl contextProvider() {
        return new SAMLContextProviderImpl();
    }

    @Bean(name = "successRedirectHandler")
    public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl(successTargetUrl());
        return handler;
    }

    @Bean(name = "failureRedirectHandler")
    public SimpleUrlAuthenticationFailureHandler failureRedirectHandler() {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setDefaultFailureUrl(failureTargetUrl());
        return handler;
    }

    protected ArtifactResolutionProfileImpl artifactResolutionProfile() {
        ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
        return artifactResolutionProfile;
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient(new MultiThreadedHttpConnectionManager());
    }

    @Bean
    public CustomPermissionEvaluator permissionEvaluator() {
        CustomPermissionEvaluator evaluator = new CustomPermissionEvaluator();
        evaluator.setAuthorizationAttributesProvider(authorizationAttributesProvider());
        evaluator.setPermissionObjects(permissionObjects());
        return evaluator;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeUrls()
                    .antMatchers("/saml/web/**").permitAll()
                    .anyRequest().fullyAuthenticated()
                .and()
                    .addFilterBefore(metadataGeneratorFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(samlEntryPoint());
    }
}
