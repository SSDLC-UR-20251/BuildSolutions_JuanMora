package com.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testLeerArchivo() {
        String rutaArchivo = "resources/transactions.txt";
        String contenido = App.leerArchivo(rutaArchivo);
        assertNotNull(contenido, "El contenido del archivo no debe ser nulo");
        assertFalse(contenido.isEmpty(), "El contenido del archivo no debe estar vacío");
    }

    @Test
    public void testObtenerTransacciones() {
        String rutaArchivo = "resources/transactions.txt";
        String jsonData = App.leerArchivo(rutaArchivo);
        assertNotNull(jsonData, "El contenido del archivo no debe ser nulo");

        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, "juan.jose@urosario.edu.co");
        assertNotNull(transacciones, "La lista de transacciones no debe ser nula");
        assertFalse(transacciones.isEmpty(), "La lista de transacciones no debe estar vacía");
        assertEquals(3, transacciones.size(), "El usuario debe tener 3 transacciones");
    }

    @Test
    public void testGenerarExtracto() {
        String usuario = "juan.jose@urosario.edu.co";
        String rutaArchivo = "resources/transactions.txt";
        String jsonData = App.leerArchivo(rutaArchivo);
        assertNotNull(jsonData, "El contenido del archivo no debe ser nulo");

        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, usuario);
        assertNotNull(transacciones, "La lista de transacciones no debe ser nula");

        App.generarExtracto(usuario, transacciones);

        File extracto = new File("extracto_" + usuario + ".txt");
        assertTrue(extracto.exists(), "El archivo de extracto debe existir");
        assertTrue(extracto.length() > 0, "El archivo de extracto no debe estar vacío");

        // Clean up
        extracto.delete();
    }
}