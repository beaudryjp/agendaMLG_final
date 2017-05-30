package grupog.agendamlg.business;

import grupog.agendamlg.entities.*;
import java.util.Date;
import java.util.List;
import javax.ejb.Local; 

/**
 *
 * @author Jean Paul Beaudry
 */
@Local
public interface BusinessLocal {
    public boolean validateLogin(String email, String password);
    public boolean validateRegister(String pseudonimo, String email);
    public void updateUser(Usuario u);
    public void createUser(Usuario u);
    public List<Notificacion> getNotifications(long u);
    public List<Tarea> getTasks(long u);
    public List<Evento> getEventsImportant(); 
    public List<Evento> getEventsBySearch(String text, String loca, String etiq, String dest);
    public List<Evento> getEventsByTag(long etiq); 
    public List<Evento> getEventsByDate(Date fecha); 
    public List<Evento> getEventsNearestByCurrentDate();
    public List<Evento> getLike(long usuario);
    public List<Evento> getFollow(long usuario);
    public List<Evento> getAssist(long usuario);
    public Evento updateEvent(Evento e);
    public void createEvent(Evento e);
    public void deleteEvent(long e);
    public List<Usuario> getUserByEmail(String email);
    public void createComment(Comentario c);
    public void assist(long e, long u);
    public void like(long e, long u);
    public void follow(long evento, long usuario);
    public void createTag(Etiqueta e);
    public void updateTag(Etiqueta e);
    public void deleteTag(long e);
    public void createAudience(Destinatario e);
    public void updateAudience(Destinatario e);
    public void deleteAudience(long e);
    public List<Provincia> getProvinces();
    public List<Localidad> getTowns(String prov);
    public List<Etiqueta> getTags();
    public List<Destinatario> getAudiences();
    public List<Comentario> getComments(long e);
    public List<Usuario> getUsers();
    public Evento getEventById(long event);
    public List<Evento> getEvents();
    public void setNotifications(Notificacion n);
    public Destinatario getAudienceById(long audience);
    public Etiqueta getTagById(long tag);
    public Provincia getProvinciaByName(String name);
    public Localidad getLocalidadByName(String name);
    public Destinatario getDestinatarioByDescripcion(String desc);
    public Etiqueta getEtiquetaByName(String name);
    public List<Etiqueta> getAllTagsByEvent(long event);
    public List<Destinatario> getAllAudiencesByEvent(long event);
    public List<Evento> getEventsByAudience(long audience);
    public int numAssist(long evento);
    public int numLike(long evento);
    public int numFollow(long evento);
    public boolean checkAssist(long evento, long usuario);
    public boolean checkLike(long evento, long usuario);
    public boolean checkFollow(long evento, long usuario);
    public void deleteNotificacion(long id);
    public List<Usuario> getRedactores();
    public void createTask(Tarea t);
    public void deleteTask(long tarea);
    public List<Tarea> getPeticiones(long id);
    public void deleteComentario(long comentario);
    public List<Etiqueta> getTagsWithEvents();
    public List<Evento> getUserEvents(long usuario);

    public void highlightEvent(Evento e);

    public Evento updateEvent2(Evento e);
    public List<Usuario> getUsuariosByEvento(long evento);

}
