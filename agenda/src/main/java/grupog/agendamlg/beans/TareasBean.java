package grupog.agendamlg.beans;

import grupog.agendamlg.business.BusinessEvent;
import grupog.agendamlg.business.BusinessUser;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Notificacion;
import grupog.agendamlg.entities.Tarea;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Redirect;
import grupog.agendamlg.general.Sendmail;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Susana
 */
@ViewScoped
@ManagedBean(name = "tareas")
public class TareasBean implements Serializable {

    private List<Tarea> pendiente;
    @EJB
    private BusinessUser businessUser;
    @EJB
    private BusinessEvent businessEvent;
    @Inject
    private ControlLog control;

    public List<Tarea> getPendiente() {
        return businessUser.getTasks(control.getUsuario().getId_usuario());
    }

    public void setPendiente(List<Tarea> pendiente) {
        this.pendiente = pendiente;
    }

    public void createTaskUser() {
        Tarea newTask = new Tarea();
        newTask.setFecha_hora(LocalDateTime.now());
        newTask.setCreador_peticion(control.getUsuario());
        newTask.setNombre("Solicito validación");
        newTask.setMensaje(control.getUsuario().getPseudonimo() + " ha solicitado ser usuario validado");
        businessUser.createTask(newTask);
        for (Usuario u : businessUser.getRedactores()) {
            List<Tarea> l = businessUser.getTasks(u.getId_usuario());
            l.add(newTask);
            u.setTareas(l);
            businessUser.updateUser(u);
        }
        Redirect.redirectToProfile();
    }

    public void approve(Tarea t) {

        if (t.getId_evento() == null)//Usuario
        {
            Usuario usi = t.getCreador_peticion();
            usi.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
            businessUser.updateUser(usi);
        } else {//Evento
            Evento e = businessEvent.getEventById(t.getId_evento());
            e.setVisible(true);
            businessEvent.updateEvent(e);
        }

        final StringBuilder m = new StringBuilder();
        m.append("<h2>Notificaci&oacute;n <span style='font-size: 13px'>(")
                .append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(")</span></h2><p>")
                .append("Su solicitud ha sido aceptada")
                .append("</b></p><p style='font-size: 12px'>diariosur</p>");
        if (t.getCreador_peticion().isEmail_notifier()) {
            Sendmail.mailThread(t.getCreador_peticion().getEmail(), "Notificación agendaMLG - diariosur", m.toString());
        }

        Evento eve = businessEvent.getEventById(500);
        Notificacion notiplana = new Notificacion();
        notiplana.setEvento(eve);
        notiplana.setFecha_hora(LocalDateTime.now());
        notiplana.setMensaje("Su solicitud ha sido aceptada");
        notiplana.setUsuario(t.getCreador_peticion());
        businessUser.setNotifications(notiplana);
        businessUser.deleteTask(t.getId_tarea());
    }

    public void reject(Tarea t) {

        if (t.getId_evento() != null) {

            businessEvent.deleteEvent(t.getId_evento());
        }
        final StringBuilder m = new StringBuilder();
        m.append("<h2>Notificaci&oacute;n <span style='font-size: 13px'>(")
                .append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(")</span></h2><p>")
                .append("Su solicitud ha sido rechazada")
                .append("</b></p><p style='font-size: 12px'>diariosur</p>");
        if (t.getCreador_peticion().isEmail_notifier()) {
            Sendmail.mailThread(t.getCreador_peticion().getEmail(), "Notificación agendaMLG - diariosur", m.toString());
        }

        Evento e = businessEvent.getEventById(500);
        Notificacion notiplana = new Notificacion();
        notiplana.setEvento(e);
        notiplana.setFecha_hora(LocalDateTime.now());
        notiplana.setMensaje("Su solicitud ha sido rechazada");
        notiplana.setUsuario(t.getCreador_peticion());
        businessUser.setNotifications(notiplana);
        businessUser.deleteTask(t.getId_tarea());

    }

    public boolean validado() {
        boolean b = false;
        List<Tarea> peticiones = businessUser.getPeticiones(control.getUsuario().getId_usuario());
        for (Tarea t : peticiones) {
            if (t.getId_evento() == null) {
                b = true;
                break;
            }
        }
        return b;
    }
    
    public boolean hasTasks(){
        boolean result = false;
        Usuario u = control.getUsuario();
        if(u != null){
            return !businessUser.getTasks(u.getId_usuario()).isEmpty();
        }
        return false;
    }
}
