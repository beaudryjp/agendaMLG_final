package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Destinatario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@Dependent
public class DestinatarioBean implements Serializable {

    /**
     * Creates a new instance of PublicoBean
     */
    private List<Destinatario> destinatarios;
    private String destinatario;

    public DestinatarioBean() {
    }

    @PostConstruct
    public void init() {
        destinatario = "Todos";
        destinatarios = new ArrayList<>();
        destinatarios.add(new Destinatario("Ancianos"));
        destinatarios.add(new Destinatario("Niños"));
        destinatarios.add(new Destinatario("Todos"));
        destinatarios.add(new Destinatario("Adultos"));
        destinatarios.add(new Destinatario("Jóvenes"));
    }

    public List<Destinatario> getDestinatarios() {
        Collections.sort(destinatarios, new Comparator<Destinatario>() {
            @Override
            public int compare(Destinatario o1, Destinatario o2) {
                return o1.getDescripcion().compareTo(o2.getDescripcion());
            }
        });
        return destinatarios;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
}
