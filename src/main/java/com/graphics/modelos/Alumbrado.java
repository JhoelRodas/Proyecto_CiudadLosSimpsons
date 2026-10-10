package com.graphics.modelos;

import com.graphics.Renderer;

public class Alumbrado {
    public float posX;
    public float posZ;
    public float angulo;
    public float escala = 1.0f;
    public boolean encendida = true;

    public Alumbrado(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
    }

    public void dibujar(Renderer renderer) {
        float[] colorPoste = { 0.18f, 0.24f, 0.22f };
        float[] colorAdorno = { 0.30f, 0.38f, 0.33f };
        float[] colorBase = { 0.42f, 0.42f, 0.44f };
        float[] colorCabeza = { 0.25f, 0.27f, 0.27f };
        float[] colorLuz = encendida ? new float[] { 1.0f, 0.93f, 0.55f } : new float[] { 0.55f, 0.55f, 0.50f };
        float r45 = (float) (Math.PI / 4);

        // Base escalonada
        dibujarPieza(renderer, 0, 0.12f, 0, 0.8f, 0.24f, 0.8f, colorBase, 0);
        dibujarPieza(renderer, 0, 0.12f, 0, 0.8f, 0.24f, 0.8f, colorBase, r45);
        dibujarPieza(renderer, 0, 0.4f, 0, 0.5f, 0.32f, 0.5f, colorAdorno, 0);
        dibujarPieza(renderer, 0, 0.4f, 0, 0.5f, 0.32f, 0.5f, colorAdorno, r45);

        // Poste con anillos decorativos
        dibujarPieza(renderer, 0, 2.5f, 0, 0.18f, 4.0f, 0.18f, colorPoste, 0);
        dibujarPieza(renderer, 0, 0.9f, 0, 0.3f, 0.12f, 0.3f, colorAdorno, 0);
        dibujarPieza(renderer, 0, 1.2f, 0, 0.26f, 0.08f, 0.26f, colorAdorno, 0);
        dibujarPieza(renderer, 0, 4.3f, 0, 0.3f, 0.12f, 0.3f, colorAdorno, 0);

        // Brazo curvado hacia +X (escalones que suben y bajan)
        dibujarPieza(renderer, 0.2f, 4.55f, 0, 0.5f, 0.16f, 0.16f, colorPoste, 0);
        dibujarPieza(renderer, 0.6f, 4.6f, 0, 0.4f, 0.16f, 0.16f, colorPoste, 0);
        dibujarPieza(renderer, 0.95f, 4.5f, 0, 0.3f, 0.16f, 0.16f, colorPoste, 0);

        // Cabeza de la lámpara
        dibujarPieza(renderer, 1.2f, 4.5f, 0, 0.9f, 0.14f, 0.5f, colorCabeza, 0);
        dibujarPieza(renderer, 1.2f, 4.6f, 0, 0.6f, 0.1f, 0.35f, colorCabeza, 0);
        if (encendida) renderer.entero("uEmision", 1);
        dibujarPieza(renderer, 1.2f, 4.38f, 0, 0.7f, 0.1f, 0.38f, colorLuz, 0);
        dibujarPieza(renderer, 1.2f, 4.28f, 0, 0.4f, 0.08f, 0.22f, colorLuz, 0);
        if (encendida) renderer.entero("uEmision", 0);

        // Segundo farol pequeño del lado opuesto, estilo clásico de doble farol
        dibujarPieza(renderer, -0.2f, 4.5f, 0, 0.4f, 0.16f, 0.16f, colorPoste, 0);
        dibujarPieza(renderer, -0.5f, 4.35f, 0, 0.35f, 0.4f, 0.35f, colorCabeza, 0);
        if (encendida) renderer.entero("uEmision", 1);
        dibujarPieza(renderer, -0.5f, 4.3f, 0, 0.26f, 0.3f, 0.26f, colorLuz, 0);
        if (encendida) renderer.entero("uEmision", 0);
    }

    private void dibujarPieza(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb, float giro) {
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);
        float lx = xLocal * escala;
        float lz = zLocal * escala;
        float mundoX = posX + (coseno * lx) + (seno * lz);
        float mundoZ = posZ - (seno * lx) + (coseno * lz);
        renderer.cajaInclinada(mundoX, yLocal * escala, mundoZ, sx * escala, sy * escala, sz * escala, rgb[0], rgb[1],
                rgb[2], angulo + giro, 0, 0);
    }
}
