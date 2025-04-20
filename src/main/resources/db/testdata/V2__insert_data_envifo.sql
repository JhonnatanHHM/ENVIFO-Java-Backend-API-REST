--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2025-04-13 23:56:51

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
-- TOC entry 4912 (class 0 OID 42096)
-- Dependencies: 230
-- Data for Name: permisos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.permisos (id_permiso, edit_permisos, vista_usuarios, edit_usuarios, vista_proyectos, edit_proyectos, vista_disenios_3d, edit_disenios_3d, vista_materiales, edit_materiales, vista_informes, vista_categorias, edit_categorias) FROM stdin;
3	t	t	t	t	t	t	t	t	t	t	t	t
16	f	t	f	t	t	t	t	t	f	f	t	f
17	f	t	f	t	t	t	t	t	f	f	t	f
\.


--
-- TOC entry 4919 (class 0 OID 42133)
-- Dependencies: 237
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id_rol, nombre, descripcion, id_permiso) FROM stdin;
5	GLOBAL	Usuario con permisos globales	3
10	RESTRINGIDO	Usuario general	16
13	VENDEDOR	Usuario vendedor	17
\.


--
-- TOC entry 4923 (class 0 OID 42146)
-- Dependencies: 241
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id_usuario, fecha_registro, estado, user_name, nombre, apellido, edad, celular, email, password, id_rol, fecha_nacimiento) FROM stdin;
13	2025-02-02 22:44:00.252303	t	maria123	Maria	Tabarez	28	+0987654321	mariatabarez@example.com	$2a$10$JMd1FOZGf/IdvkytmMSpXOcSyJH9q8KBxL1Zq7vkLbHUQkYOyFCnO	13	1996-08-22
3	2025-01-30 23:00:06.273392	t	johndoe	John	Doeman	30	+1234567890	johndoe@gmail.com	$2a$10$JMd1FOZGf/IdvkytmMSpXOcSyJH9q8KBxL1Zq7vkLbHUQkYOyFCnO	5	1994-05-15
12	2025-01-31 21:00:13.602822	t	juangarcia	Juan	Garcia	30	+0123456789	juangarcia@example.com	$2a$10$skcKQRCCcxtCz2wP2B3.GuUgIQMnAh2PBzatqEo5wlC7OCURG0zSG	10	1993-04-15
\.


--
-- TOC entry 4899 (class 0 OID 42031)
-- Dependencies: 217
-- Data for Name: categorias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categorias (id_categoria, nombre, descripcion, fecha_creacion, estado, id_usuario) FROM stdin;
1	Ceramica	Valdosa en ceramica	2025-04-03 00:00:00	t	3
\.


--
-- TOC entry 4897 (class 0 OID 42023)
-- Dependencies: 215
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.clientes (id_cliente, nombre, direccion, telefono, url, estado, fecha_registro, id_categoria) FROM stdin;
2	Corona	Minorista	6045984752	./public/informes/John-abril.pdf	t	2025-04-03 00:00:00	1
\.


--
-- TOC entry 4901 (class 0 OID 42039)
-- Dependencies: 219
-- Data for Name: disenios_3d; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.disenios_3d (id_disenio, nombre_disenio, descripcion, fecha_creacion, ultima_modificacion, estado, usuario_creador, configuracion) FROM stdin;
\.


--
-- TOC entry 4921 (class 0 OID 42140)
-- Dependencies: 239
-- Data for Name: texturas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.texturas (id_textura, id_categoria, nombre, url) FROM stdin;
\.


--
-- TOC entry 4906 (class 0 OID 42074)
-- Dependencies: 224
-- Data for Name: materiales; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.materiales (id_material, id_categoria, nombre, url, alto, ancho, estado, id_textura) FROM stdin;
\.


--
-- TOC entry 4903 (class 0 OID 42048)
-- Dependencies: 221
-- Data for Name: disenios_materiales; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.disenios_materiales (id_disenio, id_material) FROM stdin;
\.


--
-- TOC entry 4910 (class 0 OID 42089)
-- Dependencies: 228
-- Data for Name: objetos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.objetos (id_objeto, id_categoria, nombre, url, alto, ancho, profundidad, estado) FROM stdin;
\.


--
-- TOC entry 4904 (class 0 OID 42051)
-- Dependencies: 222
-- Data for Name: disenios_objetos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.disenios_objetos (id_disenio, id_objeto, cantidad, posx, posy, posz, rotacion, alto, ancho) FROM stdin;
\.


--
-- TOC entry 4914 (class 0 OID 42114)
-- Dependencies: 232
-- Data for Name: predeterminados_3d; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.predeterminados_3d (id_prede_3d, nombre_prede_3d, descripcion, url, estado, id_categoria) FROM stdin;
\.


--
-- TOC entry 4905 (class 0 OID 42056)
-- Dependencies: 223
-- Data for Name: disenios_prede_3d; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.disenios_prede_3d (id_disenio, id_prede_3d) FROM stdin;
\.


--
-- TOC entry 4908 (class 0 OID 42081)
-- Dependencies: 226
-- Data for Name: notas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notas (id_nota, titulo, contenido, fecha_creacion, fecha_modificacion, id_usuario) FROM stdin;
1	Reporte de ventas	Análisis de ventas del primer trimestre.	2025-03-23 15:54:39.048663	2025-03-23 15:58:31.374641	3
2	Informe de rendimiento	Resumen del rendimiento del equipo en el último mes.	2025-03-23 16:37:51.328933	2025-03-23 16:37:51.328933	12
3	Reporte financiero	Estado financiero y proyección de ingresos.	2025-03-23 16:39:38.828398	2025-03-23 16:39:38.828398	13
5	Introducción a Spring Boot	Este documento explica los conceptos básicos de Spring Boot.	2025-03-24 17:17:53.819364	2025-03-24 17:17:53.819364	3
6	Patrones de Diseño en Java	Se presentan patrones de diseño comunes en aplicaciones Java.	2025-03-24 17:18:14.961309	2025-03-24 17:18:14.961309	3
7	Actualización Arquitectura Onion	Explicación sobre cómo estructurar aplicaciones con la arquitectura Onion.	2025-03-24 17:18:31.708817	2025-03-24 17:19:39.664234	3
\.


--
-- TOC entry 4916 (class 0 OID 42121)
-- Dependencies: 234
-- Data for Name: proyectos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proyectos (id_proyecto, nombre, descripcion, fecha_creacion, fecha_modificacion, estado, propietario, id_usuario, id_cliente) FROM stdin;
\.


--
-- TOC entry 4918 (class 0 OID 42130)
-- Dependencies: 236
-- Data for Name: proyectos_prede_3d; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proyectos_prede_3d (id_proyecto, id_prede_3d) FROM stdin;
\.


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 216
-- Name: casas_de_ceramica_id_casa_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.casas_de_ceramica_id_casa_seq', 2, true);


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 218
-- Name: categorias_id_categoria_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categorias_id_categoria_seq', 1, true);


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 220
-- Name: disenios_3d_id_disenio_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.disenios_3d_id_disenio_seq', 1, false);


--
-- TOC entry 4933 (class 0 OID 0)
-- Dependencies: 225
-- Name: materiales_id_material_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.materiales_id_material_seq', 1, false);


--
-- TOC entry 4934 (class 0 OID 0)
-- Dependencies: 227
-- Name: notas_id_nota_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notas_id_nota_seq', 7, true);


--
-- TOC entry 4935 (class 0 OID 0)
-- Dependencies: 229
-- Name: objetos_id_objeto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.objetos_id_objeto_seq', 1, false);


--
-- TOC entry 4936 (class 0 OID 0)
-- Dependencies: 231
-- Name: permisos_id_permiso_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.permisos_id_permiso_seq', 17, true);


--
-- TOC entry 4937 (class 0 OID 0)
-- Dependencies: 233
-- Name: predeterminados_3d_id_prede_3d_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.predeterminados_3d_id_prede_3d_seq', 1, false);


--
-- TOC entry 4938 (class 0 OID 0)
-- Dependencies: 235
-- Name: proyectos_id_proyecto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.proyectos_id_proyecto_seq', 1, false);


--
-- TOC entry 4939 (class 0 OID 0)
-- Dependencies: 238
-- Name: roles_id_rol_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_rol_seq', 15, true);


--
-- TOC entry 4940 (class 0 OID 0)
-- Dependencies: 240
-- Name: texturas_id_textura_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.texturas_id_textura_seq', 1, false);


--
-- TOC entry 4941 (class 0 OID 0)
-- Dependencies: 242
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_id_usuario_seq', 13, true);


-- Completed on 2025-04-13 23:56:52

--
-- PostgreSQL database dump complete
--

