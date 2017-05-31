package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Provincia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@ViewScoped
public class ProvinciaBean implements Serializable {

    /**
     * Creates a new instance of ProvinciaBean
     */
    private List<Provincia> provincias;
    private List<Localidad> localidades;
    private String provincia;
    private String localidad;
    private Localidad loca;
    @EJB
    private Business business;

    public ProvinciaBean() {
    }

    @PostConstruct
    public void init() {
        List<Provincia> p = business.getProvinces();
        if (!p.isEmpty()) {
            provincia = p.get(6).getNombre();
            localidad = "Málaga";
            localidades = provinciaGetLocalidades(provincia);
        }

    }

    public List<Provincia> getProvincias() {
        provincias = business.getProvinces();
        return provincias;
    }

    public void onCambioProvincia() {
        localidad = provincia;
        if (provincia != null && !provincia.equals("")) {
           
            localidades = provinciaGetLocalidades(provincia);
            
        } else {
            localidades = new ArrayList<>();
            
        }
    }

    private List<Localidad> provinciaGetLocalidades(String provincia) {

        localidades = business.getTowns(provincia);

        Collections.sort(localidades, new Comparator<Localidad>() {
            @Override
            public int compare(Localidad o1, Localidad o2) {
                return o1.getNombre().compareTo(o2.getNombre());
            }
        });
        return localidades;
    }

    public String getProvincia() {

        provincias = business.getProvinces();
        Collections.sort(provincias, new Comparator<Provincia>() {
            @Override
            public int compare(Provincia o1, Provincia o2) {
                return o1.getNombre().compareTo(o2.getNombre());
            }
        });
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public List<Localidad> getLocalidades() {
        return business.getTowns(provincia);
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    public Localidad getLoca() {
        return loca;
    }

    public void setLoca(Localidad loca) {
        this.loca = loca;
    }

    
}
