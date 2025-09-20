package com.example.myapp.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;




@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // 管理者と一般ユーザーでのホーム画面の分離
    @Bean
    public AuthenticationSuccessHandler successHandler() {
    return (request, response, authentication) -> {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/home");
                return;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                response.sendRedirect("/home");
                return;
            }
        }
        response.sendRedirect("/login?error");
    };
}


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
            .authenticationProvider(daoAuthenticationProvider())
            .formLogin(login -> login
                .loginPage("/login")
                .usernameParameter("name")
                .passwordParameter("password")
                // ログイン成功時の遷移先
                .successHandler(successHandler())
                .permitAll())
            .logout(logout -> logout
                // ログアウト成功時の遷移先
                .logoutSuccessUrl("/login?logout")
                .permitAll())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home").hasAnyRole("USER","ADMIN")
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/top","/manager","/login","/register","/eventcalendar","/eventlist","/eventdetail/{id}","/api/events/**","/css/**","/js/**").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
