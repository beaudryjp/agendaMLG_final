package grupog.agendamlg.beans;

import grupog.agendamlg.general.DateUtils;
import grupog.agendamlg.entities.Comentario;
import grupog.agendamlg.entities.Destinatario;
import grupog.agendamlg.entities.Etiqueta;
import grupog.agendamlg.entities.Evento;
import grupog.agendamlg.entities.Localidad;
import grupog.agendamlg.entities.Provincia;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

/**
 *
 * @author Jean Paul Beaudry
 */
@ManagedBean
@SessionScoped
public class EventoBean implements Serializable {

    /**
     * Creates a new instance of EventoBean
     */
    @Inject
    private ControlLog current_user;
    @Inject
    private Usuariobean usuario;
    private TagCloudModel model;
    private List<Evento> eventos;
    private List<Evento> evento_asiste;
    private List<Evento> evento_gusta;
    private List<Evento> evento_sigue;
    private List<Etiqueta> etiquetas;
    private List<Destinatario> destinatarios;
    private List<Destinatario> publico_evento;
    private List<Destinatario> publico_evento2;
    private List<Destinatario> publico_evento3;
    private List<Destinatario> publico_evento4;
    private List<Comentario> comentarios;
    private int evento;
    private LocalDate fecha;
    private String searchProvincia;
    private String searchLocalidad;
    private String searchDestinatario;
    private String searchEtiqueta;
    private String searchText;
    private String tag;
    private String evento_info = "0";
    private String event_type = "0";
    private UploadedFile uploadedPicture;

    public EventoBean() {
    }

    @PostConstruct
    public void init() {
        List<Etiqueta> e = new ArrayList<>();
        eventos = new ArrayList<>();
        etiquetas = new ArrayList<>();
        destinatarios = new ArrayList<>();
        publico_evento = new ArrayList<>();
        publico_evento2 = new ArrayList<>();
        publico_evento3 = new ArrayList<>();
        publico_evento4 = new ArrayList<>();
        comentarios = new ArrayList<>();
        model = new DefaultTagCloudModel();

        model.addTag(new DefaultTagCloudItem("Actos académicos", "event_tags.xhtml?tag=Actos académicos", 3));
        model.addTag(new DefaultTagCloudItem("Conciertos", "event_tags.xhtml?tag=Conciertos", 2));
        model.addTag(new DefaultTagCloudItem("Espectáculos", "event_tags.xhtml?tag=Espectáculos", 4));
        model.addTag(new DefaultTagCloudItem("Cineclub", "event_tags.xhtml?tag=Cineclub", 2));
        model.addTag(new DefaultTagCloudItem("Exposiciones", "event_tags.xhtml?tag=Exposiciones", 4));
        model.addTag(new DefaultTagCloudItem("Conferencias", "event_tags.xhtml?tag=Conferencias", 2));
        model.addTag(new DefaultTagCloudItem("Libros", "event_tags.xhtml?tag=Libros", 4));
        model.addTag(new DefaultTagCloudItem("Varios", "event_tags.xhtml?tag=Varios", 2));
        model.addTag(new DefaultTagCloudItem("Festivales", "event_tags.xhtml?tag=Festivales", 1));

        Provincia provincia = new Provincia();
        provincia.setNombre("Málaga");

        Localidad localidad = new Localidad("Málaga", provincia);
        Localidad localidad2 = new Localidad("Benalmádena", provincia);
        Localidad localidad3 = new Localidad("Alhaurín de la Torre", provincia);
        Localidad localidad4 = new Localidad("Torremolinos", provincia);
        Localidad localidad5 = new Localidad("Rincón de la Victoria", provincia);
        Localidad localidad6 = new Localidad("Nerja", provincia);

        eventos.add(new Evento("Murakami", "Presentación del autor y su último libro: Kishi dancho koroshi."
                + "Haruki Murakami (Kioto, 1949) es uno de los pocos autores japoneses que han dado el salto de escritor de "
                + "prestigio a autor con grandes ventas en todo el mundo. Ha recibido numerosos premios, entre ellos "
                + "el Noma, el Tanizaki, el Yomiuri, el Franz Kafka o el Jerusalem Prize, "
                + "y su nombre suena reiteradamente como candidato al Nobel de Literatura. "
                + "En España, ha merecido el Premio Arcebispo Juan de San Clemente, la Orden de las Artes y las Letras, concedida por el Gobierno español, "
                + "y el Premi Internacional Catalunya 2011. Tusquets Editores ha publicado dieciocho de sus obras: doce novelas —entre ellas la aclamada Tokio blues. "
                + "Norwegian Wood y Los años de peregrinación del chico sin color—, y las personalísimas obras De qué hablo cuando hablo de correr, Underground, "
                + "y De qué hablo cuando hablo de escribir, así como cuatro volúmenes de relatos: Sauce ciego, mujer dormida, "
                + "Después del terremoto, Hombres sin mujeres y El elefante desaparece.",
                DateUtils.asDate(LocalDate.of(2017, Month.JUNE, 1)), DateUtils.asDate(LocalDate.of(2017, Month.JUNE, 3)), "10:00 - 11:00", "gratis", 36.7203713, -4.4248549, true, localidad));
        eventos.add(new Evento("El Bosco: Vida y Obra", "La exposición que conmemora el V centenario de la muerte del Bosco, una ocasión irrepetible "
                + "para disfrutar del  extraordinario grupo de las ocho pinturas de su mano que se conservan en España junto a excelentes obras procedentes "
                + "de colecciones y museos de todo el mundo. Se trata del repertorio más completo del Bosco, uno de los artistas más enigmáticos e influyentes "
                + "del Renacimiento, que invita al público a adentrarse en su personal visión del mundo a través de un montaje expositivo espectacular "
                + "que presenta exentos sus trípticos más relevantes para que se puedan contemplar tanto el anverso como el reverso.",
                DateUtils.asDate(LocalDate.of(2017, Month.JUNE, 2)), DateUtils.asDate(LocalDate.of(2017, Month.AUGUST, 2)), "10:00 - 22:00", "5€", 36.7135979, -4.42488000000003, true, localidad));
        eventos.add(new Evento("Concierto: Reflets Trio", "Podremos disfrutar de un concierto de flauta travesera, viola y arpa, a cargo de Reflets Trio. "
                + "Vivimos en una sociedad en la que escuchar, respetar y compartir son las bases para su funcionamiento. "
                + "La música de cámara es la representación artística de los pilares de dicha sociedad. Su función es inculcar los valores que nos unen y hacen humanos. "
                + "Tanto para el que es músico como para el que no, la música de cámara muestra un grupo de personas que se unen en tomar una decisión conjunta por un bien. "
                + "En este festival se mostrará en los más altos niveles de ejecución artística. El público malagueño será obsequiado con una "
                + "música e interpretación que harán crecer su sabiduría artística y musical. Les será transmitidos unos principios artísticos que serán el presente "
                + "y futuro de la creación cultural.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 28)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 28)), "19:00 - 20:00", "gratis", 36.7268091, -4.4261242, false, localidad));
        eventos.add(new Evento("Encuentros con el Autor. Andrés Neuman", "Uno de los escritores más interesantes y con mayor proyección de las "
                + "letras españolas visita la Biblioteca Pública arroyo de la Miel para charlar y compartir impresiones sobre sus obras. Neuman poeta\n\n"
                + "Ante la pregunta ¿quién es Neuman poeta? dijo: Redescubrir el asombro de lo elemental, subrayar una palabra reparando en su extrañeza es “ser poeta”. "
                + "Pero confiesa tener el apetito y el vicio de probar todos los moldes, lenguajes, estilos. "
                + "Hay que morirse habiendo intentado un aforismo, una novela, un poema, un haiku, etcétera.\n\n\n\n"
                + "Y de la misma forma ve las dos caras del oficio: “La palabra atornilla al asiento y también mueve por dentro, significa hacer un viaje interior”.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 30)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 30)), "20:00 - 22:00", "10€", 36.594771, -4.5320213, false, localidad2));
        eventos.add(new Evento("Taller de pintura de paisaje al aire libre", "Taller de pintura de paisaje al aire libre."
                + "El Ayuntamiento de Alhaurín de la Torre, a través del Área Sociocultural y Servicios, "
                + "informa que está abierto el plazo de inscripción para el taller de “Pintura de Paisaje al Aire Libre” que se impartirá "
                + "en el Centro Cultural Padre Manuel el próximo sábado 3 de mayo, de 10:00 a 14:00 horas, organizado por el "
                + "Centro de Cultura Contemporánea de la Diputación de Málaga, La Térmica, en colaboración con la delegación municipal de Cultura.\n\n"
                + "El taller será impartido por “art&museum”, una empresa dedicada a la comunicación y difusión del arte a la sociedad, "
                + "y estará dirigido por Cristina Céspedes y Marta Bustos, ambas licenciadas en Bellas Artes y artistas plásticas, "
                + "quienes aportarán todo el material necesario para el desarrollo del taller, incluido caballetes, óleos, pinceles, espátulas…\n\n"
                + "El objetivo de este curso de pintura rápida es saber mirar, más que pintar; enfrentarse a las dificultades que presenta el reto de pintar al natural, "
                + "ya que la naturaleza o el paisaje sin encuadrar, sin límites claros o bordes establecidos, dependerá de la observación y de las decisiones que tome cada persona, "
                + "para apropiarse de una porción de paisaje. \n\n"
                + "Además de disfrutar de una mañana de pintura al aire libre, los participantes aprenderán a montar su propio soporte, "
                + "a encuadrar, a usar las primeras manchas, a dar color, a crear detalles, a aplicar luces y dar profundidad, disfrutando de un maravilloso día al aire libre. \n\n"
                + "El precio de esta actividad es de 10 euros, más 3 euros para materiales. \n\n"
                + "Las personas interesadas pueden realizar la inscripción llamando al número de teléfono 635 823 692, "
                + "en el correo info@artmuseum.es ó a través de la web de La Térmica: www.latermicamalaga.com",
                DateUtils.asDate(LocalDate.of(2017, Month.JUNE, 3)), DateUtils.asDate(LocalDate.of(2017, Month.JUNE, 6)), "10:00 - 14:00", "13€", 36.661575, -4.5715127, false, localidad3));
        eventos.add(new Evento("El otro Bigas Luna: La seducción de lo tangible", " 'El otro Bigas Luna' ofrece por vez primera un recorrido multidisciplinar "
                + "por más de un centenar de obras que completan de una manera global la figura de este enigmático Artista y Cineasta. "
                + "La muestra ofrece la trayectoria vital de Bigas Luna a partir de su imaginario genuino, el cual cultivó en paralelo a su creación "
                + "cinematográfica y que ha permanecido en el territorio de lo íntimo hasta ahora. Una obra prácticamente inédita para la mayoría "
                + "y que junto al libro que la acompaña van a revelar al gran público ese 'otro' Bigas Luna que sin duda resulta imprescindible "
                + "para entender el controvertido y apasionante personaje en toda su dimensión vital y creativa. "
                + "Málaga se convierte en el punto de partida de esta exposición itinerante que mostrará al 'otro Bigas Luna'.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 5)), DateUtils.asDate(LocalDate.of(2017, Month.JUNE, 5)), "10:00 - 14:00 y 17:00 - 20:00", "gratis", 36.7211581, -4.4147584, false, localidad));
        eventos.add(new Evento("Malaca y su pasado romano", "Sábado 6. Taller Pequeños Musivarios. Realizados a base de piezas cúbicas denominadas teselas, "
                + "los mosaicos eran uno de los elementos arquitectónicos más bellos y representativos del mundo romano. "
                + "Con este taller nos atreveremos a hacer uno de ellos con temática geométrica que hará las delicias de los más pequeños. "
                + "Sábado 13. Taller Policromía Romana. Aunque han llegado hasta nuestros días del color de la piedra o el metal en el que estaban hechas, "
                + "la mayoría de las estatuas romanas estaban pintadas con vivos colores cuidadosamente elegidos por los artesanos que las hicieron y decoraron. "
                + "Cómo los pintaban y qué colores elegían lo sabremos en el taller del día 8 de abril. Sábado 20. Pintando Frescos Romanos. "
                + "En la Antigua Roma, las paredes de las distintas estancias de las casas eran decoradas con frescos o pinturas que representaban variados temas. "
                + "En este taller realizaremos un fresco que podría decorar una de las villas más suntuosas. Sábado 27. Juegos en Roma. "
                + "Muchos de los juegos de mesa a los que estamos acostumbrados a jugar en la actualidad se crearon en la antigua Roma. "
                + "En este divertido taller los descubriremos y aprenderemos a jugar como los niños romanos lo hacían hace 2.000 años.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 22)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 29)), "11:00 - 12:30 y 12:30 - 14:00", "8€", 36.7211581, -4.4147584, true, localidad));
        eventos.add(new Evento("CUENTACUENTO EL MAGO DE OZ", "El MIMMA, junto con el grupo 'Donde viven los cuentos', "
                + "presentan esta adaptación musical basada en el primer de los libros de Oz, del escritor estadounidense Lyman Frank Baum. "
                + "Una de las historias más célebres y fascinantes de la literatura infantil de todos los tiempos, narrada a través de sus protagonistas.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 29)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 29)), "12:00 - 14:00", "5€", 36.7233145, -4.421965, false, localidad));
        eventos.add(new Evento("Arte contemporáneo para niños", "Queremos acercar de manera lúdica a los más pequeños el gusto por el arte actual con "
                + "ejemplos prácticos. Los artistas analizados serán Evrnesto Neto(Brasil), Annete Messager (Francia), Takashi Murakami (Japón), "
                + "RomeroBritto (Brasil) - duración: una sesión de 3h",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 13)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 13)), "13:30 - 16:30", "5€", 36.6285609, -4.4999552, false, localidad4));
        eventos.add(new Evento("Cultura Urbana Hip Hop", "¿Te interesa el hip-hop y quieres saber más? "
                + "¿Sientes que la cultura urbana te rodea pero no acabas de entenderla ni disfrutarla por completo? "
                + "Descubre una nueva manera de mirar el arte y la vida, pues las calles por las que te mueves suelen tener tanto interés como el mejor museo.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 9)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 9)), "17:00 - 22:00", "5€", 36.7227918, -4.2866072, false, localidad5));
        eventos.add(new Evento("Robotix", "ROBOTIX es una actividad innovadora para desarrollar las habilidades y competencias del siglo XXI, "
                + "utilizando como plataforma la robótica de LEGO Education. Con más de 10 años de experiencia, LEGO Education ROBOTIX "
                + "ofrece los mejores recursos educativos para crear experiencias de aprendizaje únicas con las que nuestros jóvenes "
                + "desarrollarán las habilidades y competencias del S.XXI.",
                DateUtils.asDate(LocalDate.of(2017, Month.MAY, 20)), DateUtils.asDate(LocalDate.of(2017, Month.MAY, 20)), "10:00 - 13:00", "gratis", 36.746682, -3.8804524, false, localidad6));

        etiquetas.add(new Etiqueta("Actos académicos"));
        etiquetas.add(new Etiqueta("Conciertos"));
        etiquetas.add(new Etiqueta("Espectáculos"));
        etiquetas.add(new Etiqueta("Cineclub"));
        etiquetas.add(new Etiqueta("Exposiciones"));
        etiquetas.add(new Etiqueta("Conferencias"));
        etiquetas.add(new Etiqueta("Libros"));
        etiquetas.add(new Etiqueta("Varios"));
        etiquetas.add(new Etiqueta("Festivales"));
        etiquetas.add(new Etiqueta("Teatro"));
        etiquetas.add(new Etiqueta("Todos"));

        destinatarios.add(new Destinatario("Ancianos"));
        destinatarios.add(new Destinatario("Niños"));
        destinatarios.add(new Destinatario("Todos"));
        destinatarios.add(new Destinatario("Adultos"));
        destinatarios.add(new Destinatario("Jóvenes"));

        eventos.get(0).setImagen_url("img/eventos/murakami2.png");
        eventos.get(1).setImagen_url("img/eventos/elbosco.jpg");
        eventos.get(2).setImagen_url("img/eventos/concierto2.jpg");
        eventos.get(3).setImagen_url("img/eventos/encuentros2.jpg");
        eventos.get(4).setImagen_url("img/eventos/pintura.jpg");
        eventos.get(5).setImagen_url("img/eventos/bigas_luna.jpg");
        eventos.get(6).setImagen_url("img/eventos/malaca.jpg");
        eventos.get(7).setImagen_url("img/eventos/cuentacuentos2.jpg");
        eventos.get(8).setImagen_url("img/eventos/arteninios2.png");
        eventos.get(9).setImagen_url("img/eventos/hiphop.jpg");
        eventos.get(10).setImagen_url("img/eventos/robotix2.png");

        eventos.get(0).setId_evento(0L);
        eventos.get(1).setId_evento(1L);
        eventos.get(2).setId_evento(2L);
        eventos.get(3).setId_evento(3L);
        eventos.get(4).setId_evento(4L);
        eventos.get(5).setId_evento(5L);
        eventos.get(6).setId_evento(6L);
        eventos.get(7).setId_evento(7L);
        eventos.get(8).setId_evento(8L);
        eventos.get(9).setId_evento(9L);
        eventos.get(10).setId_evento(10L);

        publico_evento.add(destinatarios.get(3));
        publico_evento.add(destinatarios.get(4));
        publico_evento2.add(destinatarios.get(1));
        publico_evento2.add(destinatarios.get(4));
        publico_evento3.add(destinatarios.get(0));
        publico_evento3.add(destinatarios.get(3));
        publico_evento4.add(destinatarios.get(2));
        eventos.get(0).setDestinatario(publico_evento);
        eventos.get(1).setDestinatario(publico_evento4);
        eventos.get(2).setDestinatario(publico_evento4);
        eventos.get(3).setDestinatario(publico_evento);
        eventos.get(4).setDestinatario(publico_evento3);
        eventos.get(5).setDestinatario(publico_evento);
        eventos.get(6).setDestinatario(publico_evento4);
        eventos.get(7).setDestinatario(publico_evento2);
        eventos.get(8).setDestinatario(publico_evento2);
        eventos.get(9).setDestinatario(publico_evento2);
        eventos.get(10).setDestinatario(publico_evento2);

        e.add(etiquetas.get(5));
        e.add(etiquetas.get(6));
        e.add(etiquetas.get(10));
        eventos.get(0).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(4));
        e.add(etiquetas.get(10));
        eventos.get(1).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(1));
        e.add(etiquetas.get(2));
        e.add(etiquetas.get(10));
        eventos.get(2).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(5));
        e.add(etiquetas.get(6));
        e.add(etiquetas.get(10));
        eventos.get(3).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(4));
        e.add(etiquetas.get(10));
        eventos.get(4).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(5));
        e.add(etiquetas.get(6));
        e.add(etiquetas.get(10));
        eventos.get(5).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(4));
        e.add(etiquetas.get(10));
        eventos.get(6).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(6));
        e.add(etiquetas.get(7));
        e.add(etiquetas.get(10));
        eventos.get(7).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(7));
        e.add(etiquetas.get(10));
        eventos.get(8).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(2));
        e.add(etiquetas.get(10));
        eventos.get(9).setEtiqueta(e);

        e = new ArrayList<>();
        e.add(etiquetas.get(7));
        e.add(etiquetas.get(10));
        eventos.get(10).setEtiqueta(e);

        Comentario c1 = new Comentario();
//        Usuario usuario = new Usuario();
//        usuario.setNombre("Susana");
//        usuario.setApellidos("LJ");
//        usuario.setEmail("SLJ@gmail.com");
//        usuario.setRol_usuario(Usuario.Tipo_Rol.REGISTRADO);
//        usuario.setPassword_hash("potato");
//        usuario.setPseudonimo("susana");

//        DateTime dt = new DateTime(new Date(2017, 4, 30));
//        dt.
        c1.setId_comentario(0L);
        c1.setEvento(eventos.get(0));
        c1.setFecha(DateUtils.asDate(LocalDate.of(2017, Month.MARCH, 30)));
        c1.setHora(new Time(18, 14, 0));
        c1.setUsuario(usuario.getUsuarios().get(0));
        c1.setMensaje("prueba 1");
        comentarios.add(c1);

        c1 = new Comentario();
        c1.setId_comentario(1L);
        c1.setEvento(eventos.get(0));
       c1.setFecha(DateUtils.asDate(LocalDate.of(2017, Month.MARCH, 30)));
        c1.setHora(new Time(18, 17, 0));
        c1.setUsuario(usuario.getUsuarios().get(0));
        c1.setMensaje("prueba 2");
        comentarios.add(c1);

        c1 = new Comentario();
        c1.setId_comentario(2L);
        c1.setEvento(eventos.get(0));
        c1.setFecha(DateUtils.asDate(LocalDate.of(2017, Month.MARCH, 30)));
        c1.setHora(new Time(18, 24, 0));
        c1.setUsuario(usuario.getUsuarios().get(0));
        c1.setMensaje("prueba 3");
        comentarios.add(c1);

        eventos.get(0).setComentarios(comentarios);
        
        evento_asiste = new ArrayList<>();
        evento_asiste.add(eventos.get(0));
        evento_asiste.add(eventos.get(1));
        usuario.getUsuarios().get(0).setAsiste(evento_asiste);
        
        
        evento_gusta = new ArrayList<>();
        evento_gusta.add(eventos.get(2));
        evento_gusta.add(eventos.get(3));
        usuario.getUsuarios().get(1).setMegusta(evento_gusta);
        
        
        evento_sigue = new ArrayList<>();
        evento_sigue.add(eventos.get(4));
        evento_sigue.add(eventos.get(5));
        usuario.getUsuarios().get(2).setSigue(evento_sigue);
    }

    public List<Evento> getEventosProximos() {
        Collections.sort(eventos, new Comparator<Evento>() {
            @Override
            public int compare(Evento o1, Evento o2) {
                return o1.getFecha_inicio().compareTo(o2.getFecha_inicio());
            }
        });
        /*
       List<Evento> first_events = eventos.stream().limit(4).collect(Collectors.toList());
       return first_events;
         */
        return eventos.subList(0, 2);
    }

    public List<Evento> getEventos() {
        Collections.sort(eventos, new Comparator<Evento>() {
            @Override
            public int compare(Evento o1, Evento o2) {
                return o1.getFecha_inicio().compareTo(o2.getFecha_inicio());
            }
        });
        return eventos;
    }

    public List<Evento> getEventosDestacados() {
        List<Evento> evd = new ArrayList<>();
        for (Evento x : eventos) {
            if (x.isDestacado()) {
                evd.add(x);
            }
        }
        return evd.subList(0, 2);
    }

    public List<Evento> getEventosByFecha() {
        List<Evento> evf = new ArrayList<>();
        for (Evento x : eventos) {
            if (x.getFecha_inicio() == DateUtils.asDate(fecha)) {
                evf.add(x);
            }
        }
        return evf;
    }

    public List<Evento> getSearchEventos() {
        List<Evento> s = new ArrayList<>();
        for (Evento x : eventos) {
            if (x.getLocalidad().getNombre().equals("Málaga")) {
                s.add(x);
            }
        }
        return s;
    }

    public List<Evento> getSearchEventosEtiquetas(String e) {
        List<Evento> s = new ArrayList<>();
        for (Evento x : eventos) {
            for (Etiqueta et : x.getEtiqueta()) {
                if (et.getNombre().equals(e)) {
                    s.add(x);
                }
            }
        }
        return s;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<Destinatario> getDestinatarios() {
        return destinatarios;
    }

    public Evento getEvento(String e) {
        try{
            return eventos.get(Integer.parseInt(e));
        }catch(NumberFormatException n){
            return null;
        }
    }
    
    public int getId()
    {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public String getSearchProvincia() {
        return searchProvincia;
    }

    public void setSearchProvincia(String searchProvincia) {
        this.searchProvincia = searchProvincia;
    }

    public String getSearchLocalidad() {
        return searchLocalidad;
    }

    public void setSearchLocalidad(String searchLocalidad) {
        this.searchLocalidad = searchLocalidad;
    }

    public String getSearchDestinatario() {
        return searchDestinatario;
    }

    public void setSearchDestinatario(String searchDestinatario) {
        this.searchDestinatario = searchDestinatario;
    }

    public String getSearchEtiqueta() {
        return searchEtiqueta;
    }

    public void setSearchEtiqueta(String searchEtiqueta) {
        this.searchEtiqueta = searchEtiqueta;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TagCloudModel getModel(String e) {
        Random r = new Random();
        r.setSeed(0);
        TagCloudModel s = new DefaultTagCloudModel();
        for (Evento x : eventos) {
            if (x.getId_evento() == Integer.parseInt(e)) {
                for (Etiqueta et : x.getEtiqueta()) {
                    s.addTag(new DefaultTagCloudItem(et.getNombre(), "events_tags.xhtml?tag=" + et.getNombre(), r.nextInt(4) + 1));
                }
            }

        }
        return s;
    }
    
    public String sendNotification(String e){
        System.out.println("Evento " + e);
        System.out.println();
        
//        String s1 = "" + e;
//        String s2 = "" + o;
//        
//        System.out.println("evento " + s1);
//        System.out.println("opcion " + o);
//        
//        Usuario u = current_user.getUsuario();
//        
//        Evento ev = eventos.get(Integer.parseInt(s1));
//        System.out.println("usuario " + u.getNombre());
//        System.out.println("titulo " + ev.getTitulo());
//        System.out.println();
//        String message = "";
//        switch(Integer.parseInt(s2)){
//            case 0:
//                message = "Has pinchado like en el evento " + ev.getTitulo();
//                break;
//            case 1:
//                message = "Has indicado que vas a asistir al evento " + ev.getTitulo() + "\n"
//                        + "Hora: " + ev.getHorario() + " Precio: " + ev.getPrecio();
//                break;
//            case 2:
//                message = "Has indicado que quieres seguir al evento " + ev.getTitulo() + "\n"
//                        + "Hora: " + ev.getHorario() + " Precio: " + ev.getPrecio();
//                break;
//        }
//        System.out.println(u.isEmail_notifier());
//        if(u.isEmail_notifier()){
//            try {
//                System.out.println("send mail");
//                Sendmail.mail(u.getEmail(), "Notificaion: " + ev.getTitulo(), message);
//            } catch (AddressException ex) {
//                Logger.getLogger(EventoBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        System.out.println("terminado");
        //return "event_info?event="+evento_info+"?faces-redirect=true";
        return "index?faces-redirect=true";
    }

    public String getEvento_info() {
        return evento_info;
    }

    public void setEvento_info(String evento_info) {
        this.evento_info = evento_info;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public List<Evento> getEvento_asiste() {
        return evento_asiste;
    }

    public void setEvento_asiste(List<Evento> evento_asiste) {
        this.evento_asiste = evento_asiste;
    }

    public List<Evento> getEvento_gusta() {
        return evento_gusta;
    }

    public void setEvento_gusta(List<Evento> evento_gusta) {
        this.evento_gusta = evento_gusta;
    }

    public List<Evento> getEvento_sigue() {
        return evento_sigue;
    }

    public void setEvento_sigue(List<Evento> evento_sigue) {
        this.evento_sigue = evento_sigue;
    }
    
    
}
