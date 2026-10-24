package oficina.estructuras;

import oficina.modelo.Turno;

/** Nodo de la lista enlazada que usa la cola de turnos. */
public class Nodo {
    private final Turno dato;
    private Nodo siguiente;

    public Nodo(Turno dato) {
        this.dato = dato;
    }

    public Turno getDato() { return dato; }
    public Nodo getSiguiente() { return siguiente; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }
}
