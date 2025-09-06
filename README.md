# ENVIFO-Java-Backend-API-REST
Proyecto de titulación para el SENA, API REST Java + Spring Boot + Spring Security + JWT + JPA + PostgreSQL, gestiona todos los requerimientos a la base de datos, genera y valida Tokens de seguridad para autenticación de usuarios.

---

## 🔹 Diagrama de Secuencia de la Arquitectura

El siguiente diagrama muestra cómo interactúan los diferentes servicios y componentes principales del proyecto:

- **Autenticación y roles**: El usuario se autentica mediante credenciales y recibe un token JWT, que valida roles y permisos.
- **Gestión de materiales**: Los materiales son creados y procesados con apoyo del servicio Python/Blender, almacenados en **Cloudflare** y registrados en **PostgreSQL**.
- **Simulador 3D**: Recupera configuraciones de proyectos y descarga los modelos desde **Cloudflare** para su renderizado.

```mermaid
sequenceDiagram
    actor Client as Cliente (React)
    participant JavaAPI as API Java
    participant PythonAPI as API Python
    participant Blender as Blender Service
    participant MQ as RabbitMQ
    participant DB as PostgreSQL
    participant Cloudflare as Cloudflare (R2 Buckets)

    %% ================== AUTENTICACIÓN ==================
    rect rgb(200, 230, 255)
    Client ->> JavaAPI: POST /api/auth/login (email, password)
    JavaAPI ->> DB: SELECT usuario + roles
    DB -->> JavaAPI: Datos usuario + permisos
    JavaAPI -->> Client: JWT + Roles
    end

    %% ================== ADMINISTRACIÓN DE ROLES ==================
    rect rgb(220, 255, 220)
    Client ->> JavaAPI: PUT /api/roles/{id} (actualizar permisos)
    JavaAPI ->> DB: UPDATE roles y permisos
    DB -->> JavaAPI: OK
    JavaAPI -->> Client: Confirmación
    end

    %% ================== INVENTARIO DE MATERIALES ==================
    rect rgb(255, 245, 200)
    Client ->> JavaAPI: POST /api/materials (crear material)
    JavaAPI ->> Cloudflare: Subir texturas (temporal)
    JavaAPI ->> PythonAPI: Enviar datos para generar objeto3D

    PythonAPI ->> Blender: Procesar imagen/material
    Blender -->> PythonAPI: Objeto3D generado

    PythonAPI ->> Cloudflare: Subir objeto3D + texturas
    PythonAPI ->> MQ: Publicar evento "MaterialGenerado"

    MQ -->> JavaAPI: Consumir evento con metadata
    JavaAPI ->> DB: INSERT material + URLs
    JavaAPI -->> Client: Confirmación + URLs
    end

    %% ================== SIMULADOR 3D ==================
    rect rgb(245, 220, 255)
    Client ->> JavaAPI: GET /api/simulator/{config}
    JavaAPI ->> DB: SELECT proyecto + materiales + objetos
    DB -->> JavaAPI: Resultados
    JavaAPI ->> Cloudflare: Descargar modelos 3D
    Cloudflare -->> JavaAPI: Archivos 3D
    JavaAPI -->> Client: Respuesta con datos + URLs prefirmadas
    end
