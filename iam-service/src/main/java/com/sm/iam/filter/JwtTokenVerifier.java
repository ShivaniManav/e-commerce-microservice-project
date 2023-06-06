package com.sm.iam.filter;

import com.sm.core.util.JWTUtil;
import com.sm.iam.service.TokenService;
import com.sm.iam.utils.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

	private final TokenService tokenService;

	private final JWTUtil jwtUtil;

	@Override
	@SuppressWarnings("unchecked")
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String token = CookieUtil.getAccessTokenFromCookie(request.getCookies());
		if (token == null || token.equals("")) {
			filterChain.doFilter(request, response);
			return;
		}
		if(tokenService.isTokenInBlacklist(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			Claims body = jwtUtil.getAllClaimsFromToken(token);
			String username = body.getSubject();
			List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
					.map(m -> new SimpleGrantedAuthority(m.get("authority")))
					.collect(Collectors.toSet());
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			request.setAttribute("username", username);
			request.setAttribute("authorities", simpleGrantedAuthorities);
		} catch (JwtException e) {
			throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
		}
		filterChain.doFilter(request, response);
	}

}
