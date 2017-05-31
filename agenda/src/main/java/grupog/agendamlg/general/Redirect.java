package grupog.agendamlg.general;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Redirect.java
 *
 * May 13, 2017
 *
 * @author Jean Paul Beaudry
 */
public class Redirect {

    public static void redirectTo(String option) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {
            ec.redirect("http://localhost:8080/agenda" + option);
        } catch (IOException ex) {
            Logger.getLogger(Redirect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void redirectToIndex() {
        redirectTo("/");
    }

    public static void redirectToEventInfo(Long eventId) {
        redirectTo("/event/show/" + eventId.toString());
    }

    public static void redirectToProfile() {
        redirectTo("/profile");
    }

    public static void redirectToLogin() {
        redirectTo("/profile/login");
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static void redirect2(String page){
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, page);
    }
}
