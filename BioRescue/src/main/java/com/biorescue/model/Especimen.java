package com.biorescue.model;

public class Especimen {
    private String codigoFicha;
    private String nombreComun;
    private String claseFauna;
    private String estadoSalud;
    private String fechaIngreso;
    private String zona;

    public Especimen(String codigoFicha, String nombreComun, String claseFauna, String estadoSalud, String fechaIngreso, String zona) {
        this.codigoFicha = codigoFicha;
        this.nombreComun = nombreComun;
        this.claseFauna = claseFauna;
        this.estadoSalud = estadoSalud;
        this.fechaIngreso = fechaIngreso;
        this.zona = zona;
    }

    public String getCodigoFicha() {
        return codigoFicha;
    }

    public String getNombreComun() {
        return nombreComun;
    }

    public String getClaseFauna() {
        return claseFauna;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public String getZona() {
        return zona;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }
}
