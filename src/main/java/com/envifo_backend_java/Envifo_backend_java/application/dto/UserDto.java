package com.envifo_backend_java.Envifo_backend_java.application.dto;


import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;

import java.time.LocalDateTime;

public class UserDto {


//Atributos


    private Long idUsuario;

    private LocalDateTime dateRecord;

    private boolean state;

    private String userName;

    private String name;

    private String lastName;

    private int age;

    private String phone;

    private String email;

    private String password;

    private String birthDate;

    private RolesEntity rol;


    //Constructores

    public UserDto(Long idUsuario, LocalDateTime dateRecord, boolean state, String userName, String name,
                   String lastName, int age, String phone, String email, String password,
                   String birthDate, RolesEntity rol) {

        this.idUsuario = idUsuario;
        this.dateRecord = dateRecord;
        this.state = state;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.rol = rol;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public RolesEntity getRol() {
        return rol;
    }

    public void setRol(RolesEntity rol) {
        this.rol = rol;
    }

}
