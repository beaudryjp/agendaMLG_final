
package grupog.agendamlg.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
* Provincia.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"nombre"}))
@NamedQueries({
    @NamedQuery(name="getAllProvinces", query="SELECT p from Provincia p ORDER BY p.nombre ASC"),
    @NamedQuery(name = "getProvinciaByName", query = "SELECT p from Provincia p where p.nombre = :nombre"),
})
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_provincia;
    @Column(nullable=false)
    private String nombre;
    @OneToMany(orphanRemoval=true, mappedBy="provincia")
    private List<Localidad> localidades;

    public Long getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(Long id) {
        this.id_provincia = id;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id_provincia);
        hash = 71 * hash + Objects.hashCode(this.nombre);
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
        final Provincia other = (Provincia) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.id_provincia, other.id_provincia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Provincia{" + "id_provincia=" + id_provincia + ", nombre=" + nombre + ", localidades=" + localidades + '}';
    }

}
