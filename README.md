# agendaMLG
La aplicación Java EE (JPA + JSF) de la aplicación agendaMLG para el Diario Sur - SII

# Comando para hacer clean, build y deploy (es necesario tener instalado maven en el pc)
Seleccionar el proyecto agendaMLG_final y ir a Tools -> Open in terminal

Ejecutar el comando cada vez que se quiera desplegar

mvn clean install; cd agendaMLG-ear/; mvn wildfly:deploy -X; cd ..

# Importar datos
Para importar datos hay que ejecutar el siguiente comando:

mysql -u root -p agenda < data.sql 

Es posible que no salgan los acentos y caracteres especiales, en este caso en la consola mysql ejecutar:
ALTER DATABASE agenda CHARACTER SET utf8 COLLATE utf8_unicode_ci;

# Usuarios

jeanpaul.beaudry@gmail.com - redactor

Poppo@gmail.com - registrado

Pepe@patata.com - validado

todos tienen la misma contraseña (123456)

# Base de datos
### En esta practica hay que añadir una base de datos externa(mysql) ya que no se puede utiliza JavaDB.

### 1. Descargar y instalar un servidor mysql en vuestro equipo, https://dev.mysql.com/downloads/mysql/
#### 1.1 Una vez instalado ejecutar los siguientes comandos:
    ```
    create database agenda;

    CREATE USER 'agenda'@'localhost' IDENTIFIED BY '9cx#7QjyTWXO';

    GRANT ALL PRIVILEGES ON agenda. * TO 'agenda'@'localhost';

    flush privileges;
    ```

### 2. Tendréis descargar el driver JDBC de mysql: https://dev.mysql.com/downloads/connector/j/ y descomprimir el fichero

### 3. En vuestra maquina abrir una consola e ir a la carpeta donde este el servidor wildfly, ejecutar el script add-user y crear el usuario para poder acceder al panel de administracion

### 4. Una vez hecho esto, acceder al enlace http://127.0.0.1:9990/ y loguearse con el usuario y contraseña.

### 5. En el apartado Deployments al lado de "Deploy an application" pinchar en start, añadir uno nuevo, seleccionar "upload a new deployment" y buscar el jar de mysql-connector y lo aceptais.

### 6. En el apartado Configuration al lado de "Create datasource" pinchar en start Ir a Datasources.

#### 6.1 Pinchar en Subsystems -> Datasources -> Non-XA -> pinchar en add.

#### 6.2 Seleccionáis Mysql datasource y poneis los siguientes datos:
    ```
    Name: agendaMLG

    JNDI: java:/agendaMLG
    ```

#### 6.3 Seleccionar el driver en la pestaña "Detected driver" y en el siguiente paso poneis los datos de acceso a la base de datos
    ```
    Connection URL: jdbc:mysql://localhost:3306/agenda

    Username: agenda

    Password: 9cx#7QjyTWXO
    ```
