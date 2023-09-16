package com.sm.core.util;

import javax.servlet.http.Cookie;

public class CookieUtil {
	
	public static String getAccessTokenFromCookie(Cookie[] cookies) {
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("jwt_token")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	public static String generateCookieForToken(String token, long maxAge) {
		return "jwt_token="+token+";Max-Age="+maxAge+";Path=/;HttpOnly=true;SameSite=None;Secure=false";
	}
	
}
