package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class App {

    // 🔹 1. Leer el archivo JSON desde un .txt
    public static String leerArchivo(String rutaArchivo) {
        try {
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 🔹 2. Obtener transacciones de un usuario específico
    public static List<JSONObject> obtenerTransacciones(String jsonData, String usuario) {
        List<JSONObject> transaccionesUsuario = new ArrayList<>();
        try {
            JSONObject transacciones = new JSONObject(jsonData);
            JSONArray transaccionesArray = transacciones.getJSONArray(usuario);
            for (int i = 0; i < transaccionesArray.length(); i++) {
                transaccionesUsuario.add(transaccionesArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return transaccionesUsuario;
    }

    // 🔹 3. Generar extracto bancario en un archivo .txt
    public static void generarExtracto(String usuario, List<JSONObject> transacciones) {
        String nombreArchivo = "extracto_" + usuario + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("Extracto bancario para el usuario: " + usuario + "\n\n");
            for (JSONObject transaccion : transacciones) {
                writer.write(transaccion.toString() + "\n");
            }
            System.out.println("Extracto bancario generado: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar usuario
        System.out.print("Ingrese el nombre del usuario: ");
        String usuario = scanner.nextLine();

        // Leer archivo JSON
        String rutaArchivo = "resources/transactions.txt"; // Ruta correcta del archivo
        String jsonData = leerArchivo(rutaArchivo);
        if (jsonData == null) {
            System.out.println("Error al leer el archivo de transacciones.");
            return;
        }

        // Obtener transacciones del usuario
        List<JSONObject> transacciones = obtenerTransacciones(jsonData, usuario);

        // Generar extracto bancario
        generarExtracto(usuario, transacciones);
    }
}