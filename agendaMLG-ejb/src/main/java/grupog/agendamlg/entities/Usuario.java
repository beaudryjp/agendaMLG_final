
package grupog.agendamlg.entities;

import com.google.common.collect.ComparisonChain;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
* Usuario.java
*
* Mar 31, 2017
* @author Jean Paul Beaudry
*/
@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"email","pseudonimo","sal"}))
@NamedQueries({
    @NamedQuery(name="checkEmail", query="SELECT u from Usuario u WHERE u.email = :uemail"),
    @NamedQuery(name="checkUsername", query="SELECT u from Usuario u WHERE u.pseudonimo = :upseudonimo"),
    @NamedQuery(name="getAllUsers", query="SELECT u from Usuario u"),
    @NamedQuery(name="getUser", query="SELECT u from Usuario u WHERE u.id_usuario = :id_usuario"),
    @NamedQuery(name="getRedactores", query="SELECT u from Usuario u WHERE u.rol = :rol"),
})
public class Usuario implements Serializable, Comparable {
    
    public enum Tipo_Rol {
        REGISTRADO,
        VALIDADO,
        REDACTOR;
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_usuario;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=false)
    private String apellidos;
    @Column(nullable=false)
    private String pseudonimo;

    @Column(nullable=false)
    private String email;
    @Column(nullable=false)  
    private Boolean email_notifier;
    @Column(nullable=false)
    private String password_hash;
    @Column(nullable=false)
    private String sal;
    @Enumerated(EnumType.STRING)
    private Tipo_Rol rol;
    
    @OneToMany(mappedBy="usuario")
    private List<Comentario> comentarios;
    
    @OneToMany(orphanRemoval = true, mappedBy="usuario")
    private List <Notificacion> notificaciones;
    
    @ManyToMany
    @JoinTable(name="jn_megusta_id",joinColumns=@JoinColumn(name="id_usuario"),inverseJoinColumns=@JoinColumn(name="id_evento"))
    private List<Evento> megusta;
    @ManyToMany
    @JoinTable(name="jn_sigue_id",joinColumns=@JoinColumn(name="id_usuario"),inverseJoinColumns=@JoinColumn(name="id_evento"))
    private List<Evento> sigue;
    @ManyToMany
    @JoinTable(name="jn_asiste_id",joinColumns=@JoinColumn(name="id_usuario"),inverseJoinColumns=@JoinColumn(name="id_evento"))
    private List<Evento> asiste;
    
    @ManyToMany
    @JoinTable(name = "jn_tareas_id", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_tarea"))
    private List<Tarea> tareas;
    
    @OneToMany(mappedBy="creador_peticion",orphanRemoval=true)
    private List<Tarea> peticion;
    
    @OneToMany
    @JoinColumn(name="propietario")
    private List<Evento> misEventos;
    
    public Usuario(){
    }
    
    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPseudonimo() {
        return pseudonimo;
    }

    public void setPseudonimo(String pseudonimo) {
        this.pseudonimo = pseudonimo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmail_notifier() {
        return email_notifier;
    }

    public void setEmail_notifier(boolean email_notifier) {
        this.email_notifier = email_notifier;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public List<Evento> getMegusta() {
        return megusta;
    }

    public void setMegusta(List<Evento> megusta) {
        this.megusta = megusta;
    }

    public List<Evento> getSigue() {
        return sigue;
    }

    public void setSigue(List<Evento> sigue) {
        this.sigue = sigue;
    }

    public List<Evento> getAsiste() {
        return asiste;
    }

    public void setAsiste(List<Evento> asiste) {
        this.asiste = asiste;
    }
    
    public Tipo_Rol getRol_usuario() {
        return rol;
    }

    public void setRol_usuario(Tipo_Rol rol) {
        this.rol = rol;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public List<Tarea> getPeticion() {
        return peticion;
    }

    public void setPeticion(List<Tarea> peticion) {
        this.peticion = peticion;
    }

    public List<Evento> getMisEventos() {
        return misEventos;
    }

    public void setMisEventos(List<Evento> misEventos) {
        this.misEventos = misEventos;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id_usuario);
        hash = 37 * hash + Objects.hashCode(this.pseudonimo);
        hash = 37 * hash + Objects.hashCode(this.email);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.pseudonimo, other.pseudonimo)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id_usuario, other.id_usuario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id_usuario=" + id_usuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", pseudonimo=" + pseudonimo + ", email=" + email + ", email_notifier=" + email_notifier + ", password_hash=" + password_hash + ", sal=" + sal + ", rol=" + rol + ", comentarios=" + comentarios + ", notificaciones=" + notificaciones + ", megusta=" + megusta + ", sigue=" + sigue + ", asiste=" + asiste + '}';
    }

    

    @Override
    public int compareTo(Object o) {
        Usuario us = (Usuario)o;
        return ComparisonChain.start()
                .compare(this.getNombre(), us.getNombre())
                .compare(this.getApellidos(), us.getApellidos())
                .compare(this.getEmail(), us.getEmail())
                .result(); 
    }
}
