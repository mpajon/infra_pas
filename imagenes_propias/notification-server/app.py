from flask import Flask, request, jsonify
import requests
import os
import logging

app = Flask(__name__)

# Configura el modo de depuración de Flask
app.config['DEBUG'] = True


# Configura el registro de logging para que imprima en la salida estándar del contenedor
handler = logging.StreamHandler()
handler.setLevel(logging.DEBUG)
app.logger.addHandler(handler)

# Lee las variables de entorno para la configuración del proxy
http_proxy = os.getenv('HTTP_PROXY')
https_proxy = os.getenv('HTTPS_PROXY')

# Define el proxy usando las variables de entorno
proxy = {
    "http": http_proxy,
    "https": https_proxy,
}

@app.route('/api', methods=['POST'])
def handle_post():
    # Imprime información de la solicitud recibida
    app.logger.debug(f"Recibida solicitud POST en '/api'")
    app.logger.debug(f"Headers de la solicitud: {request.headers}")
    app.logger.debug(f"Datos de la solicitud JSON: {request.json}")

    try:
        url = "https://prod-211.westeurope.logic.azure.com:443/workflows/b167dbee4c5b446580aec8dbecfb3140/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=FxgJs3373YCmGl8o1hQkYfjwXrYQ4tmwrKpQBOJvs5k"
        payload = {}
        headers = {}

        if proxy.get("http") and len(proxy["http"]) > 0:
            response = requests.request("POST", url, headers=headers, data=payload, proxies=proxy)
        else:
            response = requests.request("POST", url, headers=headers, data=payload)

        app.logger.debug(f"Response status code: {response.status_code}")
        app.logger.debug(f"Response text: {response.text}")

        # Retorna una respuesta de ejemplo
        response_data = {"mensaje": "Solicitud POST recibida correctamente"}
        return jsonify(response_data), 200
    except requests.RequestException as e:
        # Maneja errores en la petición
        app.logger.error(f"Error en la solicitud: {str(e)}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.logger.debug("INICIO")
    app.run(debug=True, host='0.0.0.0', port=5342)
