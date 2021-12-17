package cn.ruiheyun.athena.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JsonWebTokenUtils {

    public static final String CLAIM_KEY_SUBJECT = "sub";
    public static final String CLAIM_KEY_CREATED = "created";
    public static final String CLAIM_KEY_ROLES = "roles";
    public static final String CLAIM_KEY_IP = "ip";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpireTime())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date generateExpireTime() {
        return Date.from(LocalDateTime.now().plusSeconds(expiration).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Claims getClaimsForToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("Json Web Token格式验证失败: {}", token);
        }
        return claims;
    }

    public String getSubjectForToken(String token) {
        String subject = "";
        try {
            Claims claims = getClaimsForToken(token);
            subject = claims.getSubject();
        } catch (Exception e) {
            log.info("获取令牌主题失败: {}", token);
        }
        return subject;
    }

    public boolean verificationToken(String token, String ip, UserDetails userDetails) {
        Claims claims = getClaimsForToken(token);
        String username = claims.getSubject();
        String tokenIp = claims.get(CLAIM_KEY_IP, String.class);
        return username.equals(userDetails.getUsername()) && tokenIp.equals(ip) && !isExpire(token);
    }

    private boolean isExpire(String token) {
        Date expireTime = getExpireTimeForToken(token);
        return LocalDateTime.now().isAfter(expireTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    private Date getExpireTimeForToken(String token) {
        Claims claims = getClaimsForToken(token);
        return claims.getExpiration();
    }

    public String refresh(String token) {
        Claims claims = getClaimsForToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public String generateToken(UserDetails userDetails, String ip) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUBJECT, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_IP, ip);

        List<String> roleList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put(CLAIM_KEY_ROLES, roleList);
        return generateToken(claims);
    }

}
