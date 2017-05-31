package grupog.agendamlg.business;

import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Provincia;
import java.util.List;

/**
 *
 * @author Jean Paul Beaudry
 */
public interface OtherLocal {
    public List<Etiqueta> getTagsWithEvents();
    public void createTag(Etiqueta e);
    public void updateTag(Etiqueta e);
    public void deleteTag(long e);
    public void createAudience(Destinatario e);
    public void updateAudience(Destinatario e);
    public void deleteAudience(long e);
    public List<Provincia> getProvinces();
    public List<Localidad> getTowns(String prov);
    public List<Etiqueta> getTags();
    public List<Destinatario> getAudiences();
    public Destinatario getAudienceById(long audience);
    public Etiqueta getTagById(long tag);
    public Provincia getProvinciaByName(String name);
    public Localidad getLocalidadByName(String name);
    public Destinatario getDestinatarioByDescripcion(String desc);
    public Etiqueta getEtiquetaByName(String name);
}
