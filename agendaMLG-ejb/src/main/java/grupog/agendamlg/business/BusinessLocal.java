package grupog.agendamlg.business;

import grupog.agendamlg.entities.*;
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
    public Usuario updateUser(Usuario u);
    public void createUser(Usuario u);
    public List<Notificacion> getNotifications(Usuario u);
    public List<Tarea> getTasks(Usuario u);
    public List<Evento> getEventsImportant(); 
    public List<Evento> getEventsBySearch(); //and getEventsByTag
    public Evento updateEvent(Evento e);
    public void createEvent(Evento e);
    public void deleteEvent(Evento e);
    public Evento getEvent(int id);
    public List<Comentario> createComment(Comentario c);
    //asiste, megusta, sigue
    public void createTag(Etiqueta e);
    public Etiqueta updateTag(Etiqueta e);
    public void deleteTag(Etiqueta e);
    public void createAudience(Destinatario e);
    public Destinatario updateAudience(Destinatario e);
    public void deleteAudience(Destinatario e);
}
