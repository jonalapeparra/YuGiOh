# Yu-Gi-Oh! Duel Lite
 
Es una versión simplificada del juego de cartas Yu-Gi-Oh!, implementada en Java. Permite a los jugadores enfrentarse en un duelo por turnos contra una inteligencia artificial, utilizando datos reales de cartas obtenidos de la API de YGOPRODECK. La aplicación cuenta con una interfaz gráfica construida con Swing, que muestra las cartas del jugador y de la IA, y soporta mecánicas básicas de juego como los modos de ataque y defensa.

# Características

Interfaz Gráfica: Desarrollada con Java Swing, muestra las cartas del jugador y de la IA con un diseño temático.
Integración con API: Obtiene datos reales de cartas Yu-Gi-Oh! (imágenes, estadísticas) desde la API de YGOPRODECK.
Lógica de Juego: Implementa reglas simplificadas de duelo, con selección de ataque/defensa y puntuación (gana el primero en alcanzar 2 puntos).
Diseño Modular: Organizado en paquetes (ui, api, duelo, model) para facilitar el mantenimiento y la escalabilidad.
Carga Asíncrona: Utiliza SwingWorker para cargar datos de cartas sin bloquear la interfaz gráfica.

# Requisitos Previos

Java Development Kit (JDK): Versión 11 o superior (recomendado: OpenJDK o Oracle JDK).
Maven: Para gestionar dependencias y compilar el proyecto.
Conexión a Internet: Necesaria para descargar datos de cartas desde la API de YGOPRODECK.
Git (opcional): Para clonar el repositorio desde GitHub.
IDE (opcional): IntelliJ IDEA recomendado para una configuración más sencilla.
