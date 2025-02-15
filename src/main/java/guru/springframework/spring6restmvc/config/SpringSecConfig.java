package guru.springframework.spring6restmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()    // we need to configure authentication in order too because we are overwriting the default behavior
                        .anyRequest().authenticated()
                        .and().httpBasic(Customizer.withDefaults())
                .csrf(httpSecurityCsrfConfigurer -> {
                    httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/**");
                });
                // csrf is not needed so we disable csrf protection to be able to use unsafe http methods
                // if we did have a web application and have web pages then we should enable csrf,
                // but since we are using pure rest API and we are not using cookies, we don't need csrf
        return httpSecurity.build();
    }
}
