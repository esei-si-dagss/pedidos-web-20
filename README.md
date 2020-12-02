# Ejemplo Spring MVC + Thymeleaf  (SI-2020, semana 4)
Ejemplo de proyecto Soring MVC
Ejemplo de uso del motor de plantillas Thymeleaf

## Previo
### Requisitos previos

* Servidor de BD MySQL
* Maven (versión > 3.5.x)
* (opcional) GIT
* (opcional) IDE Java (Eclipse, Netbeans, IntelliJ)

### Crear BD para los ejemplos  (si no se ha hecho antes)

* Crear BD "pruebas_si" en MySQL 

```
mysql -u root -p    [pedirá la contraseña de MySQL]

mysql> create database pruebas_si;
mysql> create user si@localhost identified by "si";
mysql> grant all privileges on pruebas_si.* to si@localhost;

```

Adicionalmente, puede ser necesario establecer un formato de fecha compatible
```
mysql> set @@global.time_zone = '+00:00';
mysql> set @@session.time_zone = '+00:00';
```

## CREAR PROYECTO SPRING BOOT
Existen varias alternativas
* Crear un proyecto Maven vacío e incluir las dependencias de los _starters_ de Spring Boot
* Usar Spring Tool Suite ([https://spring.io/tools](https://spring.io/tools)) y crear un nuevo proyecto _String Starter project_
* Crear el proyecto desde Spring Initializr ([https://start.spring.io/](https://start.spring.io/))

### Características del proyecto
```
Spring Boot version: 2.2.0
Type: Maven
Java version: 8
Packaging: Jar

Proyecto: 
   groupId: es.uvigo.mei
   artefactId: pedidos-spring
   version: 1.0
   package: es.uvigo.mei.pedidos
   
Dependencias a incluir:
    Spring Data JPA
    MySQLDriver
    Spring Web
    Thymeleaf
    Validation    
```

Fichero `pom.xml` resultante

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.0</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>es.uvigo.esei.mei</groupId>
	<artifactId>pedidos-web</artifactId>
	<version>1.0</version>
	<name>pedidos-web</name>
	<description>Ejemplo Spring MVC + Thymeleaf</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

Clase de configuración resultante (en `src/main/java/es/uvigo/mei/pedidos/PedidosWebApplication`)

### Ejecutar la aplicación Web

En Spring Tool Suite: Proyecto 'pedidos-web' `[botón derecho] > Run As > Spring Boot App`

Desde línea de comandos:
```
mvn spring-boot:run
```

ó

```
mvn install
java -jar target/pedidos-spring-1.0.jar
```

Estará accesible en la URL (http://localhost:8080/)
