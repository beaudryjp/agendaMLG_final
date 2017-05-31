package grupog.agendamlg.business;

import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Provincia;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jean Paul Beaudry
 */
@Stateless
@LocalBean
public class BusinessOther implements OtherLocal {

    @PersistenceContext(unitName = "AgendaMLG-PU")
    private EntityManager em;

    @Override
    public List<Etiqueta> getTagsWithEvents() {
        List<Etiqueta> tags = new ArrayList<>();
        for (Etiqueta x : getTags()) {
            if (x.getEvento().size() > 0) {
            //if (getEventsByTag(x.getId_etiqueta()).size() > 0) {
                tags.add(x);
            }
        }

        return tags;
    }
    
    @Override
    public void createTag(Etiqueta e) {
        em.persist(e);
    }
    
        

    @Override
    public void updateTag(Etiqueta e
    ) {

        em.merge(e);

    }

    @Override
    public void deleteTag(long e
    ) {

        Etiqueta et = em.find(Etiqueta.class, e);
        em.remove(em.merge(et));
    }

    @Override
    public void createAudience(Destinatario e
    ) {
        em.persist(e);

    }

    @Override
    public void updateAudience(Destinatario e
    ) {

        em.merge(e);

    }

    @Override
    public void deleteAudience(long e
    ) {

        Destinatario d = em.find(Destinatario.class, e);
        em.remove(em.merge(d));
    }
    
        @Override
    public List<Provincia> getProvinces() {
        TypedQuery<Provincia> query = em.createNamedQuery("getAllProvinces", Provincia.class);
        return query.getResultList();
    }

    @Override
    public List<Localidad> getTowns(String prov
    ) {

        String a;
        List<Localidad> l = new ArrayList<>();
        for (Provincia p : getProvinces()) {
            if (p.getNombre().equals(prov)) {
                a = p.getLocalidades().get(0).getNombre();
                l = p.getLocalidades();
            }
        }

        return l;
    }

    @Override
    public List<Destinatario> getAudiences() {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAllAudience", Destinatario.class);
        return query.getResultList();
    }
    
    @Override
    public Destinatario getAudienceById(long audience
    ) {
        Destinatario d = em.find(Destinatario.class, audience);
        return d;
    }

    @Override
    public Etiqueta getTagById(long tag
    ) {
        Etiqueta et = em.find(Etiqueta.class, tag);
        return et;
    }

    @Override
    public List<Etiqueta> getTags() {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getAllTags", Etiqueta.class);
        return query.getResultList();
    }

    @Override
    public Provincia getProvinciaByName(String name
    ) {
        TypedQuery<Provincia> query = em.createNamedQuery("getProvinciaByName", Provincia.class)
                .setParameter("nombre", name);
        return query.getResultList().get(0);
    }

    @Override
    public Localidad getLocalidadByName(String name
    ) {
        TypedQuery<Localidad> query = em.createNamedQuery("getLocalidadByName", Localidad.class)
                .setParameter("nombre", name);
        return query.getResultList().get(0);
    }

    @Override
    public Destinatario getDestinatarioByDescripcion(String desc
    ) {
        TypedQuery<Destinatario> query = em.createNamedQuery("getAudienceByDescription", Destinatario.class)
                .setParameter("descripcion", desc);
        return query.getResultList().get(0);
    }

    @Override
    public Etiqueta getEtiquetaByName(String name
    ) {
        TypedQuery<Etiqueta> query = em.createNamedQuery("getTagByName", Etiqueta.class)
                .setParameter("nombre", name);
        return query.getResultList().get(0);
    }


}
