package com.example.personal_finance_api.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.example.personal_finance_api.entity.User;
import com.example.personal_finance_api.exception.ResourceNotFoundException;
import com.example.personal_finance_api.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		Integer userId = jwtService.extractUserId(token);
		String role = jwtService.extractRole(token);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("token yaratishda user topilmadi!" + userId));

		if (!jwtService.isTokenValid(token, user)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token yaroqsiz yoki muddati tugagan");
		}
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
				List.of(authority));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

}
