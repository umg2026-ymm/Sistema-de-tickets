package oficina.estructuras;

import java.util.ArrayList;
import java.util.List;
import oficina.modelo.Cliente;

/** Árbol binario de búsqueda de clientes, con el código como clave. */
public class ArbolClientes {

    private static class NodoArbol {
        private final Cliente cliente;
        private NodoArbol izquierdo;
        private NodoArbol derecho;

        private NodoArbol(Cliente cliente) {
            this.cliente = cliente;
        }
    }

    private NodoArbol raiz;

    /** Inserta un cliente; devuelve false si el código ya existe. */
    public boolean insertar(Cliente cliente) {
        if (buscar(cliente.getCodigo()) != null) {
            return false;
        }
        raiz = insertarRec(raiz, cliente);
        return true;
    }

    private NodoArbol insertarRec(NodoArbol nodo, Cliente cliente) {
        if (nodo == null) {
            return new NodoArbol(cliente);
        }
        if (cliente.getCodigo() < nodo.cliente.getCodigo()) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, cliente);
        } else {
            nodo.derecho = insertarRec(nodo.derecho, cliente);
        }
        return nodo;
    }

    /** Búsqueda recursiva; null si no existe. */
    public Cliente buscar(int codigo) {
        return buscarRec(raiz, codigo);
    }

    private Cliente buscarRec(NodoArbol nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo == nodo.cliente.getCodigo()) {
            return nodo.cliente;
        }
        return codigo < nodo.cliente.getCodigo()
                ? buscarRec(nodo.izquierdo, codigo)
                : buscarRec(nodo.derecho, codigo);
    }

    /** Recorrido inorden recursivo: clientes ordenados por código. */
    public List<Cliente> inorden() {
        List<Cliente> clientes = new ArrayList<>();
        inordenRec(raiz, clientes);
        return clientes;
    }

    private void inordenRec(NodoArbol nodo, List<Cliente> resultado) {
        if (nodo == null) {
            return;
        }
        inordenRec(nodo.izquierdo, resultado);
        resultado.add(nodo.cliente);
        inordenRec(nodo.derecho, resultado);
    }
}
