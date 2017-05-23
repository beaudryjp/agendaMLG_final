# agendaMLG
La aplicación Java EE (JPA + JSF) de la aplicación agendaMLG para el Diario Sur - SII

# Error de memoria al compilar
Si sale el error al desplegar el ear de agendaMLG: 

[ERROR] Failed to execute goal org.wildfly.plugins:wildfly-maven-plugin:1.1.0.Alpha6:deploy (default-cli) on project agendaMLG-ear: Error executing FORCE_DEPLOY: java.util.concurrent.ExecutionException: Operation failed: java.lang.OutOfMemoryError:Java heap space -> [Help 1]

1. Comprobar que tengais arrancado el servidor mysql

2. Hay un ear que se ha desplegado mal, teneis que ir a donde tengais instalado el servidor wildfly, ir a la carpeta standalone, ir a deployments y borrar todos los ficheros referentes a agendaMLG.

3. Existe un problema en la configuración de wildfly, teneis que ir a donde tengais instalado el servidor, ir a la carpeta standalone, ir a configuration y abrir los ficheros standalone.xml y standalone.xml, ir al final del documento y en el apartado de deployments borrar el deployment donde haga referencia a agendaMLG.

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
