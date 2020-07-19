package jwt.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jwt.entities.AppUser;

public class JWTFilter extends UsernamePasswordAuthenticationFilter{

private AuthenticationManager authenticationManager;

public JWTFilter(AuthenticationManager authenticationManager) {
	super();
	this.authenticationManager = authenticationManager;
}

@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
	
		try {
			AppUser user=null;
			user=new ObjectMapper().readValue(request.getInputStream(),AppUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Probleme in request content");
		}
		
	}
@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
	User user=(User)authResult.getPrincipal();
	List<String> roles=new ArrayList<String>();
	user.getAuthorities().forEach(au->{
	roles.add(au.getAuthority());	
	});
	
	String jwt=JWT.create().
			withIssuer(request.getRequestURI())
			.withSubject(user.getUsername())
			.withArrayClaim("roles",roles.toArray(new String[roles.size()]))
			.withExpiresAt(new Date(System.currentTimeMillis()+10*24*3600))
			.sign(Algorithm.HMAC256("secret1"));
	response.addHeader("Authorization", jwt);
}

}
