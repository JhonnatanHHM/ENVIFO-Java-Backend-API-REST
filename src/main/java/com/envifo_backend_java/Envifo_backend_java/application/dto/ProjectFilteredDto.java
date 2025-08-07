package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.util.Optional;

public class ProjectFilteredDto {

    private Long idProject;
    private String projectName;
    private String description;
    private StorageDto proyect;

    public ProjectFilteredDto() {
    }

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StorageDto getProyect() {
        return proyect;
    }

    public void setProyect(StorageDto proyect) {
        this.proyect = proyect;
    }
}
