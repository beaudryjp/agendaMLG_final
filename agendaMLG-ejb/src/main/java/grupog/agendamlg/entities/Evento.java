package grupog.agendamlg.entities;

import com.google.common.collect.ComparisonChain;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Evento.java
 *
 * Mar 31, 2017
 *
 * @author Jean Paul Beaudry
 */
@Entity
@NamedQueries({
    @NamedQuery(name="getAllEvents", query="SELECT e from Evento e WHERE e.visible = true"),
    @NamedQuery(name="getEventById", query="SELECT e from Evento e WHERE e.id_evento = :evento and e.visible = true"),
    @NamedQuery(name="getEventsImportant", query="SELECT e from Evento e WHERE e.destacado = true and e.visible = true"),
    @NamedQuery(name="getEventsByDate", query="SELECT e from Evento e WHERE e.fecha_inicio = :fecha ORDER BY e.fecha_inicio ASC"),
    @NamedQuery(name="getEventsNearestByDate", query="SELECT e from Evento e WHERE e.fecha_inicio >= current_date() and e.visible = true ORDER BY e.fecha_inicio ASC"),
    @NamedQuery(name="getEventsBySearch", query="SELECT e from Evento e inner join e.etiqueta et inner join e.destinatario d "
            + "WHERE e.localidad.nombre = :localidad and e.visible = true and "
            + " et.nombre = :etiqueta and d.descripcion = :destinatario"),
    @NamedQuery(name="getUserAssists", query="select e from Evento e join fetch e.asiste a where a.id_usuario = :id"),
    @NamedQuery(name="getUserLikes", query="select e from Evento e join fetch e.megusta a where a.id_usuario = :id"),
    @NamedQuery(name="getUserFollows", query="select e from Evento e join fetch e.sigue a where a.id_usuario = :id"),
    @NamedQuery(name="getEventsByTag", query="SELECT e from Evento e INNER JOIN e.etiqueta et WHERE et.nombre = :nombre"),
    @NamedQuery(name="getEventsByAudience", query="SELECT e from Evento e INNER JOIN e.destinatario de WHERE de.descripcion = :descripcion"),
    @NamedQuery(name="getUserEvents", query="SELECT e from Evento e inner join e.propietario u WHERE u.id_usuario = :usuario"),
})

public class Evento implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_evento;
    @Column(nullable = false)
    private String titulo;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha_inicio;
    @Temporal(TemporalType.DATE)
    private Date fecha_fin;
    @Column(nullable = false)
    private String horario;
    @Column(nullable = false)
    private String precio;
    @Column(nullable = false)
    private Double longitud;
    @Column(nullable = false)
    private Double latitud;
    @Column(nullable = false)
    private Boolean destacado;
    @Column(nullable = false)
    private String imagen_url;
    @Column(nullable = false)
    private String imagen_titulo;
    private Integer valoracion;
    @Column (nullable = false)
    private Boolean visible;

    @OneToMany(mappedBy="evento")
    private List<Comentario> comentarios;
    
    @ManyToMany
    private List<Destinatario> destinatario;
    
    @ManyToMany
    private List<Etiqueta> etiqueta;
    
    @ManyToOne
    private Localidad localidad;
    
    @OneToMany(mappedBy="evento")
    private List<Notificacion> notificaciones;
    
    @ManyToMany(mappedBy = "megusta")
    private List<Usuario> megusta;
    @ManyToMany(mappedBy = "sigue")
    private List<Usuario> sigue;
    @ManyToMany(mappedBy = "asiste")
    private List<Usuario> asiste;
    
    @ManyToOne
    @JoinColumn(name="propietario", insertable=false, updatable=false)
    private Usuario propietario;

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public String getImagen_titulo() {
        return imagen_titulo;
    }

    public void setImagen_titulo(String imagen_titulo) {
        this.imagen_titulo = imagen_titulo;
    }

    
    public List<Destinatario> getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(List<Destinatario> destinatario) {
        this.destinatario = destinatario;
    }
    
    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horarios) {
        this.horario = horarios;
    }

    public Long getId_evento() {
        return id_evento;
    }

    public void setId_evento(Long id_evento) {
        this.id_evento = id_evento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_inicio() {
        return this.fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return this.fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
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

    public List<Etiqueta> getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(List<Etiqueta> etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public List<Usuario> getMegusta() {
        return megusta;
    }

    public void setMegusta(List<Usuario> megusta) {
        this.megusta = megusta;
    }

    public List<Usuario> getSigue() {
        return sigue;
    }

    public void setSigue(List<Usuario> sigue) {
        this.sigue = sigue;
    }

    public List<Usuario> getAsiste() {
        return asiste;
    }

    public void setAsiste(List<Usuario> asiste) {
        this.asiste = asiste;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_evento != null ? id_evento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id_evento fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id_evento == null && other.id_evento != null) || (this.id_evento != null && !this.id_evento.equals(other.id_evento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Evento{" + "id_evento=" + id_evento + ", titulo=" + titulo + ", descripcion=" + descripcion + ", fecha_inicio=" + fecha_inicio + ", fecha_fin=" + fecha_fin + ", horario=" + horario + ", precio=" + precio + ", longitud=" + longitud + ", latitud=" + latitud + '}';
    }

    @Override
    public int compareTo(Object o) {
        Evento e2 = (Evento) o;
        return ComparisonChain.start()
                .compare(this.getTitulo(), e2.getTitulo())
                .compare(this.getLocalidad(), e2.getLocalidad())
                .compare(this.getFecha_inicio(), e2.getFecha_inicio())
                .compare(this.getFecha_fin(), e2.getFecha_fin())
                .result();
    }

}
