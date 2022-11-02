package ru.msugrobov.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import ru.msugrobov.entities.Player;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import static ru.msugrobov.repositories.PropertiesReader.tokenValidityDuration;

/**
 * Implements methods for create and parse JWT
 */
public class JwtManager {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final SecretKey SECRET_KEY = MacProvider.generateKey(SIGNATURE_ALGORITHM);
    private static final TemporalAmount TOKEN_VALIDITY = Duration
            .ofHours(Long.parseLong((tokenValidityDuration)));

    /**
     * Builds the JWT
     *
     * @param player entity to create JWT
     * @return compactToken
     */
    public String createToken(Player player) {
        final Instant now = Instant.now();
        final Date expiryDate = Date.from(now.plus(TOKEN_VALIDITY));
        return Jwts.builder()
                .setSubject("Authorization")
                .claim("login", player.getLogin())
                .claim("role", player.getRole())
                .setExpiration(expiryDate)
                .setIssuedAt(Date.from(now))
                .signWith(SIGNATURE_ALGORITHM, SECRET_KEY)
                .compact();
    }

    /**
     * Parses the given JWT
     *
     * @param compactToken JWT to parse and verify
     * @return claims of the token, if no exceptions were thrown - token can be trusted
     */
    public Jws<Claims> parseToken(String compactToken )
            throws ExpiredJwtException,
                   UnsupportedJwtException,
                   MalformedJwtException,
                   SignatureException,
                   IllegalArgumentException {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(compactToken);
    }
}
