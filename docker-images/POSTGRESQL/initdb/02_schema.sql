-- postgres-docker/initdb/02_schema.sql

-- Guarda eventos procesados con éxito (desde tu subscriber)
CREATE TABLE IF NOT EXISTS success_events (
  id              BIGSERIAL PRIMARY KEY,

  -- Kafka metadata
  topic           TEXT NOT NULL,
  partition       INT  NOT NULL,
  offset          BIGINT NOT NULL,
  message_key     TEXT NULL,

  -- Idempotencia (evita duplicados si re-procesas)
  event_id        TEXT NOT NULL,
  event_type      TEXT NOT NULL,

  -- Payload
  payload         JSONB NOT NULL,

  -- Timestamps
  produced_at     TIMESTAMPTZ NULL,
  stored_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- No duplicates por event_id (ideal si tu producer trae un uuid)
CREATE UNIQUE INDEX IF NOT EXISTS ux_success_events_event_id
ON success_events(event_id);

-- También puedes garantizar que no insertes el mismo mensaje Kafka:
CREATE UNIQUE INDEX IF NOT EXISTS ux_success_events_kafka_pos
ON success_events(topic, partition, offset);

-- Para búsquedas rápidas
CREATE INDEX IF NOT EXISTS ix_success_events_type
ON success_events(event_type);

CREATE INDEX IF NOT EXISTS ix_success_events_stored_at
ON success_events(stored_at);
