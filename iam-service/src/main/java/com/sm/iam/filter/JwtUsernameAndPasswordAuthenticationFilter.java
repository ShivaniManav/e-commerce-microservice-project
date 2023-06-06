package com.sm.iam.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.core.exception.SMServiceException;
import com.sm.core.util.JWTUtil;
import com.sm.iam.dto.request.LoginRequest;
import com.sm.iam.dto.response.AuthenticationResponse;
import com.sm.iam.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	private final JWTUtil jwtUtil;

	private final ObjectMapper mapper = new ObjectMapper();

	//this method is executed for default spring security endpoint /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
												HttpServletResponse response) throws AuthenticationException {
		try {
			LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
													loginRequest.getUsername(),
													loginRequest.getPassword()
												);
			return authenticationManager.authenticate(authentication);
		} catch (IOException e) {
			throw new SMServiceException("Authentication Failure");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		final String token = jwtUtil.generateToken(authResult.getName(), authResult.getAuthorities());
		long maxAge = jwtUtil.getExpirationDateFromToken(token).getTime()/1000;
		response.addHeader("Set-Cookie", CookieUtil.generateCookieForToken(token, maxAge));
		response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().write(mapper.writeValueAsBytes(AuthenticationResponse.builder()
				.status(HttpStatus.OK.getReasonPhrase()).username(authResult.getName()).token(token)
				.authorities((List<GrantedAuthority>) authResult.getAuthorities()).isAuthenticated(true).build()));
	}
	
}
