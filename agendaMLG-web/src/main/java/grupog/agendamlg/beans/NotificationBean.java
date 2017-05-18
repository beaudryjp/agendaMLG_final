/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.entities.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author James
 */

@ViewScoped
@ManagedBean(name = "notification")
public class NotificationBean implements Serializable {

    List<Notificacion> notificaciones;

    public NotificationBean() {
    }

    @PostConstruct
    public void init() {
        notificaciones = new ArrayList<>();

        Notificacion not_0 = new Notificacion();
        not_0.setId_notificacion(0L);
        not_0.setMensaje("El evento ha cambiado.");
        not_0.setFecha_hora(LocalDateTime.now());
        
    
        
        Notificacion not_2 = new Notificacion();
        not_2.setId_notificacion(1L);
        not_2.setMensaje("El evento ha sido cancelado de nuevo.");
        not_2.setFecha_hora(LocalDateTime.now());

        notificaciones.add(not_0);
    
        notificaciones.add(not_2);
        
        //System.out.println(notificaciones.get(1).getMensaje());
    }

    public void dissapear(Notificacion not) {
        notificaciones.remove(not);
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

}
