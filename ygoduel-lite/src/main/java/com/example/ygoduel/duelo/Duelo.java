package com.example.ygoduel.duelo;

import com.example.ygoduel.model.Carta;
import java.util.*;
/**
 * Es la lógica principal del duelo.
 * Controla rondas, cálculo de ganador y puntuación.
 */
public class Duelo {
    private final List<Carta> playerCards;
    private final List<Carta> aiCards;
    private final BattleListener listener;
    private int playerScore = 0;
    private int aiScore = 0;
    private final Random rng = new Random();

    // Posiciones posibles
    public enum Position { ATTACK, DEFENSE }

    public Duelo(List<Carta> playerCards, List<Carta> aiCards, BattleListener listener) {
        this.playerCards = new ArrayList<>(playerCards);
        this.aiCards = new ArrayList<>(aiCards);
        this.listener = listener;
    }

    /**
     * Se ejecuta una ronda con la carta que elige el jugador
     */
    public void playRound(int playerIndex, Position playerPos) {
        if (playerIndex < 0 || playerIndex >= playerCards.size()) {
            listener.onLogMessage("Índice de carta de jugador inválido.");
            return;
        }
        if (playerScore >= 2 || aiScore >= 2) {
            listener.onLogMessage("El duelo ya terminó.");
            return;
        }
        Carta pCard = playerCards.get(playerIndex);

        // La IA elige carta y posición al azar
        int aiIndex = rng.nextInt(aiCards.size());
        Carta aCard = aiCards.get(aiIndex);
        Position aiPos = rng.nextBoolean() ? Position.ATTACK : Position.DEFENSE;

        // Determinamos los valores a comparar según posiciones
        int pValue, aValue;
        if (playerPos == Position.ATTACK && aiPos == Position.ATTACK) {
            pValue = pCard.getAtk(); aValue = aCard.getAtk();
        } else if (playerPos == Position.ATTACK) {
            pValue = pCard.getAtk(); aValue = aCard.getDef();
        } else if (playerPos == Position.DEFENSE && aiPos == Position.ATTACK) {
            pValue = pCard.getDef(); aValue = aCard.getAtk();
        } else {
            pValue = pCard.getDef(); aValue = aCard.getDef();
        }

        // Decidimos ganador de la ronda
        String roundWinner;
        if (pValue > aValue) {
            roundWinner = "Jugador";
            playerScore++;
        } else if (aValue > pValue) {
            roundWinner = "IA";
            aiScore++;
        } else {
            roundWinner = "Empate";
        }

        // Quitamos las cartas usadas
        playerCards.remove(playerIndex);
        aiCards.remove(aiIndex);

        // Notificamos a la IA
        listener.onTurn(pCard.toString(), aCard.toString(), roundWinner);
        listener.onScoreChanged(playerScore, aiScore);

        // Revisamos si alguien ya ganó el duelo
        if (playerScore >= 2) listener.onDuelEnded("Jugador");
        else if (aiScore >= 2) listener.onDuelEnded("IA");
    }
}
