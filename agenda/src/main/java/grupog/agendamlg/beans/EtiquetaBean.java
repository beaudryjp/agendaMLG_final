package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Etiqueta;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@SessionScoped
public class EtiquetaBean implements Serializable {

    /**
     * Creates a new instance of EtiquetaBean
     */
    private List<Etiqueta> etiquetas;
    private String etiqueta;
    @EJB
    private Business business;

    public EtiquetaBean() {
    }

    public List<Etiqueta> getEtiquetas() {
        etiquetas = business.getTags();
        Collections.sort(etiquetas, new Comparator<Etiqueta>() {
            @Override
            public int compare(Etiqueta o1, Etiqueta o2) {
                return o1.getNombre().compareTo(o2.getNombre());
            }
        });
        return etiquetas;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

}
