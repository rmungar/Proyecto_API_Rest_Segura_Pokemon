# Proyecto_API_Rest_Segura

# Punto 1 - La idea
  La idea de esta API consiste en una "implementación" de la *PokéAPI* de forma mas sencilla. En mi caso, cuento con un archivo de texto plano en el cual figuran todos los datos de todos y cada uno de los     pokemons y movimientos. Por lo que he pensado poblar la base de datos con los datos que ya hay en los documentos para luego poder realizar las llamadas pertinentes. La idea sería una aplicación muy          parecida a la DataDex o la wiki de pokémon.

> [!WARNING]
> En el archivo no figuran las imágenes de los pokemons.
  
# Punto 2 - Las tablas
  **Pokemons**
  
```sql
CREATE TABLE Pokemons (
    idPokemon INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    tipo1 VARCHAR(15) NOT NULL,
    tipo2 VARCHAR(15),
    habilidad VARCHAR(30) NOT NULL,
    movmimientos Movimientos NOT NULL,
    legendario BOOLEAN NOT NULL,
    generacion INTEGER NOT NULL
);
```

**Movimientos**
  
```sql
CREATE TABLE Movimientos (
    nombre VARCHAR(100) NOT NULL PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    categoria VARCHAR(30) NOT NULL,
    poder INTEGER(3),
    precision INTEGER(3),
    usos INTEGER(2)
);
```

**Usuarios**
```sql
CREATE TABLE Usuarios (
  idUsuario INTEGER PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  rol VARCHAR(25)
);
```
