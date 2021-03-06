/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.business.BusinessUser;
import grupog.agendamlg.entities.*;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author James
 */

@ViewScoped
@ManagedBean(name = "notification")
public class NotificationBean implements Serializable {

    List<Notificacion> notificaciones;
    @EJB
    private BusinessUser business;
    @Inject
    private ControlLog usuario;

    public NotificationBean() {
    }

    public void dissapear(Notificacion not) {
        business.deleteNotificacion(not.getId_notificacion());
    }

    public List<Notificacion> getNotificaciones() {
        notificaciones = business.getNotifications(usuario.getUsuario().getId_usuario());
        return notificaciones;
    }
}
