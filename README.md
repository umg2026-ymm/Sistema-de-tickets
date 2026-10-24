# Sistema de Atención de Turnos

Proyecto de Programación II — Java 21, SQLite, JDBC, menú de consola.

## Integrantes
- Nombre: Yerli Isabel Jumique López   Carné: 998925805
- Nombre: Melanny Saraí Flores Rivas   Carné: 99892514900
- Nombre: Marvin Antonio Yanes Hernández  Carné: 7690526482

## Distribución del trabajo
Proyecto realizado de forma grupal: clientes y BST, turnos y cola enlazada, servicios, persistencia y reportes.

## Repositorio Git
Enlace: https://github.com/umg2026-ymm/Sistema-de-tickets.git

## Informe de la secuencia
- docs/informe_secuencia.pdf

## Diagramas
- docs/diagrama_uml.png
- docs/diagrama_er.png


## Requisitos
- JDK 21
- Maven 3.9+ (o la extensión "Maven for Java" de VS Code)
- VS Code con "Extension Pack for Java"

## Abrir y ejecutar en VS Code
1. Abrir la carpeta del proyecto (File > Open Folder), la que contiene `pom.xml`.
2. Esperar a que VS Code importe el proyecto Maven (descarga el driver SQLite).
3. Abrir una terminal en la raíz del proyecto y ejecutar: `mvn -q compile exec:java`
   (o abrir `src/main/java/oficina/Main.java` y pulsar "Run").
4. Siempre se debe ejecutar desde la raíz del proyecto: las rutas `data/turnos.db` y `sql/crear_y_cargar.sql` son relativas.

## Crear la base de datos desde el script
- Automático: si `data/turnos.db` no existe, la aplicación la crea al iniciar usando `sql/crear_y_cargar.sql`.
- Manual (con el cliente `sqlite3`): `sqlite3 data/turnos.db < sql/crear_y_cargar.sql`
- Para reiniciar los datos: cerrar la aplicación, borrar `data/turnos.db` y volver a ejecutar.
  La aplicación nunca borra ni reinicia los datos por sí sola.

## Estructura
- `modelo/` Cliente, Servicio, Turno, EstadoTurno, ResumenServicio
- `estructuras/` Nodo, ColaTurnos (lista enlazada propia), ArbolClientes (BST)
- `dao/` ConexionSqlite, interfaces DAO y su implementación SQLite
- `logica/` servicios de negocio y validaciones
- `vista/` menú de consola
- `docs/` diagramas UML y ER, informe de la secuencia
