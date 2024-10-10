package com.dev5ops.healthtart.common.util;


import com.dev5ops.healthtart.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // JWT 토큰으로부터 사용자 ID 추출 -> userService 통해 사용자 정보 로드
    private UserService userService;
    // JWT 토큰 서명 및 검증에 사용할 비밀 키
    // @Value 어노테이션 통해 환경설정에서 읽어온 secretKey 사용하여 생성
    private final Key key;

    // JWT 토큰 서명에 사용할 key 생성
    public JwtUtil(
            @Value("${token.secret}") String secretKey
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 43200000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    /* 설명. Token 검증(Bearer 토큰이 넘어왔고, 우리 사이트의 secret key로 만들어 졌는가, 만료되었는지와 내용이 비어있진 않은지) */
    // JWT 토큰의 유효성 검사
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        } catch (ExpiredJwtException e) {
        } catch (UnsupportedJwtException e) {
        } catch (IllegalArgumentException e) {
        }
        return true;
    }

    /* 설명. 넘어온 AccessToken으로 인증 객체 추출 */
    public Authentication getAuthentication(String token) {

        /* 설명. 토큰을 들고 왔던 들고 오지 않았던(로그인 시) 동일하게 security가 관리 할 UserDetails 타입을 정의 */
        UserDetails userDetails = userService.findUserByUsername(this.getUserId(token));

        /* 설명. 토큰에서 claim들 추출 */
        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities = null;
        if(claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        } else {
            /* 설명. 클레임에서 권한 정보들 가져오기 */
            authorities =
                    Arrays.stream(claims.get("auth").toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(", "))
                            .map(role -> new SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /* 설명. Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /* 설명. Token에서 사용자의 id(subject 클레임) 추출 */
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }
}
