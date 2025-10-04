package com.example.ygoduel.model;

import javax.swing.ImageIcon;

public class Carta {
    private final String name; // Nombre de la carta
    private final int atk;// Valor de ataque
    private final int def;// Valor de defensa
    private final String imageUrl;// URL de la imagen obtenida desde la API
    private ImageIcon image; // Imagen ya cargada (se carga después desde la URL)

    /**
     *  Esta es la clase que representa una carta del juego.
     * Contiene nombre, valores de ataque/defensa, URL de la imagen y un icono ya cargado.
     */

    // Constructor
    public Carta(String name, int atk, int def, String imageUrl) {
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.imageUrl = imageUrl;
    }
    // Getters y un setter
    public String getName() { return name; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public String getImageUrl() { return imageUrl; }

    public ImageIcon getImage() { return image; }
    public void setImage(ImageIcon image) { this.image = image; }

    @Override
    public String toString() {
        return String.format("%s (ATK:%d DEF:%d)", name, atk, def);
    }
}
