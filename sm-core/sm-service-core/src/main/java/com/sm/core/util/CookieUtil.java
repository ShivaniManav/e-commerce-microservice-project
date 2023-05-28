package com.sm.core.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

public class CookieUtil {
	
	public static String getAccessTokenFromCookie(Cookie[] cookies) {
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("access_token")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static String generateCookieForToken(String token, long maxAge) {
		return "access_token="+token+";Max-Age="+maxAge+";Path=/;HttpOnly=true;SameSite=None;Secure=false";
	}
	
}
