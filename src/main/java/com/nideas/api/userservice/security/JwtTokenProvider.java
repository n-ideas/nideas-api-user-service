package com.nideas.api.userservice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nideas.api.userservice.data.dto.auth.JwtPayLoad;
import com.nideas.api.userservice.data.dto.auth.UserPrincipal;
import com.nideas.api.userservice.enumeration.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 7/17/2018. */
@Service
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  @Value("${nideas.user-service.jwtSecret}")
  private String jwtSecret;

  @Value("${nideas.user-service.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  @Autowired private ObjectMapper objectMapper;

  private final char delimiter = '\007';

  public String generateToken(Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
    String subject =
        Long.toString(userPrincipal.getId()) + delimiter + userPrincipal.getUserRole().name();

    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String newToken(Authentication authentication) throws JsonProcessingException {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(
            objectMapper.writeValueAsString(
                new JwtPayLoad(
                    userPrincipal.getId(), userPrincipal.getEmail(), userPrincipal.getUserRole())))
        .setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + jwtExpirationInMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Pair<UserRole, Long> getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    String subject = claims.getSubject();
    String[] split = subject.split(String.valueOf(delimiter));
    UserRole userRole = UserRole.valueOf(split[1]);
    return Pair.of(userRole, Long.parseLong(split[0]));
  }

  public JwtPayLoad extractJwtPayLoad(String jwt) throws IOException {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
    return objectMapper.readValue(claims.getSubject(), JwtPayLoad.class);
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
      //    } catch (SignatureException ex) {
      //      logger.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return false;
  }
}
