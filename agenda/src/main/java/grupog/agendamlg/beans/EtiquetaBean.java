package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
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
    private List<String> etiquetaDelete;        
    @EJB
    private Business business;

    public EtiquetaBean() {
    }

    public List<Etiqueta> getEtiquetas() {
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
            return new ArrayList<Etiqueta>();
        }
    }

    public List<Etiqueta> getEtiquetasCabecera() {
        List<Etiqueta> e = getEtiquetas();
        if (!e.isEmpty()) {
            if (e.size() > 9) {
                e.remove(0);
                return e.subList(0, 9);
            }
            else
                return e;
        } else {
            return new ArrayList<Etiqueta>();
        }
        return new ArrayList<Etiqueta>();
    }

    public String createTag(){
        System.out.println("Estoy intentando crear Tags");
        System.out.println("etiqueta " + etiquetaCreate);
        Etiqueta e = new Etiqueta();
        e.setNombre(etiquetaCreate);
        business.createTag(e);
        return "index?faces-redirect=true";
    }

    public String updateTag(){
        System.out.println("Estoy intentando update Tags");
        List<Etiqueta> search = business.getTags();
        for (Etiqueta e : search) {
            if (e.getNombre() == etiquetaUpdate) {

            }
        }

        Etiqueta e = new Etiqueta();
        e.setNombre(etiquetaUpdate);
        business.updateTag(e);
        return "index?faces-redirect=true";
    }

    public String deleteTag(){
        System.out.println("Estoy intentando delete Tags");

        for (String str : etiquetaDelete) {
            business.deleteTag(business.getEtiquetaByName(str));

        }
        return "index?faces-redirect=true";
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
