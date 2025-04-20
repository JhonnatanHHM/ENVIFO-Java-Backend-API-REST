--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2025-04-20 13:57:13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 247 (class 1255 OID 42014)
-- Name: actualizar_fecha_modificacion(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.actualizar_fecha_modificacion() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.fecha_modificacion = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$;


ALTER FUNCTION public.actualizar_fecha_modificacion() OWNER TO postgres;

--
-- TOC entry 248 (class 1255 OID 42015)
-- Name: actualizar_ultima_modificacion(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.actualizar_ultima_modificacion() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.ultima_modificacion = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$;


ALTER FUNCTION public.actualizar_ultima_modificacion() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 42023)
-- Name: clientes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clientes (
    id_cliente bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    direccion text,
    telefono character varying(100),
    url text,
    estado boolean DEFAULT true,
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    email character varying(150) NOT NULL,
    password character varying NOT NULL,
    id_rol bigint
);


ALTER TABLE public.clientes OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 42030)
-- Name: casas_de_ceramica_id_casa_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.casas_de_ceramica_id_casa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.casas_de_ceramica_id_casa_seq OWNER TO postgres;

--
-- TOC entry 4968 (class 0 OID 0)
-- Dependencies: 216
-- Name: casas_de_ceramica_id_casa_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.casas_de_ceramica_id_casa_seq OWNED BY public.clientes.id_cliente;


--
-- TOC entry 217 (class 1259 OID 42031)
-- Name: categorias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categorias (
    id_categoria integer NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true,
    id_cliente bigint
);


ALTER TABLE public.categorias OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 42038)
-- Name: categorias_id_categoria_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categorias_id_categoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categorias_id_categoria_seq OWNER TO postgres;

--
-- TOC entry 4969 (class 0 OID 0)
-- Dependencies: 218
-- Name: categorias_id_categoria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categorias_id_categoria_seq OWNED BY public.categorias.id_categoria;


--
-- TOC entry 246 (class 1259 OID 76516)
-- Name: cliente_usuario_rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente_usuario_rol (
    id_cli_usu_rol bigint NOT NULL,
    id_usuario integer,
    id_cliente integer,
    id_rol integer
);


ALTER TABLE public.cliente_usuario_rol OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 76515)
-- Name: cliente_usuario_rol_id_cli_usu_rol_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cliente_usuario_rol_id_cli_usu_rol_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cliente_usuario_rol_id_cli_usu_rol_seq OWNER TO postgres;

--
-- TOC entry 4970 (class 0 OID 0)
-- Dependencies: 245
-- Name: cliente_usuario_rol_id_cli_usu_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cliente_usuario_rol_id_cli_usu_rol_seq OWNED BY public.cliente_usuario_rol.id_cli_usu_rol;


--
-- TOC entry 219 (class 1259 OID 42039)
-- Name: disenios_3d; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.disenios_3d (
    id_disenio integer NOT NULL,
    nombre_disenio character varying(100) NOT NULL,
    descripcion text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    ultima_modificacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true,
    usuario_creador character varying(100),
    configuracion character(1)
);


ALTER TABLE public.disenios_3d OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 42047)
-- Name: disenios_3d_id_disenio_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.disenios_3d_id_disenio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.disenios_3d_id_disenio_seq OWNER TO postgres;

--
-- TOC entry 4971 (class 0 OID 0)
-- Dependencies: 220
-- Name: disenios_3d_id_disenio_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.disenios_3d_id_disenio_seq OWNED BY public.disenios_3d.id_disenio;


--
-- TOC entry 221 (class 1259 OID 42048)
-- Name: disenios_materiales; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.disenios_materiales (
    id_disenio integer NOT NULL,
    id_material integer NOT NULL
);


ALTER TABLE public.disenios_materiales OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 42051)
-- Name: disenios_objetos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.disenios_objetos (
    id_disenio integer NOT NULL,
    id_objeto integer NOT NULL,
    cantidad integer,
    posx numeric,
    posy numeric,
    posz numeric,
    rotacion text,
    alto numeric,
    ancho numeric
);


ALTER TABLE public.disenios_objetos OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 42056)
-- Name: disenios_prede_3d; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.disenios_prede_3d (
    id_disenio integer NOT NULL,
    id_prede_3d integer NOT NULL
);


ALTER TABLE public.disenios_prede_3d OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 42074)
-- Name: materiales; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.materiales (
    id_material integer NOT NULL,
    id_categoria integer,
    nombre character varying(100) NOT NULL,
    url text,
    alto numeric,
    ancho numeric,
    estado boolean DEFAULT true,
    id_textura integer
);


ALTER TABLE public.materiales OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 42080)
-- Name: materiales_id_material_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.materiales_id_material_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.materiales_id_material_seq OWNER TO postgres;

--
-- TOC entry 4972 (class 0 OID 0)
-- Dependencies: 225
-- Name: materiales_id_material_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.materiales_id_material_seq OWNED BY public.materiales.id_material;


--
-- TOC entry 226 (class 1259 OID 42081)
-- Name: notas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notas (
    id_nota integer NOT NULL,
    titulo character varying(255) NOT NULL,
    contenido text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    id_usuario bigint,
    id_cliente bigint
);


ALTER TABLE public.notas OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 42088)
-- Name: notas_id_nota_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notas_id_nota_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.notas_id_nota_seq OWNER TO postgres;

--
-- TOC entry 4973 (class 0 OID 0)
-- Dependencies: 227
-- Name: notas_id_nota_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.notas_id_nota_seq OWNED BY public.notas.id_nota;


--
-- TOC entry 228 (class 1259 OID 42089)
-- Name: objetos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.objetos (
    id_objeto integer NOT NULL,
    id_categoria integer,
    nombre character varying(100) NOT NULL,
    url text,
    alto numeric,
    ancho numeric,
    profundidad numeric,
    estado boolean DEFAULT true
);


ALTER TABLE public.objetos OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 42095)
-- Name: objetos_id_objeto_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.objetos_id_objeto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.objetos_id_objeto_seq OWNER TO postgres;

--
-- TOC entry 4974 (class 0 OID 0)
-- Dependencies: 229
-- Name: objetos_id_objeto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.objetos_id_objeto_seq OWNED BY public.objetos.id_objeto;


--
-- TOC entry 244 (class 1259 OID 76470)
-- Name: password_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_tokens (
    id_password_token bigint NOT NULL,
    token character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    expiration timestamp without time zone NOT NULL,
    used boolean DEFAULT false
);


ALTER TABLE public.password_tokens OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 76469)
-- Name: password_tokens_id_password_token_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.password_tokens_id_password_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.password_tokens_id_password_token_seq OWNER TO postgres;

--
-- TOC entry 4975 (class 0 OID 0)
-- Dependencies: 243
-- Name: password_tokens_id_password_token_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.password_tokens_id_password_token_seq OWNED BY public.password_tokens.id_password_token;


--
-- TOC entry 230 (class 1259 OID 42096)
-- Name: permisos; Type: TABLE; Schema: public; Owner: postgres
--

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


ALTER TABLE public.permisos OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 42113)
-- Name: permisos_id_permiso_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.permisos_id_permiso_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.permisos_id_permiso_seq OWNER TO postgres;

--
-- TOC entry 4976 (class 0 OID 0)
-- Dependencies: 231
-- Name: permisos_id_permiso_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.permisos_id_permiso_seq OWNED BY public.permisos.id_permiso;


--
-- TOC entry 232 (class 1259 OID 42114)
-- Name: predeterminados_3d; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.predeterminados_3d (
    id_prede_3d integer NOT NULL,
    nombre_prede_3d character varying(100) NOT NULL,
    descripcion text,
    url text,
    estado boolean DEFAULT true,
    id_categoria integer
);


ALTER TABLE public.predeterminados_3d OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 42120)
-- Name: predeterminados_3d_id_prede_3d_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.predeterminados_3d_id_prede_3d_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.predeterminados_3d_id_prede_3d_seq OWNER TO postgres;

--
-- TOC entry 4977 (class 0 OID 0)
-- Dependencies: 233
-- Name: predeterminados_3d_id_prede_3d_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.predeterminados_3d_id_prede_3d_seq OWNED BY public.predeterminados_3d.id_prede_3d;


--
-- TOC entry 234 (class 1259 OID 42121)
-- Name: proyectos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proyectos (
    id_proyecto bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true,
    propietario character varying(100),
    id_usuario bigint,
    id_cliente bigint
);


ALTER TABLE public.proyectos OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 42129)
-- Name: proyectos_id_proyecto_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.proyectos_id_proyecto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.proyectos_id_proyecto_seq OWNER TO postgres;

--
-- TOC entry 4978 (class 0 OID 0)
-- Dependencies: 235
-- Name: proyectos_id_proyecto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.proyectos_id_proyecto_seq OWNED BY public.proyectos.id_proyecto;


--
-- TOC entry 236 (class 1259 OID 42130)
-- Name: proyectos_prede_3d; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proyectos_prede_3d (
    id_proyecto integer NOT NULL,
    id_prede_3d integer NOT NULL
);


ALTER TABLE public.proyectos_prede_3d OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 42133)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id_rol bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    id_permiso integer
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 42139)
-- Name: roles_id_rol_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_rol_seq OWNER TO postgres;

--
-- TOC entry 4979 (class 0 OID 0)
-- Dependencies: 238
-- Name: roles_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_rol_seq OWNED BY public.roles.id_rol;


--
-- TOC entry 239 (class 1259 OID 42140)
-- Name: texturas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.texturas (
    id_textura integer NOT NULL,
    id_categoria integer,
    nombre character varying(100) NOT NULL,
    url text
);


ALTER TABLE public.texturas OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 42145)
-- Name: texturas_id_textura_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.texturas_id_textura_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.texturas_id_textura_seq OWNER TO postgres;

--
-- TOC entry 4980 (class 0 OID 0)
-- Dependencies: 240
-- Name: texturas_id_textura_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.texturas_id_textura_seq OWNED BY public.texturas.id_textura;


--
-- TOC entry 241 (class 1259 OID 42146)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id_usuario bigint NOT NULL,
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true,
    user_name character varying(100) NOT NULL,
    nombre character varying(100),
    apellido character varying(100),
    edad integer,
    celular character varying(15),
    email character varying(150) NOT NULL,
    password character varying NOT NULL,
    id_rol bigint,
    fecha_nacimiento character varying(10)
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 42153)
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_usuario_seq OWNER TO postgres;

--
-- TOC entry 4981 (class 0 OID 0)
-- Dependencies: 242
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuarios_id_usuario_seq OWNED BY public.usuarios.id_usuario;


--
-- TOC entry 4704 (class 2604 OID 42156)
-- Name: categorias id_categoria; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias ALTER COLUMN id_categoria SET DEFAULT nextval('public.categorias_id_categoria_seq'::regclass);


--
-- TOC entry 4744 (class 2604 OID 76519)
-- Name: cliente_usuario_rol id_cli_usu_rol; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente_usuario_rol ALTER COLUMN id_cli_usu_rol SET DEFAULT nextval('public.cliente_usuario_rol_id_cli_usu_rol_seq'::regclass);


--
-- TOC entry 4701 (class 2604 OID 58715)
-- Name: clientes id_cliente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes ALTER COLUMN id_cliente SET DEFAULT nextval('public.casas_de_ceramica_id_casa_seq'::regclass);


--
-- TOC entry 4707 (class 2604 OID 42157)
-- Name: disenios_3d id_disenio; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_3d ALTER COLUMN id_disenio SET DEFAULT nextval('public.disenios_3d_id_disenio_seq'::regclass);


--
-- TOC entry 4711 (class 2604 OID 42160)
-- Name: materiales id_material; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materiales ALTER COLUMN id_material SET DEFAULT nextval('public.materiales_id_material_seq'::regclass);


--
-- TOC entry 4713 (class 2604 OID 42161)
-- Name: notas id_nota; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notas ALTER COLUMN id_nota SET DEFAULT nextval('public.notas_id_nota_seq'::regclass);


--
-- TOC entry 4716 (class 2604 OID 42162)
-- Name: objetos id_objeto; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objetos ALTER COLUMN id_objeto SET DEFAULT nextval('public.objetos_id_objeto_seq'::regclass);


--
-- TOC entry 4742 (class 2604 OID 76473)
-- Name: password_tokens id_password_token; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_tokens ALTER COLUMN id_password_token SET DEFAULT nextval('public.password_tokens_id_password_token_seq'::regclass);


--
-- TOC entry 4718 (class 2604 OID 42163)
-- Name: permisos id_permiso; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permisos ALTER COLUMN id_permiso SET DEFAULT nextval('public.permisos_id_permiso_seq'::regclass);


--
-- TOC entry 4731 (class 2604 OID 42164)
-- Name: predeterminados_3d id_prede_3d; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predeterminados_3d ALTER COLUMN id_prede_3d SET DEFAULT nextval('public.predeterminados_3d_id_prede_3d_seq'::regclass);


--
-- TOC entry 4733 (class 2604 OID 58734)
-- Name: proyectos id_proyecto; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos ALTER COLUMN id_proyecto SET DEFAULT nextval('public.proyectos_id_proyecto_seq'::regclass);


--
-- TOC entry 4737 (class 2604 OID 76385)
-- Name: roles id_rol; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id_rol SET DEFAULT nextval('public.roles_id_rol_seq'::regclass);


--
-- TOC entry 4738 (class 2604 OID 42167)
-- Name: texturas id_textura; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.texturas ALTER COLUMN id_textura SET DEFAULT nextval('public.texturas_id_textura_seq'::regclass);


--
-- TOC entry 4739 (class 2604 OID 42360)
-- Name: usuarios id_usuario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuarios_id_usuario_seq'::regclass);


--
-- TOC entry 4748 (class 2606 OID 76376)
-- Name: categorias categorias_id_cliente_nombre_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_id_cliente_nombre_key UNIQUE (id_cliente, nombre);


--
-- TOC entry 4750 (class 2606 OID 42174)
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id_categoria);


--
-- TOC entry 4746 (class 2606 OID 58717)
-- Name: clientes cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- TOC entry 4792 (class 2606 OID 76521)
-- Name: cliente_usuario_rol cliente_usuario_rol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_pkey PRIMARY KEY (id_cli_usu_rol);


--
-- TOC entry 4752 (class 2606 OID 42176)
-- Name: disenios_3d disenios_3d_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_3d
    ADD CONSTRAINT disenios_3d_pkey PRIMARY KEY (id_disenio);


--
-- TOC entry 4754 (class 2606 OID 42178)
-- Name: disenios_materiales disenios_materiales_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_materiales
    ADD CONSTRAINT disenios_materiales_pkey PRIMARY KEY (id_disenio, id_material);


--
-- TOC entry 4756 (class 2606 OID 42180)
-- Name: disenios_objetos disenios_objetos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_objetos
    ADD CONSTRAINT disenios_objetos_pkey PRIMARY KEY (id_disenio, id_objeto);


--
-- TOC entry 4758 (class 2606 OID 42182)
-- Name: disenios_prede_3d disenios_prede_3d_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_prede_3d
    ADD CONSTRAINT disenios_prede_3d_pkey PRIMARY KEY (id_disenio, id_prede_3d);


--
-- TOC entry 4760 (class 2606 OID 42190)
-- Name: materiales materiales_id_textura_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_id_textura_key UNIQUE (id_textura);


--
-- TOC entry 4762 (class 2606 OID 42192)
-- Name: materiales materiales_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_pkey PRIMARY KEY (id_material);


--
-- TOC entry 4764 (class 2606 OID 42194)
-- Name: notas notas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notas
    ADD CONSTRAINT notas_pkey PRIMARY KEY (id_nota);


--
-- TOC entry 4766 (class 2606 OID 42196)
-- Name: objetos objetos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objetos
    ADD CONSTRAINT objetos_pkey PRIMARY KEY (id_objeto);


--
-- TOC entry 4788 (class 2606 OID 76478)
-- Name: password_tokens password_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_tokens
    ADD CONSTRAINT password_tokens_pkey PRIMARY KEY (id_password_token);


--
-- TOC entry 4790 (class 2606 OID 76480)
-- Name: password_tokens password_tokens_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_tokens
    ADD CONSTRAINT password_tokens_token_key UNIQUE (token);


--
-- TOC entry 4768 (class 2606 OID 42198)
-- Name: permisos permisos_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permisos
    ADD CONSTRAINT permisos_key PRIMARY KEY (id_permiso);


--
-- TOC entry 4770 (class 2606 OID 42200)
-- Name: predeterminados_3d predeterminados_3d_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predeterminados_3d
    ADD CONSTRAINT predeterminados_3d_pkey PRIMARY KEY (id_prede_3d);


--
-- TOC entry 4772 (class 2606 OID 58736)
-- Name: proyectos proyectos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_pkey PRIMARY KEY (id_proyecto);


--
-- TOC entry 4774 (class 2606 OID 42204)
-- Name: proyectos_prede_3d proyectos_prede_3d_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos_prede_3d
    ADD CONSTRAINT proyectos_prede_3d_pkey PRIMARY KEY (id_proyecto, id_prede_3d);


--
-- TOC entry 4776 (class 2606 OID 42334)
-- Name: roles roles_nombre_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_nombre_key UNIQUE (nombre);


--
-- TOC entry 4778 (class 2606 OID 76387)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id_rol);


--
-- TOC entry 4780 (class 2606 OID 42210)
-- Name: texturas texturas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.texturas
    ADD CONSTRAINT texturas_pkey PRIMARY KEY (id_textura);


--
-- TOC entry 4782 (class 2606 OID 76378)
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- TOC entry 4784 (class 2606 OID 42362)
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id_usuario);


--
-- TOC entry 4786 (class 2606 OID 42216)
-- Name: usuarios usuarios_user_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_user_name_key UNIQUE (user_name);


--
-- TOC entry 4818 (class 2620 OID 42217)
-- Name: notas trigger_actualizar_fecha_modificacion; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_actualizar_fecha_modificacion BEFORE UPDATE ON public.notas FOR EACH ROW EXECUTE FUNCTION public.actualizar_fecha_modificacion();


--
-- TOC entry 4819 (class 2620 OID 42218)
-- Name: proyectos trigger_actualizar_fecha_modificacion; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_actualizar_fecha_modificacion BEFORE UPDATE ON public.proyectos FOR EACH ROW EXECUTE FUNCTION public.actualizar_fecha_modificacion();


--
-- TOC entry 4817 (class 2620 OID 42219)
-- Name: disenios_3d trigger_actualizar_ultima_modificacion; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_actualizar_ultima_modificacion BEFORE UPDATE ON public.disenios_3d FOR EACH ROW EXECUTE FUNCTION public.actualizar_ultima_modificacion();


--
-- TOC entry 4794 (class 2606 OID 76368)
-- Name: categorias categorias_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) NOT VALID;


--
-- TOC entry 4814 (class 2606 OID 76527)
-- Name: cliente_usuario_rol cliente_usuario_rol_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) ON DELETE CASCADE;


--
-- TOC entry 4815 (class 2606 OID 76532)
-- Name: cliente_usuario_rol cliente_usuario_rol_id_rol_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol) ON DELETE CASCADE;


--
-- TOC entry 4816 (class 2606 OID 76522)
-- Name: cliente_usuario_rol cliente_usuario_rol_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente_usuario_rol
    ADD CONSTRAINT cliente_usuario_rol_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario) ON DELETE CASCADE;


--
-- TOC entry 4793 (class 2606 OID 76400)
-- Name: clientes clientes_id_rol_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol) NOT VALID;


--
-- TOC entry 4795 (class 2606 OID 42225)
-- Name: disenios_materiales disenios_materiales_id_disenio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_materiales
    ADD CONSTRAINT disenios_materiales_id_disenio_fkey FOREIGN KEY (id_disenio) REFERENCES public.disenios_3d(id_disenio) NOT VALID;


--
-- TOC entry 4796 (class 2606 OID 42230)
-- Name: disenios_materiales disenios_materiales_id_material_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_materiales
    ADD CONSTRAINT disenios_materiales_id_material_fkey FOREIGN KEY (id_material) REFERENCES public.materiales(id_material) NOT VALID;


--
-- TOC entry 4797 (class 2606 OID 42235)
-- Name: disenios_objetos disenios_objetos_id_disenio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_objetos
    ADD CONSTRAINT disenios_objetos_id_disenio_fkey FOREIGN KEY (id_disenio) REFERENCES public.disenios_3d(id_disenio) NOT VALID;


--
-- TOC entry 4798 (class 2606 OID 42240)
-- Name: disenios_objetos disenios_objetos_id_objeto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_objetos
    ADD CONSTRAINT disenios_objetos_id_objeto_fkey FOREIGN KEY (id_objeto) REFERENCES public.objetos(id_objeto) NOT VALID;


--
-- TOC entry 4799 (class 2606 OID 42245)
-- Name: disenios_prede_3d disenios_prede_3d_id_disenio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_prede_3d
    ADD CONSTRAINT disenios_prede_3d_id_disenio_fkey FOREIGN KEY (id_disenio) REFERENCES public.disenios_3d(id_disenio);


--
-- TOC entry 4800 (class 2606 OID 42250)
-- Name: disenios_prede_3d disenios_prede_3d_id_prede_3d_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.disenios_prede_3d
    ADD CONSTRAINT disenios_prede_3d_id_prede_3d_fkey FOREIGN KEY (id_prede_3d) REFERENCES public.predeterminados_3d(id_prede_3d);


--
-- TOC entry 4801 (class 2606 OID 42265)
-- Name: materiales materiales_id_categoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;


--
-- TOC entry 4802 (class 2606 OID 42270)
-- Name: materiales materiales_id_textura_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materiales
    ADD CONSTRAINT materiales_id_textura_fkey FOREIGN KEY (id_textura) REFERENCES public.texturas(id_textura) NOT VALID;


--
-- TOC entry 4803 (class 2606 OID 76429)
-- Name: notas notas_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notas
    ADD CONSTRAINT notas_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) NOT VALID;


--
-- TOC entry 4804 (class 2606 OID 76418)
-- Name: notas notas_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notas
    ADD CONSTRAINT notas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- TOC entry 4805 (class 2606 OID 42280)
-- Name: objetos objetos_id_categoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.objetos
    ADD CONSTRAINT objetos_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;


--
-- TOC entry 4806 (class 2606 OID 42285)
-- Name: predeterminados_3d predeterminados_3d_id_categoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predeterminados_3d
    ADD CONSTRAINT predeterminados_3d_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;


--
-- TOC entry 4807 (class 2606 OID 58759)
-- Name: proyectos proyectos_id_cliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.clientes(id_cliente) NOT VALID;


--
-- TOC entry 4808 (class 2606 OID 58748)
-- Name: proyectos proyectos_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos
    ADD CONSTRAINT proyectos_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario) NOT VALID;


--
-- TOC entry 4809 (class 2606 OID 42305)
-- Name: proyectos_prede_3d proyectos_prede_3d_id_prede_3d_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos_prede_3d
    ADD CONSTRAINT proyectos_prede_3d_id_prede_3d_fkey FOREIGN KEY (id_prede_3d) REFERENCES public.predeterminados_3d(id_prede_3d) NOT VALID;


--
-- TOC entry 4810 (class 2606 OID 58737)
-- Name: proyectos_prede_3d proyectos_prede_3d_id_proyecto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proyectos_prede_3d
    ADD CONSTRAINT proyectos_prede_3d_id_proyecto_fkey FOREIGN KEY (id_proyecto) REFERENCES public.proyectos(id_proyecto) NOT VALID;


--
-- TOC entry 4811 (class 2606 OID 42397)
-- Name: roles roles_id_permiso_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_id_permiso_fkey FOREIGN KEY (id_permiso) REFERENCES public.permisos(id_permiso) NOT VALID;


--
-- TOC entry 4812 (class 2606 OID 42320)
-- Name: texturas texturas_id_categoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.texturas
    ADD CONSTRAINT texturas_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categorias(id_categoria) NOT VALID;


--
-- TOC entry 4813 (class 2606 OID 76405)
-- Name: usuarios usuarios_id_rol_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.roles(id_rol) NOT VALID;


-- Completed on 2025-04-20 13:57:20

--
-- PostgreSQL database dump complete
--

