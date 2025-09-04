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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new UsuarioEntity();
        usuario.setIdUsuario(1L);

        cliente = new ClientesEntity();
        cliente.setIdCliente(2L);

        disenio = new Disenios3dEntity();
        disenio.setIdDisenio(4L);
        disenio.setMateriales("[1,2]");
        disenio.setObjetos("[10,20]");

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
    }

    @Test
    void testGetByIdProject_found() throws IOException {
        when(projectsRepository.getByIdProject(100L)).thenReturn(Optional.of(proyecto));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "proyectos")).thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(500L)).thenReturn("http://url.com/file");

        // mock de materiales y objetos
        when(materialsService.getMaterialsByIds(anyList())).thenReturn(List.of(new MaterialCompleteDto()));
        when(objectsService.getObjectsByIdsObjects(anyList())).thenReturn(List.of(new ObjectCompleteDto()));

        Optional<ProjectCompleteDto> result = projectsService.getByIdProject(100L);

        assertTrue(result.isPresent());
        assertEquals("Proyecto Test", result.get().getProjectName());
        assertNotNull(result.get().getMaterials());
        assertNotNull(result.get().getObjects());
        verify(storageService).getPresignedUrl(500L);
    }

    @Test
    void testGetByIdProject_notFound() {
        when(projectsRepository.getByIdProject(999L)).thenReturn(Optional.empty());
        Optional<ProjectCompleteDto> result = projectsService.getByIdProject(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveProject_success() throws IOException {
        ProjectDto dto = new ProjectDto();
        dto.setProjectName("Nuevo Proyecto");
        dto.setDescription("Desc");
        dto.setStatus(true);
        dto.setUserId(1L);
        dto.setClientId(2L);
        dto.setScenarioId(3L);
        dto.setDesign3d(new Designs3dDto());

        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(clientesRepository.getByIdCliente(2L)).thenReturn(Optional.of(cliente));
        when(designs3dService.saveDesign(any())).thenReturn(disenio);
        when(projectsRepository.saveProject(any())).thenReturn(proyecto);
        when(mockFile.isEmpty()).thenReturn(false);

        String result = projectsService.saveProject(dto, mockFile);

        assertEquals("Material creado correctamente", result);
        verify(storageService).saveFile(eq(mockFile), any(), eq("proyectos"), eq("imagenes"));
    }

    @Test
    void testUpdateProject_successWithNullClient() throws IOException {
        ProjectDto dto = new ProjectDto();
        dto.setIdProject(100L);
        dto.setProjectName("Updated Project");
        dto.setDescription("Updated Desc");
        dto.setStatus(false);
        dto.setUserId(1L);
        dto.setClientId(null);
        dto.setScenarioId(3L);
        dto.setDesign3d(new Designs3dDto());

        when(projectsRepository.getByIdProject(100L)).thenReturn(Optional.of(proyecto));
        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(designs3dService.updateDesign(any())).thenReturn(disenio);
        when(projectsRepository.saveProject(any())).thenReturn(proyecto);
        when(mockFile.isEmpty()).thenReturn(true);

        String result = projectsService.updateProject(dto, mockFile);

        assertEquals("Proyecto actualizado correctamente", result);
        assertNull(proyecto.getCliente());
    }

    @Test
    void testDeleteProject_withImage() throws IOException {
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "proyectos")).thenReturn(Optional.of(almacenamiento));
        when(projectsRepository.getByIdProject(100L)).thenReturn(Optional.of(proyecto));

        projectsService.deleteProject(100L);

        verify(storageService).deleteFileById(500L);
        verify(projectsRepository).deleteProject(100L);
    }

    @Test
    void testDeleteProject_notFound() {
        when(projectsRepository.getByIdProject(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> projectsService.deleteProject(999L));
        assertEquals("No se encontró el proyecto con ID: 999", ex.getMessage());
    }
}
