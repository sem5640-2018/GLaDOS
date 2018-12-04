package oauth.gatekeeper;

import com.github.scribejava.core.model.OAuth2AccessToken;

import java.util.Date;

public class GatekeeperOAuth2AccessToken extends OAuth2AccessToken {

    private final String userId;
    private final Date start;
    private final Date expiry;

    public GatekeeperOAuth2AccessToken(String accessToken, String openIdToken, String rawResponse) {
        this(accessToken, null, null, null, null, openIdToken, rawResponse, null, null);
    }

    public GatekeeperOAuth2AccessToken(String accessToken, String tokenType, Integer expiresIn, String refreshToken,
                                       String scope, String userId, String rawResponse, Date start, Date expiry) {
        super(accessToken, tokenType, expiresIn, refreshToken, scope, rawResponse);
        this.userId = userId;
        this.start = start;
        this.expiry = expiry;
    }

    public String getUserId() {
        return userId;
    }

    public Date getStart() {
        return start;
    }

    public Date getExpiry() {
        return expiry;
    }
}
