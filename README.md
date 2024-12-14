# Proyecto_API_Rest_Segura

# La idea
  La idea de esta API consiste en una "implementación" de la *PokéAPI* de forma mas sencilla. En mi caso, cuento con un archivo de texto plano en el cual figuran todos los datos de todos y cada uno de los     pokemons y movimientos. Por lo que he pensado poblar la base de datos con los datos que ya hay en los documentos para luego poder realizar las llamadas pertinentes. La idea sería una aplicación muy          parecida a la DataDex o la wiki de pokémon.

  
# Las tablas
##   **Pokemons**
  
```sql
CREATE TABLE Pokemons (
    idPokemon INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    tipo1 VARCHAR(15) NOT NULL,
    tipo2 VARCHAR(15),
    habilidad VARCHAR(30) NOT NULL,
    legendario BOOLEAN NOT NULL,
    generacion INTEGER NOT NULL
);
```

| **Campo**       | **Valor**        | **RESTRICCIONES** |
|-----------------|------------------|-------------------|
| **idPokemon**   | INTEGER          | PRIMARY KEY       |
| **nombre**      | VARCHAR(100)     | NOT NULL          |
| **descripcion** | VARCHAR(1000)    | NOT NULL          |
| **tipo1**       | VARCHAR(15)      | NOT NULL          |
| **tipo2**       | VARCHAR(15)      |                   |
| **habilidad**   | VARCHAR(30)      | NOT NULL          |
| **legendario**  | BOOLEAN          | NOT NULL          |
| **generacion**  | INTEGER          | NOT NULL          |



##  **Movimientos**
  
```sql
CREATE TABLE Movimientos (
    nombre VARCHAR(100) NOT NULL PRIMARY KEY,
    descripcion VARCHAR(1000) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    categoria VARCHAR(30) NOT NULL,
    poder INTEGER(3),
    precision INTEGER(3),
    usos INTEGER(2)
);
```

| **Campo**        | **Valor**      | **RESTRICCIONES** |
|------------------|----------------|-------------------|
| **nombre**       | VARCHAR(100)   | PRIMARY KEY       |
| **descripcion**  | VARCHAR(1000)  | NOT NULL          |
| **tipo**         | VARCHAR(15)    | NOT NULL          |
| **categoria**    | VARCHAR(30)    | NOT NULL          |
| **poder**        | INTEGER(3)     |                   |
| **precision**    | INTEGER(3)     |                   |
| **usos**         | INTEGER(2)     |                   |




## **Usuarios**
```sql
CREATE TABLE Usuarios (
  idUsuario INTEGER PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  rol VARCHAR(25)
);
```

| **Campo**      | **Valor**     | **RESTRICCIONES** |
|----------------|---------------|-------------------|
| **idUsuario**  | INTEGER       | PRIMARY KEY       |
| **username**   | VARCHAR(50)   | NOT NULL          |
| **password**   | VARCHAR(50)   | NOT NULL          |
| **rol**        | VARCHAR(25)   |                   |





# Los Endpoints

###  **Pokemon**

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

###  **Movimientos**

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

  - **GET** `/movimientos/categoria/{categoria}/tipo/{tipo}`:
    Devuelve una lista de movimientos cuyos categoria y tipo coincidan con los deseados.

### **Usuarios**

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





## **Seguridad**

###  **Pokemon**

| **Método** | **Ruta**                         | **Seguridad** |
|------------|----------------------------------|---------------|
| GET        | /pokemon/                        | permitAll     |
| POST       | /pokemon/                        | authenticated |
| GET        | /pokemon/{id}                    | permitAll     |
| PUT        | /pokemon/{id}                    | ADMIN only    |
| DELETE     | /pokemon/{id}                    | ADMIN only    |
| GET        | /pokemon/tipo/{tipo}             | permitAll     |
| GET        | /pokemon/generacion/{generacion} | permitAll     |



###  **Movimientos**

| **Método** | **Ruta**                                       | **Seguridad** |
|------------|------------------------------------------------|---------------|
| GET        | /movimientos/                                  | permitAll     |
| POST       | /movimientos/                                  | authenticated |
| GET        | /movimientos/{id}                              | permitAll     |
| PUT        | /movimientos/{id}                              | ADMIN only    |
| DELETE     | /movimientos/{id}                              | ADMIN only    |
| GET        | /movimientos/categoria/{categoria}             | permitAll     |
| GET        | /movimientos/tipo/{tipo}                       | permitAll     |
| GET        | /movimientos/categoria/{categoria}/tipo/{tipo} | permitAll     |



###  **Usuarios**

| **Método** | **Ruta**           | **Seguridad** |
|------------|--------------------|---------------|
| POST       | /usuarios/register | permitAll     |
| GET        | /usuarios/login    | permitAll     |
| PUT        | /usuarios/usuario  | ADMIN only    |
| DELETE     | /usuarios/{id}     | ADMIN only    |





## **Excepciones Y Códigos de Estado**

| **Excepcion**             | **Causante**                                                                                                |
|---------------------------|-------------------------------------------------------------------------------------------------------------|
| `ParameterException()`    | Cuando el parámetro pasado a la petición por URI no es válido o uno de los valores del body no es correcto. |
| `NotFoundException()`     | Cuando no se encontró ningún valor en la base de datos.                                                     |
| `FileNotFoundException()` | Cuando uno de los dos ficheros usados para cargar los datos no se encuentre por el programa.                |



* **200 OK** Para la modificación o eliminación de usuarios, movimientos y pokemon y la función de login.
* **201 CREATED** Para acciones como el registro de un nuevo usuario o la adición de un nuevo pokemon o movimiento.
* **401 UNAUTHORIZED** Cuando se intenta acceder a los endpoints sin antes haber realizado el login y ser portador de un token. 
* **403 FORBIDDEN** Cuando se intentan llevar a cabo acciones a las cuales el usuario no tiene acceso.
* **500 INTERNAL SERVER ERROR** Cuando salta una excepción desconocida a la hora de realizar una acción.




