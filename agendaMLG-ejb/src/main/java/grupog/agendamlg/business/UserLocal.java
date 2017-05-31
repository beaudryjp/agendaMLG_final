package grupog.agendamlg.business;

import grupog.agendamlg.entities.*;
import java.util.List;
import javax.ejb.Local; 

/**
 *
 * @author Jean Paul Beaudry
 */
@Local
public interface UserLocal {
    public boolean validateLogin(String email, String password);
    public boolean validateRegister(String pseudonimo, String email);
    public void updateUser(Usuario u);
    public void createUser(Usuario u);
    public List<Notificacion> getNotifications(long u);
    public List<Tarea> getTasks(long u);
    public List<Evento> getLike(long usuario);
    public List<Evento> getFollow(long usuario);
    public List<Evento> getAssist(long usuario);
    public List<Usuario> getUserByEmail(String email);
    public List<Usuario> getUsers();
    public void setNotifications(Notificacion n);
    public List<Usuario> getUsuariosByEvento(long evento);
    public List<Tarea> getPeticiones(long id) ;
    public void deleteTask(long t);
    public void createTask(Tarea t);
    public List<Usuario> getRedactores() ;
    public void deleteNotificacion(long id);
}
