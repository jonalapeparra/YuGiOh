package com.example.ygoduel.duelo;

public interface BattleListener {
    void onTurn(String playerCard, String aiCard, String winner);
    void onScoreChanged(int playerScore, int aiScore);
    void onDuelEnded(String winner);
    void onLogMessage(String message);
}
