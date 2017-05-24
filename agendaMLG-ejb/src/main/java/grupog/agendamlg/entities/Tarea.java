
package grupog.agendamlg.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


/**
* Notificacion.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity

public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_tarea;
    private String nombre;
    private String mensaje;
    private boolean aceptado;
    private LocalDateTime fecha_hora;
    @ManyToMany
    private List <Usuario> redactores;
    @ManyToOne(optional=true)
    @JoinTable(name = "jn_tarea_peticion_id", joinColumns = @JoinColumn(name = "id_tarea"), inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    private Usuario creador_peticion;

    public Long getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(Long id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public List<Usuario> getRedactores() {
        return redactores;
    }

    public void setRedactores(List <Usuario> redactores) {
        this.redactores = redactores;
    }

    public Usuario getCreador_peticion() {
        return creador_peticion;
    }

    public void setCreador_peticion(Usuario creador_peticion) {
        this.creador_peticion = creador_peticion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id_tarea);
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
        final Tarea other = (Tarea) obj;
        if (!Objects.equals(this.id_tarea, other.id_tarea)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tarea{" + "id_tarea=" + id_tarea + ", nombre=" + nombre + ", mensaje=" + mensaje + ", aceptado=" + aceptado + ", fecha_hora=" + fecha_hora + ", dador=" + redactores + ", beneficiario=" + creador_peticion + '}';
    }
    
    
}
