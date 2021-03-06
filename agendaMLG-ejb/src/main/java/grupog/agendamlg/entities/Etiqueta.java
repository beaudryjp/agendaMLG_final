
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
* Etiqueta.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"nombre"}))
@NamedQueries({
    @NamedQuery(name="getAllTags", query="SELECT t from Etiqueta t"),
    @NamedQuery(name="getTagById", query="SELECT t from Etiqueta t WHERE t.id_etiqueta = :etiqueta"),
    @NamedQuery(name="getTagByName", query="SELECT t from Etiqueta t WHERE t.nombre = :nombre"),
    @NamedQuery(name="getAllTagsByEventId", query="SELECT t from Etiqueta t inner join t.evento e WHERE e.id_evento = :evento"),
})
public class Etiqueta implements Serializable, Comparable<Etiqueta>  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_etiqueta;
    @Column(nullable=false)
    private String nombre;
    @ManyToMany(mappedBy="etiqueta")
    private List<Evento> evento;
    
    public List<Evento> getEvento() {
        return evento;
    }

    public void setEvento(List<Evento> evento) {
        this.evento = evento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_etiqueta() {
        return id_etiqueta;
    }

    public void setId_etiqueta(Long id_etiqueta) {
        this.id_etiqueta = id_etiqueta;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id_etiqueta);
        hash = 97 * hash + Objects.hashCode(this.nombre);
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
        final Etiqueta other = (Etiqueta) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.id_etiqueta, other.id_etiqueta)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Etiqueta{" + "id_etiqueta=" + id_etiqueta + ", nombre=" + nombre + '}';
    }

    @Override
    public int compareTo(Etiqueta other) {
        return this.getNombre().compareTo(other.getNombre());
    }
   
}
