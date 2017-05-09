
package grupog.agendamlg.entities;

import com.google.common.collect.ComparisonChain;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
public class Usuario implements Serializable, Comparable {
    
    public enum Tipo_Rol {
        REGISTRADO,
        VALIDADO,
        REDACTOR;
    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_usuario;
    @Column(name="nombre", nullable=false)
    private String nombre;
    @Column(name="apellidos", nullable=false)
    private String apellidos;
    @Column(name="pseudonimo", nullable=false)
    private String pseudonimo;

    @Column(name="email", nullable=false)
    private String email;
    @Column(name="email_notifier", nullable=false)
    private boolean email_notifier;
    @Column(name="password_hash", nullable=false)
    private String password_hash;
    @Column(name="sal", nullable=false)
    private String sal;
    private double longitud;
    private double latitud; 
    @Enumerated(EnumType.STRING)
    private Tipo_Rol rol;
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<Comentario> comentarios;
    @OneToMany(cascade=CascadeType.ALL)
    private List <Notificacion> notificaciones;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="jn_megusta_id",joinColumns=@JoinColumn(name="id_usuario"),inverseJoinColumns=@JoinColumn(name="id_evento"))
    private List<Evento> megusta;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="jn_sigue_id",joinColumns=@JoinColumn(name="id_usuario"),inverseJoinColumns=@JoinColumn(name="id_evento"))
    private List<Evento> sigue;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="jn_asiste_id",joinColumns=@JoinColumn(name="id_usuario"),inverseJoinColumns=@JoinColumn(name="id_evento"))
    private List<Evento> asiste;

    public Usuario(String nombre, String apellidos, String email) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }
    
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

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
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
        return "Usuario{" + "id_usuario=" + id_usuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", pseudonimo=" + pseudonimo + ", email=" + email + ", email_notifier=" + email_notifier + ", password_hash=" + password_hash + ", sal=" + sal + ", longitud=" + longitud + ", latitud=" + latitud + ", comentarios=" + comentarios + ", notificaciones=" + notificaciones + ", megusta=" + megusta + ", sigue=" + sigue + ", asiste=" + asiste + ", rol=" + rol + '}';
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
