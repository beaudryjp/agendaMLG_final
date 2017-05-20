package grupog.agendamlg.beans;

import grupog.agendamlg.general.Sendmail;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Jean Paul Beaudry
 */
@Named(value = "contactarBean")
@RequestScoped
public class ContactarBean {
    private String nombre;
    private String apellidos;
    private String email;
    private String mensaje;

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
        final StringBuilder msg = new StringBuilder();
        msg.append("<h2>Formulario de contacto</h2>");
        msg.append("<h3>Se ha enviado un nuevo mensaje el dia ")
                .append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(": </h3>");
        msg.append("<h3><b>Nombre completo: </b></h3>")
                .append(StringEscapeUtils.escapeHtml4(this.nombre)).append(" ")
                .append(StringEscapeUtils.escapeHtml4(this.apellidos));
        msg.append("<h3><b>Email: </b></h3>")
                .append(this.email);
        msg.append("<h3><b>Mensaje: </b></h3>")
                .append("<p>")
                .append(StringEscapeUtils.escapeHtml4(this.mensaje))
                .append("</p>");
        Sendmail.mailThread(Sendmail.username, "Formulario de contacto", msg.toString());
        return "index?faces-redirect=true";
    }
}
