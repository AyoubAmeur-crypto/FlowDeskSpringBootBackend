package spring.tuto.flowdesk.jwt.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import spring.tuto.flowdesk.entities.Role;
import spring.tuto.flowdesk.entities.User;
import spring.tuto.flowdesk.enums.AppRole;
import spring.tuto.flowdesk.jwt.services.AuthEntryPointJwt;
import spring.tuto.flowdesk.jwt.services.AuthTokenFilter;
import spring.tuto.flowdesk.jwt.services.UserDetailsServiceImplt;
import spring.tuto.flowdesk.repositories.RoleRepository;
import spring.tuto.flowdesk.repositories.UserRepository;

import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class WebSecurityConfig {


    @Autowired
    UserDetailsServiceImplt userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Use patterns instead of specific origins
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Set-Cookie")); // Add this

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()

                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()

                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }


    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles



            // Create users if not already present
            if (!userRepository.existsByUserEmail("user1@example.com")) {
                User user1 = new User("user1","user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
                Role clientRole = roleRepository.findByRole(AppRole.ROLE_CLIENT)
                        .orElseGet(() -> {
                            Role newUserRole = new Role(AppRole.ROLE_CLIENT);

                            return roleRepository.save(newUserRole);
                        });
                Set<Role> userRoles = Set.of(clientRole);
                user1.setUserRoles(userRoles);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserEmail("freelancer@example.com")) {
                User freelancer = new User("freelancer1","freelancer1", "freelancer@example.com", passwordEncoder.encode("password2"));
                userRepository.save(freelancer);
                Role freelancerRole = roleRepository.findByRole(AppRole.ROLE_FREELANCER)
                        .orElseGet(() -> {
                            Role newSellerRole = new Role(AppRole.ROLE_FREELANCER);
                            return roleRepository.save(newSellerRole);
                        });
                Set<Role> freelancerRoles = Set.of(freelancerRole);
                freelancer.setUserRoles(freelancerRoles);
                userRepository.save(freelancer);
            }

            if (!userRepository.existsByUserEmail("admin@example.com")) {
                User admin = new User("admin","admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
                Role adminRole = roleRepository.findByRole(AppRole.ROLE_ADMIN)
                        .orElseGet(() -> {
                            Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                            return roleRepository.save(newAdminRole);
                        });
                Role freelancerRole = roleRepository.findByRole(AppRole.ROLE_FREELANCER)
                        .orElseGet(() -> {
                            Role newSellerRole = new Role(AppRole.ROLE_FREELANCER);
                            return roleRepository.save(newSellerRole);
                        });
                Role clientRole = roleRepository.findByRole(AppRole.ROLE_CLIENT)
                        .orElseGet(() -> {
                            Role newUserRole = new Role(AppRole.ROLE_CLIENT);

                            return roleRepository.save(newUserRole);
                        });


                Set<Role> adminRoles = Set.of(clientRole, freelancerRole, adminRole);
                admin.setUserRoles(adminRoles);
                userRepository.save(admin);

            }










        };
    }
}
