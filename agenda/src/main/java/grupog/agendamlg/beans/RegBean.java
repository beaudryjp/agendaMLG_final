/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Password;
import java.io.Serializable;
import javax.ejb.EJB;
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

    private String Nombre;
    private String Apellidos;
    private String email;
    private String pseudonimo;
    private String contrasenia;
    private String contrasenia2;
    private boolean acepta;
    private boolean email_notifier;

    @EJB
    private Business business;

    public RegBean() {

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
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

    public boolean isEmail_notifier() {
        return email_notifier;
    }

    public void setEmail_notifier(boolean email_notifier) {
        this.email_notifier = email_notifier;
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
            if (!acepta) {
                addMessage();
            }

            return "registration?faces-redirect=true";
        }
        if (business.validateRegister(pseudonimo, email)) {
            //Generate salt
            byte[] salt_bytes = Password.getNextSalt();
            //Convert password to char[]
            char[] password = contrasenia.toCharArray();
            //Generate hash
            byte[] hash_bytes = Password.hash(password, salt_bytes);
            Usuario u = new Usuario();
            u.setRol_usuario(Usuario.Tipo_Rol.REGISTRADO);
            u.setApellidos(Apellidos);
            u.setEmail(email);
            u.setNombre(Nombre);
            u.setPassword_hash(Password.bytesToHex(hash_bytes));
            u.setSal(Password.bytesToHex(salt_bytes));
            u.setPseudonimo(pseudonimo);
            u.setEmail_notifier(email_notifier);
            business.createUser(u);
        }
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registrado satisfactoriamente"));

        return "login?faces-redirect=true";
    }

    public void addMessage() {
        String summary = acepta ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(FacesMessage.FACES_MESSAGES, new FacesMessage("Debe aceptar los terminos de uso"));
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "informacion", "informacion");

        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
}
