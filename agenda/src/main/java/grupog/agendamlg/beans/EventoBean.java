package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.business.Business;
import grupog.agendamlg.general.Sendmail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

/**
 *
 * @author Jean Paul Beaudry
 */

@ManagedBean
@SessionScoped
public class EventoBean implements Serializable {

    @EJB
    private Business business;
    @Inject
    private ControlLog current_user;
    private TagCloudModel model;
    private List<Evento> eventos;
    private List<Evento> evento_asiste;
    private List<Evento> evento_gusta;
    private List<Evento> evento_sigue;
    private List<Evento> evento_etiquetas;
    private List<Etiqueta> etiquetas;
    private List<Destinatario> destinatarios;
    private List<Destinatario> publico_evento;
    private List<Destinatario> publico_evento2;
    private List<Destinatario> publico_evento3;
    private List<Destinatario> publico_evento4;
    private List<Comentario> comentarios;
    private String searchProvincia;
    private String searchLocalidad;
    private String searchDestinatario;
    private String searchEtiqueta;
    private String searchText;
    private String tag;
    private String eventId;
    private LocalDate dateSelected;
    private String event_new_titulo;
    private String event_new_descripcion;
    private Date event_new_fecha_inicio;
    private Date event_new_fecha_fin;
    private String event_new_horario;
    private String event_new_precio;
    private double event_new_longitud;
    private double event_new_latitud;
    private boolean event_new_destacado;
    private String event_new_imagen_url;
    private String event_new_imagen_titulo;

    public EventoBean() {
    }

    public List<Evento> getEventosProximos() {
        eventos = business.getEvents();
        Collections.sort(eventos, new Comparator<Evento>() {
            @Override
            public int compare(Evento o1, Evento o2) {
                return o1.getFecha_inicio().compareTo(o2.getFecha_inicio());
            }
        });
        return eventos.subList(0, 2);
    }

    public List<Evento> getEventos() {
        eventos = business.getEvents();
        Collections.sort(eventos, new Comparator<Evento>() {
            @Override
            public int compare(Evento o1, Evento o2) {
                return o1.getFecha_inicio().compareTo(o2.getFecha_inicio());
            }
        });
        return eventos;
    }

    public List<Evento> getEventosDestacados() {
        return business.getEventsImportant();
    }

    public List<Evento> getEventosByFecha() {
        return business.getEventsByDate(dateSelected);
    }

    public List<Evento> getSearchEventos() {
        return business.getEventsBySearch(searchText, searchProvincia, searchLocalidad, searchEtiqueta, searchDestinatario);
    }

    public List<Evento> getSearchEventosEtiquetas() {
        List<Evento> s = new ArrayList<>();
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        return business.getEventsByTag(hsr.getParameter("tag"));
    }

    public Evento getEvento() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            return business.getEvent(Integer.parseInt(hsr.getParameter("id")));
        } catch (NumberFormatException n) {
            return null;
        }
    }

    public String getSearchProvincia() {
        return searchProvincia;
    }

    public void setSearchProvincia(String searchProvincia) {
        this.searchProvincia = searchProvincia;
    }

    public String getSearchLocalidad() {
        return searchLocalidad;
    }

    public void setSearchLocalidad(String searchLocalidad) {
        this.searchLocalidad = searchLocalidad;
    }

    public String getSearchDestinatario() {
        return searchDestinatario;
    }

    public void setSearchDestinatario(String searchDestinatario) {
        this.searchDestinatario = searchDestinatario;
    }

    public String getSearchEtiqueta() {
        return searchEtiqueta;
    }

    public void setSearchEtiqueta(String searchEtiqueta) {
        this.searchEtiqueta = searchEtiqueta;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagCloudModel getModel(String e) {
        Random r = new Random(0);
        TagCloudModel s = new DefaultTagCloudModel();
        for (Evento x : business.getEvents()) {
            if (x.getId_evento() == Integer.parseInt(e)) {
                for (Etiqueta et : x.getEtiqueta()) {
                    s.addTag(new DefaultTagCloudItem(et.getNombre(), "/event/tag/" + et.getNombre(), r.nextInt(4) + 1));
                }
            }
        }
        return s;
    }

    public void sendNotificationLike(ActionEvent e) {
        sendMailSocial("Has pinchado like en el evento");
    }

    public void sendNotificationAssist(ActionEvent e) {
        sendMailSocial("Has indicado que vas a asistir al evento");
    }

    public void sendNotificationFollow(ActionEvent e) {
        sendMailSocial("Has indicado que quieres seguir el evento");
    }

    private void sendMailSocial(String msg) {
        Evento ev = business.getEvent(Integer.parseInt(eventId));
        final Usuario u = current_user.getUsuario();
        final StringBuilder m = new StringBuilder();
        String full_url = "http://localhost:8080/agenda/event/show/" + this.eventId;
        m.append("<h2>Notificaci&oacute;n <span style='font-size: 13px'>(")
                .append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(")</span></h2><p>")
                .append(msg)
                .append(" <b>\"")
                .append(changeHtmlChars(ev.getTitulo()))
                .append("\"</b></p></p>Horario: ")
                .append(ev.getHorario())
                .append("<br>Precio: ")
                .append(changeHtmlChars(ev.getPrecio()))
                .append("<br>Puede ver m&aacute;s informaci&oacute;n en el siguiente <a href='").append(full_url).append("' target='_blank'>enlace</a>")
                .append("</p><p style='font-size: 12px'>diariosur</p>");
        if (u.isEmail_notifier()) {
            Sendmail.mailThread(u.getEmail(), "Notificación agendaMLG - diariosur", m.toString());
        }
        Notificacion n = new Notificacion();
        n.setEvento(ev);
        n.setUsuario(u);
        n.setMensaje(msg);
        business.setNotifications(n);
    }

    private String changeHtmlChars(String m) {
        m = m.replaceAll("á", "&aacute;");
        m = m.replaceAll("é", "&eacute;");
        m = m.replaceAll("í", "&iacute;");
        m = m.replaceAll("ó", "&oacute;");
        m = m.replaceAll("ú", "&uacute;");
        m = m.replaceAll("ñ", "&ntilde;");
        m = m.replaceAll("€", "&euro;");
        return m;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void onload() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.setEventId(hsr.getParameter("id"));
    }

    public void tag_onLoad() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.setTag(hsr.getParameter("tag"));
    }

    public void createEvent() {
        Evento e = new Evento();
        e.setTitulo(event_new_titulo);
        e.setDescripcion(event_new_descripcion);
        e.setFecha_inicio(event_new_fecha_inicio);
        e.setFecha_fin(event_new_fecha_fin);
        e.setHorario(event_new_horario);
        e.setPrecio(event_new_precio);
        e.setLongitud(event_new_longitud);
        e.setLatitud(event_new_latitud);
        e.setDestacado(event_new_destacado);
        e.setImagen_url(event_new_imagen_url);
        e.setImagen_titulo(event_new_imagen_titulo);
        business.createEvent(e);
    }

    // verify if these are necessary
    
    public List<Comentario> getComentarios() {
        return comentarios;
    }
    
    
    public List<Destinatario> getDestinatarios() {
        return destinatarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public List<Evento> getEvento_etiquetas() {
        return evento_etiquetas;
    }

    public void setEvento_etiquetas(List<Evento> evento_etiquetas) {
        this.evento_etiquetas = evento_etiquetas;
    }
    
    
    public List<Evento> getEvento_asiste() {
        return evento_asiste;
    }

    public void setEvento_asiste(List<Evento> evento_asiste) {
        this.evento_asiste = evento_asiste;
    }

    public List<Evento> getEvento_gusta() {
        return evento_gusta;
    }

    public void setEvento_gusta(List<Evento> evento_gusta) {
        this.evento_gusta = evento_gusta;
    }

    public List<Evento> getEvento_sigue() {
        return evento_sigue;
    }

    public void setEvento_sigue(List<Evento> evento_sigue) {
        this.evento_sigue = evento_sigue;
    }

}
