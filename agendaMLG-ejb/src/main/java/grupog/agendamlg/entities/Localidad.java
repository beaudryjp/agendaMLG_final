
package grupog.agendamlg.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
* Localidad.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"nombre"}))
@NamedQueries({
    @NamedQuery(name = "getLocalidadByName", query = "Select l from Localidad l where l.nombre = :nombre")
})
public class Localidad implements Serializable, Comparable<Localidad> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_localidad;
    @Column(nullable=false)
    private String nombre;
    @ManyToOne
    private Provincia provincia;
    @OneToMany(mappedBy="localidad")
    private List<Evento> evento;
    
    public List<Evento> getEvento() {
        return evento;
    }

    public void setEvento(List<Evento> evento) {
        this.evento = evento;
    }
    
    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(Long id_localidad) {
        this.id_localidad = id_localidad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id_localidad);
        hash = 59 * hash + Objects.hashCode(this.nombre);
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
        final Localidad other = (Localidad) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.id_localidad, other.id_localidad)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Localidad{" + "id_localidad=" + id_localidad + ", nombre=" + nombre + ", provincia=" + provincia + ", evento=" + evento + '}';
    }

    @Override
    public int compareTo(Localidad o) {
        return this.getNombre().compareTo(o.getNombre());
    }
    

}
