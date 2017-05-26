package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.general.Redirect;
import grupog.agendamlg.general.Sendmail;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.LatLng;
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
    @Inject
    private ProvinciaBean prov;

    private TagCloudModel model;
    private List<Evento> eventos;
    private List<Evento> evento_asiste;
    private List<Evento> evento_gusta;
    private List<Evento> evento_sigue;
    private List<Evento> evento_etiquetas;
    private List<Evento> eventoSearch;
    private List<String> etiquetas;
    private List<String> destinatarios;
    private List<Destinatario> eventDestinatarios;
    private List<Comentario> eventComentarios;
    private List<Etiqueta> eventoEtiquetas;
    private String eventNewComment;
    private String searchLocalidad;
    private String searchDestinatario;
    private String searchEtiqueta;
    private String searchText;
    private String tag;
    private String eventId;
    private Date dateSelected;
    private String event_new_titulo;
    private String event_new_descripcion;
    private String event_new_fecha_inicio;
    private String event_new_fecha_fin;
    private String event_new_horario;
    private String event_new_precio;
    private double event_new_longitud;
    private double event_new_latitud;
    private boolean event_new_destacado;
    private UploadedFile event_new_imagen_url;
    private String event_new_imagen_titulo;
    private int numAssists;
    private int numLikes;
    private int numFollows;

    @PostConstruct
    public void init() {
        searchLocalidad = "Málaga";
        searchDestinatario = "Todos";
        searchEtiqueta = "Todos";
    }

    public List<Evento> getEventosProximos() {
        List<Evento> e = business.getEventsNearestByCurrentDate();
        if (!e.isEmpty()) {
            if (e.size() > 2) {
                return e.subList(0, 2);
            } else {
                return e;
            }
        } else {
            return new ArrayList<>();
        }
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

    public void searchEvents() {
        eventoSearch = business.getEventsBySearch(searchText, prov.getLocalidad(), searchEtiqueta, searchDestinatario);
        Redirect.redirectTo("/event/search");
    }

    public List<Evento> getSearchEventosEtiquetas() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return business.getEventsByTag(hsr.getParameter("tag"));
    }

    public Evento getEvento() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return business.getEventById(hsr.getParameter("id"));
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

    public TagCloudModel getModelTags() {
        TagCloudModel s = new DefaultTagCloudModel();
        Random r = new Random(0);
        Evento x = business.getEventById(eventId);
        if (x != null) {
            for (Etiqueta et : business.getAllTagsByEvent(eventId)) {
                s.addTag(new DefaultTagCloudItem(et.getNombre(), "/event/tag/" + et.getNombre(), r.nextInt(4) + 1));
            }
        }
        return s;
    }

    public TagCloudModel getModel() {
        return model;
    }

    public void sendNotificationLike(ActionEvent e) {
        Evento evento = business.getEventById(eventId);
        if (!business.checkLike(evento, current_user.getUsuario())) {
            sendMailSocial("Has pinchado like en el evento");
            business.like(evento, current_user.getUsuario());
        }
        Redirect.redirectToEventInfo(eventId);
    }

    public void sendNotificationAssist(ActionEvent e) {
        Evento evento = business.getEventById(eventId);
        if (!business.checkAssist(evento, current_user.getUsuario())) {
            sendMailSocial("Has indicado que vas a asistir al evento");
            business.assist(evento, current_user.getUsuario());
        }
        Redirect.redirectToEventInfo(eventId);
    }

    public void sendNotificationFollow(ActionEvent e) {
        Evento evento = business.getEventById(eventId);
        if (!business.checkFollow(evento, current_user.getUsuario())) {
            sendMailSocial("Has indicado que quieres seguir el evento");
            business.follow(evento, current_user.getUsuario());
        }
        Redirect.redirectToEventInfo(eventId);
    }

    private void sendMailSocial(String msg) {
        Evento ev = business.getEventById(eventId);
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
        Redirect.redirectToEventInfo(eventId);
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

    public void setModel(TagCloudModel model) {
        this.model = model;
    }

    public Date getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(Date dateSelected) {
        this.dateSelected = dateSelected;
    }

    public String getEvent_new_titulo() {
        return event_new_titulo;
    }

    public void setEvent_new_titulo(String event_new_titulo) {
        this.event_new_titulo = event_new_titulo;
    }

    public String getEvent_new_descripcion() {
        return event_new_descripcion;
    }

    public void setEvent_new_descripcion(String event_new_descripcion) {
        this.event_new_descripcion = event_new_descripcion;
    }

    public String getEvent_new_fecha_inicio() {
        return event_new_fecha_inicio;
    }

    public void setEvent_new_fecha_inicio(String event_new_fecha_inicio) {
        this.event_new_fecha_inicio = event_new_fecha_inicio;
    }

    public String getEvent_new_fecha_fin() {
        return event_new_fecha_fin;
    }

    public void setEvent_new_fecha_fin(String event_new_fecha_fin) {
        this.event_new_fecha_fin = event_new_fecha_fin;
    }

    public String getEvent_new_horario() {
        return event_new_horario;
    }

    public void setEvent_new_horario(String event_new_horario) {
        this.event_new_horario = event_new_horario;
    }

    public String getEvent_new_precio() {
        return event_new_precio;
    }

    public void setEvent_new_precio(String event_new_precio) {
        this.event_new_precio = event_new_precio;
    }

    public double getEvent_new_longitud() {
        return event_new_longitud;
    }

    public void setEvent_new_longitud(double event_new_longitud) {
        this.event_new_longitud = event_new_longitud;
    }

    public double getEvent_new_latitud() {
        return event_new_latitud;
    }

    public void setEvent_new_latitud(double event_new_latitud) {
        this.event_new_latitud = event_new_latitud;
    }

    public boolean isEvent_new_destacado() {
        return event_new_destacado;
    }

    public void setEvent_new_destacado(boolean event_new_destacado) {
        this.event_new_destacado = event_new_destacado;
    }

    public UploadedFile getEvent_new_imagen_url() {
        return event_new_imagen_url;
    }

    public void setEvent_new_imagen_url(UploadedFile event_new_imagen_url) {
        this.event_new_imagen_url = event_new_imagen_url;
    }

    public String getEvent_new_imagen_titulo() {
        return event_new_imagen_titulo;
    }

    public void setEvent_new_imagen_titulo(String event_new_imagen_titulo) {
        this.event_new_imagen_titulo = event_new_imagen_titulo;
    }

    public List<Destinatario> getEventDestinatarios() {
        return eventDestinatarios;
    }

    public void setEventDestinatarios(List<Destinatario> eventDestinatarios) {
        this.eventDestinatarios = eventDestinatarios;
    }

    public void onload() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.setEventId(hsr.getParameter("id"));
        model = getModelTags();
        Evento e = business.getEventById(eventId);
        eventDestinatarios = business.getAllAudiencesByEvent(eventId);
        eventComentarios = business.getComments(e);
        numAssists = business.numAssist(e);
        numLikes = business.numLike(e);
        numFollows = business.numFollow(e);
        /*
        numAssists = business.getEventById(eventId).getAsiste().size();
        System.out.println("numAssists: " + numAssists);
         */
    }

    public void tag_onLoad() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.setTag(hsr.getParameter("tag"));
    }

    public void createEvent() throws java.text.ParseException {

        System.out.println("Estoy intentando crear un evento 1");
        Evento e = new Evento();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        Date endDate = df.parse(event_new_fecha_fin);
        Date startDate = df.parse(event_new_fecha_inicio);
        e.setTitulo(event_new_titulo);
        e.setDescripcion(event_new_descripcion);
        e.setFecha_inicio(startDate);
        e.setFecha_fin(endDate);
        e.setHorario(event_new_horario);
        e.setPrecio(event_new_precio);
        e.setLongitud(event_new_longitud);
        e.setLatitud(event_new_latitud);
        e.setDestacado(event_new_destacado);
        List<Destinatario> s = new ArrayList<>();
        for (String str : destinatarios) {
            s.add(business.getDestinatarioByDescripcion(str));
        }

        e.setDestinatario(s);
        List<Etiqueta> etq = new ArrayList<>();
        for (String etiqueta : etiquetas) {
            etq.add(business.getEtiquetaByName(etiqueta));
        }

        e.setEtiqueta(etq);
        Localidad l = business.getLocalidadByName(prov.getLocalidad());
        e.setLocalidad(l);
        e.setImagen_url(event_new_imagen_url.getFileName());
        e.setImagen_titulo(event_new_titulo);

        System.out.println("Estoy intentando crear un evento 8");
        //business.createEvent(e);
        Redirect.redirectToIndex();
    }

    public void uploadImage() throws IOException {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        System.out.println("Name " + event_new_imagen_url.getFileName());
        System.out.println("tmp directory" + System.getProperty("java.io.tmpdir"));
        System.out.println("Size " + event_new_imagen_url.getSize());
        String filePath = extContext.getRealPath("//resources//img//eventos//");
        System.out.println("destination folder: " + filePath);
        if (event_new_imagen_url != null) {
            byte[] bytes = event_new_imagen_url.getContents();
            String filename = FilenameUtils.getName(event_new_imagen_url.getFileName());
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath + filename)))) {
                stream.write(bytes);
            }
        }
    }

    public void createComment() {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = hsr.getParameter("id");
        Evento e = business.getEventById(id);

        Comentario c = new Comentario();
        c.setEvento(e);
        c.setUsuario(current_user.getUsuario());
        c.setFecha_hora(LocalDateTime.now());
        c.setMensaje(eventNewComment);
        business.createComment(c);
        Redirect.redirectToEventInfo(id);
    }

    public List<String> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<String> d) {
        this.destinatarios = d;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
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

    public int getNumAssists() {
        return numAssists;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public int getNumFollows() {
        return numFollows;
    }

    public void setNumAssists(int numAssists) {
        this.numAssists = numAssists;
    }

    public void setNumLikes(int numLikes) {
        int n = business.getEventById(eventId).getMegusta().size();
        System.out.println(n);
        this.numLikes = n;
    }

    public void setNumFollows(int numFollows) {
        int n = business.getEventById(eventId).getSigue().size();
        System.out.println(n);
        this.numFollows = n;
    }

    public ProvinciaBean getProv() {
        return prov;
    }

    public void setProv(ProvinciaBean prov) {
        this.prov = prov;
    }

    public List<Evento> getEventoSearch() {
        return eventoSearch;
    }

    public void setEventoSearch(List<Evento> eventoSearch) {
        this.eventoSearch = eventoSearch;
    }

    public List<Etiqueta> getEventoEtiquetas() {
        return eventoEtiquetas;
    }

    public void setEventoEtiquetas(List<Etiqueta> eventoEtiquetas) {
        this.eventoEtiquetas = eventoEtiquetas;
    }

    public String getEventNewComment() {
        return eventNewComment;
    }

    public void setEventNewComment(String eventNewComment) {
        this.eventNewComment = eventNewComment;
    }

    public List<Comentario> getEventComentarios() {
        return eventComentarios;
    }

    public void setEventComentarios(List<Comentario> eventComentarios) {
        this.eventComentarios = eventComentarios;
    }

    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();

        event_new_latitud = latlng.getLat();
        event_new_longitud = latlng.getLng();
    }
}
