package com.envifo_backend_java.Envifo_backend_java.application.dto;


public class ProjectDto {

    private Long idProject;
    private String projectName;
    private String description;
    private boolean status;
    private Long userId;
    private Long clientId;
    private Long scenarioId;
    private Designs3dDto design3d;

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

    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
    }

    public Designs3dDto getDesign3d() {
        return design3d;
    }

    public void setDesign3d(Designs3dDto design3d) {
        this.design3d = design3d;
    }
}
