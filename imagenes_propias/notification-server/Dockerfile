# Usa una imagen base oficial de Python
FROM python:3.9-slim

# Define argumentos para el proxy
ARG HTTP_PROXY
ARG HTTPS_PROXY

# Configura el proxy (si se proporciona)
ENV http_proxy=${HTTP_PROXY}
ENV https_proxy=${HTTPS_PROXY}

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo de requerimientos en el directorio de trabajo
COPY requirements.txt .

# Instala las dependencias
RUN pip install --no-cache-dir -r requirements.txt

# Copia el resto del código de la aplicación en el directorio de trabajo
COPY app.py .

# Define la variable de entorno que especifica el puerto en el que la aplicación se ejecuta
ENV FLASK_RUN_PORT=5342

# Expone el puerto en el que la aplicación correrá
EXPOSE 5342

# Define el comando para ejecutar la aplicación
CMD ["python", "app.py"]
