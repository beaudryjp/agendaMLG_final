package grupog.agendamlg.business;

import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.entities.Usuario;
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
public class BusinessEvent implements EventLocal {

    @PersistenceContext(unitName = "AgendaMLG-PU")
    private EntityManager em;

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
        System.out.println("localidad: " + loca);
        System.out.println("etiqueta: " + etiq);
        System.out.println("destinatario: " + dest);
        for (Evento x : query.getResultList()) {
            System.out.println(x.getTitulo());
        }
        List<Evento> ev = new ArrayList<>();
        if (!text.isEmpty() && !text.equals("")) {
            System.out.println("getEventsBySearch(): if text not empty");

            for (Evento e : query.getResultList()) {
                if (e.getTitulo().toUpperCase().contains(text.toUpperCase())) {
                    System.out.println("getEventsBySearch(): event exists with that text");

                    ev.add(e);
                }
            }
            return ev;
        } else {
            System.out.println("getEventsBySearch(): text is empty");
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
        em.flush();
    }

    @Override
    public void createComment(Comentario c
    ) {

        em.persist(c);

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
    public List<Comentario> getComments(long evento
    ) {
        Evento e = em.find(Evento.class, evento);
        List<Comentario> evs = e.getComentarios();
        boolean b = evs.isEmpty();
        return evs;
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
    public void deleteComentario(long c) {
        Comentario co = em.find(Comentario.class, c);
        System.out.println(co.getMensaje());
        em.remove(em.merge(co));
    }

    @Override
    public void highlightEvent(Evento e
    ) {
        em.merge(e);
    }
}
