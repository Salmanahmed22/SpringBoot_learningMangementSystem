    package com.example.demo.config;

    import com.example.demo.models.Role;
    import jakarta.servlet.DispatcherType;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    @EnableMethodSecurity(
            jsr250Enabled = true,
            securedEnabled = true
    )
    public class SecurityConfig {
        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
    //                .securityMatcher("/api/**")
    //                .authorizeHttpRequests(authorize -> authorize
    //                        .requestMatchers("/api/users/**").permitAll()
    ////                        students and admin and instructor all are authorized to api/students
    //                        .requestMatchers("/api/students/**").hasAnyRole(Role.STUDENT.name(), Role.ADMIN.name(),
    //                                Role.INSTRUCTOR.name())
    //                        //only Admin and instructor are authorized to api/instructors
    ////                        .requestMatchers("/api/instructors/**").hasAnyRole(Role.INSTRUCTOR.name(), Role.ADMIN.name())
    //                        //only admin
    ////                        .requestMatchers("/api/admins/**").hasRole(Role.ADMIN.name())
    //                        .requestMatchers("/api/admins/**").permitAll()
    ////                        .anyRequest().authenticated()
    //                ).sessionManagement((session) -> session
    //                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //                )
    //                .exceptionHandling(
    //                        e -> e.authenticationEntryPoint(
    //                                (request, response, authException) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
    //                        )
    //                )
    //                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

    }