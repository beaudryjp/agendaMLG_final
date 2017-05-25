create table Comentario(
	id_comentario bigint auto_increment
		primary key,
	fecha_hora datetime null,
	mensaje varchar(255) null,
	evento_id_evento bigint null,
	usuario_id_usuario bigint null
);

create index FKgmk41wggq7acr5cin4y6petdr
	on Comentario (evento_id_evento);

create index FKq2utsndscehnpau990r39jlgg
	on Comentario (usuario_id_usuario);

create table Destinatario(
	id_destinatario bigint auto_increment
		primary key,
	descripcion varchar(255) not null,
	constraint UK2sjbycku5b4t3jqhi2g5gmr30
		unique (descripcion)
);

create table Etiqueta(
	id_etiqueta bigint auto_increment
		primary key,
	nombre varchar(255) not null,
	constraint UKije0j9xo5bv710qs724e9r3un
		unique (nombre)
);

create table Evento(
	id_evento bigint auto_increment
		primary key,
	descripcion text not null,
	destacado bit not null,
	fecha_fin date null,
	fecha_inicio date not null,
	horario varchar(255) null,
	imagen_titulo varchar(255) null,
	imagen_url varchar(255) null,
	latitud double not null,
	longitud double not null,
	precio varchar(255) null,
	titulo varchar(255) null,
	localidad_id_localidad bigint null
);

create index FK56opiht2l9kwnn8dax44cru9g
	on Evento (localidad_id_localidad);

alter table Comentario
	add constraint FKgmk41wggq7acr5cin4y6petdr
		foreign key (evento_id_evento) references agenda.Evento (id_evento);

create table Localidad(
	id_localidad bigint auto_increment
		primary key,
	nombre varchar(255) not null,
	provincia_id_provincia bigint null,
	constraint UK5cxvsj9bb8rxa6uvt6oh7kahq
		unique (nombre)
);

create index FKf8uibl8ose2ibp6pps8jsqpqi
	on Localidad (provincia_id_provincia);

alter table Evento
	add constraint FK56opiht2l9kwnn8dax44cru9g
		foreign key (localidad_id_localidad) references agenda.Localidad (id_localidad);

create table Notificacion(
	id_notificacion bigint auto_increment
		primary key,
	fecha_hora datetime null,
	mensaje varchar(255) null,
	evento_id_evento bigint null,
	usuario_id_usuario bigint null
);

create table Provincia(
	id_provincia bigint auto_increment
		primary key,
	nombre varchar(255) not null,
	constraint UK8mf2hn9jl0ilqosl320daxen6
		unique (nombre)
);

alter table Localidad
	add constraint FKf8uibl8ose2ibp6pps8jsqpqi
		foreign key (provincia_id_provincia) references agenda.Provincia (id_provincia);

create table Tarea(
	id_tarea bigint auto_increment
		primary key,
	aceptado bit not null,
	fecha_hora datetime null,
	mensaje varchar(255) null,
	nombre varchar(255) null
);

create table Tarea_Usuario(
	Tarea_id_tarea bigint not null,
	redactores_id_usuario bigint not null
);

create table Usuario(
	id_usuario bigint auto_increment
		primary key,
	apellidos varchar(255) not null,
	email varchar(255) not null,
	email_notifier tinyint not null,
	nombre varchar(255) not null,
	password_hash varchar(255) not null,
	pseudonimo varchar(255) not null,
	rol varchar(255) null,
	sal varchar(255) not null,
	constraint UKp2n8frvlleaccfkhl6ea1csyn
		unique (email, pseudonimo, sal)
);

alter table Comentario
	add constraint FKq2utsndscehnpau990r39jlgg
		foreign key (usuario_id_usuario) references agenda.Usuario (id_usuario);

create table Usuario_Comentario(
	Usuario_id_usuario bigint not null,
	comentarios_id_comentario bigint not null,
	constraint UK_f60d62tcpd4i615w55bi7299a
		unique (comentarios_id_comentario)
);

create table Usuario_Notificacion(
	Usuario_id_usuario bigint not null,
	notificaciones_id_notificacion bigint not null,
	constraint UK_tiyymu90gk1cl2a422fj9dvqk
		unique (notificaciones_id_notificacion)
);

create table jn_asiste_id(
	id_usuario bigint not null,
	id_evento bigint not null,
	constraint FKibsvj08cy6cmv51d1wdcx5jwl
		foreign key (id_usuario) references agenda.Usuario (id_usuario),
	constraint FKovbsrolbousftowmxb09rx6m5
		foreign key (id_evento) references agenda.Evento (id_evento)
);

create index FKibsvj08cy6cmv51d1wdcx5jwl
	on jn_asiste_id (id_usuario);

create index FKovbsrolbousftowmxb09rx6m5
	on jn_asiste_id (id_evento);

create table jn_comentarios_id(
	id_evento bigint not null,
	id_comentario bigint not null,
	constraint UK_7g2o2ots9n8k8qj5gscj6w9oi
		unique (id_comentario),
	constraint FKpwwkvvej5bpsklunm6mvik337
		foreign key (id_evento) references agenda.Evento (id_evento),
	constraint FK3k1qhp4v45uoqm8nkaykx1ngr
		foreign key (id_comentario) references agenda.Comentario (id_comentario)
);

create index FKpwwkvvej5bpsklunm6mvik337
	on jn_comentarios_id (id_evento);

create table jn_destinatario_id(
	id_evento bigint not null,
	id_destinatario bigint not null,
	constraint FK4d7f76lcbl4ns95qfy8dsohmf
		foreign key (id_evento) references agenda.Evento (id_evento),
	constraint FK9amxm8d403yft4astl2qjb3tu
		foreign key (id_destinatario) references agenda.Destinatario (id_destinatario)
);

create index FK4d7f76lcbl4ns95qfy8dsohmf
	on jn_destinatario_id (id_evento);

create index FK9amxm8d403yft4astl2qjb3tu
	on jn_destinatario_id (id_destinatario);

create table jn_etiqueta_id(
	id_evento bigint not null,
	id_etiqueta bigint not null,
	constraint FK9aijur6g9lccsi7fb6hega1hr
		foreign key (id_evento) references agenda.Evento (id_evento),
	constraint FKkynorwdijmpyv1utmu0py9ax4
		foreign key (id_etiqueta) references agenda.Etiqueta (id_etiqueta)
);

create index FK9aijur6g9lccsi7fb6hega1hr
	on jn_etiqueta_id (id_evento);

create index FKkynorwdijmpyv1utmu0py9ax4
	on jn_etiqueta_id (id_etiqueta);

create table jn_megusta_id(
	id_usuario bigint not null,
	id_evento bigint not null,
	constraint FKa52g9ffuufl77w57q9e5u3sf1
		foreign key (id_usuario) references agenda.Usuario (id_usuario),
	constraint FKsv6e8tl6af3r3wfyj7ig6tf1u
		foreign key (id_evento) references agenda.Evento (id_evento)
);

create index FKa52g9ffuufl77w57q9e5u3sf1
	on jn_megusta_id (id_usuario);

create index FKsv6e8tl6af3r3wfyj7ig6tf1u
	on jn_megusta_id (id_evento);

create table jn_notificaciones_id(
	id_evento bigint not null,
	id_notificacion bigint not null,
	constraint UK_4y9n1gcb6tj6vox08g1fcuiml
		unique (id_notificacion),
	constraint FKbp6k5qsbo9knc133imhpk3as2
		foreign key (id_evento) references agenda.Evento (id_evento),
	constraint FK486syap1s5jap40gfow7pwvw9
		foreign key (id_notificacion) references agenda.Notificacion (id_notificacion)
);

create index FKbp6k5qsbo9knc133imhpk3as2
	on jn_notificaciones_id (id_evento);

create table jn_sigue_id(
	id_usuario bigint not null,
	id_evento bigint not null
);

create table jn_tarea_peticion_id(
	id_usuario bigint null,
	id_tarea bigint not null
		primary key
);

create table jn_tareas_id(
	id_usuario bigint not null,
	id_tarea bigint not null
);
