package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ProjectsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.ProjectsService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectsServiceImple implements ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final MaterialsServiceImpl materialsService;
    private final ObjectsServiceImpl objectsService;
    private final StorageService storageService;
    private final Designs3dServiceImpl designs3dService;
    private final UsuarioRepository usuarioRepository;
    private final ClientesRepository clientesRepository;
    private final StorageRepository storageRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProjectsServiceImple(ProjectsRepository projectsRepository, MaterialsServiceImpl materialsService,
                                ObjectsServiceImpl objectsService, StorageService storageService,
                                Designs3dServiceImpl designs3dService, UsuarioRepository usuarioRepository,
                                ClientesRepository clientesRepository, StorageRepository storageRepository) {
        this.projectsRepository = projectsRepository;
        this.materialsService = materialsService;
        this.objectsService = objectsService;
        this.storageService = storageService;
        this.designs3dService = designs3dService;
        this.usuarioRepository = usuarioRepository;
        this.clientesRepository = clientesRepository;
        this.storageRepository = storageRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<ProjectCompleteDto> getByIdProject(Long idProject) {
        Optional<ProyectosEntity> projectOpt = projectsRepository.getByIdProject(idProject);

        if (projectOpt.isEmpty()) {
            return Optional.empty();
        }

        ProyectosEntity proyecto = projectOpt.get();
        ProjectCompleteDto dto = new ProjectCompleteDto();

        dto.setIdProject(proyecto.getIdProyecto());
        dto.setProjectName(proyecto.getNombreProyecto());
        dto.setDescription(proyecto.getDescripcion());
        dto.setCreationDate(proyecto.getFechaCreacion());
        dto.setModificationDate(proyecto.getFechaModificacion());
        dto.setStatus(proyecto.isEstado());

        if (proyecto.getUsuario() != null) {
            dto.setUserId(proyecto.getUsuario().getIdUsuario());
        }

        if (proyecto.getCliente() != null) {
            dto.setClientId(proyecto.getCliente().getIdCliente());
        }

        // Diseño
        if (proyecto.getDisenio() != null) {
            Disenios3dEntity disenio = proyecto.getDisenio();

            Designs3dDto designDto = new Designs3dDto();
            designDto.setIdDisenio(disenio.getIdDisenio());
            designDto.setConfiguracion(disenio.getConfiguracion());
            designDto.setMateriales(disenio.getMateriales());
            designDto.setObjetos(disenio.getObjetos());

            dto.setDesign(designDto);

            try {
                // Convertir JsonNode de materiales a lista de Long
                List<Long> materialIds = new ArrayList<>();
                if (disenio.getMateriales() != null && disenio.getMateriales().isArray()) {
                    ArrayNode materialesArray = (ArrayNode) disenio.getMateriales();
                    for (JsonNode node : materialesArray) {
                        if (node.isNumber()) {
                            materialIds.add(node.asLong());
                        }
                    }
                }

                // Convertir JsonNode de objetos a lista de Long
                List<Long> objetoIds = new ArrayList<>();
                if (disenio.getObjetos() != null && disenio.getObjetos().isArray()) {
                    ArrayNode objetosArray = (ArrayNode) disenio.getObjetos();
                    for (JsonNode node : objetosArray) {
                        if (node.isNumber()) {
                            objetoIds.add(node.asLong());
                        }
                    }
                }

                // Obtener lista de materiales
                List<MaterialCompleteDto> materiales = materialsService.getMaterialsByIds(materialIds);
                dto.setMaterials(materiales);

                // Obtener lista de objetos
                List<ObjectCompleteDto> objetos = objectsService.getObjectsByIdsObjects(objetoIds);
                dto.setObjects(objetos);

            } catch (Exception e) {
                System.err.println("Error al procesar JSON de materiales u objetos: " + e.getMessage());
                dto.setMaterials(Collections.emptyList());
                dto.setObjects(Collections.emptyList());
            }
        } else {
            dto.setMaterials(Collections.emptyList());
            dto.setObjects(Collections.emptyList());
        }

        // Imagen del proyecto
        Optional<AlmacenamientoEntity> imageOpt = storageRepository.findByIdEntidadAndTipoEntidad(proyecto.getIdProyecto(), "proyectos");
        Optional<StorageDto> storageDto = imageOpt.flatMap(this::convertToStorageDto);
        dto.setProyect(storageDto);

        return Optional.of(dto);
    }

    @Override
    public List<ProjectFilteredDto> getProjectByCustomer(Long idCustomer) {
        List<ProyectosEntity> proyectos = projectsRepository.getProjectByCustomer(idCustomer);

        List<ProjectFilteredDto> projectsDto = new ArrayList<>();

        for (ProyectosEntity proyecto : proyectos) {
            ProjectFilteredDto dto = new ProjectFilteredDto();
            dto.setIdProject(proyecto.getIdProyecto());
            dto.setProjectName(proyecto.getNombreProyecto());
            dto.setDescription(proyecto.getDescripcion());

            Optional<AlmacenamientoEntity> imageOpt =
                    storageRepository.findByIdEntidadAndTipoEntidad(proyecto.getIdProyecto(), "proyectos");

            imageOpt.ifPresent(imagen -> {
                Optional<StorageDto> storageDto = convertToStorageDto(imagen);
                storageDto.ifPresent(dto::setProyect);
            });

            projectsDto.add(dto);
        }

        return projectsDto;
    }

    @Override
    public List<ProjectFilteredDto> getProjectByUser(Long idUser) {
        List<ProyectosEntity> proyectos = projectsRepository.getProjectByUser(idUser);

        List<ProjectFilteredDto> projectsDto = new ArrayList<>();

        for (ProyectosEntity proyecto : proyectos) {
            ProjectFilteredDto dto = new ProjectFilteredDto();
            dto.setIdProject(proyecto.getIdProyecto());
            dto.setProjectName(proyecto.getNombreProyecto());
            dto.setDescription(proyecto.getDescripcion());

            Optional<AlmacenamientoEntity> imageOpt =
                    storageRepository.findByIdEntidadAndTipoEntidad(proyecto.getIdProyecto(), "proyectos");

            imageOpt.ifPresent(imagen -> {
                Optional<StorageDto> storageDto = convertToStorageDto(imagen);
                storageDto.ifPresent(dto::setProyect);
            });

            projectsDto.add(dto);
        }

        return projectsDto;
    }

    @Override
    public List<ProjectFilteredDto> getProjectByUserAndCustomer(Long idUser, Long idCustomer) {
        List<ProyectosEntity> proyectos = projectsRepository.getProjectByUserAndCustomer(idUser, idCustomer);

        List<ProjectFilteredDto> projectsDto = new ArrayList<>();

        for (ProyectosEntity proyecto : proyectos) {
            ProjectFilteredDto dto = new ProjectFilteredDto();
            dto.setIdProject(proyecto.getIdProyecto());
            dto.setProjectName(proyecto.getNombreProyecto());
            dto.setDescription(proyecto.getDescripcion());

            Optional<AlmacenamientoEntity> imageOpt =
                    storageRepository.findByIdEntidadAndTipoEntidad(proyecto.getIdProyecto(), "proyectos");

            imageOpt.ifPresent(imagen -> {
                Optional<StorageDto> storageDto = convertToStorageDto(imagen);
                storageDto.ifPresent(dto::setProyect);
            });

            projectsDto.add(dto);
        }

        return projectsDto;
    }

    @Override
    public String saveProject(ProjectDto projectDto, MultipartFile image) throws IOException {
        if (projectDto == null || projectDto.getDesign3d() == null) {
            throw new IllegalArgumentException("ProjectDto o Design3dDto no puede ser nulo");
        }

        ProyectosEntity newProject = new ProyectosEntity();
        newProject.setNombreProyecto(projectDto.getProjectName());
        newProject.setDescripcion(projectDto.getDescription());
        newProject.setEstado(projectDto.isStatus());

        UsuarioEntity usuario = usuarioRepository.getByIdUsuario(projectDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        newProject.setUsuario(usuario);

        if (projectDto.getClientId() != null) {
            ClientesEntity cliente = clientesRepository.getByIdCliente(projectDto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            newProject.setCliente(cliente);
        } else {
            newProject.setCliente(null);
        }

        Disenios3dEntity disenio = designs3dService.saveDesign(projectDto.getDesign3d());
        newProject.setDisenio(disenio);

        ProyectosEntity projectSaved = projectsRepository.saveProject(newProject);

        // Procesar imagen si se envía
        if (image != null && !image.isEmpty()) {
            StorageDto dto = new StorageDto();
            dto.setIdEntity(projectSaved.getIdProyecto());
            storageService.saveFile(image, dto, "proyectos", "imagenes");
        }

        return "Proyecto creado correctamente";
    }

    @Override
    public String updateProject(ProjectDto projectDto, MultipartFile image) throws IOException {
        if (projectDto == null) {
            throw new IllegalArgumentException("ProjectDto no puede ser nulo");
        }

        ProyectosEntity existingProject = projectsRepository.getByIdProject(projectDto.getIdProject())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        existingProject.setNombreProyecto(projectDto.getProjectName());
        existingProject.setDescripcion(projectDto.getDescription());
        existingProject.setEstado(projectDto.isStatus());

        UsuarioEntity usuario = usuarioRepository.getByIdUsuario(projectDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existingProject.setUsuario(usuario);

        if (projectDto.getClientId() != null) {
            ClientesEntity cliente = clientesRepository.getByIdCliente(projectDto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            existingProject.setCliente(cliente);
        } else {
            existingProject.setCliente(null);
        }

        if (projectDto.getDesign3d() != null) {
            Disenios3dEntity disenio = designs3dService.updateDesign(projectDto.getDesign3d());
            existingProject.setDisenio(disenio);
        }

        projectsRepository.saveProject(existingProject);

        // Procesar imagen si se envía y no está vacía
        if (image != null && !image.isEmpty()) {
            StorageDto dto = new StorageDto();
            dto.setIdEntity(existingProject.getIdProyecto());
            storageService.saveFile(image, dto, "proyectos", "imagenes");
        }

        return "Proyecto actualizado correctamente";
    }

    @Override
    public void deleteProject(Long idProjecto) {
        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idProjecto, "proyectos");

        Optional<ProyectosEntity> projectOpt = projectsRepository.getByIdProject(idProjecto);

        // Validar existencia
        if (projectOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el proyecto con ID: " + idProjecto);
        }

        imageOpt.ifPresent(image -> {
            try {
                storageService.deleteFileById(image.getIdArchivo());
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen asociada al proyecto: " + e.getMessage());
            }
        });

        projectOpt.ifPresent(proyecto -> projectsRepository.deleteProject(proyecto.getIdProyecto()));
    }

    private Optional<StorageDto> convertToStorageDto(AlmacenamientoEntity imagen) {
        StorageDto storageDto = new StorageDto();
        storageDto.setIdFile(imagen.getIdArchivo());
        storageDto.setNameFile(imagen.getNombreArchivo());
        storageDto.setIdEntity(imagen.getIdEntidad());

        try {
            String url = storageService.getPresignedUrl(imagen.getIdArchivo());
            storageDto.setKeyR2(url);
        } catch (Exception e) {
            System.err.println("Error al generar URL: " + e.getMessage());
            storageDto.setKeyR2("Error al generar URL");
        }

        return Optional.of(storageDto);
    }
}