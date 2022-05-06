package com.bepastem.security;

import com.bepastem.jwtAuth.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.SecureRandom;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.httpBasic().disable();
        security.csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/authenticate/user").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/register/user").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/register/agency").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/passwordUpdate").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/victim/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/notification/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/test/**").permitAll()
                .anyRequest().authenticated() //aside the above two endpoints, any request to any other endpoint must be authenticated

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         security.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

   @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8080/api/v1")
                .build();
    }

   /*@Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder().baseUrl("http://localhost:8080/api/v1");
    }*/

    // To enable CORS
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:63342");
        config.addAllowedOrigin("http://localhost:63343");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }
}