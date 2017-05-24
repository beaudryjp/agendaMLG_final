
package grupog.agendamlg.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
* Destinatario.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity
@NamedQueries({
    @NamedQuery(name="getAllAudience", query="SELECT d from Destinatario d"),
    @NamedQuery(name="getAudienceById", query="SELECT d from Destinatario d WHERE d.id_destinatario = :destinatario"),
    @NamedQuery(name="getAudienceByDescription", query="SELECT d from Destinatario d WHERE d.descripcion = :descripcion")    
})
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"descripcion"}))
public class Destinatario implements Serializable, Comparable<Destinatario> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_destinatario;
    @Column(name="descripcion", nullable=false)
    private String descripcion;
    @ManyToMany(mappedBy="destinatario")
    private List<Evento> evento;
    
    public Destinatario(){
    }
    
    public Destinatario(String descripcion) {
        this.descripcion = descripcion;
    }
    

    public List<Evento> getEvento() {
        return evento;
    }

    public void setEvento(List<Evento> evento) {
        this.evento = evento;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId_destinatario() {
        return id_destinatario;
    }

    public void setId_destinatario(Long id_destinatario) {
        this.id_destinatario = id_destinatario;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id_destinatario);
        hash = 59 * hash + Objects.hashCode(this.descripcion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Destinatario other = (Destinatario) obj;
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.id_destinatario, other.id_destinatario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Destinatario{" + "id_destinatario=" + id_destinatario + ", descripcion=" + descripcion + ", evento=" + evento + '}';
    }

    @Override
    public int compareTo(Destinatario other) {
        return this.getDescripcion().compareTo(other.getDescripcion());
    }
}
