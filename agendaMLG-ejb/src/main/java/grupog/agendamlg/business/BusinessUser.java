package grupog.agendamlg.business;

import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.entities.Tarea;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Password;
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
public class BusinessUser implements UserLocal {

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
    public List<Usuario> getUserByEmail(String email
    ) {
        TypedQuery<Usuario> query = em.createNamedQuery("checkEmail", Usuario.class)
                .setParameter("uemail", email);
        return query.getResultList();
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

    @Override
    public List<Usuario> getUsers() {
        TypedQuery<Usuario> query = em.createNamedQuery("getAllUsers", Usuario.class);
        return query.getResultList();
    }

    @Override
    public void setNotifications(Notificacion n) {

        em.persist(n);

    }
    
    @Override
    public void deleteNotificacion(long id) {
        Notificacion noti = em.find(Notificacion.class, id);
        em.remove(em.merge(noti));
    }

}
