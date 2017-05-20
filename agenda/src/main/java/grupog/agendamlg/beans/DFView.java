package grupog.agendamlg.beans;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Jean Paul Beaudry
 */
@Named(value = "dFView")
@Dependent
public class DFView {

    /**
     * Creates a new instance of DFView
     */
    public DFView() {
    }
    
    public void showMessage() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Borrar evento", "Se ha borrado el evento");
         
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
}
