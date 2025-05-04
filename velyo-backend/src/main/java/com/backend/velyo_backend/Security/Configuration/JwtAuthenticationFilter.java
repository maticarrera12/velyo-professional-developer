package com.backend.velyo_backend.Security.Configuration;

import com.backend.velyo_backend.Security.Service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
       final String authHeader = request.getHeader("Authorization");
       final String jwt;
//       final String userEmail
       String userEmail = null;

       if (authHeader == null || !authHeader.startsWith("Bearer ")){
           filterChain.doFilter(request,response);
           return;
       }

       jwt = authHeader.substring(7);
       try{
           userEmail = jwtService.extractUsername(jwt);
       }catch (ExpiredJwtException e){
           log.error("El token JWT ha expirado");
           response.setStatus(HttpStatus.UNAUTHORIZED.value());
           return;
       }
       if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
           log.info("Validando el token JWT para el usuario: {}", userEmail);
           try {
               UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

               if(!jwtService.isValidToken(jwt, userDetails)){
                   response.setStatus(HttpStatus.UNAUTHORIZED.value());
                   return;
               }

               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                       userDetails,
                       null,
                       userDetails.getAuthorities());
               authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }catch (ExpiredJwtException e){
               response.setStatus(HttpStatus.UNAUTHORIZED.value());
               return;
           }catch (Exception e){
           response.setStatus(HttpStatus.FORBIDDEN.value());
           return;
           }
       }
       filterChain.doFilter(request, response);
    }
}
