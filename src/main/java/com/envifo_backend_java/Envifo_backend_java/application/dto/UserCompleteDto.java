package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserCompleteDto {

    // Atributos
    private Long idUsuario;

    private LocalDateTime dateRecord;

    private boolean state;

    private String userName;

    private String firstName;

    private String middleName;

    private String firstSurname;

    private String secondSurname;

    private String age;

    private String phone;

    private String email;

    private String password;

    private RolDto rol;

    private Optional<StorageDto> images;

    // Constructores

    public UserCompleteDto() {
    }

    public UserCompleteDto(Long idUsuario, LocalDateTime dateRecord, boolean state, String userName,
                           String firstName, String middleName, String firstSurname, String secondSurname,
                           String age, String phone, String email, String password, RolDto rol,
                           Optional<StorageDto> images) {
        this.idUsuario = idUsuario;
        this.dateRecord = dateRecord;
        this.state = state;
        this.userName = userName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.images = images;
    }

    // Getters and Setters

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(LocalDateTime dateRecord) {
        this.dateRecord = dateRecord;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolDto getRol() {
        return rol;
    }

    public void setRol(RolDto rol) {
        this.rol = rol;
    }

    public Optional<StorageDto> getImages() {
        return images;
    }

    public void setImages(Optional<StorageDto> images) {
        this.images = images;
    }
}
