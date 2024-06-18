from flask import Flask, request, jsonify
import requests
import os

app = Flask(__name__)

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
    print(f"Recibida solicitud POST en '/api'")
    print(f"Headers de la solicitud: {request.headers}")
    print(f"Datos de la solicitud JSON: {request.json}")    
    try:
        url = "https://prod-211.westeurope.logic.azure.com:443/workflows/b167dbee4c5b446580aec8dbecfb3140/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=FxgJs3373YCmGl8o1hQkYfjwXrYQ4tmwrKpQBOJvs5k"
        payload = {}
        headers = {}
        if proxy.get("http") and len(proxy["http"]) > 0:
            response = requests.request("POST", url, headers=headers, data=payload, proxies=proxy)
        else:
            response = requests.request("POST", url, headers=headers, data=payload)        
        # Retorna una respuesta de ejemplo
        response_data = {"mensaje": "Solicitud POST recibida correctamente"}
        return jsonify(response_data), 200
    except requests.RequestException as e:
        # Maneja errores en la petición
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5342)
