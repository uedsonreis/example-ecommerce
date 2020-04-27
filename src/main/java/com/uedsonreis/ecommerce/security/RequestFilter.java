package com.uedsonreis.ecommerce.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.UserService;
import com.uedsonreis.ecommerce.utils.Util;

@Component
public class RequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String jwtToken = request.getHeader(Util.AUTH);
		User user = null;
		
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			
			try {
				user = this.userService.getLoggedUser(jwtToken);
			} catch (Exception e) {
				System.err.print("Token "+ jwtToken +" ");
				System.err.println("is invalid: "+ e.getMessage());
			}

			if (user != null) {
				UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userToken);
			}
		}

		chain.doFilter(request, response);
	}

}