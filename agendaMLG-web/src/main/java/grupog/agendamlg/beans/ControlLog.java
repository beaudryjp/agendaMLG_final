/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Redirect;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
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

    public void checkUserPrivileges() throws IOException {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (usuario == null) {
            Redirect.redirectTo("index.xhtml");
        } else if (hsr.getParameterMap().containsKey("event") && (usuario.getRol_usuario() != Usuario.Tipo_Rol.REDACTOR && usuario.getRol_usuario() != Usuario.Tipo_Rol.VALIDADO)) {
            Redirect.redirectTo("events_all.xhtml");
        }
        
    }

    public void checkEventParameter() throws IOException, Exception {
        String eventId;
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
        if (hsr.getParameterMap().containsKey("event")) {
            eventId = hsr.getParameter("event");
            if (hsr.getParameter("event") != null && !eventId.isEmpty()) {
                //System.out.println("attribute " + hsr.getParameter("event"));
                //TODO - Optional, verify that the event exists
            }
            else{
                Redirect.redirectTo("index.xhtml");
            }
        } else {
            Redirect.redirectTo("index.xhtml");
        }
    }
    
    public void checkLoggedIn(){
        if(usuario != null){
            Redirect.redirectTo("profile.xhtml");
        }   
    }

    public void solicitar() {
        //this.usuario.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Se ha solicitado la validación al redactor."));
    }

}
