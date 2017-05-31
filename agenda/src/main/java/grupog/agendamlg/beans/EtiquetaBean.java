package grupog.agendamlg.beans;

import grupog.agendamlg.business.BusinessOther;
import grupog.agendamlg.entities.Etiqueta;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    private List<String> etiquetas;

    private String etiquetaCreate;
    private String etiquetaUpdate;
    private String etiquetaAnt;
    private List<String> etiquetaDelete;
    @EJB
    private BusinessOther business;

    public EtiquetaBean() {
    }

    public List<Etiqueta> getEtiquetas() {
        //List<Etiqueta> e = business.getTagsWithEvents();
        List<Etiqueta> e = business.getTags();
        if (!e.isEmpty()) {
            Collections.sort(e, new Comparator<Etiqueta>() {
                @Override
                public int compare(Etiqueta o1, Etiqueta o2) {
                    return o1.getNombre().compareTo(o2.getNombre());
                }
            });
            return e;
        } else {
            return new ArrayList<>();
        }
    }

    public List<Etiqueta> getEtiquetasCabecera() {
        List<Etiqueta> e = business.getTagsWithEvents();
        if (!e.isEmpty()) { //get tags who have at least one event
            if (e.size() > 9) {
                return e.subList(0, 9);
            } else {
                return e;
            }
        } else { //get all tags
            e = business.getTags();
            if (!e.isEmpty()) {
                if (e.size() > 9) {
                    return e.subList(0, 9);
                } else {
                    return e;
                }
            } else {
                return new ArrayList<>();
            }
        }
    }

    public String createTag() {
        Etiqueta e = new Etiqueta();
        e.setNombre(etiquetaCreate);
        business.createTag(e);
        return "index?faces-redirect=true";
    }

    public String updateTag() {
        Etiqueta a = business.getEtiquetaByName(etiquetaAnt);
        a.setNombre(etiquetaUpdate);
        business.updateTag(a);
        return "index?faces-redirect=true";
    }

    public String deleteTag() {
        for (String str : etiquetaDelete) {
            business.deleteTag(business.getEtiquetaByName(str).getId_etiqueta());

        }
        return "index?faces-redirect=true";
    }

    public String getEtiquetaAnt() {
        return etiquetaAnt;
    }

    public void setEtiquetaAnt(String etiquetaAnt) {
        this.etiquetaAnt = etiquetaAnt;
    }

    public String getEtiquetaCreate() {
        return etiquetaCreate;
    }

    public void setEtiquetaCreate(String etiquetaCreate) {
        this.etiquetaCreate = etiquetaCreate;
    }

    public String getEtiquetaUpdate() {
        return etiquetaUpdate;
    }

    public void setEtiquetaUpdate(String etiquetaUpdate) {
        this.etiquetaUpdate = etiquetaUpdate;
    }

    public List<String> getEtiquetaDelete() {
        return etiquetaDelete;
    }

    public void setEtiquetaDelete(List<String> etiquetaDelete) {
        this.etiquetaDelete = etiquetaDelete;
    }

}
