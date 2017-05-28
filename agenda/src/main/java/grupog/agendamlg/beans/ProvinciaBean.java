package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Provincia;
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
public class ProvinciaBean implements Serializable {

    /**
     * Creates a new instance of ProvinciaBean
     */
    private List<Provincia> provincias;
    private List<Localidad> localidades;
    private String provincia;
    private String localidad;
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

    /*
    public List<Localidad> getLocalidades() {
        Collections.sort(localidades, new Comparator<Localidad>() {
            @Override
            public int compare(Localidad o1, Localidad o2) {
                return o1.getNombre().compareTo(o2.getNombre());
            }
        });
        return localidades;
    }
     */

    public void onCambioProvincia() {
//        System.out.println("Provincia: " + provincia);
//        System.out.println("Localidad: " + localidad);
//        System.out.println();
        localidad = provincia;
        if (provincia != null && !provincia.equals("")) {
            //localidades = provincias.get(provincias.indexOf(provincia)).getLocalidades();
            localidades = provinciaGetLocalidades(provincia);
            // System.out.println("Lista llena de Localidades");
        } else {
            localidades = new ArrayList<>();
            // System.out.println("Lista Vacia de Localidades");
        }
    }

    private List<Localidad> provinciaGetLocalidades(String provincia) {
//        System.out.println("Provincia: " + provincia);
//        System.out.println();
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

}
