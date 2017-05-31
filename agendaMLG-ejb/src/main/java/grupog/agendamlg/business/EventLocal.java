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
public interface EventLocal {
    public void assist(long e, long u);
    public void like(long e, long u);
    public void follow(long evento, long usuario);
    public List<Etiqueta> getAllTagsByEvent(long event);
    public List<Destinatario> getAllAudiencesByEvent(long event);
    public List<Evento> getEventsByAudience(long audience);
    public int numAssist(long evento);
    public int numLike(long evento);
    public int numFollow(long evento);
    public boolean checkAssist(long evento, long usuario);
    public boolean checkLike(long evento, long usuario);
    public boolean checkFollow(long evento, long usuario);
    public void deleteComentario(long comentario);
    public void highlightEvent(Evento e);
    public Evento updateEvent2(Evento e);
    public Evento getEventById(long event);
    public List<Evento> getEvents();
    public List<Evento> getEventsImportant(); 
    public List<Evento> getEventsBySearch(String text, String loca, String etiq, String dest);
    public List<Evento> getEventsByTag(long etiq); 
    public List<Evento> getEventsByDate(Date fecha); 
    public List<Evento> getEventsNearestByCurrentDate();
    public Evento updateEvent(Evento e);
    public void createEvent(Evento e);
    public void deleteEvent(long e);
    public void createComment(Comentario c);
    public void deleteNotificacion(long id);
    public List<Comentario> getComments(long e);
}
