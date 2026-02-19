# PostgreSQL con Docker para almacenamiento de eventos Kafka

Este proyecto levanta una instancia de PostgreSQL utilizando Docker para
almacenar eventos procesados correctamente dentro de una arquitectura
orientada a eventos:

Producer → Kafka → DLS Subscriber → PostgreSQL

La configuración utiliza la imagen oficial `postgres:16` y ejecuta
scripts SQL automáticamente mediante la carpeta `initdb`.

------------------------------------------------------------------------

## Estructura del proyecto

    postgresql/
    │
    ├── docker-compose.yml
    └── initdb/
        └── 02_schema.sql

------------------------------------------------------------------------

## Archivo docker-compose.yml

``` yaml
services:
  postgres:
    image: postgres:16
    container_name: kafka-postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: kafka_user
      POSTGRES_PASSWORD: kafka_pass
      POSTGRES_DB: kafka_events
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./initdb:/docker-entrypoint-initdb.d
    restart: unless-stopped

volumes:
  pgdata:
```

------------------------------------------------------------------------

## Explicación de la configuración

### Mapeo de puertos

    5432:5432

-   Puerto en la máquina local: `localhost:5432`
-   Puerto dentro del contenedor: `5432`

Permite la conexión desde pgAdmin o aplicaciones externas.

------------------------------------------------------------------------

### Variables de entorno

  Variable            Descripción
  ------------------- -----------------------------
  POSTGRES_USER       Usuario de la base de datos
  POSTGRES_PASSWORD   Contraseña del usuario
  POSTGRES_DB         Base creada automáticamente

En esta configuración se crea la base de datos:

    kafka_events

------------------------------------------------------------------------

## Volúmenes

### Volumen de persistencia (pgdata)

``` yaml
- pgdata:/var/lib/postgresql/data
```

Este volumen almacena físicamente los datos del motor PostgreSQL.

-   Los datos no se pierden al detener el contenedor
-   Permite reinicios sin afectar la información

------------------------------------------------------------------------

### Carpeta initdb (inicialización automática)

``` yaml
- ./initdb:/docker-entrypoint-initdb.d
```

PostgreSQL ejecuta automáticamente los archivos `.sql` ubicados en esta
carpeta únicamente la primera vez que el volumen está vacío.

------------------------------------------------------------------------

## Archivo 02_schema.sql

Ejemplo de tabla para almacenar eventos procesados correctamente:

``` sql
CREATE TABLE IF NOT EXISTS success_events (
  id BIGSERIAL PRIMARY KEY,
  topic TEXT NOT NULL,
  partition INT NOT NULL,
  offset BIGINT NOT NULL,
  message_key TEXT,
  event_id TEXT NOT NULL,
  event_type TEXT NOT NULL,
  payload JSONB NOT NULL,
  produced_at TIMESTAMPTZ,
  stored_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_success_event_id
ON success_events(event_id);

CREATE UNIQUE INDEX IF NOT EXISTS ux_kafka_position
ON success_events(topic, partition, offset);
```

Características:

-   Uso de `JSONB` para almacenar el payload completo
-   Idempotencia mediante `event_id`
-   Protección contra duplicados por posición en Kafka

------------------------------------------------------------------------

## Levantar el servicio

Desde la carpeta del proyecto ejecutar:

``` bash
docker compose up -d
```

Verificar que el contenedor esté en ejecución:

``` bash
docker ps
```

------------------------------------------------------------------------

## Verificar bases de datos

Listar bases creadas:

``` bash
docker exec -it kafka-postgres psql -U kafka_user -d postgres -c "\l"
```

Verificar tablas dentro de la base principal:

``` bash
docker exec -it kafka-postgres psql -U kafka_user -d kafka_events -c "\dt"
```

------------------------------------------------------------------------

## Reinicializar la base de datos

Si necesitas que los scripts de `initdb` se ejecuten nuevamente:

``` bash
docker compose down -v
docker compose up -d
```

El parámetro `-v` elimina el volumen `pgdata`.

Esto elimina todos los datos almacenados.

------------------------------------------------------------------------

## Conexión desde pgAdmin

  Campo           Valor
  --------------- --------------
  Host            localhost
  Puerto          5432
  Base de datos   kafka_events
  Usuario         kafka_user
  Contraseña      kafka_pass

------------------------------------------------------------------------

## Conexión desde Spring Boot

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/kafka_events
spring.datasource.username=kafka_user
spring.datasource.password=kafka_pass
spring.jpa.hibernate.ddl-auto=none
```
