package eco.spring.adminserver.security;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.UUID;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/close", "/webjars/**", "/manage/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler
                = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl("/");

        http.authorizeRequests()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login")
                .successHandler(successHandler).and()
                .logout().logoutUrl("/logout").and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(
                        "/instances",
                        "/actuator/**"
                );
    }


//    private final AdminServerProperties adminServer;
//
//    public WebSecurityConfig(AdminServerProperties adminServer) {
//        this.adminServer = adminServer;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        SavedRequestAwareAuthenticationSuccessHandler successHandler =
//                new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setTargetUrlParameter("redirectTo");
//        successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");
//
//        http
//                .authorizeRequests()
//                .antMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll()
//                .antMatchers(this.adminServer.getContextPath() + "/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage(this.adminServer.getContextPath() + "/login")
//                .successHandler(successHandler)
//                .and()
//                .logout()
//                .logoutUrl(this.adminServer.getContextPath() + "/logout")
//                .and()
//                .httpBasic()
//                .and()
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .ignoringRequestMatchers(
//                        new AntPathRequestMatcher(this.adminServer.getContextPath() +
//                                "/instances", HttpMethod.POST.toString()),
//                        new AntPathRequestMatcher(this.adminServer.getContextPath() +
//                                "/instances/*", HttpMethod.DELETE.toString()),
//                        new AntPathRequestMatcher(this.adminServer.getContextPath() + "/actuator/**"))
//                .and()
//                .rememberMe()
//                .key(UUID.randomUUID().toString())
//                .tokenValiditySeconds(1209600);
//    }
}
