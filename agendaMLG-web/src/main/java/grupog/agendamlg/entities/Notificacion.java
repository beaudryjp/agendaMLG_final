
package grupog.agendamlg.entities;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
* Notificacion.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_notificacion;
    private String mensaje;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Time hora;
    @ManyToOne(optional=true)
    private Usuario usuario;
    @ManyToOne(optional=true)
    private Evento evento;
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public Long getId_notificacion() {
        return id_notificacion;
    }

    public void setId_notificacion(Long id_notificacion) {
        this.id_notificacion = id_notificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_notificacion != null ? id_notificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.id_notificacion == null && other.id_notificacion != null) || (this.id_notificacion != null && !this.id_notificacion.equals(other.id_notificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Notificacion{" + "id_notificacion=" + id_notificacion + ", mensaje=" + mensaje + ", fecha=" + fecha + ", hora=" + hora + ", user=" + usuario + ", evento=" + evento + '}';
    }

    

}
