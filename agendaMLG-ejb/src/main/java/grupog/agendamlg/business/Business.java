package grupog.agendamlg.business;

import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.entities.Provincia;
import grupog.agendamlg.entities.Tarea;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Password;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.checkEmail", Usuario.class)
                .setParameter("email", email);
        boolean validado = false;
        Usuario u = query.getSingleResult();
        if (u != null) {
            byte[] salt_bytes = Password.hexStringToByteArray(u.getSal());
            char[] pass = password.toCharArray();
            byte[] hash_bytes = Password.hash(pass, salt_bytes);
            String hash = Password.bytesToHex(hash_bytes);

            if (hash.equals(u.getPassword_hash())) {
                validado = true;
            }
        }
        return validado;
    }

    @Override
    public boolean validateRegister(String pseudonimo, String email) {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.checkEmail", Usuario.class)
                .setParameter("email", email);
        TypedQuery<Usuario> query2 = em.createNamedQuery("Usuario.checkUsername", Usuario.class)
                .setParameter("upseudonimo", pseudonimo);
        boolean validado = false;
        Usuario u = query.getSingleResult();
        if (u == null) {
            Usuario u2 = query2.getSingleResult();
            if (u2 == null) {
                validado = true;
            }
        }

        return validado;
    }

    @Override
    public void updateUser(Usuario u) {
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
    }

    @Override
    public void createUser(Usuario u) {
        if (validateRegister(u.getPseudonimo(), u.getEmail())) {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Notificacion> getNotifications(Usuario u) {
        TypedQuery<Notificacion> query = em.createNamedQuery("Notificacion.getNotifications", Notificacion.class)
                .setParameter("id_usuario", u.getId_usuario());
        //Query query = em.createNamedQuery("getNotifications").setParameter("id_usuario", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Tarea> getTasks(Usuario u) {
        TypedQuery<Tarea> query = em.createNamedQuery("Tarea.getTasks", Tarea.class)
                .setParameter("id_usuario", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsImportant() {
        TypedQuery<Evento> query = em.createNamedQuery("Evento.getEventsImportant", Evento.class)
                .setMaxResults(2);
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsBySearch(String text, String prov, String loca, String etiq, String dest) {
        TypedQuery<Evento> query = em.createNamedQuery("Evento.getEventsBySearch", Evento.class)
                .setParameter("localidad", loca)
                .setParameter("provincia", prov)
                .setParameter("etiqueta", etiq)
                .setParameter("destinatario", dest);
        List<Evento> ev = new ArrayList<>();
        for (Evento e : query.getResultList()) {
            if (e.getTitulo().toUpperCase().contains(text.toUpperCase())) {
                ev.add(e);
            }
        }
        return ev;
    }

    @Override
    public List<Evento> getEventsByTag(String etiq) {
        List<Evento> ev = new ArrayList<>();
        for (Evento x : getEvents()) {
            for (Etiqueta et : x.getEtiqueta()) {
                if (et.getNombre().equals(etiq)) {
                    ev.add(x);
                }
            }
        }
        return ev;
    }

    @Override
    public List<Evento> getEventsByDate(LocalDate fecha) {
        TypedQuery<Evento> query = em.createNamedQuery("Evento.getEventsByDate", Evento.class)
                .setParameter("fecha", fecha);
        return query.getResultList();
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
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteEvent(Evento e) {
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public void createComment(Comentario c) {
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
    }

    @Override
    public void createTag(Etiqueta e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void updateTag(Etiqueta e) {
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteTag(Etiqueta e) {
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public void createAudience(Destinatario e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void updateAudience(Destinatario e) {
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteAudience(Destinatario e) {
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public Evento getEvent(int id) {
        return em.find(Evento.class, id);
    }

    @Override
    public void assist(Evento e, Usuario u) {
        em.getTransaction().begin();
        u.getAsiste().add(e);
        em.persist(u);
        //em.merge(u);
        em.getTransaction().commit();
    }

    @Override
    public void like(Evento e, Usuario u) {
        em.getTransaction().begin();
        u.getMegusta().add(e);
        em.persist(u);
        //em.merge(u);
        em.getTransaction().commit();
    }

    @Override
    public void follow(Evento e, Usuario u) {
        em.getTransaction().begin();
        u.getSigue().add(e);
        em.persist(u);
        //em.merge(u);
        em.getTransaction().commit();
    }

    @Override
    public Usuario getUserByEmail(String email) {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.checkEmail", Usuario.class)
                .setParameter("uemail", email);
        return query.getSingleResult();
    }

    @Override
    public List<Provincia> getProvinces() {
        TypedQuery<Provincia> query = em.createNamedQuery("Provincia.getAllProvinces", Provincia.class);
        return query.getResultList();
    }

    @Override
    public List<Localidad> getTowns(String prov) {
        List<Localidad> l = new ArrayList<>();
        for (Provincia p : getProvinces()) {
            if (p.getNombre().equals(prov)) {
                l = p.getLocalidades();
            }
        }
        return l;
    }

    @Override
    public List<Etiqueta> getTags() {
        TypedQuery<Etiqueta> query = em.createNamedQuery("Etiqueta.getAllTags", Etiqueta.class);
        return query.getResultList();
    }

    @Override
    public List<Destinatario> getAudiences() {
        TypedQuery<Destinatario> query = em.createNamedQuery("Destinatario.getAllAudience", Destinatario.class);
        return query.getResultList();
    }

    @Override
    public List<Comentario> getComentarios(Evento e) {
        TypedQuery<Comentario> query = em.createNamedQuery("Comentario.getAllCommentsFromEvent", Comentario.class)
                .setParameter("evento", e.getId_evento());
        return query.getResultList();
    }

    @Override
    public List<Usuario> getUsers() {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.getAllUsers", Usuario.class);
        return query.getResultList();
    }

    @Override
    public Evento getEventById(int event) {
        TypedQuery<Evento> query = em.createNamedQuery("Evento.getEventById", Evento.class)
                .setParameter("evento", event);
        return query.getSingleResult();
    }

    @Override
    public List<Evento> getEvents() {
        TypedQuery<Evento> query = em.createNamedQuery("Evento.getAllEvents", Evento.class);
        return query.getResultList();
    }

    @Override
    public void setNotifications(Notificacion n) {
        em.getTransaction().begin();
        em.persist(n);
        em.getTransaction().commit();
    }

    @Override
    public Destinatario getAudienceById(int audience) {
        TypedQuery<Destinatario> query = em.createNamedQuery("Destinatario.getEventById", Destinatario.class)
                .setParameter("destinatario", audience);
        return query.getSingleResult();
    }

    @Override
    public Etiqueta getTagById(int tag) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("Etiqueta.getEventById", Etiqueta.class)
                .setParameter("etiqueta", tag);
        return query.getSingleResult();
    }
}
