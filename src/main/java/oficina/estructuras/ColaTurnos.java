package oficina.estructuras;

import java.util.ArrayList;
import java.util.List;
import oficina.modelo.Turno;

/** Cola FIFO implementada con nodos propios (sin LinkedList ni ArrayDeque). */
public class ColaTurnos {
    private Nodo primero;
    private Nodo ultimo;
    private int tamanio;

    /** Agrega un turno al final de la cola. */
    public void encolar(Turno turno) {
        Nodo nuevo = new Nodo(turno);
        if (ultimo == null) {
            primero = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
        }
        ultimo = nuevo;
        tamanio++;
    }

    /** Retira y devuelve el primer turno; null si la cola está vacía. */
    public Turno desencolar() {
        if (primero == null) {
            return null;
        }
        Turno turno = primero.getDato();
        primero = primero.getSiguiente();
        if (primero == null) {
            ultimo = null;
        }
        tamanio--;
        return turno;
    }

    /** Devuelve el primer turno sin retirarlo; null si está vacía. */
    public Turno verPrimero() {
        return primero == null ? null : primero.getDato();
    }

    /** Quita de la cola el turno con ese número, conservando el orden de los demás. */
    public boolean cancelar(int numero) {
        Nodo anterior = null;
        Nodo actual = primero;
        while (actual != null) {
            if (actual.getDato().getNumero() == numero) {
                if (anterior == null) {
                    primero = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                if (actual == ultimo) {
                    ultimo = anterior;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    public Turno buscar(int numero) {
        Nodo actual = primero;
        while (actual != null) {
            if (actual.getDato().getNumero() == numero) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /** Recorre la cola de primero a último. */
    public List<Turno> recorrer() {
        List<Turno> turnos = new ArrayList<>();
        Nodo actual = primero;
        while (actual != null) {
            turnos.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return turnos;
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int tamanio() {
        return tamanio;
    }
}
