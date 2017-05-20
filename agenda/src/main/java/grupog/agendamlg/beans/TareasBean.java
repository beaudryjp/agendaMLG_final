/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Evento;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Susana
 */
@ViewScoped
@ManagedBean(name = "tareas")
public class TareasBean implements Serializable {

    private List<Evento> pendiente;
    @EJB
    private Business business;

    public TareasBean() {
    }

    public List<Evento> getPendiente() {
        return pendiente;
    }

    public void setPendiente(List<Evento> pendiente) {
        this.pendiente = pendiente;
    }
    /* optional
    public void approve(Evento e) {
        
        pendiente.remove(e);
    }
    
    public void reject(Evento e) {
        pendiente.remove(e);
    }
    */
}
