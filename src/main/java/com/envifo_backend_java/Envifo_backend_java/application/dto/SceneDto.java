package com.envifo_backend_java.Envifo_backend_java.application.dto;

public class SceneDto {

    private Long idScene;
    private String sceneName;
    private String description;
    private boolean status;
    private Long categoryId;
    private String metadata;

    public Long getIdScene() {
        return idScene;
    }

    public void setIdScene(Long idScene) {
        this.idScene = idScene;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}
