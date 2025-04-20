package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="permisos")
public class PermisosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long idPermiso;

    @Column(name = "edit_permisos")
    private boolean editPermisos;

    @Column(name = "vista_usuarios")
    private boolean vistaUsuarios;

    @Column(name = "edit_usuarios")
    private boolean editUsuarios;

    @Column(name = "vista_proyectos")
    private boolean vistaProyectos;

    @Column(name = "edit_proyectos")
    private boolean editProyectos;

    @Column(name = "vista_disenios_3d")
    private boolean vistaDisenios3d;

    @Column(name = "edit_disenios_3d")
    private boolean editDisenios3d;

    @Column(name = "vista_materiales")
    private boolean vistaMateriales;

    @Column(name = "edit_materiales")
    private boolean editMateriales;

    @Column(name = "vista_informes")
    private boolean vistaInformes;

    @Column(name = "vista_categorias")
    private boolean vistaCategorias;

    @Column(name = "edit_categorias")
    private boolean editCategorias;

    // Constructores

    public PermisosEntity() {

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
