
package grupog.agendamlg.general;

import javax.faces.context.FacesContext;

/**
* Redirect.java
*
* May 13, 2017
* @author Jean Paul Beaudry
*/
public class Redirect {
    public static void redirectTo(String page){
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, page);
    }
}
