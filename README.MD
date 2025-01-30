# 🎮 Discord Reactions Game Bot

Este proyecto es un bot para **Discord** que permite a los usuarios competir reaccionando con emojis en un mensaje dentro de un canal. A medida que los usuarios interactúan, el bot actualiza un **gráfico en vivo** mostrando qué equipo ha recibido más interacciones. 🚀

---

## 📌 Características
✅ Publica un **mensaje inicial** con tres reacciones (equipo 1, equipo 2, equipo 3).  
✅ Monitorea **clics** en las reacciones, contando solo los aumentos (sin decrementar si se quitan).  
✅ **Muestra una gráfica en vivo** con el conteo de reacciones.  
✅ **Borra y actualiza** el mensaje del **equipo ganador** conforme cambian los datos.  
✅ **Funciona sin base de datos**, usando solo cache.

---

## 🔧 Requisitos previos
Antes de iniciar, asegúrate de tener instalado:

- [Java 17 o superior](https://adoptium.net/)
- [Maven](https://maven.apache.org/download.cgi) (para compilar y manejar dependencias)
- [Git](https://git-scm.com/downloads) (para clonar el repositorio)
- **Discord Bot Token** y **Webhook URL** (para configurar correctamente la app)

### 📌 Verifica que los programas estén instalados
```sh
java -version
mvn -version
git --version
```

---

## 📂 Clonar el repositorio

```sh
git clone https://github.com/Drstone-23/discord-reactions-game
cd discord-reactions-game
```

---

## ⚙️ Configurar `application.properties`
Dentro de `src/main/resources/application.properties`, edita y completa los valores:
```properties
server.port=8081

# Token del bot de Discord
discord.bot.token=TU_DISCORD_BOT_TOKEN

# ID del canal donde el bot publicará mensajes
discord.channel.id=TU_CANAL_ID

# Webhook de Discord
discord.webhook.url=https://discord.com/api/webhooks/TU_WEBHOOK_ID/TU_WEBHOOK_TOKEN
```

### 📌 Cómo obtener estos valores
1️⃣ **Bot Token** → Desde [Discord Developer Portal](https://discord.com/developers/applications).  
2️⃣ **Canal ID** → Activa `Developer Mode` en Discord y haz clic derecho en el canal → "Copiar ID".  
3️⃣ **Webhook URL** → Desde **"Integraciones"** en el canal donde quieras los mensajes.

---

## 🚀 Construir y ejecutar el proyecto

### 🔧 Opción 1: Ejecutarlo directamente con Maven
```sh
mvn spring-boot:run
```

### 🏗 Opción 2: Compilar y ejecutar manualmente
1️⃣ Construir el `.jar`
```sh
mvn clean package
```
2️⃣ Ejecutar la aplicación:
```sh
java -jar target/discord-reactions-game-0.0.1-SNAPSHOT.jar
```
Esto iniciará el servidor en el **puerto 8081** por defecto.

---

## ✅ Verificar que funciona

### 🖥 Comprobar que la API corre bien
Abre un navegador y ve a:
```
http://localhost:8081
```
Si ves la página con la gráfica, **todo funciona correctamente**. 🎉

### 🤖 Comprobar que el bot está en línea
1️⃣ Ve a **Discord** y revisa si el bot está activo en tu servidor.  
2️⃣ El bot debería haber publicado el mensaje con las reacciones.  
3️⃣ Agrega reacciones para probar si actualiza correctamente la gráfica.

---

## 🛠 Modo producción (Opcional)
Si quieres correr el bot en un **servidor remoto** sin que se cierre la terminal:
```sh
nohup java -jar target/discord-reactions-game-0.0.1-SNAPSHOT.jar > logs.txt 2>&1 &
```

Para **detener la aplicación**, usa:
```sh
pkill -f "discord-reactions-game"
```

---

## 📜 Licencia
Este proyecto está bajo la licencia **MIT**, lo que significa que puedes usarlo, modificarlo y distribuirlo libremente.

¡Disfruta del juego de reacciones en Discord! 🎉🔥

