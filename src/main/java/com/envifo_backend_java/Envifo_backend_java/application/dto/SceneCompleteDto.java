package com.envifo_backend_java.Envifo_backend_java.application.dto;

public class SceneCompleteDto {

    private Long idScene;
    private String sceneName;
    private String description;
    private boolean status;
    private String metadata;
    private StorageDto image;

    public SceneCompleteDto() {
    }

    public Long getIdScene() {
        return idScene;
    }

    public void setIdScene(Long idScene) {
        this.idScene = idScene;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public StorageDto getImage() {
        return image;
    }

    public void setImage(StorageDto image) {
        this.image = image;
    }
}
