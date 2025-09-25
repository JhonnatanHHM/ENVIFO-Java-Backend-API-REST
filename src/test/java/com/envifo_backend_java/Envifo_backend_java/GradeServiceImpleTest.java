package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.GradesDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.GradeServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.NotasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.NotasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime; // <-- agregado
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeServiceImpleTest {

    private NotasRepository notasRepository;
    private GradeServiceImple gradeService;

    @BeforeEach
    void setUp() {
        notasRepository = mock(NotasRepository.class);
        gradeService = new GradeServiceImple();
        gradeService.notasRepository = notasRepository; // inyección simulada
    }

    private NotasEntity createNota(Long id, Long idUsuario, Long idCliente, String titulo, String contenido) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(idUsuario);
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(idCliente);

        NotasEntity nota = new NotasEntity();
        nota.setIdNota(id);
        nota.setTitulo(titulo);
        nota.setContenido(contenido);
        nota.setUsuario(usuario);
        nota.setCliente(cliente);
        nota.setFechaModificacion(LocalDateTime.now()); // <-- agregado
        return nota;
    }

    @Test
    void testSave() {
        GradesDto dto = new GradesDto(
                1L,
                "Titulo",
                LocalDateTime.now(), // <-- agregado updatedDate
                "Contenido",
                10L,
                20L
        );
        NotasEntity savedEntity = createNota(1L, 10L, 20L, "Titulo", "Contenido");

        when(notasRepository.save(any(NotasEntity.class))).thenReturn(savedEntity);

        GradesDto result = gradeService.save(dto);

        assertEquals(dto.getIdGrade(), result.getIdGrade());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getContent(), result.getContent());
        assertEquals(dto.getIdUser(), result.getIdUser());
        assertEquals(dto.getIdCustomer(), result.getIdCustomer());
        assertNotNull(result.getUpdatedDate()); // <-- validamos fecha
    }

    @Test
    void testGetByIdGrade_Found() {
        NotasEntity entity = createNota(2L, 11L, 22L, "Nota 2", "Contenido 2");
        when(notasRepository.getByIdGrade(2L)).thenReturn(Optional.of(entity));

        Optional<GradesDto> result = gradeService.getByIdGrade(2L);

        assertTrue(result.isPresent());
        assertEquals("Nota 2", result.get().getTitle());
    }

    @Test
    void testGetAllGrades() {
        List<NotasEntity> entities = Arrays.asList(
                createNota(1L, 10L, 20L, "Titulo1", "Contenido1"),
                createNota(2L, 11L, 21L, "Titulo2", "Contenido2")
        );
        when(notasRepository.getAllGrades()).thenReturn(entities);

        List<GradesDto> result = gradeService.getAllGrades();

        assertEquals(2, result.size());
        assertEquals("Titulo1", result.get(0).getTitle());
    }

    @Test
    void testSearchByTitleOrContent() {
        List<NotasEntity> entities = List.of(createNota(3L, 12L, 22L, "Busqueda", "Algo"));
        when(notasRepository.searchByTitleOrContent("Busqueda")).thenReturn(entities);

        List<GradesDto> result = gradeService.searchByTitleOrContent("Busqueda");

        assertEquals(1, result.size());
        assertEquals("Busqueda", result.get(0).getTitle());
    }

    @Test
    void testDelete() {
        gradeService.delete(5L);
        verify(notasRepository, times(1)).delete(5L);
    }

    @Test
    void testEditGrade() {
        GradesDto dto = new GradesDto(
                4L,
                "Editado",
                LocalDateTime.now(), // <-- agregado updatedDate
                "Contenido editado",
                15L,
                25L
        );

        gradeService.editGrade(dto);

        ArgumentCaptor<NotasEntity> captor = ArgumentCaptor.forClass(NotasEntity.class);
        verify(notasRepository).save(captor.capture());

        NotasEntity captured = captor.getValue();
        assertEquals(dto.getIdGrade(), captured.getIdNota());
        assertEquals(dto.getTitle(), captured.getTitulo());
    }

    @Test
    void testGetByIdUsuario() {
        when(notasRepository.getByIdUsuario(100L))
                .thenReturn(List.of(createNota(1L, 100L, 200L, "U1", "C1")));

        List<GradesDto> result = gradeService.getByIdUsuario(100L);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getIdUser());
    }

    @Test
    void testGetByIdCustomer() {
        when(notasRepository.getByIdCliente(200L))
                .thenReturn(List.of(createNota(2L, 100L, 200L, "Titulo", "Contenido")));

        List<GradesDto> result = gradeService.getByIdCustomer(200L);

        assertEquals(1, result.size());
        assertEquals(200L, result.get(0).getIdCustomer());
    }

    @Test
    void testGetGradesFilterByUser() {
        when(notasRepository.getGradesFilterByUser("data", 10L))
                .thenReturn(List.of(createNota(3L, 10L, 20L, "Filtrado", "Contenido")));

        List<GradesDto> result = gradeService.getGradesFilterByUser("data", 10L);

        assertEquals(1, result.size());
        assertEquals("Filtrado", result.get(0).getTitle());
    }

    @Test
    void testGetGradesFilterByClient() {
        when(notasRepository.getGradesFilterByClient("data", 20L))
                .thenReturn(List.of(createNota(4L, 10L, 20L, "Cliente", "Nota Cliente")));

        List<GradesDto> result = gradeService.getGradesFilterByClient("data", 20L);

        assertEquals(1, result.size());
        assertEquals("Cliente", result.get(0).getTitle());
    }
}
