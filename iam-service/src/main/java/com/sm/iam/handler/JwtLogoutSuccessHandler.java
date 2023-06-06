package com.sm.iam.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.core.util.JWTUtil;
import com.sm.iam.dto.response.AuthenticationResponse;
import com.sm.iam.service.TokenService;
import com.sm.iam.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final TokenService tokenService;

    private final JWTUtil jwtUtil;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String token = CookieUtil.getAccessTokenFromCookie(request.getCookies());
        if(token != null) {
            long ttl = (jwtUtil.getExpirationDateFromToken(token).getTime()/1000) - (new Date().getTime()/1000);
            tokenService.addTokenInBlacklist(token, ttl);
        }
        response.addHeader("Set-Cookie", CookieUtil.generateCookieForToken(token, Long.parseLong("0")));
        response.getOutputStream().write(mapper.writeValueAsBytes(AuthenticationResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase()).isAuthenticated(false).build()));
    }
}
