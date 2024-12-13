# Proyecto_API_Rest_Segura

# Punto 1 - La idea
  La idea de esta API consiste en una "implementación" de la *PokéAPI* de forma mas sencilla. En mi caso, cuento con un archivo de texto plano en el cual figuran todos los datos de todos y cada uno de los     pokemons y movimientos. Por lo que he pensado poblar la base de datos con los datos que ya hay en los documentos para luego poder realizar las llamadas pertinentes. La idea sería una aplicación muy          parecida a la DataDex o la wiki de pokémon.

  
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
    movmimientos Movimiento NOT NULL,
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
  username VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  rol VARCHAR(25)
);
```

# Punto 3 - Los Endpoints

 **Pokemon**

  - **GET** `/pokemon/`:
    Devuelve una lista con todos los pokemons que hay en la base de datos.

  - **POST** `/pokemon/`:
    Repoblará la tabla correspondiente de la base de datos con nuevos datos.

  - **GET** `/pokemon/{id}`:
    Devolverá el pokemon correspondiente al id.
    Lanza una ParameterException si el id no existe o no es válido.

  - **PUT** `/pokemon/{id}`:
    Actualizará el pokemon cuyo id se corresponda con el pasado por parámetro con los datos.
    Lanza una ParameterException si el id o el cuerpo de la petición no existen o no son válidos.

  - **DELETE** `/pokemon/{id}`:
    Eliminará el pokemon correspondiente al id.
    Lanza una ParameterException si el id no existe o no es válido.

  - **GET** `/pokemon/tipo/{tipo}`:
    Devolverá una lista de todos los pokemons los cuales uno de sus tipos coincide con el tipo ingresado.
    Lanza una ParameterException si el tipo no existe o no es válido.

  - **GET** `/pokemon/generacion/{generacion}`:
    Devolverá una lista de todos los pokemons cuya generación coincida con la ingresada.
    Lanza una ParameterException si la generación no existe.

 **Movimientos**

  - **GET** `/movimientos`:
    Devuelve una lista con todos los movimientos que hay en la base de datos.

  - **POST** `/movimientos`:
    Repoblará la tabla correspondiente de la base de datos con nuevos datos.

  - **GET** `/movimientos/{id}`:
    Devuelve el movimiento correspondiente al id.
    Lanza una ParameterException si el id no existe o no es válido.

  - **PUT** `/movimientos/{id}`:
    Actualizará el movimiento cuyo id se corresponda con el pasado por parámetro con los datos.
    Lanza una ParameterException si el id o el cuerpo de la petición no existen o no son válidos.

  - **DELETE** `/movimientos/{id}`:
    Eliminará el movimiento correspondiente al id.
    Lanza una ParameterException si el id no existe o no es válido.   

  - **GET** `/movimientos/categoria/{categoria}`:
    Devuelve una lista de movimientos cuya categoría (Físico, Especial, Estado) se corresponde con la ingresada.
    Lanza una ParameterException si la categoría no existe o no es válida.

  - **GET** `/movimientos/tipo/{tipo}`:
    Devuelve una lista de movimientos cuyo tipo corresponda con el deseado.
    Lanza una ParameterException si el tipo no existe o no es válido.

  - **GET** `/categoria/{categoria}/tipo/{tipo}`:
    Devuelve una lista de movimientos cuyos categoria y tipo coincidan con los deseados.

 **Usuarios**

  - **POST** `/usuarios/register`:
    Procederá a registrar al usuario en la base de datos, los datos del usuario serán pasados por el cuerpo de la petición.
    Devolverá una ParameterException si el cuerpo de la función esta vacío, o un usuario en el caso de un registro correcto.

  - **GET** `/usuarios/login`:
    Procederá a realizar el login con los datos de un usuario que serán pasados a través del cuerpo de la petición.
    Devolverá un token en caso positivo y una ParameterExcepetion en caso de que el cuerpo de la petición esté vacío.

  - **PUT** `/usuarios/usuario`:
    Actualizará los datos del usuario cuyo id coincida con el id del pasado por el cuerpo de la petición.
    Lanzará una ParameterException si el cuerpo de la función contiene un valor no válido o no existe.

  - **DELETE** `/usuarios/{id}`:
    Eliminará el usuario cuyo id coincida con el id pasado por parámetro.
    Lanzará una ParameterException si el cuerpo de la función contiene un valor no válido o no existe.