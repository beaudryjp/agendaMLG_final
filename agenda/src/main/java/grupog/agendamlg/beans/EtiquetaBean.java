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

    private String etiqueta;
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
        }
        else
            return new ArrayList<Etiqueta>();
    }

    
    public List<Etiqueta> getEtiquetasCabecera() {
        List<Etiqueta> e = getEtiquetas();
        if(!e.isEmpty()){
            e.remove(0);
            return e.subList(0, 9);
        }
        else
            return new ArrayList<Etiqueta>();
    }
  
    
    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String createTag() throws java.text.ParseException {
        System.out.println("Estoy intentando crear Tags");
        Etiqueta e = new Etiqueta();
        e.setNombre(etiqueta);
        business.createTag(e);
        return "index?faces-redirect=true";
    }
    
    public String updateTag() throws java.text.ParseException {
        System.out.println("Estoy intentando update Tags");
        List<Etiqueta> search = business.getTags();
        for(Etiqueta e:search){
            if(e.getNombre()==etiqueta){
                
            }
        }
        
        Etiqueta e = new Etiqueta();
        e.setNombre(etiqueta);
        business.updateTag(e);
        return "index?faces-redirect=true";
    }
   
    public String deleteTag() throws java.text.ParseException {
        System.out.println("Estoy intentando delete Tags");
        
         for(String str : etiquetas)
        {
            business.deleteTag(business.getEtiquetaByName(str));
 
        }
        return "index?faces-redirect=true";
    }
}