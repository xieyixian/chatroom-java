package top.team7.chatroom.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtUtils {
//    @Value("${jwt.secret}")
    private static String SECRET_KEY = "7732c3447cea57c5f360971ad026ff194e78f30dc579c55670f177bc4f50eff9";

    public static String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 12 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public static String validateTokenAndRetrieveSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Invalid JWT Token");
        }
    }
}
