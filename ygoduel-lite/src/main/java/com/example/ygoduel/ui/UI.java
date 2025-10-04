package com.example.ygoduel.ui;

import com.example.ygoduel.api.YgoApiClient;
import com.example.ygoduel.duelo.BattleListener;
import com.example.ygoduel.duelo.Duelo;
import com.example.ygoduel.model.Carta;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Clase que representa la interfaz gráfica del juego Yu-Gi-Oh! Duel Lite.
 * Gestiona la visualización de cartas del jugador y la IA, el control del duelo y los mensajes de log.
 * Implementa la interfaz BattleListener para recibir actualizaciones del estado del duelo.
 */
public class UI implements BattleListener {
    private final JFrame frame; // Ventana principal
    private final JPanel playerPanel; // Panel para las cartas del jugador
    private final JPanel aiPanel; // Panel para las cartas de la IA
    private final JTextArea logArea; // Área de texto para mensajes de log
    private final JButton btnStart; // Botón para iniciar el duelo
    private final JLabel scoreLabel; // Etiqueta para mostrar el puntaje
    private final JComboBox<String> modeCombo; // Selector de modo (ataque/defensa)

    private final List<Carta> playerCards = new ArrayList<>(); // Lista de cartas del jugador
    private final List<Carta> aiCards = new ArrayList<>(); // Lista de cartas de la IA
    private Duelo duel; // Instancia del duelo
    private final YgoApiClient apiClient = new YgoApiClient(); // Cliente para consumir la API de YGOPRODECK

    /**
     * Constructor que inicializa la interfaz gráfica con todos sus componentes.
     */
    public UI() {
        // Configuración de la ventana principal
        frame = new JFrame("Yu-Gi-Oh! Duel Lite");
        Color VERDE_DUEL = new Color(34, 139, 34); // Verde temático para el fondo
        frame.getContentPane().setBackground(VERDE_DUEL);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Panel superior con controles
        JPanel top = new JPanel();
        Color NARANJA_DUEL = new Color(255, 165, 0); // Naranja para el panel superior
        top.setBackground(NARANJA_DUEL);
        btnStart = new JButton("Iniciar duelo");
        btnStart.setBackground(new Color(255, 69, 0)); // Color rojo-anaranjado para el botón
        btnStart.setForeground(Color.WHITE); // Texto blanco para contraste
        top.add(btnStart);
        top.add(new JLabel("Modo:"));
        modeCombo = new JComboBox<>(new String[]{"ATAQUE", "DEFENSA"});
        Color AZUL_DEFENSA_CLARO = new Color(70, 130, 180); // Azul suave para el combo
        modeCombo.setBackground(AZUL_DEFENSA_CLARO);
        modeCombo.setForeground(Color.BLUE);
        top.add(modeCombo);

        // Etiqueta para mostrar el puntaje
        scoreLabel = new JLabel("Puntaje - Jugador: 0 | IA: 0");
        top.add(scoreLabel);
        frame.add(top, BorderLayout.NORTH);

        // Panel central para las cartas
        JPanel center = new JPanel(new GridLayout(1, 2));
        playerPanel = new JPanel();
        playerPanel.setBackground(new Color(221, 160, 221)); // Fondo para el panel del jugador
        playerPanel.setBorder(new TitledBorder(null, "Tus cartas", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.RED));
        aiPanel = new JPanel();
        aiPanel.setBackground(new Color(221, 160, 221)); // Fondo  para el panel de la IA
        aiPanel.setBorder(new TitledBorder(null, "Cartas IA", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.RED));
        center.add(new JScrollPane(playerPanel));
        center.add(new JScrollPane(aiPanel));
        frame.add(center, BorderLayout.CENTER);

        // Área de log para mensajes
        logArea = new JTextArea(10, 60);
        logArea.setEditable(false);
        logArea.setBackground(new Color(255, 165, 0)); // naranja para el fondo del log
        logArea.setForeground(Color.WHITE); // Texto amarillo para visibilidad
        logArea.setFont(new Font("Arial", Font.BOLD, 12));
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Asignar acción al botón de iniciar duelo
        btnStart.addActionListener(e -> startDuel());
        frame.setVisible(true);
    }

    /**
     * Registra un mensaje en el área de log de forma asíncrona.
     * @param msg Mensaje a mostrar.
     */
    private void log(String msg) {
        SwingUtilities.invokeLater(() -> logArea.append(msg + "\n"));
    }

    /**
     * Inicia un nuevo duelo, cargando cartas para el jugador y la IA.
     */
    private void startDuel() {
        // Deshabilitar el botón mientras se cargan las cartas
        btnStart.setEnabled(false);
        logArea.setText("");
        playerPanel.removeAll();
        aiPanel.removeAll();
        playerCards.clear();
        aiCards.clear();
        scoreLabel.setText("Puntaje - Jugador: 0 | IA: 0");
        log("Cargando cartas...");

        // Cargar cartas en un hilo secundario para no bloquear la UI
        SwingWorker<Void, String> loader = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Cargar 3 cartas para el jugador
                for (int i = 0; i < 3; i++) {
                    playerCards.add(apiClient.fetchRandomMonsterCard(8));
                }
                // Cargar 3 cartas para la IA
                for (int i = 0; i < 3; i++) {
                    aiCards.add(apiClient.fetchRandomMonsterCard(8));
                }
                // Cargar imágenes de las cartas del jugador
                for (Carta c : playerCards) {
                    if (c.getImageUrl() != null) {
                        BufferedImage img = ImageIO.read(new URL(c.getImageUrl()));
                        c.setImage(new ImageIcon(img.getScaledInstance(140, 200, Image.SCALE_SMOOTH)));
                    }
                }
                // Cargar imágenes de las cartas de la IA
                for (Carta c : aiCards) {
                    if (c.getImageUrl() != null) {
                        BufferedImage img = ImageIO.read(new URL(c.getImageUrl()));
                        c.setImage(new ImageIcon(img.getScaledInstance(140, 200, Image.SCALE_SMOOTH)));
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Verificar si hubo excepciones
                    onCardsLoaded();
                } catch (InterruptedException | ExecutionException ex) {
                    log("Error cargando cartas: " + ex.getCause().getMessage());
                    btnStart.setEnabled(true);
                }
            }
        };
        loader.execute();
    }

    /**
     * Actualiza la interfaz una vez que las cartas están cargadas.
     */
    private void onCardsLoaded() {
        log("Cartas cargadas.");
        updatePlayerPanel(); // Actualizar panel del jugador
        updateAiPanel(); // Actualizar panel de la IA
        duel = new Duelo(playerCards, aiCards, this); // Iniciar nuevo duelo
        btnStart.setEnabled(true); // Reactivar botón
    }

    /**
     * Actualiza el panel del jugador con sus cartas actuales.
     */
    private void updatePlayerPanel() {
        playerPanel.removeAll();
        // Crear un panel para cada carta del jugador
        for (int i = 0; i < playerCards.size(); i++) {
            final int idx = i;
            Carta c = playerCards.get(i);
            JPanel cardPane = new JPanel(new BorderLayout());
            JLabel lblImage = new JLabel(c.getImage());
            cardPane.add(lblImage, BorderLayout.CENTER);
            JButton btnPlay = new JButton("Jugar");
            btnPlay.addActionListener(e -> onPlayerChooseCard(idx));
            cardPane.add(btnPlay, BorderLayout.SOUTH);
            playerPanel.add(cardPane);
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    /**
     * Actualiza el panel de la IA con sus cartas actuales.
     */
    private void updateAiPanel() {
        aiPanel.removeAll();
        // Crear un panel para cada carta de la IA
        for (Carta c : aiCards) {
            JPanel cardPane = new JPanel(new BorderLayout());
            JLabel lblImage = new JLabel(c.getImage());
            cardPane.add(lblImage, BorderLayout.CENTER);
            JLabel lblName = new JLabel(c.getName(), SwingConstants.CENTER);
            lblName.setForeground(Color.BLACK);
            lblName.setFont(new Font("Arial", Font.BOLD, 12));
            cardPane.add(lblName, BorderLayout.SOUTH);
            aiPanel.add(cardPane);
        }
        aiPanel.revalidate();
        aiPanel.repaint();
    }

    /**
     * Maneja la acción de jugar una carta del jugador en una ronda.
     * @param idx Índice de la carta seleccionada.
     */
    private void onPlayerChooseCard(int idx) {
        String sel = (String) modeCombo.getSelectedItem();
        Duelo.Position pos = "DEFENSA".equals(sel) ? Duelo.Position.DEFENSE : Duelo.Position.ATTACK;
        duel.playRound(idx, pos); // Ejecutar ronda
        if (idx < playerCards.size()) playerCards.remove(idx); // Eliminar carta jugada
        if (!aiCards.isEmpty()) aiCards.remove(0); // Eliminar carta de la IA
        updatePlayerPanel(); // Actualizar panel del jugador
        updateAiPanel(); // Actualizar panel de la IA
    }

    /**
     * Notifica el resultado de un turno.
     * @param playerCard Carta del jugador.
     * @param aiCard Carta de la IA.
     * @param winner Ganador del turno.
     */
    @Override
    public void onTurn(String playerCard, String aiCard, String winner) {
        log("Turno: " + playerCard + " vs " + aiCard + " -> " + winner);
    }

    /**
     * Actualiza el puntaje mostrado en la interfaz.
     * @param playerScore Puntaje del jugador.
     * @param aiScore Puntaje de la IA.
     */
    @Override
    public void onScoreChanged(int playerScore, int aiScore) {
        scoreLabel.setText("Puntaje - Jugador: " + playerScore + " | IA: " + aiScore);
    }

    /**
     * Notifica el fin del duelo y muestra el ganador.
     * @param winner Ganador del duelo ("Jugador" o "IA").
     */
    @Override
    public void onDuelEnded(String winner) {
        log("DUEL TERMINADO: " + winner);
        JOptionPane.showMessageDialog(frame, "Ganador: " + winner);
    }

    /**
     * Registra un mensaje de log genérico.
     * @param message Mensaje a registrar.
     */
    @Override
    public void onLogMessage(String message) {
        log(message);
    }

    /**
     * Método principal para ejecutar la aplicación.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(com.example.ygoduel.ui.UI::new);
    }
}





