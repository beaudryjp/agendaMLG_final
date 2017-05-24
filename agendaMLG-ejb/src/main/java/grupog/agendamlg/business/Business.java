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
        TypedQuery<Usuario> query = em.createNamedQuery("checkEmail", Usuario.class)
                .setParameter("uemail", email);
        boolean validado = false;
        List<Usuario> u = query.getResultList();
        if (u.isEmpty()) {
            byte[] salt_bytes = Password.hexStringToByteArray(u.get(0).getSal());
            char[] pass = password.toCharArray();
            byte[] hash_bytes = Password.hash(pass, salt_bytes);
            String hash = Password.bytesToHex(hash_bytes);

            if (hash.equals(u.get(0).getPassword_hash())) {
                validado = true;
            }
        }
        return validado;
    }

    @Override
    public boolean validateRegister(String pseudonimo, String email) {
        TypedQuery<Usuario> query = em.createNamedQuery("checkEmail", Usuario.class)
                .setParameter("uemail", email);
        TypedQuery<Usuario> query2 = em.createNamedQuery("checkUsername", Usuario.class)
                .setParameter("upseudonimo", pseudonimo);
        boolean validado = false;
        List<Usuario> u = query.getResultList();
        if (u.isEmpty()) {
            List<Usuario> u2 = query2.getResultList();
            if (u2.isEmpty()) {
                validado = true;
            }
        }

        return validado;
    }

    @Override
    public void updateUser(Usuario u) {

        em.merge(u);

    }

    @Override
    public void createUser(Usuario u) {

        if (validateRegister(u.getPseudonimo(), u.getEmail())) {
            //         
            em.persist(u);
            //          
        }
    }

    @Override
    public List<Notificacion> getNotifications(Usuario u) {
        TypedQuery<Notificacion> query = em.createNamedQuery("getNotifications", Notificacion.class)
                .setParameter("id_usuario", u.getId_usuario());
        //Query query = em.createNamedQuery("getNotifications").setParameter("id_usuario", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Tarea> getTasks(Usuario u) {
        TypedQuery<Tarea> query = em.createNamedQuery("getTasks", Tarea.class)
                .setParameter("id_usuario", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsImportant() {
        TypedQuery<Evento> query = em.createNamedQuery("getEventsImportant", Evento.class)
                .setMaxResults(2);
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsBySearch(String text, String loca, String etiq, String dest) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventsBySearch", Evento.class)
                .setParameter("localidad", loca)
                .setParameter("etiqueta", etiq)
                .setParameter("destinatario", dest);
        List<Evento> ev = new ArrayList<>();
//        System.out.println("text: " + text);
//        System.out.println("localidad: " + loca);
//        System.out.println("etiqueta: " + etiq);
//        System.out.println("destinatario: " + dest);
//        for (Evento x : query.getResultList()) {
//            System.out.println(x.getTitulo());
//        }

        if (!text.isEmpty() && !text.equals("")) {
            for (Evento e : query.getResultList()) {
                if (e.getTitulo().toUpperCase().contains(text.toUpperCase())) {
                    ev.add(e);
                }
            }
        }
        else
            ev = query.getResultList();

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
        TypedQuery<Evento> query = em.createNamedQuery("getEventsByDate", Evento.class)
                .setParameter("fecha", fecha);
        return query.getResultList();
    }

    @Override
    public Evento updateEvent(Evento e) {

        em.merge(e);

        //[Optional] RF-005 Mandar notificaciones al correo y notificaciones internas si un evento cambia
        //Get all users who are liking, assisting or following
        //Check if the users have the email notifier enabled
        //Send internal notification in any case
        return e;
    }

    @Override
    public void createEvent(Evento e) {

        em.persist(e);

    }

    @Override
    public void deleteEvent(Evento e) {

        em.remove(e);

    }

    @Override
    public void createComment(Comentario c) {

        em.persist(c);

    }

    @Override
    public void createTag(Etiqueta e) {

        em.persist(e);

    }

    @Override
    public void updateTag(Etiqueta e) {

        em.merge(e);

    }

    @Override
    public void deleteTag(Etiqueta e) {

        em.remove(e);

    }

    @Override
    public void createAudience(Destinatario e) {

        em.persist(e);

    }

    @Override
    public void updateAudience(Destinatario e) {

        em.merge(e);

    }

    @Override
    public void deleteAudience(Destinatario e) {

        em.remove(e);

    }

    @Override
    public Evento getEvent(String id) {
        return em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", Long.parseLong(id)).getSingleResult();
    }

    @Override
    public void assist(Evento e, Usuario u) {

        u.getAsiste().add(e);
        em.persist(u);
        //em.merge(u);

    }

    @Override
    public void like(Evento e, Usuario u) {

        u.getMegusta().add(e);
        em.persist(u);
        //em.merge(u);

    }

    @Override
    public void follow(Evento e, Usuario u) {

        u.getSigue().add(e);
        em.persist(u);
        //em.merge(u);

    }

    @Override
    public List<Usuario> getUserByEmail(String email) {
        TypedQuery<Usuario> query = em.createNamedQuery("checkEmail", Usuario.class)
                .setParameter("uemail", email);
        return query.getResultList();
    }

    @Override
    public List<Provincia> getProvinces() {
        TypedQuery<Provincia> query = em.createNamedQuery("getAllProvinces", Provincia.class);
        return query.getResultList();
    }

    @Override
    public List<Localidad> getTowns(String prov) {
        //UserTransaction tx = cx.getUserTransaction();

        //try{tx.begin();}catch(Exception e){};
        String a;
        List<Localidad> l = new ArrayList<>();
        for (Provincia p : getProvinces()) {
            if (p.getNombre().equals(prov)) {
                a = p.getLocalidades().get(0).getNombre();
                l = p.getLocalidades();
            }
        }
        //try{tx.commit();}catch(Exception e){};
        return l;
    }

    @Override
    public List<Destinatario> getAudiences() {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAllAudience", Destinatario.class);
        return query.getResultList();
    }

    @Override
    public List<Comentario> getComentarios(Evento e) {
        TypedQuery<Comentario> query = em.createNamedQuery("getAllCommentsFromEvent", Comentario.class)
                .setParameter("evento", e.getId_evento());
        return query.getResultList();
    }

    @Override
    public List<Usuario> getUsers() {
        TypedQuery<Usuario> query = em.createNamedQuery("getAllUsers", Usuario.class);
        return query.getResultList();
    }

    @Override
    public Evento getEventById(String event) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", Long.parseLong(event));
        return query.getSingleResult();
    }

    @Override
    public List<Evento> getEvents() {
        TypedQuery<Evento> query = em.createNamedQuery("getAllEvents", Evento.class);
        return query.getResultList();
    }

    @Override
    public void setNotifications(Notificacion n) {

        em.persist(n);

    }

    @Override
    public Destinatario getAudienceById(int audience) {
        TypedQuery<Destinatario> query = em.createNamedQuery("getEventById", Destinatario.class)
                .setParameter("destinatario", audience);
        return query.getSingleResult();
    }

    @Override
    public Etiqueta getTagById(int tag) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getEventById", Etiqueta.class)
                .setParameter("etiqueta", tag);
        return query.getSingleResult();
    }

    @Override
    public List<Etiqueta> getTags() {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getAllTags", Etiqueta.class);
        return query.getResultList();
    }

    @Override
    public Provincia getProvinciaByName(String name) {
        TypedQuery<Provincia> query = em.createNamedQuery("getProvinciaByName", Provincia.class)
                .setParameter("nombre", name);
        return query.getSingleResult();
    }

    @Override
    public Localidad getLocalidadByName(String name) {
        TypedQuery<Localidad> query = em.createNamedQuery("getLocalidadByName", Localidad.class)
                .setParameter("nombre", name);
        return query.getSingleResult();
    }

    @Override
    public Destinatario getDestinatarioByDescripcion(String desc) {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAudienceByDescription", Destinatario.class)
                .setParameter("descripcion", desc);
        return query.getSingleResult();
    }

    @Override
    public Etiqueta getEtiquetaByName(String name) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getTagByName", Etiqueta.class)
                .setParameter("nombre", name);
        return query.getSingleResult();
    }

}
