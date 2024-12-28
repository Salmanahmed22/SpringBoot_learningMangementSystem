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
            http.csrf(AbstractHttpConfigurer::disable)
                    .securityMatcher("/api/**")
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/api/users/**").permitAll()
                            .requestMatchers("/api/students/**").hasAnyRole(Role.STUDENT.name(), Role.ADMIN.name(),
                                    Role.INSTRUCTOR.name())
                            .requestMatchers("/api/instructors/**").hasAnyRole(Role.INSTRUCTOR.name(), Role.ADMIN.name())
                            .requestMatchers("/api/admins/**").hasRole(Role.ADMIN.name())
                            .anyRequest().authenticated()
                    ).sessionManagement((session) -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .exceptionHandling(
                            e -> e.authenticationEntryPoint(
                                    (request, response, authException) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
                            )
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }

    }