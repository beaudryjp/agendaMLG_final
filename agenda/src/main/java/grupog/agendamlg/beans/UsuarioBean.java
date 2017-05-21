/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Password;
import grupog.agendamlg.general.Sendmail;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Susana
 */
@Named(value = "usuario")
@SessionScoped
public class UsuarioBean implements Serializable {

    private String email;
    private String email2;
    private String contrasenia;
    
    private String emailLogueado;
    private String nombreLogueado;
    private String contraseniaLogueado;
    private String contrasenia2Logueado;

    @Inject
    private ControlLog ctrl;
    @EJB
    private Business business;

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmailLogueado() {
        return emailLogueado;
    }

    public void setEmailLogueado(String emailLogueado) {
        this.emailLogueado = emailLogueado;
    }

    public String getNombreLogueado() {
        return nombreLogueado;
    }

    public void setNombreLogueado(String nombreLogueado) {
        this.nombreLogueado = nombreLogueado;
    }

    public String getContraseniaLogueado() {
        return contraseniaLogueado;
    }

    public void setContraseniaLogueado(String contraseniaLogueado) {
        this.contraseniaLogueado = contraseniaLogueado;
    }

    public String getContrasenia2Logueado() {
        return contrasenia2Logueado;
    }

    public void setContrasenia2Logueado(String contrasenia2Logueado) {
        this.contrasenia2Logueado = contrasenia2Logueado;
    }
    
    public void setControl(ControlLog con){
        ctrl = con;
    }
    

    public String autenticar() {
        char[] password;
        byte[] hash;
        byte[] sal;
        byte[] expected_hash;
        String authentication_result_site = "login?faces-redirect=true";
        for (Usuario index_user : business.getUsers()) {
            if (index_user.getEmail().equals(email)) {
                //Convert string password to char[]
                password = contrasenia.toCharArray();
                //Get users salt(string) and convert it to byte[]
                sal = Password.hexStringToByteArray(index_user.getSal());
                //Get users hash(string) and convert it to byte[]
                hash = Password.hexStringToByteArray(index_user.getPassword_hash());
                //Generate the expected hash depending on the password passed and the user's salt
                expected_hash = Password.hash(password, sal);

                //Check if they are same hashed password
                if (Arrays.equals(hash, expected_hash)) {
                    ctrl.setUsuario(index_user);
                    authentication_result_site = ctrl.home();
                }
                else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario o contraseña incorrecto."));
                }
                
            }
        }

        return authentication_result_site;
    }

    public String resetPassword(Usuario u) {
        //TODO - verify that the user exists in the database
        final StringBuilder msg = new StringBuilder();
        String new_password = Password.generateSecurePassword(16);
        msg.append("<h2>Reseteo de password</h2>");
        msg.append("<h3>A d&iacute;a y hora ")
                .append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(" ha solicitado que se resetee la contraseña de su cuenta de agendaMLG")
                .append("</h3>")
                .append("<h3><b>Su nueva contraseña es: </b>")
                .append(new_password)
                .append("</h3>")
                .append("<p>En cuanto acceda a su perfil, por motivos de seguridad resetee de nuevo su contraseña.</p><p style='font-size: 12px'>diariosur</p>");
        
        setPassword(u, new_password);
        Sendmail.mailThread(u.getEmail(), "Reseteo de password", msg.toString());

        return "index?faces-redirect=true";
    }

    private void setPassword(Usuario usuario, String password){
        //Generate a salt
        byte[] salt_bytes = Password.getNextSalt();
        //Convert salt to hexadecimal and set it for the corresponding user
        usuario.setSal(Password.bytesToHex(salt_bytes));
        //Generate a hash from the password and the salt
        byte[] hash_bytes = Password.hash(password.toCharArray(), salt_bytes);
        //Convert hash to hexadecimal and set it for the corresponding user
        usuario.setPassword_hash(Password.bytesToHex(hash_bytes));
    }
    public String validate() {
        if(ctrl.getUsuario() == null){
            List<Usuario> u = business.getUserByEmail(email);
            if(!u.isEmpty()){
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo enviado a:", email);

                RequestContext.getCurrentInstance().showMessageInDialog(message);
                return resetPassword(u.get(0));
            }
            
        }
        return "index?faces-redirect=true";
    }
    
    public void modificar (){
        boolean modificado = false;
        if(!emailLogueado.isEmpty() ){
            List<Usuario> us = business.getUserByEmail(emailLogueado);
            if(us.isEmpty()){
            ctrl.getUsuario().setEmail(emailLogueado);
            modificado = true;
            }
        } 
        if (!nombreLogueado.isEmpty()){
            ctrl.getUsuario().setNombre(nombreLogueado);
            modificado = true;
        }
        if(!contraseniaLogueado.isEmpty() && !contrasenia2Logueado.isEmpty() && contraseniaLogueado.equals(contrasenia2Logueado)){
            //Generate salt
            byte[] salt_bytes = Password.getNextSalt();
            //Convert password to char[]
            char[] password = contraseniaLogueado.toCharArray();
            //Generate hash
            byte[] hash_bytes = Password.hash(password, salt_bytes);
            ctrl.getUsuario().setPassword_hash(Password.bytesToHex(hash_bytes));
            ctrl.getUsuario().setSal(Password.bytesToHex(salt_bytes));
            modificado = true;
        }
        if(modificado){
            business.updateUser(ctrl.getUsuario());
        }
    } 
}
