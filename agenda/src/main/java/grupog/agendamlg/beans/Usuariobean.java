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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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
    @EJB
    private Business business;

    @PostConstruct
    public void init() {
        byte[] salt_bytes;
        byte[] hash_bytes;
        String hash;
        String sal;
        usuarios = new ArrayList<>();
        email = "jeanpaul.beaudry@gmail.com";
        contrasenia = "potato";
        Usuario usuario = new Usuario("Susana", "LJ", "SLJ@gmail.com");
        usuario.setRol_usuario(Usuario.Tipo_Rol.REGISTRADO);
        usuario.setEmail_notifier(true);
        setPassword(usuario, contrasenia);

        Usuario usuario1 = new Usuario("Marie", "Poppo", "Poppo@gmail.com");
        usuario1.setRol_usuario(Usuario.Tipo_Rol.REDACTOR);
        usuario1.setEmail_notifier(true);
        setPassword(usuario1, contrasenia);
        

        Usuario usuario2 = new Usuario("Pepe", "Patata", "Pepe@patata.com");
        usuario2.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
        usuario2.setEmail_notifier(true);
        setPassword(usuario2, contrasenia);
        
        Usuario usuario3 = new Usuario("Jean-Paul", "Beaudry", "jeanpaul.beaudry@gmail.com");
        usuario3.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
        usuario3.setEmail_notifier(true);
        setPassword(usuario3, contrasenia);
        

        usuarios.add(usuario);
        usuarios.add(usuario1);
        usuarios.add(usuario2);
        usuarios.add(usuario3);
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
        //TODO - verify that the user exists in the database
        final StringBuilder msg = new StringBuilder();
        String new_password = Password.generateSecurePassword(16);
        msg.append("<h2>Reseteo de password</h2>");
        msg.append("<h3>A d&iacute;a y hora ")
                .append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(" ha solicitado que se resetee la constraseña de su cuenta de agendaMLG")
                .append("</h3>")
                .append("<h3><b>Su nueva contraseña es: </b>")
                .append(new_password)
                .append("</h3>")
                .append("<p>En cuanto acceda a su perfil, por motivos de seguridad resetee de nuevo su contraseña.</p><p style='font-size: 12px'>diariosur</p>");

        
        for (Usuario x : usuarios) {
            if (x.getEmail().equals(em)) {
                setPassword(x, new_password);
                Sendmail.mailThread(x.getEmail(), "Reseteo de password", msg.toString());
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
}
