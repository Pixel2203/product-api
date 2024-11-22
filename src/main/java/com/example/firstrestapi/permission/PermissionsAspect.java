package com.example.firstrestapi.permission;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.firstrestapi.responses.EventResponse;
import com.example.firstrestapi.util.ErrorCode;
import com.mysql.cj.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionsAspect {

  @Autowired
  private HttpServletRequest httpServletRequest;

  private static final Logger log = LoggerFactory.getLogger(PermissionsAspect.class);
  @Around("@annotation(requiresPermission)")
  public EventResponse<?> hasPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
    if(Objects.isNull(httpServletRequest)){
      throw new SystemException("HttpRequest is null for RequirePermissions");
    }

    String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if(StringUtils.isNullOrEmpty(token)){
      return EventResponse.failed("You need to be logged in", ErrorCode.INVALID_REQUEST);
    }


    String requiredPermission = requiresPermission.requiredPermission();


    Algorithm algorithm = Algorithm.HMAC256("MYKEY");
    JWTVerifier verifier = JWT.require(algorithm).build();

    try {

      DecodedJWT jwt = verifier.verify(token);
      String[] permissions = jwt.getClaim("permissions").asArray(String.class);
      boolean hasPermission = Arrays.asList(permissions).contains(requiredPermission);

      if (!hasPermission) {
        return EventResponse.failed("Insufficient Permissions", ErrorCode.INSUFFICIENT_PERMISSIONS);
      }

    } catch (JWTVerificationException e) {
      log.warn("Token has been expired or could not be verified - {}", token);
      return EventResponse.failed("Token invalid", ErrorCode.INVALID_REQUEST);
    }
    return (EventResponse<?>) joinPoint.proceed();
  }
}
