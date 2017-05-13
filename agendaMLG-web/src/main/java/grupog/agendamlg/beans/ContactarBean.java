package grupog.agendamlg.beans;

import grupog.agendamlg.general.Sendmail;
import org.joda.time.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.internet.AddressException;

/**
 *
 * @author Jean Paul Beaudry
 */
@Named(value = "contactarBean")
@RequestScoped
public class ContactarBean {
    /*
    private String nombre = "Jean-Paul";
    private String apellidos = "Beaudry";
    private String email = "jeanpaul.beaudry@outlook.com";
    private String mensaje = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vel ultricies nunc. "
            + "Maecenas lacinia porta posuere. Fusce vel ullamcorper odio, a iaculis turpis. Pellentesque viverra porttitor congue. "
            + "Nam sed congue quam, in efficitur diam. Sed ac ex elementum, imperdiet ex sit amet, ornare mauris. "
            + "Donec accumsan, lacus sed ultrices dignissim, urna orci mollis lectus, nec consectetur justo sem nec leo.\n";
    */
    private String nombre;
    private String apellidos;
    private String email;
    private String mensaje;
    /**
     * Creates a new instance of ContactarBean
     */
    public ContactarBean() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "ContactarBean{" + "nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email + ", mensaje=" + mensaje + '}';
    }

    public String submit() {
        LocalTime localTime = new LocalTime();
        LocalDate localDate = new LocalDate();
        final StringBuilder msg = new StringBuilder();
        msg.append("<h2>Formulario de contacto</h2>");
        msg.append("<h3>Se ha enviado un nuevo mensaje el dia ")
                .append(localDate.toString())
                .append(" a la hora ")
                .append(localTime.toString())
                .append(": </h3>");
        msg.append("<h3><b>Nombre completo: </b></h3>")
                .append(this.nombre).append(" ")
                .append(this.apellidos);
        msg.append("<h3><b>Email: </b></h3>")
                .append(this.email);
        msg.append("<h3><b>Mensaje: </b></h3>")
                .append("<p>")
                .append(this.mensaje)
                .append("</p>");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ContactarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Sendmail.mail(Sendmail.username, "Formulario de contacto", msg.toString());
                } catch (AddressException ex) {
                    Logger.getLogger(ContactarBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        return "index?faces-redirect=true";
    }
}
