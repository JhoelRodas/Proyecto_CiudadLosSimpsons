package com.graphics.modelos;

import com.graphics.Renderer;

/**
 * Parque genérico de una celda.
 * Contiene pasto, un árbol y un banco (para cumplir el requisito del enunciado).
 */
public class Parque {
    public float posX;
    public float posZ;
    public float escala = 1.0f;
    public int variante;

    private Arbol arbol;

    public Parque(float posX, float posZ, int variante) {
        this.posX = posX;
        this.posZ = posZ;
        this.variante = variante;
        // Tipo de árbol según variante
        int tipoArbol = (variante % 6) + 1;
        this.arbol = new Arbol(0, 0, 1.2f, tipoArbol); // Escala 1.2f para que sea visible
    }

    public void dibujar(Renderer r) {
        float[] pasto = { 0.35f, 0.65f, 0.28f };
        float[] pastoOscuro = { 0.30f, 0.58f, 0.24f };
        float[] sendero = { 0.75f, 0.72f, 0.65f };

        // Terreno base (10x10 escalado)
        float tamañoBase = 10.0f * escala;
        r.caja(posX, 0.05f, posZ, tamañoBase, 0.1f, tamañoBase, pasto[0], pasto[1], pasto[2]);
        
        // Un pequeño sendero en diagonal o al centro
        r.cajaGirada(posX, 0.06f, posZ, 4f * escala, 0.1f, tamañoBase, sendero[0], sendero[1], sendero[2], (float)(Math.PI / 4) * (variante % 2));

        // Un banco de madera
        float bancoX = posX + 2.0f * escala;
        float bancoZ = posZ - 1.5f * escala;
        dibujarBanca(r, bancoX, bancoZ, (float)(Math.PI / 2) * (variante % 4));

        // El árbol
        arbol.posX = posX - 1.5f * escala;
        arbol.posZ = posZ + 1.5f * escala;
        // Mantener el tamaño del árbol acorde a la escala del parque
        float escalaArbolOriginal = arbol.escala;
        arbol.escala = 1.2f * escala;
        arbol.dibujar(r);
        arbol.escala = escalaArbolOriginal; // Restaurar
    }

    // Banca de madera con respaldo
    private void dibujarBanca(Renderer r, float cx, float cz, float giro) {
        float[] madera = { 0.55f, 0.35f, 0.18f };
        float[] hierro = { 0.18f, 0.20f, 0.20f };
        float c = (float) Math.cos(giro), s = (float) Math.sin(giro);
        float[][] piezas = {
            // ox, oy, oz, sx, sy, sz, tipo (0 madera, 1 hierro)
            { 0, 0.55f, 0, 2.0f, 0.1f, 0.6f, 0 },
            { 0, 0.95f, 0.3f, 2.0f, 0.5f, 0.1f, 0 },
            { -0.85f, 0.28f, 0, 0.1f, 0.55f, 0.55f, 1 },
            { 0.85f, 0.28f, 0, 0.1f, 0.55f, 0.55f, 1 },
            { -0.85f, 0.8f, 0.3f, 0.1f, 0.9f, 0.1f, 1 },
            { 0.85f, 0.8f, 0.3f, 0.1f, 0.9f, 0.1f, 1 },
        };
        for (float[] p : piezas) {
            float lx = cx + (c * p[0] + s * p[2]) * escala;
            float lz = cz + (-s * p[0] + c * p[2]) * escala;
            float[] color = p[6] == 0 ? madera : hierro;
            r.cajaGirada(lx, p[1] * escala, lz, p[3] * escala, p[4] * escala, p[5] * escala, color[0], color[1], color[2], giro);
        }
    }
}
