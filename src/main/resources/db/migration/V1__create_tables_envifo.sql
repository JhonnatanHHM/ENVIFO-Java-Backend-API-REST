
ALTER DATABASE envifo_db OWNER TO envifoclient;

CREATE FUNCTION public.actualizar_fecha_modificacion() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.fecha_modificacion = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$;

ALTER FUNCTION public.actualizar_fecha_modificacion() OWNER TO envifoclient;

CREATE FUNCTION public.actualizar_ultima_modificacion() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.ultima_modificacion = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$;

ALTER FUNCTION public.actualizar_ultima_modificacion() OWNER TO envifoclient;

CREATE TABLE public.almacenamiento (
    id_archivo bigint NOT NULL,
    nombre_archivo text NOT NULL,
    llave_r2 text NOT NULL,
    tipo_entidad text NOT NULL,
    id_entidad bigint NOT NULL
);

ALTER TABLE public.almacenamiento OWNER TO envifoclient;

CREATE SEQUENCE public.almacenamiento_id_almacenamiento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.almacenamiento_id_almacenamiento_seq OWNER TO envifoclient;

ALTER SEQUENCE public.almacenamiento_id_almacenamiento_seq OWNED BY public.almacenamiento.id_archivo;

CREATE TABLE public.clientes (
    id_cliente bigint NOT NULL,
    nombre character varying(150) NOT NULL,
    direccion text NOT NULL,
    telefono character varying(100) NOT NULL,
    url text,
    estado boolean DEFAULT true NOT NULL,
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    email character varying(150) NOT NULL,
    password character varying NOT NULL,
    id_rol bigint
);

ALTER TABLE public.clientes OWNER TO envifoclient;

CREATE SEQUENCE public.casas_de_ceramica_id_casa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.casas_de_ceramica_id_casa_seq OWNER TO envifoclient;

ALTER SEQUENCE public.casas_de_ceramica_id_casa_seq OWNED BY public.clientes.id_cliente;

CREATE TABLE public.categorias (
    id_categoria bigint NOT NULL,
    nombre character varying(50) NOT NULL,
    seccion character varying(50) NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    estado boolean DEFAULT true NOT NULL,
    id_cliente bigint
);

ALTER TABLE public.categorias OWNER TO envifoclient;

CREATE SEQUENCE public.categorias_id_categoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.categorias_id_categoria_seq OWNER TO envifoclient;

ALTER SEQUENCE public.categorias_id_categoria_seq OWNED BY public.categorias.id_categoria;

CREATE TABLE public.cliente_usuario_rol (
    id_cli_usu_rol bigint NOT NULL,
    id_usuario integer,
    id_cliente integer,
    id_rol integer
);

ALTER TABLE public.cliente_usuario_rol OWNER TO envifoclient;

CREATE SEQUENCE public.cliente_usuario_rol_id_cli_usu_rol_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.cliente_usuario_rol_id_cli_usu_rol_seq OWNER TO envifoclient;

ALTER SEQUENCE public.cliente_usuario_rol_id_cli_usu_rol_seq OWNED BY public.cliente_usuario_rol.id_cli_usu_rol;

CREATE TABLE public.clientes_materiales (
    id_cliente bigint,
    id_material bigint NOT NULL
);

ALTER TABLE public.clientes_materiales OWNER TO envifoclient;

CREATE TABLE public.disenios_3d (
    id_disenio bigint NOT NULL,
    configuracion jsonb NOT NULL,
    materiales jsonb,
    objetos jsonb
);

ALTER TABLE public.disenios_3d OWNER TO envifoclient;

CREATE SEQUENCE public.disenios_3d_id_disenio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.disenios_3d_id_disenio_seq OWNER TO envifoclient;

ALTER SEQUENCE public.disenios_3d_id_disenio_seq OWNED BY public.disenios_3d.id_disenio;

CREATE TABLE public.escenarios (
    id_escenario bigint NOT NULL,
    nombre_escenario character varying(100) NOT NULL,
    descripcion text,
    estado boolean DEFAULT true NOT NULL,
    id_categoria bigint NOT NULL,
    metadata jsonb NOT NULL
);

ALTER TABLE public.escenarios OWNER TO envifoclient;

CREATE TABLE public.materiales (
    id_material bigint NOT NULL,
    id_categoria bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    alto numeric NOT NULL,
    ancho numeric NOT NULL,
    estado boolean DEFAULT true NOT NULL,
    id_textura bigint,
    descripcion_mate character varying(300)
);

ALTER TABLE public.materiales OWNER TO envifoclient;

CREATE SEQUENCE public.materiales_id_material_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.materiales_id_material_seq OWNER TO envifoclient;

ALTER SEQUENCE public.materiales_id_material_seq OWNED BY public.materiales.id_material;

CREATE TABLE public.notas (
    id_nota integer NOT NULL,
    titulo character varying(255) NOT NULL,
    contenido text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    id_usuario bigint,
    id_cliente bigint
);

ALTER TABLE public.notas OWNER TO envifoclient;

CREATE SEQUENCE public.notas_id_nota_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.notas_id_nota_seq OWNER TO envifoclient;

ALTER SEQUENCE public.notas_id_nota_seq OWNED BY public.notas.id_nota;

CREATE TABLE public.objetos (
    id_objeto bigint NOT NULL,
    id_categoria bigint NOT NULL,
    nombre_objeto character varying(100) NOT NULL,
    alto numeric NOT NULL,
    ancho numeric NOT NULL,
    profundidad numeric NOT NULL,
    estado boolean DEFAULT true NOT NULL,
    metadata jsonb
);

ALTER TABLE public.objetos OWNER TO envifoclient;

CREATE SEQUENCE public.objetos_id_objeto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.objetos_id_objeto_seq OWNER TO envifoclient;

ALTER SEQUENCE public.objetos_id_objeto_seq OWNED BY public.objetos.id_objeto;

CREATE TABLE public.password_tokens (
    id_password_token bigint NOT NULL,
    token character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    expiration timestamp without time zone NOT NULL,
    used boolean DEFAULT false
);

ALTER TABLE public.password_tokens OWNER TO envifoclient;

CREATE SEQUENCE public.password_tokens_id_password_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.password_tokens_id_password_token_seq OWNER TO envifoclient;

ALTER SEQUENCE public.password_tokens_id_password_token_seq OWNED BY public.password_tokens.id_password_token;

CREATE TABLE public.permisos (
    id_permiso integer NOT NULL,
    edit_permisos boolean DEFAULT false,
    vista_usuarios boolean DEFAULT false,
    edit_usuarios boolean DEFAULT false,
    vista_proyectos boolean DEFAULT false,
    edit_proyectos boolean DEFAULT false,
    vista_disenios_3d boolean DEFAULT false,
    edit_disenios_3d boolean DEFAULT false,
    vista_materiales boolean DEFAULT false,
    edit_materiales boolean DEFAULT false,
    vista_informes boolean DEFAULT false,
    vista_categorias boolean DEFAULT false,
    edit_categorias boolean DEFAULT false
);

ALTER TABLE public.permisos OWNER TO envifoclient;

CREATE SEQUENCE public.permisos_id_permiso_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.permisos_id_permiso_seq OWNER TO envifoclient;

ALTER SEQUENCE public.permisos_id_permiso_seq OWNED BY public.permisos.id_permiso;

CREATE SEQUENCE public.predeterminados_3d_id_prede_3d_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.predeterminados_3d_id_prede_3d_seq OWNER TO envifoclient;

ALTER SEQUENCE public.predeterminados_3d_id_prede_3d_seq OWNED BY public.escenarios.id_escenario;

CREATE TABLE public.proyectos (
    id_proyecto bigint NOT NULL,
    nombre_proyecto character varying(100) NOT NULL,
    descripcion text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_modificacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    estado boolean DEFAULT true NOT NULL,
    id_usuario bigint NOT NULL,
    id_cliente bigint,
    id_escenario bigint NOT NULL,
    id_disenio bigint NOT NULL
);

ALTER TABLE public.proyectos OWNER TO envifoclient;

CREATE SEQUENCE public.proyectos_id_proyecto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.proyectos_id_proyecto_seq OWNER TO envifoclient;

ALTER SEQUENCE public.proyectos_id_proyecto_seq OWNED BY public.proyectos.id_proyecto;

CREATE TABLE public.roles (
    id_rol bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    id_permiso integer
);

ALTER TABLE public.roles OWNER TO envifoclient;

CREATE SEQUENCE public.roles_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.roles_id_rol_seq OWNER TO envifoclient;

ALTER SEQUENCE public.roles_id_rol_seq OWNED BY public.roles.id_rol;

CREATE TABLE public.texturas (
    id_textura bigint NOT NULL,
    id_categoria bigint NOT NULL,
    nombre_textura character varying(100) NOT NULL,
    descripcion character varying(300)
);

ALTER TABLE public.texturas OWNER TO envifoclient;

CREATE SEQUENCE public.texturas_id_textura_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.texturas_id_textura_seq OWNER TO envifoclient;

ALTER SEQUENCE public.texturas_id_textura_seq OWNED BY public.texturas.id_textura;

CREATE TABLE public.usuarios (
    id_usuario bigint NOT NULL,
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true NOT NULL,
    user_name character varying(100),
    primer_nombre character varying(50) NOT NULL,
    primer_apellido character varying(50) NOT NULL,
    celular character varying(15),
    email character varying(150) NOT NULL,
    password character varying NOT NULL,
    id_rol bigint,
    segundo_nombre character varying(50),
    segundo_apellido character varying(50),
    edad character varying
);

ALTER TABLE public.usuarios OWNER TO envifoclient;

CREATE SEQUENCE public.usuarios_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.usuarios_id_usuario_seq OWNER TO envifoclient;

ALTER SEQUENCE public.usuarios_id_usuario_seq OWNED BY public.usuarios.id_usuario;

ALTER TABLE ONLY public.almacenamiento ALTER COLUMN id_archivo SET DEFAULT nextval('public.almacenamiento_id_almacenamiento_seq'::regclass);

ALTER TABLE ONLY public.categorias ALTER COLUMN id_categoria SET DEFAULT nextval('public.categorias_id_categoria_seq'::regclass);

ALTER TABLE ONLY public.cliente_usuario_rol ALTER COLUMN id_cli_usu_rol SET DEFAULT nextval('public.cliente_usuario_rol_id_cli_usu_rol_seq'::regclass);

ALTER TABLE ONLY public.clientes ALTER COLUMN id_cliente SET DEFAULT nextval('public.casas_de_ceramica_id_casa_seq'::regclass);

ALTER TABLE ONLY public.disenios_3d ALTER COLUMN id_disenio SET DEFAULT nextval('public.disenios_3d_id_disenio_seq'::regclass);

ALTER TABLE ONLY public.escenarios ALTER COLUMN id_escenario SET DEFAULT nextval('public.predeterminados_3d_id_prede_3d_seq'::regclass);

ALTER TABLE ONLY public.materiales ALTER COLUMN id_material SET DEFAULT nextval('public.materiales_id_material_seq'::regclass);

ALTER TABLE ONLY public.notas ALTER COLUMN id_nota SET DEFAULT nextval('public.notas_id_nota_seq'::regclass);

ALTER TABLE ONLY public.objetos ALTER COLUMN id_objeto SET DEFAULT nextval('public.objetos_id_objeto_seq'::regclass);

ALTER TABLE ONLY public.password_tokens ALTER COLUMN id_password_token SET DEFAULT nextval('public.password_tokens_id_password_token_seq'::regclass);

ALTER TABLE ONLY public.permisos ALTER COLUMN id_permiso SET DEFAULT nextval('public.permisos_id_permiso_seq'::regclass);

ALTER TABLE ONLY public.proyectos ALTER COLUMN id_proyecto SET DEFAULT nextval('public.proyectos_id_proyecto_seq'::regclass);

ALTER TABLE ONLY public.roles ALTER COLUMN id_rol SET DEFAULT nextval('public.roles_id_rol_seq'::regclass);

ALTER TABLE ONLY public.texturas ALTER COLUMN id_textura SET DEFAULT nextval('public.texturas_id_textura_seq'::regclass);

ALTER TABLE ONLY public.usuarios ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuarios_id_usuario_seq'::regclass);

ALTER TABLE ONLY public.almacenamiento
    ADD CONSTRAINT almacenamiento_pkey PRIMARY KEY (id_archivo);

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_id_cliente_nombre_key UNIQUE (id_cliente, nombre);

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id_categoria);

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_pkey PRIMARY KEY (id_cli_usu_rol);

ALTER TABLE ONLY public.disenios_3d
    ADD CONSTRAINT disenios_3d_pkey PRIMARY KEY (id_disenio);

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_id_textura_key UNIQUE (id_textura);

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_pkey PRIMARY KEY (id_material);

ALTER TABLE ONLY public.notas
    ADD CONSTRAINT notas_pkey PRIMARY KEY (id_nota);

ALTER TABLE ONLY public.objetos
    ADD CONSTRAINT objetos_pkey PRIMARY KEY (id_objeto);

ALTER TABLE ONLY public.password_tokens
    ADD CONSTRAINT password_tokens_pkey PRIMARY KEY (id_password_token);

ALTER TABLE ONLY public.password_tokens
    ADD CONSTRAINT password_tokens_token_key UNIQUE (token);

ALTER TABLE ONLY public.permisos
    ADD CONSTRAINT permisos_key PRIMARY KEY (id_permiso);

ALTER TABLE ONLY public.escenarios
    ADD CONSTRAINT predeterminados_3d_pkey PRIMARY KEY (id_escenario);

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_pkey PRIMARY KEY (id_proyecto);

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_nombre_key UNIQUE (nombre);

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id_rol);

ALTER TABLE ONLY public.texturas
    ADD CONSTRAINT texturas_pkey PRIMARY KEY (id_textura);

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id_usuario);

CREATE TRIGGER trigger_actualizar_fecha_modificacion BEFORE UPDATE ON public.notas FOR EACH ROW EXECUTE FUNCTION public.actualizar_fecha_modificacion();

CREATE TRIGGER trigger_actualizar_fecha_modificacion BEFORE UPDATE ON public.proyectos FOR EACH ROW EXECUTE FUNCTION public.actualizar_fecha_modificacion();

CREATE TRIGGER trigger_actualizar_ultima_modificacion BEFORE UPDATE ON public.disenios_3d FOR EACH ROW EXECUTE FUNCTION public.actualizar_ultima_modificacion();

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) NOT VALID;

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) ON DELETE CASCADE;

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol) ON DELETE CASCADE;

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario) ON DELETE CASCADE;

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol) NOT VALID;

ALTER TABLE ONLY public.clientes_materiales
    ADD CONSTRAINT clientes_materiales_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente);

ALTER TABLE ONLY public.clientes_materiales
    ADD CONSTRAINT clientes_materiales_id_material_fkey FOREIGN KEY (id_material) REFERENCES public.materiales(id_material);

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_id_textura_fkey FOREIGN KEY (id_textura) REFERENCES public.texturas(id_textura) NOT VALID;

ALTER TABLE ONLY public.notas
    ADD CONSTRAINT notas_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) NOT VALID;

ALTER TABLE ONLY public.notas
    ADD CONSTRAINT notas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);

ALTER TABLE ONLY public.objetos
    ADD CONSTRAINT objetos_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;

ALTER TABLE ONLY public.escenarios
    ADD CONSTRAINT predeterminados_3d_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) NOT VALID;

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_id_disenio_fkey FOREIGN KEY (id_disenio) REFERENCES public.disenios_3d(id_disenio) NOT VALID;

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_id_prede_3d_fkey FOREIGN KEY (id_escenario) REFERENCES public.escenarios(id_escenario) NOT VALID;

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario) NOT VALID;

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_id_permiso_fkey FOREIGN KEY (id_permiso) REFERENCES public.permisos(id_permiso) NOT VALID;

ALTER TABLE ONLY public.texturas
    ADD CONSTRAINT texturas_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol) NOT VALID;