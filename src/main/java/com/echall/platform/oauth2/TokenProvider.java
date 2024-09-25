package com.echall.platform.oauth2;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.echall.platform.user.domain.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenProvider {
	private final JwtProperties jwtProperties;

	public String generateToken(UserEntity user, Duration expiredAt) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuer(jwtProperties.getIssuer())
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + expiredAt.toMillis()))
			.setSubject(user.getEmail())
			.claim("id", user.getId())
			.claim("role", user.getRole().name())
			.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);

		String role = claims.get("role", String.class);
		Set<SimpleGrantedAuthority> authorities
			= Collections.singleton(new SimpleGrantedAuthority(role == null ? "ROLE_USER_UNCERTIFIED" : role));
		User user = new User(
			claims.getSubject(), "", authorities
		);
		return new UsernamePasswordAuthenticationToken(user, token, authorities);
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.setSigningKey(jwtProperties.getSecret())
			.parseClaimsJws(token)
			.getBody();
	}
}
