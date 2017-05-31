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
    public List<Notificacion> getNotifications(long usuario) {
        Usuario u = em.find(Usuario.class, usuario);
        List<Notificacion> l = u.getNotificaciones();
        boolean b = l.isEmpty();
        return l;
    }

    @Override
    public List<Tarea> getTasks(long usuario) {
        Usuario u = em.find(Usuario.class, usuario);
        List<Tarea> l = u.getTareas();
        boolean b = l.isEmpty();
        return l;
    }

    @Override
    public List<Evento> getLike(long usuario) {
        Usuario u = em.find(Usuario.class, usuario);
        List<Evento> l = u.getMegusta();
        boolean b = l.isEmpty();
        return l;
    }

    @Override
    public List<Evento> getFollow(long usuario) {
        Usuario u = em.find(Usuario.class, usuario);
        List<Evento> l = u.getSigue();
        boolean b = l.isEmpty();
        return l;
    }

    @Override
    public List<Evento> getAssist(long usuario) {
        Usuario u = em.find(Usuario.class, usuario);
        List<Evento> l = u.getAsiste();
        boolean b = l.isEmpty();
        return l;
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
        if (!text.isEmpty() && !text.equals("")) {
            for (Evento e : query.getResultList()) {
                if (e.getTitulo().toUpperCase().contains(text.toUpperCase())) {
                    ev.add(e);
                }
            }
            return ev;
        } else {
            if (query.getResultList().isEmpty()) {
                return new ArrayList<>();
            } else {
                return query.getResultList();
            }
        }
    }

    @Override
    public List<Evento> getEventsByTag(long etiq) {
        Etiqueta et = em.find(Etiqueta.class, etiq);
        List<Evento> l = et.getEvento();
        boolean b = l.isEmpty();
        return l;
    }

    @Override
    public List<Evento> getEventsByAudience(long audience) {
        Destinatario des = em.find(Destinatario.class, audience);
        return des.getEvento();
    }

    @Override
    public List<Evento> getEventsByDate(Date fecha) {
        TypedQuery<Evento> query = em.createNamedQuery("getEventsByDate", Evento.class)
                .setParameter("fecha", fecha)
                .setParameter("fecha2", fecha);
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
        //em.flush();

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
    public void deleteTag(long e
    ) {

        Etiqueta et = em.find(Etiqueta.class, e);
        em.remove(em.merge(et));
    }

    @Override
    public void createAudience(Destinatario e
    ) {
        em.persist(e);

    }

    @Override
    public void updateAudience(Destinatario e
    ) {

        em.merge(e);

    }

    @Override
    public void deleteAudience(long e
    ) {

        Destinatario d = em.find(Destinatario.class, e);
        em.remove(em.merge(d));
    }

    @Override
    public void assist(long evento, long usuario
    ) {
        Evento e = em.find(Evento.class, evento);
        Usuario u = em.find(Usuario.class, usuario);
        if (!checkAssist(e.getId_evento(), u.getId_usuario())) {
            u.getAsiste().add(e);
            em.merge(u);
        }

    }

    @Override
    public void like(long evento, long usuario
    ) {
        Evento e = em.find(Evento.class, evento);
        Usuario u = em.find(Usuario.class, usuario);
        if (!checkLike(e.getId_evento(), u.getId_usuario())) {
            u.getMegusta().add(e);
            em.merge(u);
        }
    }

    @Override
    public void follow(long evento, long usuario
    ) {
        Usuario u = em.find(Usuario.class, usuario);
        Evento e = em.find(Evento.class, evento);
        if (!checkFollow(e.getId_evento(), u.getId_usuario())) {
            u.getSigue().add(e);
            em.merge(u);
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

        String a;
        List<Localidad> l = new ArrayList<>();
        for (Provincia p : getProvinces()) {
            if (p.getNombre().equals(prov)) {
                a = p.getLocalidades().get(0).getNombre();
                l = p.getLocalidades();
            }
        }

        return l;
    }

    @Override
    public List<Destinatario> getAudiences() {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAllAudience", Destinatario.class);
        return query.getResultList();
    }

    @Override
    public List<Comentario> getComments(long evento
    ) {
        Evento e = em.find(Evento.class, evento);
        List<Comentario> evs = e.getComentarios();
        boolean b = evs.isEmpty();
        return evs;
    }

    @Override
    public List<Usuario> getUsers() {
        TypedQuery<Usuario> query = em.createNamedQuery("getAllUsers", Usuario.class);
        return query.getResultList();
    }

    @Override
    public Evento getEventById(long event) {
        Evento e = em.find(Evento.class, event);
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
    public Destinatario getAudienceById(long audience
    ) {
        Destinatario d = em.find(Destinatario.class, audience);
        return d;
    }

    @Override
    public Etiqueta getTagById(long tag
    ) {
        Etiqueta et = em.find(Etiqueta.class, tag);
        return et;
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
    public List<Etiqueta> getAllTagsByEvent(long event
    ) {
        Evento e = em.find(Evento.class, event);
        List<Etiqueta> et = e.getEtiqueta();
        boolean b = et.isEmpty();
        return et;
    }

    @Override
    public List<Destinatario> getAllAudiencesByEvent(long event) {
        Evento e = em.find(Evento.class, event);
        List<Destinatario> d = e.getDestinatario();
        boolean b = d.isEmpty();
        return d;
    }

    @Override
    public void deleteEvent(long e
    ) {
        Evento e1 = em.find(Evento.class, e);
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
    public int numAssist(long evento
    ) {
        Evento e = em.find(Evento.class, evento);
        return e.getAsiste().size();
    }

    @Override
    public int numLike(long evento) {
        Evento e = em.find(Evento.class, evento);
        return e.getMegusta().size();
    }

    @Override
    public int numFollow(long evento
    ) {
        Evento e = em.find(Evento.class, evento);
        return e.getSigue().size();
    }

    @Override
    public boolean checkAssist(long evento, long usuario
    ) {
        Evento e = em.find(Evento.class, evento);
        Usuario u = em.find(Usuario.class, usuario);
        return e.getSigue().contains(u);
    }

    @Override
    public boolean checkLike(long evento, long usuario
    ) {
        Evento e = em.find(Evento.class, evento);
        Usuario u = em.find(Usuario.class, usuario);
        return e.getMegusta().contains(u);
    }

    @Override
    public boolean checkFollow(long evento, long usuario) {
        Evento e = em.find(Evento.class, evento);
        Usuario u = em.find(Usuario.class, usuario);
        return e.getSigue().contains(u);
    }

    @Override
    public void deleteNotificacion(long id) {
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
    public void createTask(Tarea t) {
        em.persist(t);
    }

    @Override
    public void deleteTask(long t) {
        Tarea tari = em.find(Tarea.class, t);
        List<Usuario> users = tari.getRedactores();
        for (Usuario x : users) {
            x.getTareas().remove(tari);
        }
        em.remove(em.merge(tari));
    }

    @Override
    public List<Tarea> getPeticiones(long id) {
        Usuario susi = em.find(Usuario.class, id);
        boolean p = susi.getPeticion().isEmpty();
        return susi.getPeticion();
    }

    @Override
    public void deleteComentario(long c) {
        Comentario co = em.find(Comentario.class, c);
        System.out.println(co.getMensaje());
        em.remove(em.merge(co));
    }

    @Override
    public List<Etiqueta> getTagsWithEvents() {
        List<Etiqueta> tags = new ArrayList<>();
        for (Etiqueta x : getTags()) {
            if (getEventsByTag(x.getId_etiqueta()).size() > 0) {
                tags.add(x);
            }
        }

        return tags;
    }

    @Override
    public List<Evento> getUserEvents(long usuario) {
        return em.createNamedQuery("getUserEvents", Evento.class).setParameter("usuario", usuario).getResultList();
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
