
package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
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

    public String home() {

        String pageToShow = "login?faces-redirect=true";

        if (usuario.getRol_usuario() != null) {
            pageToShow = "profile?faces-redirect=true";
        }

        return pageToShow;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        usuario = null;
        return "index?faces-redirect=true";
    }

    public ControlLog() {

    }

    public void checkUserPrivileges(){
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println(hsr.getRequestURI());
        if (usuario == null) {
            Redirect.redirectTo("index?faces-redirect=true");
        } else if (hsr.getParameterMap().containsKey("event") && (usuario.getRol_usuario() != Usuario.Tipo_Rol.REDACTOR && usuario.getRol_usuario() != Usuario.Tipo_Rol.VALIDADO)) {
            Redirect.redirectTo("events_all?faces-redirect=true");
        }
        
    }
    
    public void userHasAdminPrivilege(){
        if(usuario != null){
            if(!usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR)){
                Redirect.redirectTo("index?faces-redirect=true");
            }
        }
        else{
            Redirect.redirectTo("index?faces-redirect=true");
        }
    }

    //Event
    public void checkEventParameter(){
        String eventId;
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            eventId = hsr.getParameter("id");
            if (eventId != null && !eventId.isEmpty()) {
                if(business.getEventById(Integer.parseInt(eventId)) != null){
                    
                }
                else{
                    Redirect.redirectTo("index?faces-redirect=true");
                }
            }
            else{
                Redirect.redirectTo("index?faces-redirect=true");
            }
        } else {
            Redirect.redirectTo("index?faces-redirect=true");
        }
    }
    
    //Destinatario
    public void checkPublicParameter(){
        String name;
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (hsr.getParameterMap().containsKey("id")) {
            name = hsr.getParameter("id");
            if (name != null && !name.isEmpty()) {
                //System.out.println("3");
                //System.out.println("attribute " + hsr.getParameter("event"));
                //TODO - Optional, verify that the event exists 
            }
            else{
                Redirect.redirectTo("index?faces-redirect=true");
            }
        } else {
            Redirect.redirectTo("index?faces-redirect=true");
        }
    }
    
    public void checkLoggedIn(){
        if(usuario != null){
            Redirect.redirectTo("profile?faces-redirect=true");
        }   
    }

    public void solicitar() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Se ha solicitado la validación al redactor."));
    }
    
    public boolean isUserValidated(){
        return (this.usuario != null) && (this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.VALIDADO) || this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR) );
    }
    
    public boolean isUserRegistered(){
        return this.usuario != null && this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REGISTRADO);
    }
    
    public boolean isUserAdmin(){
        return this.usuario != null && this.usuario.getRol_usuario().equals(Usuario.Tipo_Rol.REDACTOR);
    }

}
