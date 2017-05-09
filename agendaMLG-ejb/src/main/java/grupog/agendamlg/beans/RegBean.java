/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Jose
 */
@Named(value = "registro")
@RequestScoped
@ManagedBean
public class RegBean implements Serializable {

    String Nombre;
    String Apellidos;
    String email;
    String pseudonimo;
    String contrasenia;
    String contrasenia2;
    boolean acepta;

    public RegBean() {

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre =Nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudonimo() {
        return pseudonimo;
    }

    public void setPseudonimo(String pseudonimo) {
        this.pseudonimo = pseudonimo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public boolean isAcepta() {
        return acepta;
    }

    public void setAcepta(boolean acepta) {
        this.acepta = acepta;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContrasenia2() {
        return contrasenia2;
    }

    public void setContrasenia2(String contrasenia2) {
        this.contrasenia2 = contrasenia2;
    }

    public String valido() {
            
        if (Apellidos.isEmpty() || Nombre.isEmpty() || email.isEmpty() || pseudonimo.isEmpty() || contrasenia.isEmpty() || contrasenia2.isEmpty() || !acepta || !contrasenia.equals(contrasenia2)) {
            if(!acepta)
            {
                addMessage();
            }
            
            
            return "registration?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registrado satisfactoriamente"));
        
        return "login?faces-redirect=true";
    }
    
    public void addMessage() {
        String summary = acepta ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(FacesMessage.FACES_MESSAGES, new  FacesMessage("Debe aceptar los terminos de uso"));
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");
        
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
}
