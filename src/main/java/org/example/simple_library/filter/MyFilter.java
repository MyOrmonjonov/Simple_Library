package org.example.simple_library.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.simple_library.entity.Role;
import org.example.simple_library.repo.UsersRepository;
import org.example.simple_library.security.JwtService;
import org.example.simple_library.servic.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Component
public class MyFilter extends OncePerRequestFilter {


    private final UsersRepository usersRepository;
    private final JwtService jwtService;

    public MyFilter(JwtService jwtService, UsersRepository usersRepository) {
        this.jwtService = jwtService;
        this.usersRepository = usersRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");

        if (token != null) {
            if (jwtService.isValid(token)) {
                String username = jwtService.getUsername(token);
                Role roles = jwtService.getRole(token);


                GrantedAuthority authority = new SimpleGrantedAuthority(roles.getAuthority()); // getAuthority() orqali

                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(username)
                        .authorities(authority)
                        .password("")
                        .build();

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );


                SecurityContextHolder.getContext().setAuthentication(auth);


                System.out.println("Received Token: " + token);
                System.out.println("Token Validity: " + jwtService.isValid(token));
                System.out.println("Roles in Token: " + roles.getAuthority());
            }
        }

        filterChain.doFilter(request, response);
    }

}
