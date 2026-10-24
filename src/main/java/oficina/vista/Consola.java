package oficina.vista;

import java.util.Scanner;

/** Lectura de datos por consola con validación básica (vacíos y números). */
public class Consola {
    private final Scanner scanner = new Scanner(System.in);

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Valor numérico incorrecto. Intente de nuevo.");
            }
        }
    }

    public String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("El campo no puede estar vacío. Intente de nuevo.");
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
}
