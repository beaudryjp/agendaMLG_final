create table Comentario(
	id_comentario bigint auto_increment
		primary key,
	fecha_hora datetime not null,
	mensaje varchar(255) not null,
	evento_id_evento bigint null,
	usuario_id_usuario bigint null);

create index FKgmk41wggq7acr5cin4y6petdr
	on Comentario (evento_id_evento);

create index FKq2utsndscehnpau990r39jlgg
	on Comentario (usuario_id_usuario);

create table Destinatario(
	id_destinatario bigint auto_increment
		primary key,
	descripcion varchar(255) not null,
	constraint UK2sjbycku5b4t3jqhi2g5gmr30
		unique (descripcion));

create table Etiqueta(
	id_etiqueta bigint auto_increment
		primary key,
	nombre varchar(255) not null,
	constraint UKije0j9xo5bv710qs724e9r3un
		unique (nombre));

create table Evento(
	id_evento bigint auto_increment
		primary key,
	descripcion text not null,
	destacado bit not null,
	fecha_fin date null,
	fecha_inicio date not null,
	horario varchar(255) not null,
	imagen_titulo varchar(255) not null,
	imagen_url varchar(255) not null,
	latitud double not null,
	longitud double not null,
	precio varchar(255) not null,
	titulo varchar(255) not null,
	valoracion int not null,
	localidad_id_localidad bigint null,
	propietario bigint null);

create index FK56opiht2l9kwnn8dax44cru9g
	on Evento (localidad_id_localidad);

create index FK98uda8shy2qbom5y75jeh0d2h
	on Evento (propietario);

alter table Comentario
	add constraint FKgmk41wggq7acr5cin4y6petdr
		foreign key (evento_id_evento) references agenda.Evento (id_evento);

create table Evento_Destinatario(
	evento_id_evento bigint not null,
	destinatario_id_destinatario bigint not null,
	constraint FKs1epe46cg008ccq5ihsoo2hb6
		foreign key (evento_id_evento) references agenda.Evento (id_evento),
	constraint FKcrc1dxci5iw7bv34yiip2aarb
		foreign key (destinatario_id_destinatario) references agenda.Destinatario (id_destinatario));

create index FKcrc1dxci5iw7bv34yiip2aarb
	on Evento_Destinatario (destinatario_id_destinatario);

create index FKs1epe46cg008ccq5ihsoo2hb6
	on Evento_Destinatario (evento_id_evento);

create table Evento_Etiqueta(
	evento_id_evento bigint not null,
	etiqueta_id_etiqueta bigint not null,
	constraint FKfsjgd7du8vbhhau6hfbdf3n86
		foreign key (evento_id_evento) references agenda.Evento (id_evento),
	constraint FKdkkym5px3t0ck8qtxka1n4mdi
		foreign key (etiqueta_id_etiqueta) references agenda.Etiqueta (id_etiqueta));

create index FKdkkym5px3t0ck8qtxka1n4mdi
	on Evento_Etiqueta (etiqueta_id_etiqueta);

create index FKfsjgd7du8vbhhau6hfbdf3n86
	on Evento_Etiqueta (evento_id_evento);

create table Localidad(
	id_localidad bigint auto_increment
		primary key,
	nombre varchar(255) not null,
	provincia_id_provincia bigint null,
	constraint UK5cxvsj9bb8rxa6uvt6oh7kahq
		unique (nombre));

create index FKf8uibl8ose2ibp6pps8jsqpqi
	on Localidad (provincia_id_provincia);

alter table Evento
	add constraint FK56opiht2l9kwnn8dax44cru9g
		foreign key (localidad_id_localidad) references agenda.Localidad (id_localidad);

create table Notificacion(
	id_notificacion bigint auto_increment
		primary key,
	fecha_hora datetime not null,
	mensaje varchar(255) not null,
	evento_id_evento bigint null,
	usuario_id_usuario bigint null,
	constraint FKf584afdtx8glf5nnjsytid5kl
		foreign key (evento_id_evento) references agenda.Evento (id_evento));

create index FKf584afdtx8glf5nnjsytid5kl
	on Notificacion (evento_id_evento);

create index FKpkjtdox8eey9m5e7yecp80kxo
	on Notificacion (usuario_id_usuario);

create table Provincia(
	id_provincia bigint auto_increment
		primary key,
	nombre varchar(255) not null,
	constraint UK8mf2hn9jl0ilqosl320daxen6
		unique (nombre));

alter table Localidad
	add constraint FKf8uibl8ose2ibp6pps8jsqpqi
		foreign key (provincia_id_provincia) references agenda.Provincia (id_provincia);

create table Tarea(
	id_tarea bigint auto_increment
		primary key,
	fecha_hora datetime not null,
	mensaje varchar(255) not null,
	nombre varchar(255) not null,
	creador_peticion_id_usuario bigint null);

create index FKohs1nkl0d2n8co2qga7urk4bu
	on Tarea (creador_peticion_id_usuario);

create table Tarea_Usuario(
	Tarea_id_tarea bigint not null,
	redactores_id_usuario bigint not null,
	constraint FKkohrninbwknkgftrc9npkp0qc
		foreign key (Tarea_id_tarea) references agenda.Tarea (id_tarea));

create index FKkohrninbwknkgftrc9npkp0qc
	on Tarea_Usuario (Tarea_id_tarea);

create index FKlioasj0rq5l96j3nn6kvxjl69
	on Tarea_Usuario (redactores_id_usuario);

create table Usuario(
	id_usuario bigint auto_increment
		primary key,
	apellidos varchar(255) not null,
	email varchar(255) not null,
	email_notifier bit not null,
	nombre varchar(255) not null,
	password_hash varchar(255) not null,
	pseudonimo varchar(255) not null,
	rol varchar(255) null,
	sal varchar(255) not null,
	constraint UKp2n8frvlleaccfkhl6ea1csyn
		unique (email, pseudonimo, sal));

alter table Comentario
	add constraint FKq2utsndscehnpau990r39jlgg
		foreign key (usuario_id_usuario) references agenda.Usuario (id_usuario);

alter table Evento
	add constraint FK98uda8shy2qbom5y75jeh0d2h
		foreign key (propietario) references agenda.Usuario (id_usuario);

alter table Notificacion
	add constraint FKpkjtdox8eey9m5e7yecp80kxo
		foreign key (usuario_id_usuario) references agenda.Usuario (id_usuario);

alter table Tarea
	add constraint FKohs1nkl0d2n8co2qga7urk4bu
		foreign key (creador_peticion_id_usuario) references agenda.Usuario (id_usuario);

alter table Tarea_Usuario
	add constraint FKlioasj0rq5l96j3nn6kvxjl69
		foreign key (redactores_id_usuario) references agenda.Usuario (id_usuario);

create table jn_asiste_id(
	id_usuario bigint not null,
	id_evento bigint not null,
	constraint FKibsvj08cy6cmv51d1wdcx5jwl
		foreign key (id_usuario) references agenda.Usuario (id_usuario),
	constraint FKovbsrolbousftowmxb09rx6m5
		foreign key (id_evento) references agenda.Evento (id_evento));

create index FKibsvj08cy6cmv51d1wdcx5jwl
	on jn_asiste_id (id_usuario);

create index FKovbsrolbousftowmxb09rx6m5
	on jn_asiste_id (id_evento);

create table jn_megusta_id(
	id_usuario bigint not null,
	id_evento bigint not null,
	constraint FKa52g9ffuufl77w57q9e5u3sf1
		foreign key (id_usuario) references agenda.Usuario (id_usuario),
	constraint FKsv6e8tl6af3r3wfyj7ig6tf1u
		foreign key (id_evento) references agenda.Evento (id_evento));

create index FKa52g9ffuufl77w57q9e5u3sf1
	on jn_megusta_id (id_usuario);

create index FKsv6e8tl6af3r3wfyj7ig6tf1u
	on jn_megusta_id (id_evento);

create table jn_sigue_id(
	id_usuario bigint not null,
	id_evento bigint not null,
	constraint FKb2vamopaeeuc2ifmrwhw7slhp
		foreign key (id_usuario) references agenda.Usuario (id_usuario),
	constraint FKs8kjpmi65fsicwu74lvpe5n83
		foreign key (id_evento) references agenda.Evento (id_evento));

create index FKb2vamopaeeuc2ifmrwhw7slhp
	on jn_sigue_id (id_usuario);

create index FKs8kjpmi65fsicwu74lvpe5n83
	on jn_sigue_id (id_evento);

create table jn_tareas_id(
	id_usuario bigint not null,
	id_tarea bigint not null,
	constraint FKnp7s4o36t5tm5jdinle87jowy
		foreign key (id_usuario) references agenda.Usuario (id_usuario),
	constraint FKlkwwot5xf63lsy9m1thrbt2pt
		foreign key (id_tarea) references agenda.Tarea (id_tarea));

create index FKlkwwot5xf63lsy9m1thrbt2pt
	on jn_tareas_id (id_tarea);

create index FKnp7s4o36t5tm5jdinle87jowy
	on jn_tareas_id (id_usuario);
