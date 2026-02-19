# Apache Kafka (KRaft-only) — Docker Compose

Este repositorio levanta **Apache Kafka (KRaft-only, sin ZooKeeper)** usando Docker Compose (Compose v2+), ideal para desarrollo local.


## Requerimientos

- **Docker Desktop** (Windows/Mac) o **Docker Engine** (Linux)
- **Docker Compose v2+** (se usa como `docker compose`, no `docker-compose`)
- Puertos libres en tu máquina:
  - `9092` (Kafka client)
  - `9093` (Controller interno KRaft)
- **docker-compose.yml** para poder construir la imagen dentro de nuestro docker desktop

> Nota: Este proyecto **no usa** `version:` en `docker-compose.yml` (obsoleto en Compose moderno).


## Pasos para crear la imagen de Kafka
### 1) Clonar / Preparar carpeta

Crea una carpeta en tu computadora donde quieras guardar el proyecto. Por ejemplo, puedes llamarla `kafka-kraft-dev`. Luego, dentro de esa carpeta, coloca el archivo `docker-compose.yml` que se encuentra en este repositorio. Esto te permitirá gestionar y levantar Apache Kafka usando Docker Compose desde ese directorio.

### 2) Descargar la imagen (Recomendado)
Aunque el comando `docker compose up` puede descargarla automaticamente, es buena práctica hacer el pull explícito:

```bash
docker pull apache/kafka:latest
```

### 3) Levantar Kafka
A continuación debemos de ejecutar el `docker-compose` para crear la imagen dentro de nuestro docker desktop y hacer uso de kafka

```bash
docker compose up -d
```
Verificar que el contenedor esté corriendo:
```bash
docker ps
```

---

## Creación de Topicos en Kafka
El proyecto incluye la creación y consulta de tópicos en Kafka, permitiendo validar su existencia, configuración y correcto funcionamiento. Esta parte es clave para comprender cómo Kafka organiza y persiste los eventos dentro del broker antes de ser consumidos.


**1. Acceder al contenedor de Kafka**
```bash
# 1) Entra al contenedor de Kafka (para ejecutar comandos desde dentro)
docker exec -it kafka-broker bash
```

**1.1 Listar ejecutables para realizar distintas operaciones dentro de kafka:**
```bash
ls /opt/kafka/bin
```

**2. Creación de Topicos en Kafka**
```bash
# 2) Crear un tópico en Kafka
/opt/kafka/bin/kafka-topics.sh \
  --create \                              # Crea un tópico nuevo
  --topic demo.events \                   # Nombre del tópico
  --bootstrap-server localhost:9092 \     # Broker al que te conectas (desde el contenedor)
  --partitions 3 \                        # Particiones (paralelismo)
  --replication-factor 1                  # Replicación (1 broker = 1)
```

*Comando directo para copiar en consola:*
```bash
/opt/kafka/bin/kafka-topics.sh --create --topic demo.events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

**3. Listar Topicos Existentes**
```bash
# 3) Listar todos los tópicos existentes
/opt/kafka/bin/kafka-topics.sh \
  --list \                               # Lista todos los tópicos
  --bootstrap-server localhost:9092      # Broker a consultar
```

*Comando directo para copiar en consola:*
```bash
/opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

**4. Describir Topicos (Mostrar todas su descripcion)**
```bash
# 4) Describir un tópico (ver particiones, replicación, ISR, etc.)
/opt/kafka/bin/kafka-topics.sh \
  --describe \                           # Muestra detalle: particiones, replicas, ISR
  --topic demo.events \                  # Tópico a describir
  --bootstrap-server localhost:9092      # Broker a consultar
```

*Comando directo para copiar en consola:*
```bash
/opt/kafka/bin/kafka-topics.sh --describe --topic demo.events --bootstrap-server localhost:9092
```

**5. Eliminar Topicos**
```bash
# 5) (Opcional) Eliminar un tópico
/opt/kafka/bin/kafka-topics.sh \
  --delete \                             # Solicita eliminación del tópico
  --topic demo.events \                  # Tópico a borrar
  --bootstrap-server localhost:9092      # Broker a consultar
```

*Comando directo para copiar en consola:*
```bash
/opt/kafka/bin/kafka-topics.sh --delete --topic demo.events --bootstrap-server localhost:9092
```

**6. Verificar Consumidores en kafka**
```bash
/opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic demo.events \
  --from-beginning \
  --property print.key=true \
  --property key.separator=" | " \
  --property print.timestamp=true
```

*Comando directo para copiar en consola:*
```bash
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic demo.events --from-beginning --property print.key=true --property key.separator=" | " --property print.timestamp=true
```

**7. Salir del Contenedor**
```bash
exit
```

## Verificar Mensajes, Consumer Groups y Lag

**1. Ver mensajes + key + offset + partition**
```bash
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic demo.events --from-beginning --property print.offset=true --property print.partition=true --property print.key=true --property key.separator=" | "
```

**2. Ver consumer groups existentes**
```bash
/opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```
**3. Ver detalle de un consumer group (offset, lag, partition)**
```bash
/opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group NOMBRE_DEL_GROUP
```



