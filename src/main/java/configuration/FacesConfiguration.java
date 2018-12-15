package configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;

/**
 * Activates Java Facelets 2.3
 */
@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@ApplicationScoped
public class FacesConfiguration {
}
