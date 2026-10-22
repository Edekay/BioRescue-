package com.biorescue.service;

import com.biorescue.model.Especimen;
import java.io.*;
import java.util.*;

public class CentroRescate {
    private final List<Especimen> especimenes = new ArrayList<>();
    private final String RUTA_ARCHIVO = "especimenes.csv";

    public CentroRescate() {
        cargarDesdeArchivo();
    }

    public boolean registrarEspecimen(Especimen nuevo) {
        if (nuevo == null || buscarPorCodigo(nuevo.getCodigoFicha()) != null) {
            return false;
        }
        boolean agregado = especimenes.add(nuevo);
        if (agregado) {
            guardarEnArchivo();
        }
        return agregado;
    }

    public Especimen buscarPorCodigo(String codigoFicha) {
        if (codigoFicha == null) return null;
        for (Especimen e : especimenes) {
            if (e.getCodigoFicha().equalsIgnoreCase(codigoFicha.trim())) {
                return e;
            }
        }
        return null;
    }

    public boolean actualizarEstadoSalud(String codigo, String nuevoEstado) {
        Especimen e = buscarPorCodigo(codigo);
        if (e != null) {
            e.setEstadoSalud(nuevoEstado);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public boolean eliminarEspecimen(String codigo) {
        Especimen e = buscarPorCodigo(codigo);
        if (e != null) {
            especimenes.remove(e);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public List<Especimen> listarEspecimenes() {
        return new ArrayList<>(especimenes);
    }

    public List<Especimen> filtrarPorZona(String zona) {
        List<Especimen> filtrados = new ArrayList<>();
        for (Especimen e : especimenes) {
            if (e.getZona().equalsIgnoreCase(zona.trim())) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }

    public Set<String> obtenerClasesFauna() {
        Set<String> clases = new HashSet<>();
        for (Especimen e : especimenes) {
            clases.add(e.getClaseFauna());
        }
        return clases;
    }

    public Map<String, Integer> contarPorEstadoSalud() {
        Map<String, Integer> conteo = new HashMap<>();
        for (Especimen e : especimenes) {
            String estado = e.getEstadoSalud();
            conteo.put(estado, conteo.getOrDefault(estado, 0) + 1);
        }
        return conteo;
    }

    // Persistencia: Guardar datos en archivo CSV
    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Especimen e : especimenes) {
                pw.println(e.getCodigoFicha() + ";" + e.getNombreComun() + ";" + 
                           e.getClaseFauna() + ";" + e.getEstadoSalud() + ";" + 
                           e.getFechaIngreso() + ";" + e.getZona());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo: " + e.getMessage());
        }
    }

    // Persistencia: Cargar datos desde archivo CSV al iniciar
    private void cargarDesdeArchivo() {
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 6) {
                    especimenes.add(new Especimen(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar desde el archivo: " + e.getMessage());
        }
    }
}