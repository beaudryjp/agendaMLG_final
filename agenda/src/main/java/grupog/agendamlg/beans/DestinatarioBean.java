package grupog.agendamlg.beans;

import grupog.agendamlg.business.BusinessOther;
import grupog.agendamlg.entities.Destinatario;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@SessionScoped
public class DestinatarioBean implements Serializable {

    private List<String> destinatarios;
    
    private String destinatario;
    private String destinatarionew;
    private List<String> destinatariodel;
    
    private String destinatarioup;
    
    @EJB
    private BusinessOther business;

    public List<String> getDestinatariodel() {
        return destinatariodel;
    }

    public void setDestinatariodel(List<String> destinatariodel) {
        this.destinatariodel = destinatariodel;
    }
    

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

    public String getDestinatarionew() {
        return destinatarionew;
    }

    public void setDestinatarionew(String destinatarionew) {
        this.destinatarionew = destinatarionew;
    }


    public String getDestinatarioup() {
        return destinatarioup;
    }

    public void setDestinatarioup(String destinatarioup) {
        this.destinatarioup = destinatarioup;
    }
    
     public String createAudience(){
        Destinatario d = new Destinatario();
        d.setDescripcion(destinatarionew);
        business.createAudience(d);
        return "index?faces-redirect=true";
    }
     
    public String updateAudience(){
        Destinatario a = business.getDestinatarioByDescripcion(destinatario);
        a.setDescripcion(destinatarioup);
        business.updateAudience(a);
        return "index?faces-redirect=true";
    }
   
    public String deleteAudience() { 
        for(String str : destinatariodel) {
            business.deleteAudience(business.getDestinatarioByDescripcion(str).getId_destinatario());
        }

    
        return "index?faces-redirect=true";
    }
}
