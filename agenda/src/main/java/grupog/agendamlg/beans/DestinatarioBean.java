package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Destinatario;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@SessionScoped
public class DestinatarioBean implements Serializable {

    private List<Destinatario> destinatarios;
    private String destinatario;
    @EJB
    private Business business;

    public DestinatarioBean() {
    }

    public List<Destinatario> getDestinatarios() {
        List<Destinatario> d = business.getAudiences();
        if (!d.isEmpty()) {
            Collections.sort(d, new Comparator<Destinatario>() {
                @Override
                public int compare(Destinatario o1, Destinatario o2) {
                    return o1.getDescripcion().compareTo(o2.getDescripcion());
                }
            });
            return d;
        } else {
            return null;
        }
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Destinatario getSpecificDestinatario() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            return destinatarios.get(Integer.parseInt(hsr.getParameter("id")));
        } catch (NumberFormatException n) {
            return null;
        }
    }
}
