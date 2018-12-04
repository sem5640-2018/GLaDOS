package oauth;

import com.github.scribejava.core.builder.api.DefaultApi20;
import configuration.EnvironmentVariables;
import oauth.gatekeeper.GatekeeperJsonTokenExtractor;

/**
 * Implements an concrete API for accessing Gatekeeper
 */
public class GatekeeperApi extends DefaultApi20 {

        public GatekeeperApi() {}

        @Override
        public GatekeeperJsonTokenExtractor getAccessTokenExtractor() {
            return GatekeeperJsonTokenExtractor.instance();
        }

        private static class InstanceHolder {
            private static final GatekeeperApi INSTANCE = new GatekeeperApi();
        }

        public static GatekeeperApi instance() {
            return InstanceHolder.INSTANCE;
        }

        @Override
        public String getAccessTokenEndpoint() {
            return EnvironmentVariables.getGatekeeperTokenUrl();
        }

        @Override
        public String getRevokeTokenEndpoint() {
            return EnvironmentVariables.getGatekeeperRevokeUrl();
        }

        @Override
        protected String getAuthorizationBaseUrl() {
            return EnvironmentVariables.getGatekeeperAuthoriseUrl();
        }

}
