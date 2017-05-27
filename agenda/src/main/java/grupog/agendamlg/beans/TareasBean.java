
package grupog.agendamlg.beans;

import grupog.agendamlg.business.Business;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Tarea;
import grupog.agendamlg.entities.Usuario;
import grupog.agendamlg.general.Redirect;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Susana
 */
@ViewScoped
@ManagedBean(name = "tareas")
public class TareasBean implements Serializable {

    private List<Tarea> pendiente;
    @EJB
    private Business business;
    @Inject
    private ControlLog control;

    public List<Tarea> getPendiente() {
        return business.getTasks(control.getUsuario());
    }

    public void setPendiente(List<Tarea> pendiente) {
        this.pendiente = pendiente;
    }
    
    public void createTaskUser(){
        Tarea newTask = new Tarea();
        newTask.setFecha_hora(LocalDateTime.now());
        newTask.setCreador_peticion(control.getUsuario());
        newTask.setNombre("Solicito validación");
        newTask.setMensaje(control.getUsuario().getPseudonimo()+" ha solicitado ser usuario");
        newTask.setRedactores(business.getRedactores());
        business.createTask(newTask);
        Redirect.redirectToProfile();
    }
    
    public void createTaskEvent(){
         Tarea newTask = new Tarea();
    }

    
    public void approve(Tarea t) {
        
        if(t.getId_evento()==null)//Evento
        {
            Usuario usi = t.getCreador_peticion();
            usi.setRol_usuario(Usuario.Tipo_Rol.VALIDADO);
            business.updateUser(usi);
        }else{
            
        }
        business.deleteTask(t.getId_tarea());
    }
    
    public void reject(Tarea t) {
        business.deleteTask(t.getId_tarea());
    }
    
    public boolean validado(){
        boolean b = false; 
        List<Tarea> peticiones = business.getPeticiones(control.getUsuario().getId_usuario());
        for(Tarea t:peticiones){
            if(t.getId_evento()==null){
               b=true;
               break;
            }
        }
        return b;
    }
}
