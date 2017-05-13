package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Provincia;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@SessionScoped
public class LocalidadBean implements Serializable {

    /**
     * Creates a new instance of LocalidadBean
     */
    private List<Localidad> localidades;

    public LocalidadBean() {
    }
    
    @PostConstruct
    public void init() {
        localidades = new ArrayList<>();
        Provincia provincia = new Provincia();
        provincia.setNombre("Málaga");
        localidades.add(new Localidad("Benalmadena", provincia));
        localidades.add(new Localidad("Nerja", provincia));
        localidades.add(new Localidad("Rincon de la Victoria", provincia));
        localidades.add(new Localidad("Torremolinos", provincia));
//        Localidad localidad = new Localidad();
//        localidad.setNombre("Málaga");
//        localidad.setProvincia(provincia);
//        localidades.add(localidad);
//        localidad.setNombre("Benalmádena");
//        localidad.setProvincia(provincia);
//        localidades.add(localidad);
//        localidad.setNombre("Alhaurín de la Torre");
//        localidad.setProvincia(provincia);
//        localidades.add(localidad);
//        localidad.setNombre("Rincón de la Victoria");
//        localidad.setProvincia(provincia);
//        localidades.add(localidad);
//        localidad.setNombre("Nerja");
//        localidad.setProvincia(provincia);
//        localidades.add(localidad);
    }
    
    public List<Localidad> getLocalidades(){
        Collections.sort(localidades, new Comparator<Localidad>() {
            @Override
            public int compare(Localidad o1, Localidad o2) {
                return o1.getNombre().compareTo(o2.getNombre());
            }
          });
        return localidades;
    }
}
