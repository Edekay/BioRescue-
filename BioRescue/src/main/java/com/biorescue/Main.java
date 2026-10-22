package com.biorescue;

import com.biorescue.model.Especimen;
import com.biorescue.service.CentroRescate;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CentroRescate centro = new CentroRescate();

    public static void main(String[] args) {
        int opcion = 0;
        do {
            System.out.println("\n==========================================");
            System.out.println("      BIORESCUE - GESTIÓN DE FAUNA        ");
            System.out.println("==========================================");
            System.out.println("1. Registrar Especímen");
            System.out.println("2. Listar todos los Especímenes");
            System.out.println("3. Buscar Especímen por Código");
            System.out.println("4. Actualizar Estado de Salud");
            System.out.println("5. Eliminar Especímen");
            System.out.println("6. Filtrar por Zona de Rescate");
            System.out.println("7. Ver Clases de Fauna Únicas");
            
            System.out.println("8. Reporte por Estado de Salud");
            System.out.println("9. Salir");
            System.out.print("Elija una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> registrarIngreso();
                    case 2 -> listarEspecimenes();
                    case 3 -> buscarPorFicha();
                    case 4 -> actualizarEstado();
                    case 5 -> eliminarEspecimen();
                    case 6 -> filtrarPorZona();
                    case 7 -> System.out.println("\nClases de Fauna: " + centro.obtenerClasesFauna());
                    case 8 -> System.out.println("\nReporte por Estado: " + centro.contarPorEstadoSalud());
                    case 9 -> System.out.println("¡Saliendo del sistema BioRescue!");
                    default -> System.out.println("⚠️ Opción inválida.");
                }
            } else {
                System.out.println("⚠️ Debe ingresar un número entero válido.");
                scanner.next();
            }
        } while (opcion != 9);
    }

    private static String leerTextoObligatorio(String mensaje) {
        String texto = "";
        while (texto.trim().isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine();
            if (texto.trim().isEmpty()) {
                System.out.println("⚠️ El campo no puede estar vacío.");
            }
        }
        return texto.trim();
    }

    private static void registrarIngreso() {
        System.out.println("\n--- REGISTRO DE NUEVO INGRESO ---");
        String codigo = leerTextoObligatorio("Código de ficha (ej. BIO-001): ");
        String nombre = leerTextoObligatorio("Nombre común (ej. Oso de anteojos): ");
        String clase = leerTextoObligatorio("Clase de fauna (ej. MAMIFERO, AVE, REPTIL): ");
        String estado = leerTextoObligatorio("Estado de salud (ej. CRITICO, ESTABLE): ");
        String zona = leerTextoObligatorio("Zona de rescate: ");

        LocalDate fechaIngreso = null;
        while (fechaIngreso == null) {
            String fechaStr = leerTextoObligatorio("Fecha de ingreso (AAAA-MM-DD): ");
            try {
                fechaIngreso = LocalDate.parse(fechaStr);
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Formato incorrecto. Use el formato AAAA-MM-DD.");
            }
        }

        Especimen nuevo = new Especimen(codigo, nombre, clase, estado, fechaIngreso.toString(), zona);
        if (centro.registrarEspecimen(nuevo)) {
            System.out.println("✅ Especímen registrado y guardado en archivo exitosamente.");
        } else {
            System.out.println("❌ Error: Ya existe un animal registrado con ese código.");
        }
    }

    private static void listarEspecimenes() {
        var lista = centro.listarEspecimenes();
        if (lista.isEmpty()) {
            System.out.println("\nNo hay especímenes registrados actualmente.");
            return;
        }
        System.out.println("\n--- LISTADO DE ESPECÍMENES ---");
        for (Especimen e : lista) {
            System.out.printf("Ficha: %s | Nombre: %s | Clase: %s | Estado: %s | Fecha: %s | Zona: %s%n",
                    e.getCodigoFicha(), e.getNombreComun(), e.getClaseFauna(), e.getEstadoSalud(), e.getFechaIngreso(), e.getZona());
        }
    }

    private static void buscarPorFicha() {
        String codigo = leerTextoObligatorio("\nCódigo a buscar: ");
        Especimen e = centro.buscarPorCodigo(codigo);
        if (e != null) {
            System.out.printf("Encontrado: %s (%s) - Estado: %s | Zona: %s%n",
                    e.getNombreComun(), e.getClaseFauna(), e.getEstadoSalud(), e.getZona());
        } else {
            System.out.println("❌ No se encontró ningún especímen.");
        }
    }

    private static void actualizarEstado() {
        String codigo = leerTextoObligatorio("\nCódigo del especímen a actualizar: ");
        String nuevoEstado = leerTextoObligatorio("Nuevo estado de salud: ");
        if (centro.actualizarEstadoSalud(codigo, nuevoEstado)) {
            System.out.println("✅ Estado actualizado e información guardada.");
        } else {
            System.out.println("❌ Especímen no encontrado.");
        }
    }

    private static void eliminarEspecimen() {
        String codigo = leerTextoObligatorio("\nCódigo del especímen a eliminar: ");
        if (centro.eliminarEspecimen(codigo)) {
            System.out.println("✅ Especímen eliminado del registro y del archivo.");
        } else {
            System.out.println("❌ No se encontró el registro.");
        }
    }

    private static void filtrarPorZona() {
        String zona = leerTextoObligatorio("\nZona a consultar: ");
        var filtrados = centro.filtrarPorZona(zona);
        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron registros en esa zona.");
        } else {
            for (Especimen e : filtrados) {
                System.out.println("- " + e.getNombreComun() + " [" + e.getCodigoFicha() + "]");
            }
        }
    }
}