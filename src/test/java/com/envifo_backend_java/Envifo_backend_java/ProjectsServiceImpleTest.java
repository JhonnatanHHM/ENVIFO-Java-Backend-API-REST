package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.application.service.Designs3dServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.application.service.MaterialsServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.application.service.ObjectsServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.application.service.ProjectsServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ProjectsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProjectsServiceImpleTest {

    @Mock
    private ProjectsRepository projectsRepository;
    @Mock
    private MaterialsServiceImpl materialsService;
    @Mock
    private ObjectsServiceImpl objectsService;
    @Mock
    private StorageService storageService;
    @Mock
    private Designs3dServiceImpl designs3dService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ClientesRepository clientesRepository;
    @Mock
    private StorageRepository storageRepository;
    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private ProjectsServiceImple projectsService;

    private ProyectosEntity proyecto;
    private UsuarioEntity usuario;
    private ClientesEntity cliente;
    private Disenios3dEntity disenio;
    private AlmacenamientoEntity almacenamiento;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        usuario = new UsuarioEntity();
        usuario.setIdUsuario(1L);

        cliente = new ClientesEntity();
        cliente.setIdCliente(2L);

        // Crear JsonNode para los campos de Disenios3dEntity
        ObjectNode configuracionNode = objectMapper.createObjectNode();
        configuracionNode.put("ambiental", 0.8);
        ArrayNode materialesNode = objectMapper.createArrayNode();
        materialesNode.add(1);
        materialesNode.add(2);
        ArrayNode objetosNode = objectMapper.createArrayNode();
        objetosNode.add(10);
        objetosNode.add(20);

        disenio = new Disenios3dEntity();
        disenio.setIdDisenio(4L);
        disenio.setConfiguracion(configuracionNode);
        disenio.setMateriales(materialesNode);
        disenio.setObjetos(objetosNode);

        proyecto = new ProyectosEntity();
        proyecto.setIdProyecto(100L);
        proyecto.setNombreProyecto("Proyecto Test");
        proyecto.setDescripcion("Desc");
        proyecto.setEstado(true);
        proyecto.setUsuario(usuario);
        proyecto.setCliente(cliente);
        proyecto.setDisenio(disenio);

        almacenamiento = new AlmacenamientoEntity();
        almacenamiento.setIdArchivo(500L);
        almacenamiento.setIdEntidad(100L);
        almacenamiento.setNombreArchivo("archivo.png");

        // Configurar comportamiento predeterminado para el mockFile
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("archivo.png");
    }

    @Test
    void testGetByIdProject_found() throws IOException {
        when(projectsRepository.getByIdProject(100L)).thenReturn(Optional.of(proyecto));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "proyectos")).thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(500L)).thenReturn("http://url.com/file");
        when(materialsService.getMaterialsByIds(argThat(list -> list.containsAll(List.of(1L, 2L))))).thenReturn(List.of(new MaterialCompleteDto()));
        when(objectsService.getObjectsByIdsObjects(argThat(list -> list.containsAll(List.of(10L, 20L))))).thenReturn(List.of(new ObjectCompleteDto()));

        Optional<ProjectCompleteDto> result = projectsService.getByIdProject(100L);

        assertTrue(result.isPresent());
        assertEquals("Proyecto Test", result.get().getProjectName());
        assertEquals(1L, result.get().getUserId());
        assertEquals(2L, result.get().getClientId());
        assertNotNull(result.get().getDesign());
        assertEquals(4L, result.get().getDesign().getIdDisenio());
        assertNotNull(result.get().getMaterials());
        assertNotNull(result.get().getObjects());
        assertTrue(result.get().getProyect().isPresent());
        assertEquals("http://url.com/file", result.get().getProyect().get().getKeyR2());
        verify(storageService, times(1)).getPresignedUrl(500L);
        verify(materialsService, times(1)).getMaterialsByIds(argThat(list -> list.containsAll(List.of(1L, 2L))));
        verify(objectsService, times(1)).getObjectsByIdsObjects(argThat(list -> list.containsAll(List.of(10L, 20L))));
    }

    @Test
    void testGetByIdProject_notFound() {
        when(projectsRepository.getByIdProject(999L)).thenReturn(Optional.empty());
        Optional<ProjectCompleteDto> result = projectsService.getByIdProject(999L);
        assertTrue(result.isEmpty());
        verify(projectsRepository, times(1)).getByIdProject(999L);
    }

    @Test
    void testSaveProject_success() throws IOException {
        ProjectDto dto = new ProjectDto();
        dto.setProjectName("Nuevo Proyecto");
        dto.setDescription("Desc");
        dto.setStatus(true);
        dto.setUserId(1L);
        dto.setClientId(2L);
        dto.setDesign3d(new Designs3dDto());

        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(clientesRepository.getByIdCliente(2L)).thenReturn(Optional.of(cliente));
        when(designs3dService.saveDesign(any())).thenReturn(disenio);
        when(projectsRepository.saveProject(any())).thenReturn(proyecto);
        when(mockFile.isEmpty()).thenReturn(false);

        String result = projectsService.saveProject(dto, mockFile);

        assertEquals("Proyecto creado correctamente", result);
        verify(storageService).saveFile(eq(mockFile), any(), eq("proyectos"), eq("imagenes"));
    }

    @Test
    void testSaveProject_userNotFound() throws IOException {
        ProjectDto dto = new ProjectDto();
        dto.setProjectName("Nuevo Proyecto");
        dto.setDescription("Desc");
        dto.setStatus(true);
        dto.setUserId(999L);
        dto.setClientId(2L);
        dto.setDesign3d(new Designs3dDto());

        when(usuarioRepository.getByIdUsuario(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> projectsService.saveProject(dto, mockFile));
        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(usuarioRepository, times(1)).getByIdUsuario(999L);
        verify(clientesRepository, never()).getByIdCliente(anyLong());
        verify(designs3dService, never()).saveDesign(any());
        verify(projectsRepository, never()).saveProject(any());
        verify(storageService, never()).saveFile(any(), any(), any(), any());
    }

    @Test
    void testUpdateProject_successWithNullClient() throws IOException {
        // Crear JsonNode para Designs3dDto
        ObjectNode configuracionNode = objectMapper.createObjectNode();
        configuracionNode.put("ambiental", 0.8);
        ArrayNode materialesNode = objectMapper.createArrayNode();
        materialesNode.add(789);
        ArrayNode objetosNode = objectMapper.createArrayNode();

        Designs3dDto designDto = new Designs3dDto();
        designDto.setIdDisenio(4L);
        designDto.setConfiguracion(configuracionNode);
        designDto.setMateriales(materialesNode);
        designDto.setObjetos(objetosNode);

        ProjectDto dto = new ProjectDto();
        dto.setIdProject(100L);
        dto.setProjectName("Updated Project");
        dto.setDescription("Updated Desc");
        dto.setStatus(false);
        dto.setUserId(1L);
        dto.setClientId(null);
        dto.setDesign3d(designDto);

        when(projectsRepository.getByIdProject(100L)).thenReturn(Optional.of(proyecto));
        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(designs3dService.updateDesign(any(Designs3dDto.class))).thenReturn(disenio);
        when(projectsRepository.saveProject(any(ProyectosEntity.class))).thenReturn(proyecto);
        // Configurar mockFile como vacío
        when(mockFile.isEmpty()).thenReturn(true);

        String result = projectsService.updateProject(dto, mockFile);

        assertEquals("Proyecto actualizado correctamente", result);
        assertNull(proyecto.getCliente());
        verify(projectsRepository, times(1)).getByIdProject(100L);
        verify(usuarioRepository, times(1)).getByIdUsuario(1L);
        verify(clientesRepository, never()).getByIdCliente(anyLong());
        verify(designs3dService, times(1)).updateDesign(any(Designs3dDto.class));
        verify(projectsRepository, times(1)).saveProject(any(ProyectosEntity.class));
        verify(storageService, never()).saveFile(any(), any(), any(), any());
    }

    @Test
    void testUpdateProject_projectNotFound() throws IOException {
        ProjectDto dto = new ProjectDto();
        dto.setIdProject(999L);
        dto.setProjectName("Updated Project");
        dto.setDescription("Updated Desc");
        dto.setStatus(false);
        dto.setUserId(1L);
        dto.setDesign3d(new Designs3dDto());

        when(projectsRepository.getByIdProject(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> projectsService.updateProject(dto, mockFile));
        assertEquals("Proyecto no encontrado", ex.getMessage());
        verify(projectsRepository, times(1)).getByIdProject(999L);
        verify(usuarioRepository, never()).getByIdUsuario(anyLong());
        verify(designs3dService, never()).updateDesign(any());
        verify(projectsRepository, never()).saveProject(any());
        verify(storageService, never()).saveFile(any(), any(), any(), any());
    }

    @Test
    void testDeleteProject_withImage() throws IOException {
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "proyectos")).thenReturn(Optional.of(almacenamiento));
        when(projectsRepository.getByIdProject(100L)).thenReturn(Optional.of(proyecto));
        doNothing().when(storageService).deleteFileById(500L);

        projectsService.deleteProject(100L);

        verify(storageService, times(1)).deleteFileById(500L);
        verify(projectsRepository, times(1)).deleteProject(100L);
        verify(storageRepository, times(1)).findByIdEntidadAndTipoEntidad(100L, "proyectos");
        verify(projectsRepository, times(1)).getByIdProject(100L);
    }

    @Test
    void testDeleteProject_notFound() throws IOException {
        when(projectsRepository.getByIdProject(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> projectsService.deleteProject(999L));
        assertEquals("No se encontró el proyecto con ID: 999", ex.getMessage());
        verify(projectsRepository, times(1)).getByIdProject(999L);
        verify(storageRepository, times(1)).findByIdEntidadAndTipoEntidad(999L, "proyectos");
        verify(storageService, never()).deleteFileById(anyLong());
        verify(projectsRepository, never()).deleteProject(anyLong());
    }
}