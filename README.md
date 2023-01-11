# Instalación

Ejecutar en el directorio raíz:

```plaintext
mvn compile
```


Antes de instalar, en caso de querer ejecutar con docker, comentar
la prueba unitaria `contextLoads`.

```java
// @Test
void contextLoads() {
}
```

Además, verificar que en el archivo `/src/main/resources/application.properties` estén
descomentadas las líneas

```plaintext
spring.datasource.url=jdbc:mysql://database:3306/db_researchers
javax.persistence.jdbc.url=jdbc:mysql://database:3306/db_researchers
```

Y comentadas
```plaintext
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_researchers
javax.persistence.jdbc.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_researchers
```

De manera análoga, se comentarán las líneas

```plaintext
spring.datasource.url=jdbc:mysql://database:3306/db_researchers
javax.persistence.jdbc.url=jdbc:mysql://database:3306/db_researchers
```

y se descomentarán

```plaintext
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_researchers
javax.persistence.jdbc.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_researchers
```

junto con la prueba unitaria mencionada, para la ejecución con localhost.


Por último, en el archivo `/src/main/resources/META-INF/persistence.xml`:

Para ejecución con docker

```plaintext
jdbc:mysql://database:3306/db_researchers
```

Para ejecución con localhost
```plaintext
jdbc:mysql://localhost:3306/db_researchers
```

Estos cambios se harán en las líneas 12, 25, 39 y 54 del archivo `persistence.xml`.

En el caso de uso de localhost, será necesario crear una base de datos como sigue:
```plaintext
sudo mysql --password
```

```plaintext
create database db_researchers;
```
```plaintext
create user 'springuser'@'%' identified by 'ThePassword';
```
```plaintext
grant all on db_researchers.* to 'springuser'@'%';
```

En el método `storeFile` de la clase `ControladorWeb.java` será
necesario descomentar las líneas

```plaintext
String filepath =  System.getProperty("user.home");
filepath += "/redDeInvestigadores/";
```

Y comentar
```plaintext
String filepath = "/root/redDeInvestigadores/";
```

Además, en el archivo `/src/main/resources/application.properties` será necesario
definir la propiedad
```plaintext
spring.jpa.hibernate.ddl-auto
```

A `update`, al ejecutar con Docker. O a `create`, al ejecutar en `localhost`.


Una vez completados todos los requerimientos para la ejecución con docker o localhost,
ejecutar

```plaintext
mvn install
```


# Ejecución
## Ejecución con docker


Para ejecutar el programa con docker serán necesarios los
paquetes:

```plaintext
docker
```
```plaintext
docker compose
```

Ejecutar en el directorio raíz
```plaintext
docker-compose up
```

para crear una nueva imagen de la aplicación, o bien para
ejecutar la imagen que reside en el [repositorio](https://hub.docker.com/repository/docker/diegossc/proyecto_3/general).



```plaintext
docker-compose down
```
detendrá el contenedor creado para la aplicación.

```plaintext
docker ps
```

Mostrará los contenedores en ejecución.

Para acceder a la base de datos, se tendrá que ejecutar
```plaintext
docker exec -it [identificador] bash
```
Donde el identificador son los primeros tres carácteres mostrados en
el campo id del contenedor `mysql`.

Ingresar 
```plaintext
mysql -uspringuser -pThePassword
use db_researchers;
```
para autenticarse como usuario de mysql y acceder a la base de datos,
respectivamente.

## Ejecución en `localhost`

Ejecutar en el directorio raíz:
```plaintext
java -jar target/myp-0.0.1-SNAPSHOT.jar
```

En ambos casos, la aplicación se abrirá en `http://localhost:8080/`.

# Uso
Se presentará una página con submenús que contienen
la información de los estudiantes, investigadores, instituciones
y artículos registrados en la base de datos de la aplicación.
Además de páginas de información que incluyen preguntas frecuentes
o información de la aplicación.


Así como un par de botones donde será posible acceder o registrarse
como estudiante o investigador.

Cabe recalcar que para el registro de artículos será necesario que
se haya creado un perfil con el rol de investigador. El archivo .pdf
registrado se almacenará en un directorio `redDeInvestigadores`
en la raíz del directorio personal del usuario actual.

Para acceder a la interfaz de administrador, se tendrá que accesar al
siguiente link.

```plaintext
http://localhost:8080/administrator/usuarios
```

Ahora la interfaz de administrador será accesible con el link
`http://localhost:8080/administrator`.

# Diseño
La aplicación sigue el patrón modelo vista controlador.

La vista está determinada enteramente por plantillas localizadas en la carpeta
`resources` de la aplicación. Además de archivos en Javascript que otorgan
dinámica a la vista de la aplicación.

El modelo está contenido en clases de Java que representan, cada una, una
tabla de la base de datos.

El controlador se encuentra definido por una clase de Java de nombre `ControladorWeb.java`
que mapea las plantillas con las rutas accesibles en la aplicación; realizando, además,
operaciones con los objetos representantes de las tablas de la base de datos.

# Spring-Boot Admin

En el directorio `administrator/` se encuentra una aplicación secundaria que monitoriza
esta aplicación, enfocándose principalmente en la presentación de información relacionada
con los recursos locales de la computadora que ejecuta el programa (ram, almacenamiento,
hilos de ejecución) así como presentando las rutas disponibles que la aplicación implementa
y soporta. Para ejecutar esta aplicación bastará con ejecutar

```plaintext
mvn compile
```

```plaintext
mvn install
```

en el directorio administrator.

Para ejecutarla se tendrá que ejecutar el comnado:

```plaintext
java -jar target/administrator-0.0.1-SNAPSHOT.jar
```
La aplicación se abrirá en `http://localhost:8081/`. Se mostrará
un formulario de inicio de sesión, el acceso se hará con
```plaintext
usuario : admin
contraseña : admin
```


(Es necesario ejecutar la aplicación de la Red de Investigadores para monitorizar sus recursos).

# Known Bugs
- La pestaña de perfil no muestra los artículos asociados a un usuario correctamente.