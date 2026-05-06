# syntax=docker/dockerfile:1.7

FROM node:20-bookworm-slim AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./frontend/
WORKDIR /app/frontend
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY backend/pom.xml ./backend/pom.xml
COPY backend/settings.xml ./backend/settings.xml
WORKDIR /app/backend
RUN mvn -s settings.xml dependency:go-offline -B
COPY backend/src ./src
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
RUN mvn -s settings.xml package -DskipTests -B

# Runtime base: Ubuntu 22.04 fits this single-container setup because it runs MySQL, Nginx, and a Java 17 app together.
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive \
    TZ=Asia/Shanghai \
    MYSQL_ROOT_PASSWORD=root \
    MYSQL_DATABASE=health_db

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    curl \
    git \
    mysql-server \
    nginx \
    openjdk-17-jre-headless \
    procps \
    tzdata \
    && rm -f /etc/nginx/sites-enabled/default \
    && rm -rf /var/lib/apt/lists/*

COPY . /app/
COPY --from=backend-build /app/backend/target/*.jar /app/app.jar
COPY --from=frontend-build /app/frontend/dist /usr/share/nginx/html
COPY database/init.sql /docker-entrypoint-initdb.d/01-init.sql
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY start.sh /usr/local/bin/start-health-app.sh

RUN chmod +x /usr/local/bin/start-health-app.sh

EXPOSE 80 8080 3306

ENTRYPOINT ["/usr/local/bin/start-health-app.sh"]
