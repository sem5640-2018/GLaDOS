package rest.helpers;

import oauth.GatekeeperLogin;
import oauth.gatekeeper.GatekeeperInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Stateless
public class Authorization {

    private static final String AUTH_HEADER = "Authorization".toLowerCase();
    private static final String BEARER_STRING = "Bearer";

    private static final Logger log = LogManager.getLogger(Authorization.class);

    @EJB
    private GatekeeperLogin authBean;

    private String bearerToken;
    private GatekeeperInfo userInfo;


    public AuthStates verifyAccessToken(HttpServletRequest request){
        // Get the access token internally
        AuthStates tokenState = getAccessToken(request);
        if (tokenState != AuthStates.ValidToken){
            return tokenState;
        }

        // Validate the current token
        if (!authBean.validateAccessToken(bearerToken)){
            return AuthStates.Unauthorized;
        }

        userInfo = authBean.getUserInfo(bearerToken);
        return AuthStates.Authorized;
    }

    public GatekeeperInfo getUserInfo() {
        return userInfo;
    }

    private AuthStates getAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();

        String headerName = null;
        while(headers.hasMoreElements())
        {
            headerName = headers.nextElement();
            if (headerName.toLowerCase().equals(AUTH_HEADER)){
                break;
            }
        }

        if (headerName == null || headerName.isEmpty()){
            log.info("No header was passed in the REST request");
            return AuthStates.NoHeader;
        }

        String tokenValue = request.getHeader(headerName);
        if (tokenValue.isEmpty()){
            log.info("No token value was passed in the REST request");
            return AuthStates.NoToken;
        }

        if (!tokenValue.contains(BEARER_STRING)){
            log.warn("No bearer string was found in Auth REST request");
            return AuthStates.InvalidToken;
        }

        // Strip chars off around token
        tokenValue = tokenValue.replace(BEARER_STRING, "");
        tokenValue = tokenValue.replace(" ", "");

        // Validate there is still a token
        if(tokenValue.isEmpty()){
            log.warn("An empty bearer token was passed in REST request");
            return AuthStates.InvalidToken;
        }

        log.debug("Successfully found token in REST request");

        bearerToken = tokenValue;

        return AuthStates.ValidToken;
    }
}
