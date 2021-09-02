package org.richardinnocent.polysight.auth.server.services.user.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import org.richardinnocent.polysight.auth.server.Qualifiers;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.security.JwtFields;
import org.richardinnocent.polysight.auth.server.security.PublicPrivateKeyProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier(Qualifiers.JWT)
public class JwtBuilder implements TokenBuilder {

  private static final long EXPIRATION_TIME_MILLIS = 864_000_000L;

  private final PublicPrivateKeyProvider keyProvider;

  public JwtBuilder(PublicPrivateKeyProvider keyProvider) {
    this.keyProvider = keyProvider;
  }

  public Token buildTokenForUser(PolysightUser user) {
    String token = JWT
        .create()
        .withIssuer(JwtFields.ISSUER)
        .withClaim(JwtFields.USER_ID_CLAIM_KEY, user.getId())
        .withClaim(JwtFields.EMAIL_CLAIM_KEY, user.getEmail())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
        .sign(
            Algorithm.ECDSA512(
                (ECPublicKey) keyProvider.getPublicKey(),
                (ECPrivateKey) keyProvider.getPrivateKey()
            )
        );

    return new Token(token, (int) (EXPIRATION_TIME_MILLIS / 1000L));
  }

}
