/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Password;
import grupog.agendamlg.general.Sendmail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.AddressException;

/**
 *
 * @author Susana
 */
@Named(value = "login")
@SessionScoped
public class Usuariobean implements Serializable {

    private String email;
    private String contrasenia;
    private List<Usuario> usuarios;

    @Inject
    private ControlLog ctrl;
    @Inject
    private ConfigurationBean conf;

    @PostConstruct
    public void init() {
        byte[] salt_bytes;
        byte[] hash_bytes;
        String hash;
        String sal;
        usuarios = new ArrayList<>();
        email = "SLJ@gmail.com";
        contrasenia = "potato";
        Usuario usuario = new Usuario("Susana", "LJ", "SLJ@gmail.com");
        usuario.setRol_usuario(Usuario.Tipo_Rol.REGISTRADO);
        usuario.setEmail_notifier(true);
        
        //Generate a salt
        salt_bytes = Password.getNextSalt();
        //Convert salt to hexadecimal and set it for the corresponding user
        usuario.setSal(Password.bytesToHex(salt_bytes));
        //Generate a hash from the password and the salt
        hash_bytes = Password.hash(contrasenia.toCharArray(), salt_bytes);
        //Convert hash to hexadecimal and set it for the corresponding user
        usuario.setPassword_hash(Password.bytesToHex(hash_bytes));

        Usuario usuario1 = new Usuario("Marie", "Poppo", "Poppo@gmail.com");
        usuario1.setRol_usuario(Usuario.Tipo_Rol.REDACTOR);
        usuario1.setEmail_notifier(true);
        
        salt_bytes = Password.getNextSalt();
        usuario1.setSal(Password.bytesToHex(salt_bytes));
        hash_bytes = Password.hash(contrasenia.toCharArray(), salt_bytes);
        usuario1.setPassword_hash(Password.bytesToHex(hash_bytes));
        

        Usuario usuario2 = new Usuario("Pepe", "Patata", "Pepe@patata.com");
        usuario2.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
        usuario2.setEmail_notifier(true);
        
        salt_bytes = Password.getNextSalt();
        usuario2.setSal(Password.bytesToHex(salt_bytes));
        hash_bytes = Password.hash(contrasenia.toCharArray(), salt_bytes);
        usuario2.setPassword_hash(Password.bytesToHex(hash_bytes));
        

        usuarios.add(usuario);
        usuarios.add(usuario1);
        usuarios.add(usuario2);

    }

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

    public String autenticar() {
        char[] password;
        byte[] hash;
        byte[] sal;
        byte[] expected_hash;
        String authentication_result_site = "login?faces-redirect=true";
        for (Usuario index_user : usuarios) {
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
                break;
            }
        }

        return authentication_result_site;
    }

    public String resetPassword(String em) {
        String password = "123456";
        String message = "Se ha reseteado la contraseña a " + password
                + "\n\nCambie la contraseña en cuanto sea posible. Fuck you";
        /*
        try {
            Sendmail.mail("", "Reseteo de la contraseña", message);
        } catch (AddressException ex) {
            Logger.getLogger(Usuariobean.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        for (Usuario x : usuarios) {
            if (x.getEmail().equals(em)) {
                x.setPassword_hash(password);
                try {
                    Sendmail.mail(x.getEmail(), "Reseteo de la contraseña", message);
                } catch (AddressException ex) {
                    Logger.getLogger(Usuariobean.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }

        }

        return "index?faces-redirect=true";
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
