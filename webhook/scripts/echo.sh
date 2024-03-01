#!/bin/bash

#!/bin/bash

# Definir variables
TELEGRAM_BOT_TOKEN="6745271724:AAEEMBOiArurpRzQ1JQuRzs_oM_gQgoxOpA"
TELEGRAM_CHAT_ID="3036038"
MESSAGE="Hola desde Bash!"

# Enviar mensaje utilizando cURL y la API de Telegram
curl -s -X POST https://api.telegram.org/bot$TELEGRAM_BOT_TOKEN/sendMessage -d chat_id=$TELEGRAM_CHAT_ID -d text="$MESSAGE"


#echo "Hook information: name=$hook_name, id=$hook_id, method=$hook_method"
#echo "Query parameter: foo=$foo"
#echo "Header parameter: user-agent=$user_agent"
echo "Script parameters: $1" > /tmp/borrar-$hook_id.txt
