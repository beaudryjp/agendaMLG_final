package grupog.agendamlg.business;

import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.entities.Tarea;
import grupog.agendamlg.entities.Usuario;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jean Paul Beaudry
 */
@Stateless
@LocalBean
public class Business implements BusinessLocal {
    
    @PersistenceContext(unitName = "AgendaMLG-PU")
    private EntityManager em;

    @Override
    public boolean validateLogin(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateRegister(String pseudonimo, String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario updateUser(Usuario u) {
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
        return u;
    }

    @Override
    public void createUser(Usuario u) {
        if(validateRegister(u.getPseudonimo(), u.getEmail())){
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Notificacion> getNotifications(Usuario u) {
        TypedQuery<Notificacion> query = em.createNamedQuery("Notificacion.getNotifications",Notificacion.class)
                .setParameter("id_usuario", u.getId_usuario());
        //Query query = em.createNamedQuery("getNotifications").setParameter("id_usuario", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Tarea> getTasks(Usuario u) {
        TypedQuery<Tarea> query = em.createNamedQuery("Tarea.getTasks",Tarea.class)
                .setParameter("id_usuario", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsImportant() {
        TypedQuery<Evento> query = em.createNamedQuery("Evento.getEventsImportant",Evento.class)
                .setMaxResults(2);
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsBySearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Evento updateEvent(Evento e) {
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
        //[Optional] RF-005 Mandar notificaciones al correo y notificaciones internas si un evento cambia
        //Get all users who are liking, assisting or following
        //Check if the users have the email notifier enabled
        //Send internal notification in any case
        return e;
    }

    @Override
    public void createEvent(Evento e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteEvent(Evento e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Comentario> createComment(Comentario c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createTag(Etiqueta e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Etiqueta updateTag(Etiqueta e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteTag(Etiqueta e) {
       em.getTransaction().begin();
       em.getTransaction().commit();
    }

    @Override
    public void createAudience(Destinatario e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Destinatario updateAudience(Destinatario e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAudience(Destinatario e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Evento getEvent(int id) {
        return em.find(Evento.class, id);
    }

}
