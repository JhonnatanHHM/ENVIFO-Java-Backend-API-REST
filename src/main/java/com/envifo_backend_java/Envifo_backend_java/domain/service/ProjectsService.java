package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectFilteredDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProjectsService {

    Optional<ProjectCompleteDto> getByIdProject(Long idProject);
    List<ProjectFilteredDto> getProjectByCustomer(Long idCustomer);
    List<ProjectFilteredDto> getProjectByUser(Long idUser);
    List<ProjectFilteredDto> getProjectByUserAndCustomer(Long idUser, Long idCustomer);
    String saveProject (ProjectDto project, MultipartFile image) throws IOException;
    String updateProject (ProjectDto project, MultipartFile image) throws IOException;
    void deleteProject (Long idProjecto);
}
