/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Usuario;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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

    public void redirectUrl(String url) throws IOException {
        if (usuario == null) {
            url = "index.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().dispatch(url + "?facesRedirect=true");
        } else if (usuario.getRol_usuario() != Usuario.Tipo_Rol.REDACTOR && usuario.getRol_usuario() != Usuario.Tipo_Rol.VALIDADO) {
            url = "events_all.xhtml";
            FacesContext.getCurrentInstance().getExternalContext().dispatch(url + "?facesRedirect=true");
        }

    }
    
    public void solicitar(){
        //this.usuario.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Se ha solicitado la validación al redactor."));
    }
}
