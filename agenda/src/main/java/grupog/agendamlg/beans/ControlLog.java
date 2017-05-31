package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Redirect;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Susana
 */
@Named(value = "ControlLog")
@SessionScoped
public class ControlLog implements Serializable {

    private Usuario usuario;
    @EJB
    private Business business;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void home() {
        if (usuario.getRol_usuario() != null) {
            Redirect.redirectToProfile();
        } else {
            Redirect.redirectToLogin();
        }

    }

    public void logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuario = null;
        Redirect.redirectToIndex();
    }

    public ControlLog() {

    }

    public void checkUserPrivileges() {
        HttpServletRequest hsr = Redirect.getRequest();
        if (usuario == null) { //User not loggedin
            Redirect.redirectToIndex();
        } else if (hsr.getParameterMap().containsKey("id") && usuario == null) { //User not loggedin and url has event ta
            Redirect.redirectTo("/event/all");
        }

    }

    public void checkIfOwner() {
        if (!this.isUserOwnerOfEvent()) {
            Redirect.redirectTo(Redirect.getRequest().getParameter("id"));
        }
    }

    public void userHasAdminPrivilege() {
        if (usuario != null) {
            if (!usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR)) {
                Redirect.redirectToIndex();
            }
        } else {
            Redirect.redirectToIndex();
        }
    }

    //Event
    public void checkEventParameter() {
        String eventId;
        HttpServletRequest hsr = Redirect.getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            eventId = hsr.getParameter("id");
            if (eventId != null && !eventId.isEmpty()) {
                if (business.getEventById(Long.parseLong(eventId)) != null) {

                } else {
                    Redirect.redirectToIndex();
                }
            } else {
                Redirect.redirectToIndex();
            }
        } else {
            Redirect.redirectToIndex();
        }
    }

    public void checkVisibility() {
        String eventId;
        HttpServletRequest hsr = Redirect.getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            eventId = hsr.getParameter("id");
            if (eventId != null && !eventId.isEmpty()) {
                Evento e = business.getEventById(Long.parseLong(eventId));
                if(!isUserAdmin() && !e.getVisible()){
                    Redirect.redirectToIndex();
                }
            }
        }

    }

    public void checkLoggedIn() {
        if (usuario != null) {
            Redirect.redirectToProfile();
        }
    }

    public void solicitar() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Se ha solicitado la validación al redactor."));
    }

    public boolean isUserValidated() {
        return (this.usuario != null)
                && (this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.VALIDADO)
                || this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR));
    }

    public boolean isUserRegistered() {
        return this.usuario != null && this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REGISTRADO);
    }

    public boolean isUserAdmin() {
        return this.usuario != null && this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR);
    }

    public boolean isUserLoggedIn() {
        return this.usuario != null
                && (this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR)
                || this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.VALIDADO)
                || this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REGISTRADO));
    }

    public boolean isUserOwnerOfEvent() {
        boolean result = false;
        HttpServletRequest hsr = Redirect.getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            if (!hsr.getParameter("id").isEmpty()) {
                Evento e = business.getEventById(Long.parseLong(hsr.getParameter("id")));
                if (e != null && usuario != null) {
                    if (usuario.equals(e.getPropietario()) || usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR)) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }
    
        
    public boolean isEventVisible(){
        boolean result = false;
        HttpServletRequest hsr = Redirect.getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            if (!hsr.getParameter("id").isEmpty()) {
                Evento e = business.getEventById(Long.parseLong(hsr.getParameter("id")));
                if (e != null && usuario != null) {
                    result = (e.getVisible() && this.isUserRegistered()) || (isUserOwnerOfEvent());
                }
            }
        }
        return result;
    }
    
    public boolean isEventHighlighted(){
        boolean result = false;
        HttpServletRequest hsr = Redirect.getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            if (!hsr.getParameter("id").isEmpty()) {
                Evento e = business.getEventById(Long.parseLong(hsr.getParameter("id")));
                if (e != null && usuario != null) {
                    result = e.isDestacado();
                }
            }
        }
        return result;
    }
}
