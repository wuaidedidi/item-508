#!/usr/bin/env bash
set -euo pipefail

MYSQL_HOST=127.0.0.1
MYSQL_PORT=3306
BACKEND_PORT=8080
FRONTEND_PORT=80
MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD:-root}"
MYSQL_DATABASE="${MYSQL_DATABASE:-health_db}"
INIT_MARKER=/var/lib/mysql/.health-app-initialized

cleanup() {
  for pid in "${NGINX_PID:-}" "${BACKEND_PID:-}" "${MYSQL_PID:-}"; do
    if [[ -n "${pid:-}" ]] && kill -0 "$pid" 2>/dev/null; then
      kill "$pid" 2>/dev/null || true
    fi
  done
}

trap cleanup EXIT INT TERM

echo "Starting MySQL on ${MYSQL_HOST}:${MYSQL_PORT}..."
mkdir -p /var/run/mysqld /docker-entrypoint-initdb.d
chown -R mysql:mysql /var/run/mysqld /var/lib/mysql

mysqld \
  --user=mysql \
  --bind-address="${MYSQL_HOST}" \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_unicode_ci \
  --default-authentication-plugin=mysql_native_password \
  --skip-host-cache &
MYSQL_PID=$!

for _ in $(seq 1 120); do
  if mysqladmin ping --protocol=socket -uroot --silent 2>/dev/null \
    || mysqladmin ping -h "${MYSQL_HOST}" -P "${MYSQL_PORT}" -uroot -p"${MYSQL_ROOT_PASSWORD}" --silent 2>/dev/null; then
    echo "MySQL ready on ${MYSQL_HOST}:${MYSQL_PORT}"
    break
  fi
  sleep 1
done

if ! mysqladmin ping --protocol=socket -uroot --silent 2>/dev/null \
  && ! mysqladmin ping -h "${MYSQL_HOST}" -P "${MYSQL_PORT}" -uroot -p"${MYSQL_ROOT_PASSWORD}" --silent 2>/dev/null; then
  echo "MySQL did not become ready in time"
  exit 1
fi

if [[ ! -f "${INIT_MARKER}" ]]; then
  echo "Initializing ${MYSQL_DATABASE}..."
  mysql --protocol=socket -uroot < /docker-entrypoint-initdb.d/01-init.sql
  mysql --protocol=socket -uroot <<SQL
CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY '${MYSQL_ROOT_PASSWORD}';
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED WITH mysql_native_password BY '${MYSQL_ROOT_PASSWORD}';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '${MYSQL_ROOT_PASSWORD}';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
SQL
  touch "${INIT_MARKER}"
fi

export DB_HOST="${MYSQL_HOST}"
export DB_PORT="${MYSQL_PORT}"
export DB_NAME="${MYSQL_DATABASE}"
export DB_USER=root
export DB_PASSWORD="${MYSQL_ROOT_PASSWORD}"

echo "Starting backend on 0.0.0.0:${BACKEND_PORT}..."
java -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -jar /app/app.jar &
BACKEND_PID=$!

for _ in $(seq 1 180); do
  if curl -fsS "http://${MYSQL_HOST}:${BACKEND_PORT}/api/articles" >/dev/null; then
    echo "Backend ready on 0.0.0.0:${BACKEND_PORT}"
    break
  fi
  sleep 1
done

if ! curl -fsS "http://${MYSQL_HOST}:${BACKEND_PORT}/api/articles" >/dev/null; then
  echo "Backend did not become ready in time"
  exit 1
fi

echo "Starting nginx on 0.0.0.0:${FRONTEND_PORT}..."
nginx -g 'daemon off;' &
NGINX_PID=$!

for _ in $(seq 1 60); do
  if curl -fsS "http://${MYSQL_HOST}:${FRONTEND_PORT}/" >/dev/null; then
    echo "Frontend ready on 0.0.0.0:${FRONTEND_PORT}"
    break
  fi
  sleep 1
done

if ! curl -fsS "http://${MYSQL_HOST}:${FRONTEND_PORT}/" >/dev/null; then
  echo "Frontend did not become ready in time"
  exit 1
fi

echo "Health management system is ready."
echo "Frontend: http://localhost:${FRONTEND_PORT}"
echo "Backend API: http://localhost:${BACKEND_PORT}/api"
echo "Database: ${MYSQL_HOST}:${MYSQL_PORT}"

wait -n "$MYSQL_PID" "$BACKEND_PID" "$NGINX_PID"
exit $?
