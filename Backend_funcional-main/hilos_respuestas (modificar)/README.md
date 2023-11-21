# Lab 02.01: Construcción de un API con Spring Boot 3 :rocket:

## Objetivos :dart:

- Crear un API utilizando el framework **[Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)** de Java.
- Implementar las operaciones básicas de un CRUD (crear, listar, actualizar y eliminar).
- Agregar una capa de presistencia utilizando Docker y PostgreSQL.

## Tools (extenciones vs code) :wrench:

* Utilizar [Thunder Client](https://www.thunderclient.com/) para realizar los requests HTTP.
* Utilizar [Spring Initializr Java Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr) para realizar la configuración inicial del proyecto.

## Requisitos Previos :memo:

* Tener instalado y configurado **Java** (v.17 LTS) Development Kit (JDK) instalado en tu sistema. Recomendamos utilizar la distribución de Amazon (*JDK Corretto*): [Linux](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/generic-linux-install.html) | [macOS](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/macos-install.html) | [Windows](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/windows-7-install.html)
* Tener instalado [Docker](https://www.docker.com/products/docker-desktop/) desktop
* Tener instalado y configurado **Visual Studio Code**.

## Instrucciones :mega:

### Paso 00: Definición del recurso

* Implementaremos una API para una biblioteca musical. Tendremos 2 recursos: Song y Playlist.

#### [Song]

|Atributo|Tipo de dato|Descripción|
|---------|----|-----------|
|id|Long|Identificador único autogenerado|
|title|String|Título de la canción|
|artist|String|Nombre del artista o grupo que interpreta la canción|
|album|String|Nombre del álbum al que pertenece la canción|
|release_date|Date|Fecha de lanzamiento de la canción|
|genre|String|Género musical al que pertenece la canción|
|duration|Integer|Duración de la canción en segundos|
|cover_image|String|URL o ruta de la imagen de portada de la canción|
|spotify_url|String|URL de Spotify|

#### [Playlist]

|Atributo|Tipo de dato|Descripción|
|---------|----|-----------|
|id|Long|Identificador único autogenerado|
|title|String|Título o nombre de la lista de reproducción|
|songs|List<Long>|Lista de las canciones que forman parte de la lista|
|cover_image|String|URL o ruta de la imagen de portada de la lista|

#### Configuración de archivos

```
DEMO
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo  (🌟Alta Importancia🌟)
|   |   |               └── DemoApplication.java  (Punto de entrada de la aplicación Spring Boot)
|   |   |               |
|   |   |               └── song
|   |   |               |   └── domain
|   |   |               |   |   └── Song.java
|   |   |               |   |   └── SongRepository.java
|   |   |               |   └── application
|   |   |               |       └── SongController.java
|   |   |               └── playlist
|   |   |               |   └── domain
|   |   |               |   |   └── Playlist.java
|   |   |               |   |   └── PlaylistRepository.java
|   |   |               └── └── application
|   |   |                      └── PlaylistController.java
│   │   └── resources
│   │       └── application.properties  (🌟Alta Importancia🌟: Configuraciones de la aplicación)
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── demo
│                       └── DemoApplicationTests.java  (Pruebas unitarias)
├── .gitignore  (Especifica archivos a ignorar en el control de versiones)
├── pom.xml  (Archivo de construcción y dependencias de Maven)
├── mvnw  (Herramienta de envoltura de Maven que asegura la versión correcta de Maven sin requerir su instalación)
```
  
### Paso 01: Configuración Inicial

#### Verificación de instalación correcta

1. Asegúrate de tener configurados los requisitos previos.

Para Java:

```
$ javac -version
```

Para Docker:

```
$ docker -v
```


#### Creación de nuevo proyecto
2. Clona la carpeta donde trabajarás en el nuevo proyecto.

En mi caso trabajaré en Desktop:

```
 ~ % cd Desktop/
 ~/Desktop % git clone "link_del_repositorio"
```

*Nota: Recuerda efectuar los cambios con:

```
git add --all
git commit -m "mensaje de cambios"
git push
```

3. Abrimos el proyecto en Visual Studio Code.

```
 ~/Desktop % cd "nombre_del_proyecto"
 ~/"nombre_del_proyecto" % code .
```

*Nota: Usaremos demo como nombre de nuestro proyecto, puedes modificarlo pero es el valor que vamos a usar para el código que mostraremos más adelante.

##### Uso de IDE con Spring Initializr
4. Configura tu proyecto para usar Maven como sistema de construcción.
5. Ejecutamos el shortcut ctrl+shift+p (windows/linux) o cmnd+shift+p (macOS) y escribimos Spring Initializr: Generate a Maven Project with Spring Initializr. 

```
>Spring Initializr: Generate a Maven Project with Spring Initializr. 
```
Especificar version Spring Boot:

```
3.1.3
```
Especificar lenguaje:

```
java
```

Group id del proyecto:

```
com.example
```

Artifact id del proyecto:

```
demo
```

Tipo de enpaquetado:

```
Jar
```

Especificar versión de Java:

```
17
```

Agregar las siguientes dependencias:

```
* Spring Web
* Spring Data JPA
* H2 Database
* PostgreSQL Driver
```

Generamos el proyecto en la carpeta de nuestro repositorio.

6. Verificamos que todo funcionen correctamente ejecutando el siguiente comando dentro de la carpeta demo:

```
 ~/"nombre_del_proyecto" % cd demo
 ~/"nombre_del_proyecto"/demo % ./mvnw spring-boot:run
```

### Paso 02: Creación del Modelo

1. Crea una clase `Song` en el paquete `com.example.demo` para representar una canción:

```java
package com.example.demo;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String artist;

    private String album;

    private Date releaseDate;

    private String genre;

    private Integer duration;

    private String coverImage;

    private String spotifyUrl;

    public Song() {}

    public Song(Long id, String title, String artist, String album, Date releaseDate, String genre, Integer duration, String coverImage, String spotifyUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.duration = duration;
        this.coverImage = coverImage;
        this.spotifyUrl = spotifyUrl;
    }


    // Getters y setters
    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public String getGenre() {
        return this.genre;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public String getSpotifyUrl() {
        return this.spotifyUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public void setArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist cannot be null or empty");
        }
        this.artist = artist;
    }

    public void setAlbum(String album) {
        if (album == null || album.trim().isEmpty()) {
            throw new IllegalArgumentException("Album cannot be null or empty");
        }
        this.album = album;
    }

    public void setReleaseDate(Date releaseDate) {
        if (releaseDate == null) {
            throw new IllegalArgumentException("Release date cannot be null");
        }
        this.releaseDate = releaseDate;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        this.genre = genre;
    }

    public void setDuration(Integer duration) {
        if (duration == null || duration < 0) {
            throw new IllegalArgumentException("Duration cannot be null or negative");
        }
        this.duration = duration;
    }

    public void setCoverImage(String coverImage) {
        if (coverImage == null || coverImage.trim().isEmpty()) {
            throw new IllegalArgumentException("Cover image cannot be null or empty");
        }
        this.coverImage = coverImage;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        if (spotifyUrl == null || spotifyUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Spotify URL cannot be null or empty");
        }
        this.spotifyUrl = spotifyUrl;
    }

    // toString

    @Override
    public String toString() {
        return "Song{" + "id=" + this.id + ", title='" + this.title + '\'' + ", artist='" + this.artist + '\'' + ", album='" + this.album + '\'' + ", releaseDate=" + this.releaseDate + ", genre='" + this.genre + '\'' + ", duration=" + this.duration + ", coverImage='" + this.coverImage + '\'' + ", spotifyUrl='" + this.spotifyUrl + '\'' + '}';
    }

    // equals y hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Song))
            return false;
        Song song = (Song) o;
        return this.id.equals(song.id);
    }

}
```

### Paso 03: Definición de la Capa de Datos o Persistencia
  
1. Crear un interfaz `SongRepository` en el paquete `com.example.demo` que extienda `JpaRepository<Song, Long>`:
  
```java
package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {}
```

### Paso 04: Definición de Controllers
 
1. Crear un controller llamado `SongController` en el paquete `com.example.demo` para manejar las solicitudes HTTP relacionadas con las canciones. En el siguiente archivo podemos observar los métodos `GET`, `POST`, `PUT`, `DELETE`:

```java
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @GetMapping
    public ResponseEntity<List<Song>> songs() {
        List<Song> songs = songRepository.findAll();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> song(@RequestBody Song song) {
        songRepository.save(song);
        return ResponseEntity.status(201).body("Created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSong(@PathVariable Long id, @RequestBody Song song) {
        Optional<Song> optionalSong = songRepository.findById(id);
        if(optionalSong.isPresent()) {
            Song existingSong = optionalSong.get();
            existingSong.setTitle(song.getTitle());
            existingSong.setArtist(song.getArtist());
            existingSong.setAlbum(song.getAlbum());
            existingSong.setReleaseDate(song.getReleaseDate());
            existingSong.setGenre(song.getGenre());
            existingSong.setDuration(song.getDuration());
            existingSong.setCoverImage(song.getCoverImage());
            existingSong.setSpotifyUrl(song.getSpotifyUrl());
            songRepository.save(existingSong);
            return ResponseEntity.status(200).body("Updated");
        } else {
            return ResponseEntity.status(404).body("Not Found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable Long id) {
        Optional<Song> optionalSong = songRepository.findById(id);
        if(optionalSong.isPresent()) {
            songRepository.deleteById(id);
            return ResponseEntity.status(200).body("Deleted");
        } else {
            return ResponseEntity.status(404).body("Not Found");
        }
    }

    // TODO: Añadir método PATCH para actualizar parcialmente una canción
}

```

### Paso 05: Realizar pruebas usando Thunder Client

* Ejecuta tu aplicación y utiliza `Thunder Client` para realizar tus pruebas:

1. Crea una nueva colección llamada `music`
2. Crea los `request` correspondientes para cada endpoint.
3. Pon tu API a prueba invocando cada uno de los endpoints.

### Paso 06: Agregar una capa de persistencia con Docker y PostgreSQL

1. Agregar las siguientes propiedades en `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mypassword
spring.jpa.hibernate.ddl-auto=update
```

2. Efectuar el siguiente comando para crear una instancia de PostgreSQL:

```
    docker run --name my_postgres -e POSTGRES_PASSWORD=mypassword -p 5432:5432 -d postgres
```

*Nota: 
* --name my_postgres: Asigna el nombre "my_postgres" al contenedor.
* -e POSTGRES_PASSWORD=mypassword: Establece la variable de entorno para la contraseña del usuario "postgres".
* -p 5432:5432: Mapea el puerto 5432 del contenedor al puerto 5432 de tu máquina local.
* -d: Ejecuta el contenedor en modo "desprendido" (en segundo plano).
postgres: Es la imagen de Docker que se utilizará para crear el contenedor.

* PostgreSQL es un sistema de gestión de bases de datos relacional de código abierto que ofrece robustez, escalabilidad y extensibilidad. Cuando se ejecuta en Docker, PostgreSQL se encapsula en un contenedor que contiene todo lo necesario para su funcionamiento, incluido el sistema operativo y las configuraciones. Esto ofrece ventajas como portabilidad, aislamiento y facilidad de configuración, permitiendo que la base de datos se ejecute de manera uniforme en cualquier entorno que tenga Docker instalado.


Ahora los datos de nuestra aplicación se almacenarán en una base de datos PostgreSQL en lugar de en memoria! Ya van a presistir!

3. Realizamos las pruebas nuevamente con Thunder Client.
   
4. Para verificar los cambios en la base de datos, podemos conectarnos a la base de datos usando los siguientes comandos:

```
~ % docker exec -it my_postgres psql -U postgres
postgres=# \l
postgres=# \c postgres 
postgres=# \dt
postgres=# SELECT * FROM song;
postgres=# exit
```

5. Para detener el contenedor, ejecuta el siguiente comando:

```
~ % docker stop my_postgres
~ % docker rm my_postgres
```

### Paso 07: ¡Ahora te toca a ti!
Implementa el recurso `Playlist`, luego realiza las pruebas usando `Client Thunder`.

```java
package com.example.demo;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "playlist_song",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id")}
    )
    private List<Song> songs;

    private String coverImage;

    public Playlist() {}

    public Playlist(Long id, String title, List<Song> songs, String coverImage) {
        this.id = id;
        this.title = title;
        this.songs = songs;
        this.coverImage = coverImage;
    }

    // Getters y setters
}

```

### Comandos adicionales
Limpiar dependencias y re-instalarlas:
```
./mvnw clean install   
```
Instalar y ejecutar:
```
./mvnw spring-boot:run     
```

### Conclusiones
En este laboratorio, has aprendido a desarrollar una API utilizando Spring Boot. Has creado una aplicación para gestionar una lista de canciones, implementando operaciones CRUD básicas y manteniendo la información en una Base de Datos. Este conocimiento te proporciona una base para crear aplicaciones más avanzadas con Spring Boot.

¡Felicidades por completar el laboratorio! ¡Espero que hayas disfrutado aprendiendo sobre el desarrollo de APIs con Spring Boot!
