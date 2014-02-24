package gov.uscis.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"gov.uscis.web"})
public class AppMvcConfig extends AbstractAppMvcConfig {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }
}
