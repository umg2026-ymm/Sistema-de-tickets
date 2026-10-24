-- Crea la base de datos desde cero y carga los datos iniciales (sin turnos).
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS clientes (
    codigo   INTEGER PRIMARY KEY CHECK (codigo > 0),
    nombre   TEXT    NOT NULL CHECK (length(trim(nombre)) > 0),
    telefono TEXT    NOT NULL CHECK (length(telefono) = 8),
    activo   INTEGER NOT NULL DEFAULT 1 CHECK (activo IN (0, 1))
);

CREATE TABLE IF NOT EXISTS servicios (
    codigo INTEGER PRIMARY KEY CHECK (codigo > 0),
    nombre TEXT    NOT NULL CHECK (length(trim(nombre)) > 0),
    activo INTEGER NOT NULL DEFAULT 1 CHECK (activo IN (0, 1))
);

CREATE TABLE IF NOT EXISTS turnos (
    numero           INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente_codigo   INTEGER NOT NULL,
    servicio_codigo  INTEGER NOT NULL,
    fecha_generacion TEXT    NOT NULL,
    estado           TEXT    NOT NULL CHECK (estado IN ('PENDIENTE', 'ATENDIDO', 'CANCELADO')),
    fecha_cierre     TEXT,
    FOREIGN KEY (cliente_codigo)  REFERENCES clientes (codigo),
    FOREIGN KEY (servicio_codigo) REFERENCES servicios (codigo)
);

-- Un cliente solo puede tener un turno pendiente a la vez
CREATE UNIQUE INDEX IF NOT EXISTS ux_turno_pendiente_cliente
    ON turnos (cliente_codigo) WHERE estado = 'PENDIENTE';

INSERT INTO clientes (codigo, nombre, telefono, activo) VALUES (1001, 'Ana López',      '55501001', 1);
INSERT INTO clientes (codigo, nombre, telefono, activo) VALUES (1002, 'Carlos Pérez',   '55501002', 1);
INSERT INTO clientes (codigo, nombre, telefono, activo) VALUES (1003, 'María García',   '55501003', 1);
INSERT INTO clientes (codigo, nombre, telefono, activo) VALUES (1004, 'José Hernández', '55501004', 1);
INSERT INTO clientes (codigo, nombre, telefono, activo) VALUES (1005, 'Luisa Morales',  '55501005', 1);

INSERT INTO servicios (codigo, nombre, activo) VALUES (1, 'Información',           1);
INSERT INTO servicios (codigo, nombre, activo) VALUES (2, 'Entrega de documentos', 1);
INSERT INTO servicios (codigo, nombre, activo) VALUES (3, 'Actualización de datos', 1);
