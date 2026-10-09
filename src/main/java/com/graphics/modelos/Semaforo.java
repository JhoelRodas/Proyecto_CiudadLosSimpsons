package com.graphics.modelos;
import com.graphics.Renderer;
public class Semaforo {
    public float posX;
    public float posZ;
    public float angulo;
    public int estado = 2; // 0 = Rojo, 1 = Amarillo, 2 = Verde

    public Semaforo(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
    }

    public void dibujar(Renderer renderer) {
        float[] colorPoste = { 0.22f, 0.30f, 0.25f }; // Verde grisáceo oscuro
        float[] colorBase = { 0.45f, 0.45f, 0.47f };
        float[] colorCaja = { 0.12f, 0.12f, 0.13f }; // Carcasa casi negra
        float[] colorVisera = { 0.07f, 0.07f, 0.08f };
        float r45 = (float) (Math.PI / 4);

        // Base y poste
        dibujarPieza(renderer, 0, 0.15f, 0, 0.7f, 0.3f, 0.7f, colorBase, 0);
        dibujarPieza(renderer, 0, 0.15f, 0, 0.7f, 0.3f, 0.7f, colorBase, r45);
        dibujarPieza(renderer, 0, 2.3f, 0, 0.22f, 4.0f, 0.22f, colorPoste, 0);
        dibujarPieza(renderer, 0, 0.7f, 0, 0.34f, 0.2f, 0.34f, colorPoste, 0);

        // Brazo que sale sobre la calle (+X) con tirante
        dibujarPieza(renderer, 1.3f, 4.35f, 0, 2.7f, 0.18f, 0.18f, colorPoste, 0);
        dibujarPieza(renderer, 0.5f, 3.95f, 0, 0.9f, 0.1f, 0.1f, colorPoste, 0);
        dibujarPieza(renderer, 0, 4.45f, 0, 0.3f, 0.3f, 0.3f, colorPoste, 0);

        // Semáforo colgante al final del brazo
        dibujarSemaforo(renderer, 2.3f, 3.5f, 0.0f, colorCaja, colorVisera);
        // Semáforo pegado al poste (para quien espera en la esquina)
        dibujarSemaforo(renderer, 0.0f, 3.2f, 0.28f, colorCaja, colorVisera);
    }

    private void dibujarSemaforo(Renderer r, float x, float y, float zOff, float[] caja, float[] visera) {
        // Carcasa
        dibujarPieza(r, x, y, zOff, 0.62f, 1.9f, 0.45f, caja, 0);
        // Soporte de unión
        dibujarPieza(r, x, y + 1.05f, zOff, 0.14f, 0.2f, 0.14f, caja, 0);

        float[][] rojo = { { 1.0f, 0.12f, 0.08f }, { 0.34f, 0.07f, 0.05f } };
        float[][] amar = { { 1.0f, 0.85f, 0.10f }, { 0.34f, 0.29f, 0.04f } };
        float[][] verde = { { 0.12f, 0.95f, 0.30f }, { 0.04f, 0.30f, 0.10f } };

        luz(r, x, y + 0.6f, zOff, estado == 0 ? rojo[0] : rojo[1], visera);
        luz(r, x, y, zOff, estado == 1 ? amar[0] : amar[1], visera);
        luz(r, x, y - 0.6f, zOff, estado == 2 ? verde[0] : verde[1], visera);
    }

    private void luz(Renderer r, float x, float y, float zOff, float[] color, float[] visera) {
        // Lente al frente + visera encima
        dibujarPieza(r, x, y, zOff + 0.24f, 0.4f, 0.4f, 0.08f, color, 0);
        dibujarPieza(r, x, y + 0.26f, zOff + 0.33f, 0.5f, 0.07f, 0.28f, visera, 0);
    }

    private void dibujarPieza(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb, float giro) {
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);
        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);
        renderer.cajaInclinada(mundoX, yLocal, mundoZ, sx, sy, sz, rgb[0], rgb[1], rgb[2], angulo + giro, 0, 0);
    }
}
