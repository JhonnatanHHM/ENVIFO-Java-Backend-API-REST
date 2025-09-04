package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProjectCompleteDto {

    private Long idProject;
    private String projectName;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private boolean status;
    private Long userId;
    private Long clientId;
    private Designs3dDto design;
    private List<MaterialCompleteDto> materials;
    private List<ObjectCompleteDto> objects;
    private Optional<StorageDto> proyect;

    public ProjectCompleteDto() {
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Designs3dDto getDesign() {
        return design;
    }

    public void setDesign(Designs3dDto design) {
        this.design = design;
    }

    public List<MaterialCompleteDto> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialCompleteDto> materials) {
        this.materials = materials;
    }

    public List<ObjectCompleteDto> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectCompleteDto> objects) {
        this.objects = objects;
    }

    public Optional<StorageDto> getProyect() {
        return proyect;
    }

    public void setProyect(Optional<StorageDto> proyect) {
        this.proyect = proyect;
    }
}
