package com.envifo_backend_java.Envifo_backend_java.application.dto;


import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class UserDto {


//Atributos

    private Long idUsuario;

    private LocalDateTime dateRecord;

    private Boolean state;

    private String userName;

    private String firstName;

    private String middleName;

    private String firstSurname;

    private String secondSurname;

    private String age;

    private String phone;

    private String email;

    private String password;

    private RolesEntity rol;

    private Long idRolAssigned;


    //Constructores


    public UserDto(Long idUsuario, LocalDateTime dateRecord, Boolean state, String userName, String firstName, String middleName, String firstSurname, String secondSurname, String age, String phone, String email, String password, RolesEntity rol, Long idRolAssigned) {
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
        this.idRolAssigned = idRolAssigned;
    }

    public UserDto() {
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
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

    public RolesEntity getRol() {
        return rol;
    }

    public void setRol(RolesEntity rol) {
        this.rol = rol;
    }

    public Long getIdRolAssigned() {
        return idRolAssigned;
    }

    public void setIdRolAssigned(Long idRolAssigned) {
        this.idRolAssigned = idRolAssigned;
    }
}
