package com.echall.platform.user.controller;

import com.echall.platform.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserPublicController {
	private final CookieUtil cookieUtil;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String join() {
		return "signup";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		cookieUtil.removeCookie(request, response, "access_token");

		cookieUtil.removeCookie(request, response, "refresh_token");

		new SecurityContextLogoutHandler().logout(request, response
			, SecurityContextHolder.getContext().getAuthentication());

		return "redirect:/login";
	}

}
