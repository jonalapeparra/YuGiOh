# Yu-Gi-Oh! Duel Lite

Este proyecto es la implementación del **Laboratorio 1** en Java Swing.

## 🚀 Requisitos
- Java 11+
- Maven 3+
- Conexión a Internet (para consultar la API YGOPRODECK y descargar imágenes)

## 📦 Instalación
1. Clona o descomprime este proyecto en tu PC.
2. Abre la carpeta `ygoduel-lite` en tu IDE (IntelliJ IDEA, Eclipse, VSCode con extensión de Java).
3. Importa como **proyecto Maven**.

## ▶️ Ejecución
Desde terminal, estando en la carpeta del proyecto:

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.example.ygoduel.ui.MainUI"
```

O bien, desde tu IDE ejecuta la clase:
```
com.example.ygoduel.ui.MainUI
```

## 🕹️ Cómo jugar
- Pulsa **"Iniciar duelo (cargar cartas)"** para que el programa descargue 3 cartas para ti y 3 para la AI.
- Selecciona el **modo (ATTACK o DEFENSE)** en el combo superior.
- Haz clic en **"Jugar"** debajo de la carta que quieras usar.
- El sistema elegirá una carta y modo para la AI, comparará los valores y mostrará el resultado en el log.
- El primero en ganar **2 rondas** se lleva el duelo.

## 📚 Arquitectura
- `model.Card`: representa una carta (nombre, ATK, DEF, imagen).
- `api.YgoApiClient`: cliente HTTP para la API pública de YGOPRODECK.
- `duel.Duel`: lógica de las rondas y reglas de comparación.
- `duel.BattleListener`: interfaz de eventos entre la lógica y la UI.
- `ui.MainUI`: interfaz Swing con paneles de cartas, botones y log.

## ⚠️ Notas
- Se usa `HttpClient` de Java 11 y la librería `org.json` para parsear la respuesta.
- Todas las llamadas HTTP y carga de imágenes se hacen con `SwingWorker` para **no bloquear la UI**.
- La API entrega cartas de cualquier tipo, por eso el cliente filtra hasta obtener cartas tipo *Monster*.

---
✍️ Proyecto generado automáticamente para el Laboratorio 1 - Yu-Gi-Oh! Duel Lite.
