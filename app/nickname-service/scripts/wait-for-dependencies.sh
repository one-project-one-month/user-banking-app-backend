#!/bin/sh

POSTGRES_HOST=${POSTGRES_HOST:-postgresSQL}
POSTGRES_PORT=${POSTGRES_PORT:-5432}
POSTGRES_DB=${POSTGRES_DB:-bankdb}
POSTGRES_USER=${POSTGRES_USER:-postgres}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-root}
KAFKA_HOST=${KAFKA_HOST:-kafka}
KAFKA_PORT=${KAFKA_PORT:-9092}

echo "Waiting for Postgres at $POSTGRES_HOST:$POSTGRES_PORT..."
while ! nc -z $POSTGRES_HOST $POSTGRES_PORT; do
  echo "Postgres not ready yet..."
  sleep 2
done
echo "Postgres ready!"

echo "Waiting for Kafka at $KAFKA_HOST:$KAFKA_PORT..."
while ! nc -z $KAFKA_HOST $KAFKA_PORT; do
  echo "Kafka not ready yet..."
  sleep 2
done
echo "Kafka ready!"

echo "All dependencies are ready. Starting service..."
exec "$@"

