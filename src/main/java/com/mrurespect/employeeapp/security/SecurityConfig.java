package com.mrurespect.employeeapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/", "/employees/list")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employees/employees")
                        .hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/employees/addEmployee")
                        .hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/employees/enableChange/**")
                        .hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/employees/employees")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/employees/employees/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/", "/employees/seeRequets")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/", "/employees/createRequets")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/", "/employees/employees/createRequest")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/", "/employees/saveEmployee")
                        .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/", "/employees/showFormForUpdate/**")
                        .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/", "/employees/AcceptRequest")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/", "/employees/RejectRequest")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/register/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showMyLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        // Disable CSRF (if not needed for your use case)
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
//        http
//                .authorizeHttpRequests(configurer ->
//                        configurer
//                                .antMatchers(HttpMethod.GET, "/", "/employees/list")
//                                .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
//                                .antMatchers(HttpMethod.POST, "/employees/employees")
//                                .hasAnyRole("MANAGER", "ADMIN")
//                                .antMatchers(HttpMethod.GET, "/employees/addEmployee")
//                                .hasAnyRole("MANAGER", "ADMIN")
//                                .antMatchers(HttpMethod.GET, "/employees/enableChange/**")
//                                .hasAnyRole("MANAGER", "ADMIN")
//                                .antMatchers(HttpMethod.PUT, "/employees/employees")
//                                .hasRole("ADMIN")
//                                .antMatchers(HttpMethod.DELETE, "/employees/employees/**")
//                                .hasRole("ADMIN")
//
//                                .antMatchers("/register/**").permitAll()
//                                .anyRequest().authenticated()//any request to the app must be logged in
//                )
//                .formLogin(form ->
//                        form
//                                .loginPage("/showMyLoginPage")
//                                .loginProcessingUrl("/authenticateTheUser") //login form should post the data to this url for processing,no controller request needed for it
//                                .successHandler(customAuthenticationSuccessHandler)
//                                .permitAll() //allow any one to see the login page
//                ).logout(logout -> logout.permitAll()   //its neceesary for showing the logout message
//                ).exceptionHandling(configurer ->
//                        configurer.accessDeniedPage("/access-denied"));
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}