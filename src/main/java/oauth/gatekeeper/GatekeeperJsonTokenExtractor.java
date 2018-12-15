package oauth.gatekeeper;

import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import configuration.EnvironmentVariables;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public class GatekeeperJsonTokenExtractor extends OAuth2AccessTokenJsonExtractor {

    private static final Pattern ACCESS_TOKEN_REGEX_PATTERN = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");

    protected GatekeeperJsonTokenExtractor() {
    }

    public static GatekeeperJsonTokenExtractor instance() {
        return InstanceHolder.INSTANCE;
    }

    public JWTClaimsSet getJWTClaimSet(String accessToken) throws MalformedURLException, ParseException, JOSEException, BadJOSEException {
        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        JWKSource keySrc = new RemoteJWKSet( new URL(EnvironmentVariables.getGatekeeperJWKUrl()));
        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
        JWSKeySelector keySelector = new JWSVerificationKeySelector(jwsAlgorithm, keySrc);
        jwtProcessor.setJWSKeySelector(keySelector);
        SecurityContext ctx = null;
        return jwtProcessor.process(accessToken, ctx);
    }

    @Override
    protected GatekeeperOAuth2AccessToken createToken(String accessToken, String tokenType, Integer expiresIn,
                                                  String refreshToken, String scope, String response) {
        String open_id_json = extractParameter(response, ACCESS_TOKEN_REGEX_PATTERN, false);
        JWTClaimsSet claimsSet;
        try {
            claimsSet = getJWTClaimSet(open_id_json);
            String subject = claimsSet.getSubject();
            Date startTime = claimsSet.getNotBeforeTime(), expiryTime = claimsSet.getExpirationTime();

            return new GatekeeperOAuth2AccessToken(accessToken, tokenType, expiresIn, refreshToken, scope,
                    subject, response, startTime, expiryTime);
        } catch (Exception e) {
            System.err.println("[GatekeeperJsonTokenExtractor.createToken]: " + e.toString());
            return null;
        }
    }

    private static class InstanceHolder {

        private static final GatekeeperJsonTokenExtractor INSTANCE = new GatekeeperJsonTokenExtractor();
    }
}
