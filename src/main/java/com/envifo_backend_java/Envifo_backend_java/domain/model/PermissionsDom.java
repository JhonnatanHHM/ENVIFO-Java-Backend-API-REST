package com.envifo_backend_java.Envifo_backend_java.domain.model;

public class PermissionsDom {

    private Long idPermiso;

    private boolean editPermisos;

    private boolean vistaUsuarios;

    private boolean editUsuarios;

    private boolean vistaProyectos;

    private boolean editProyectos;

    private boolean vistaDisenios3d;

    private boolean editDisenios3d;

    private boolean vistaMateriales;

    private boolean editMateriales;

    private boolean vistaInformes;

    private boolean vistaCategorias;

    private boolean editCategorias;

    // Constructores

    public PermissionsDom() {

    }

    //Getter and Setter


    public Long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public boolean isEditPermisos() {
        return editPermisos;
    }

    public void setEditPermisos(boolean editPermisos) {
        this.editPermisos = editPermisos;
    }

    public boolean isVistaUsuarios() {
        return vistaUsuarios;
    }

    public void setVistaUsuarios(boolean vistaUsuarios) {
        this.vistaUsuarios = vistaUsuarios;
    }

    public boolean isEditUsuarios() {
        return editUsuarios;
    }

    public void setEditUsuarios(boolean editUsuarios) {
        this.editUsuarios = editUsuarios;
    }

    public boolean isVistaProyectos() {
        return vistaProyectos;
    }

    public void setVistaProyectos(boolean vistaProyectos) {
        this.vistaProyectos = vistaProyectos;
    }

    public boolean isEditProyectos() {
        return editProyectos;
    }

    public void setEditProyectos(boolean editProyectos) {
        this.editProyectos = editProyectos;
    }

    public boolean isVistaDisenios3d() {
        return vistaDisenios3d;
    }

    public void setVistaDisenios3d(boolean vistaDisenios3d) {
        this.vistaDisenios3d = vistaDisenios3d;
    }

    public boolean isEditDisenios3d() {
        return editDisenios3d;
    }

    public void setEditDisenios3d(boolean editDisenios3d) {
        this.editDisenios3d = editDisenios3d;
    }

    public boolean isVistaMateriales() {
        return vistaMateriales;
    }

    public void setVistaMateriales(boolean vistaMateriales) {
        this.vistaMateriales = vistaMateriales;
    }

    public boolean isEditMateriales() {
        return editMateriales;
    }

    public void setEditMateriales(boolean editMateriales) {
        this.editMateriales = editMateriales;
    }

    public boolean isVistaInformes() {
        return vistaInformes;
    }

    public void setVistaInformes(boolean vistaInformes) {
        this.vistaInformes = vistaInformes;
    }

    public boolean isVistaCategorias() {
        return vistaCategorias;
    }

    public void setVistaCategorias(boolean vistaCategorias) {
        this.vistaCategorias = vistaCategorias;
    }

    public boolean isEditCategorias() {
        return editCategorias;
    }

    public void setEditCategorias(boolean editCategorias) {
        this.editCategorias = editCategorias;
    }
}
