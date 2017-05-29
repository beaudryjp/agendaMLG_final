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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

            em.persist(u);

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

        TypedQuery<Tarea> query = em.createNamedQuery("getTareas", Tarea.class)
                .setParameter("id_usuario", u.getId_usuario());

        return query.getResultList();
    }

    @Override
    public List<Evento> getLike(Usuario u) {
        TypedQuery<Evento> query = em.createNamedQuery("getUserLikes", Evento.class)
                .setParameter("id", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Evento> getFollow(Usuario u) {
        TypedQuery<Evento> query = em.createNamedQuery("getUserFollows", Evento.class)
                .setParameter("id", u.getId_usuario());
        return query.getResultList();
    }

    @Override
    public List<Evento> getAssist(Usuario u) {
        TypedQuery<Evento> query = em.createNamedQuery("getUserAssists", Evento.class)
                .setParameter("id", u.getId_usuario());
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
//        System.out.println("localidad: " + loca);
//        System.out.println("etiqueta: " + etiq);
//        System.out.println("destinatario: " + dest);
//        for (Evento x : query.getResultList()) {
//            System.out.println(x.getTitulo());
//        }

        if (!text.isEmpty() && !text.equals("")) {
            List<Evento> ev = new ArrayList<>();
            //System.out.println("getEventsBySearch(): if text not empty");
            for (Evento e : query.getResultList()) {
                if (e.getTitulo().toUpperCase().contains(text.toUpperCase())) {
                    //System.out.println("getEventsBySearch(): event exists with that text");
                    ev.add(e);
                }
            }
            return ev;
        } else {
            //System.out.println("getEventsBySearch(): text is empty");
            return query.getResultList();
        }
    }

    @Override
    public List<Evento> getEventsByTag(String etiq) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventsByTag", Evento.class)
                .setParameter("nombre", etiq);
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsByAudience(String audience) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventsByAudience", Evento.class)
                .setParameter("descripcion", audience);
        return query.getResultList();
    }

    @Override
    public List<Evento> getEventsByDate(Date fecha) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventsByDate", Evento.class)
                .setParameter("fecha", fecha);
        return query.getResultList();
    }

    @Override
    public Evento updateEvent(Evento e) {
        em.merge(e);
        return e;
    }

    @Override
    public Evento updateEvent2(Evento e) {

        Evento original = em.find(Evento.class, e.getId_evento());

                List<Etiqueta> eti = original.getEtiqueta();
                for (Etiqueta et : eti) {
                    et.getEvento().remove(original);
                    
                }
                em.merge(original);
                original.setEtiqueta(e.getEtiqueta());
           
            
                List<Destinatario> di = original.getDestinatario();
                for (Destinatario d : di) {
                    d.getEvento().remove(original);
                   
                }
                em.merge(original);
                original.setDestinatario(e.getDestinatario());
 
            e.setAsiste(original.getAsiste());
            e.setMegusta(original.getMegusta());
            e.setSigue(original.getSigue());
            e.setComentarios(original.getComentarios());
            e.setNotificaciones(original.getNotificaciones());
            em.merge(e);
            em.flush();
            
        return e;
    }

    @Override
    public void createEvent(Evento e
    ) {
        Usuario u = e.getPropietario();
        Usuario us = em.find(Usuario.class, u.getId_usuario());
        us.getMisEventos().add(e);
        em.persist(e);
        /* 
            hago un flush para que justo despues de persistir pueda llamar 
        al evento inserto para obtener el id autogenerado
         */
        em.flush();
    }

    @Override
    public void createComment(Comentario c
    ) {

        em.persist(c);

    }

    @Override
    public void createTag(Etiqueta e
    ) {

        em.persist(e);

    }

    @Override
    public void updateTag(Etiqueta e
    ) {

        em.merge(e);

    }

    @Override
    public void deleteTag(Etiqueta e
    ) {

        List<Evento> event = getEventsByTag(e.getNombre());
        for (Evento eti : event) {
            eti.getEtiqueta().remove(e);
        }
        em.remove(em.merge(e));
    }

    @Override
    public void createAudience(Destinatario e
    ) {
        System.out.println(e.getDescripcion());
        em.persist(e);

    }

    @Override
    public void updateAudience(Destinatario e
    ) {

        em.merge(e);

    }

    @Override
    public void deleteAudience(Destinatario e
    ) {

        List<Evento> event = getEventsByAudience(e.getDescripcion());
        for (Evento doradita : event) {
            doradita.getDestinatario().remove(e);
        }
        em.remove(em.merge(e));
    }

    @Override
    public void assist(Evento e, Usuario u
    ) {
        TypedQuery<Usuario> query = em.createNamedQuery("getUser", Usuario.class)
                .setParameter("id_usuario", u.getId_usuario());

        if (!checkAssist(e, u)) {
            Usuario u2 = query.getResultList().get(0);
            u2.getAsiste().add(e);
            em.merge(u2);
        }

    }

    @Override
    public void like(Evento e, Usuario u
    ) {
        TypedQuery<Usuario> query = em.createNamedQuery("getUser", Usuario.class)
                .setParameter("id_usuario", u.getId_usuario());
        if (!checkLike(e, u)) {
            Usuario u2 = query.getResultList().get(0);
            u2.getMegusta().add(e);
            em.merge(u2);
        }
    }

    @Override
    public void follow(Evento e, Usuario u
    ) {
        TypedQuery<Usuario> query = em.createNamedQuery("getUser", Usuario.class)
                .setParameter("id_usuario", u.getId_usuario());
        if (!checkFollow(e, u)) {
            Usuario u2 = query.getResultList().get(0);
            u2.getSigue().add(e);
            em.merge(u2);
        }
    }

    @Override
    public List<Usuario> getUserByEmail(String email
    ) {
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
    public List<Localidad> getTowns(String prov
    ) {
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
    public List<Comentario> getComments(Evento e
    ) {
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
        Evento e = em.find(Evento.class, Long.parseLong(event));
        return e;
    }

    @Override
    public List<Evento> getEvents() {
        TypedQuery<Evento> query = em.createNamedQuery("getAllEvents", Evento.class);
        return query.getResultList();
    }

    @Override
    public void setNotifications(Notificacion n
    ) {

        em.persist(n);

    }

    @Override
    public Destinatario getAudienceById(Long audience
    ) {
        TypedQuery<Destinatario> query = em.createNamedQuery("getEventById", Destinatario.class)
                .setParameter("destinatario", audience);
        return query.getResultList().get(0);
    }

    @Override
    public Etiqueta getTagById(Long tag
    ) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getEventById", Etiqueta.class)
                .setParameter("etiqueta", tag);
        return query.getResultList().get(0);
    }

    @Override
    public List<Etiqueta> getTags() {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getAllTags", Etiqueta.class);
        return query.getResultList();
    }

    @Override
    public Provincia getProvinciaByName(String name
    ) {
        TypedQuery<Provincia> query = em.createNamedQuery("getProvinciaByName", Provincia.class)
                .setParameter("nombre", name);
        return query.getResultList().get(0);
    }

    @Override
    public Localidad getLocalidadByName(String name
    ) {
        TypedQuery<Localidad> query = em.createNamedQuery("getLocalidadByName", Localidad.class)
                .setParameter("nombre", name);
        return query.getResultList().get(0);
    }

    @Override
    public Destinatario getDestinatarioByDescripcion(String desc
    ) {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAudienceByDescription", Destinatario.class)
                .setParameter("descripcion", desc);
        return query.getResultList().get(0);
    }

    @Override
    public Etiqueta getEtiquetaByName(String name
    ) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getTagByName", Etiqueta.class)
                .setParameter("nombre", name);
        return query.getResultList().get(0);
    }

    @Override
    public List<Etiqueta> getAllTagsByEvent(String event
    ) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getAllTagsByEventId", Etiqueta.class)
                .setParameter("evento", Long.parseLong(event));
        return query.getResultList();
    }

    @Override
    public List<Destinatario> getAllAudiencesByEvent(String event
    ) {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAllAudiencesByEventId", Destinatario.class)
                .setParameter("evento", Long.parseLong(event));
        return query.getResultList();
    }

    @Override
    public void deleteEvent(Evento e
    ) {
        System.out.println(e.getId_evento() + " no revientes");
        TypedQuery<Evento> nombreClaro = em.createNamedQuery("getEventById", Evento.class).setParameter("evento", e.getId_evento());
        Evento e1 = nombreClaro.getResultList().get(0);
        System.out.println("deleteEvent(): entered");
        System.out.println("deleteEvent(): event " + e1.getTitulo());

        List<Etiqueta> eti = e1.getEtiqueta();
        for (Etiqueta et : eti) {
            et.getEvento().remove(e1);
        }

        List<Destinatario> di = e1.getDestinatario();
        for (Destinatario d : di) {
            d.getEvento().remove(e1);
        }

        List<Notificacion> noti = e1.getNotificaciones();
        for (Notificacion n : noti) {
            deleteNotificacion(n.getId_notificacion());
        }

        List<Comentario> comi = e1.getComentarios();
        for (Comentario c : comi) {
            deleteComentario(c.getId_comentario());
        }

        List<Usuario> usersList = e1.getAsiste();
        for (Usuario u : usersList) {
            u.getAsiste().remove(e1);
        }

        List<Usuario> usersList2 = e1.getMegusta();
        for (Usuario u : usersList2) {
            u.getMegusta().remove(e1);
        }

        List<Usuario> usersList3 = e1.getSigue();
        for (Usuario u : usersList3) {
            u.getSigue().remove(e1);
        }

        em.remove(em.merge(e1));
    }

    @Override
    public List<Evento> getEventsNearestByCurrentDate() {
        return em.createNamedQuery("getEventsNearestByDate", Evento.class).getResultList();
    }

    @Override
    public int numAssist(Evento e
    ) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", e.getId_evento());
        List<Evento> events = query.getResultList();
        if (events.isEmpty()) {
            return 0;
        } else {
            return events.get(0).getAsiste().size();
        }
    }

    @Override
    public int numLike(Evento e
    ) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", e.getId_evento());
        List<Evento> events = query.getResultList();
        if (events.isEmpty()) {
            return 0;
        } else {
            return events.get(0).getMegusta().size();
        }
    }

    @Override
    public int numFollow(Evento e
    ) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", e.getId_evento());
        List<Evento> events = query.getResultList();
        if (events.isEmpty()) {
            return 0;
        } else {
            return events.get(0).getSigue().size();
        }
    }

    @Override
    public boolean checkAssist(Evento e, Usuario u
    ) {
        TypedQuery<Evento> query2 = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", e.getId_evento());
        return query2.getResultList().get(0).getAsiste().contains(u);
    }

    @Override
    public boolean checkLike(Evento e, Usuario u
    ) {
        TypedQuery<Evento> query2 = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", e.getId_evento());
        return query2.getResultList().get(0).getMegusta().contains(u);
    }

    @Override
    public boolean checkFollow(Evento e, Usuario u
    ) {
        TypedQuery<Evento> query2 = em.createNamedQuery("getEventById", Evento.class)
                .setParameter("evento", e.getId_evento());
        return query2.getResultList().get(0).getSigue().contains(u);
    }

    @Override
    public void deleteNotificacion(Long id
    ) {
        Notificacion noti = em.find(Notificacion.class, id);
        em.remove(em.merge(noti));
    }

    @Override
    public List<Usuario> getRedactores() {
        TypedQuery<Usuario> u = em.createNamedQuery("getRedactores", Usuario.class)
                .setParameter("rol", Usuario.Tipo_Rol.REDACTOR);
        return u.getResultList();
    }

    @Override
    public void createTask(Tarea t
    ) {
        em.persist(t);
    }

    @Override
    public void deleteTask(Long t
    ) {
        Tarea tari = em.find(Tarea.class, t);
        em.remove(em.merge(tari));
    }

    @Override
    public List<Tarea> getPeticiones(Long id
    ) {
        Usuario susi = em.find(Usuario.class, id);
        boolean p = susi.getPeticion().isEmpty();
        return susi.getPeticion();
    }

    @Override
    public void deleteComentario(Long c
    ) {
        Comentario co = em.find(Comentario.class, c);
        em.remove(em.merge(co));
    }

    @Override
    public List<Etiqueta> getTagsWithEvents() {
        List<Etiqueta> tags = new ArrayList<>();
        for (Etiqueta x : getTags()) {
            if (getEventsByTag(x.getNombre()).size() > 0) {
                tags.add(x);
            }
        }

        return tags;
    }

    @Override
    public List<Evento> getUserEvents(Usuario u
    ) {
        return em.createNamedQuery("getUserEvents", Evento.class).setParameter("usuario", u.getId_usuario()).getResultList();
    }

    @Override
    public void highlightEvent(Evento e
    ) {
        em.merge(e);
    }

    @Override
    public List<Usuario> getUsuariosByEvento(long evento) {
        
        Evento e = em.find(Evento.class, evento);
         List<Usuario> enviados = new ArrayList<>();
        
        for (Usuario usi : e.getAsiste()) {
            if (!enviados.contains(usi)) {
                enviados.add(usi);
            }

        }
      
        for (Usuario usi : e.getMegusta()) {
            if (!enviados.contains(usi)) {
                enviados.add(usi);
            }

        }
        
        for (Usuario usi : e.getSigue()) {
            if (!enviados.contains(usi)) {
                enviados.add(usi);
            }

        }
        return enviados;
    }

}
