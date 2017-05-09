/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

@ViewScoped
@ManagedBean(name = "pass")
/**
 *
 * @author Jose
 */
public class PassBean implements Serializable{

    @Inject
    private Usuariobean usuario;
    private String email;

    public PassBean() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String validate() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo enviado a:", email);
        
        RequestContext.getCurrentInstance().showMessageInDialog(message);
        return usuario.resetPassword(email);
    }
    
    public void like() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Le has dado like", "PATATA");
        
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
}
