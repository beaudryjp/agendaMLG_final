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
    public List<Notificacion> getNotifications(Usuario u);
    public List<Tarea> getTasks(Usuario u);
    public List<Evento> getEventsImportant(); 
    public List<Evento> getEventsBySearch(String text, String loca, String etiq, String dest);
    public List<Evento> getEventsByTag(String etiq); 
    public List<Evento> getEventsByDate(Date fecha); 
    public List<Evento> getEventsNearestByCurrentDate();
    public List<Evento> getLike(Usuario u);
    public List<Evento> getFollow(Usuario u);
    public List<Evento> getAssist(Usuario u);
    public Evento updateEvent(Evento e);
    public void createEvent(Evento e);
    public void deleteEvent(Evento e);
    public List<Usuario> getUserByEmail(String email);
    public void createComment(Comentario c);
    public void assist(Evento e, Usuario u);
    public void like(Evento e, Usuario u);
    public void follow(Evento e, Usuario u);
    public void createTag(Etiqueta e);
    public void updateTag(Etiqueta e);
    public void deleteTag(Etiqueta e);
    public void createAudience(Destinatario e);
    public void updateAudience(Destinatario e);
    public void deleteAudience(Destinatario e);
    public List<Provincia> getProvinces();
    public List<Localidad> getTowns(String prov);
    public List<Etiqueta> getTags();
    public List<Destinatario> getAudiences();
    public List<Comentario> getComments(Evento e);
    public List<Usuario> getUsers();
    public Evento getEventById(String event);
    public List<Evento> getEvents();
    public void setNotifications(Notificacion n);
    public Destinatario getAudienceById(int audience);
    public Etiqueta getTagById(int tag);
    public Provincia getProvinciaByName(String name);
    public Localidad getLocalidadByName(String name);
    public Destinatario getDestinatarioByDescripcion(String desc);
    public Etiqueta getEtiquetaByName(String name);
    public List<Etiqueta> getAllTagsByEvent(String event);
    public List<Destinatario> getAllAudiencesByEvent(String event);
    public List<Evento> getEventsByAudience(String audience);
    public int numAssist(Evento e);
    public int numLike(Evento e);
    public int numFollow(Evento e);
    public boolean checkAssist(Evento e, Usuario u);
    public boolean checkLike(Evento e, Usuario u);
    public boolean checkFollow(Evento e, Usuario u);
}
