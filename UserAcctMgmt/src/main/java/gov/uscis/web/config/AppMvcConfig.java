package gov.uscis.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"gov.uscis.web"})
public class AppMvcConfig extends AbstractAppMvcConfig {
}
