package com.graphics.modelos;
import com.graphics.Renderer;
public class Arbol {
    public float posX;
    public float posZ;
    public float escala;
    public float angulo; // Añadido para compatibilidad con VisorModelos
    public int tipo; // 1 = Roble, 2 = Pino, 3 = Abedul, 4 = Palmera, 5 = Cerezo, 6 = Ciprés
    public static final int TIPOS = 6;

    public Arbol(float posX, float posZ, float escala, int tipo) {
        this.posX = posX;
        this.posZ = posZ;
        this.escala = escala;
        this.tipo = tipo;
    }

    public void dibujar(Renderer renderer) {
        // Variación determinista por árbol (misma posición = mismo aspecto)
        float v = Math.abs((float) Math.sin(posX * 12.9898f + posZ * 78.233f));
        float t = 0.9f + 0.2f * v; // factor de brillo 0.9 - 1.1

        float[] tronco = tinte(0.36f, 0.22f, 0.12f, 1f);
        float[] troncoOscuro = tinte(0.28f, 0.17f, 0.09f, 1f);
        float[] oscuro = tinte(0.10f, 0.38f, 0.12f, t);
        float[] medio = tinte(0.18f, 0.55f, 0.18f, t);
        float[] claro = tinte(0.32f, 0.70f, 0.25f, t);

        if (tipo == 1) {
            dibujarRoble(renderer, tronco, troncoOscuro, oscuro, medio, claro, v);
        } else if (tipo == 2) {
            dibujarPino(renderer, tronco, troncoOscuro, oscuro, medio, claro);
        } else if (tipo == 3) {
            dibujarAbedul(renderer, troncoOscuro, medio, claro);
        } else if (tipo == 4) {
            dibujarPalmera(renderer, tronco, troncoOscuro, oscuro, medio, claro);
        } else if (tipo == 5) {
            dibujarCerezo(renderer, tronco, troncoOscuro, t);
        } else if (tipo == 6) {
            dibujarCipres(renderer, tronco, oscuro, medio);
        }
    }

    private static float[] tinte(float r, float g, float b, float f) {
        return new float[] { Math.min(1f, r * f), Math.min(1f, g * f), Math.min(1f, b * f) };
    }

    private static final float R45 = (float) (Math.PI / 4);

    private void dibujarRoble(Renderer r, float[] tronco, float[] troncoOsc,
            float[] oscuro, float[] medio, float[] claro, float v) {
        // Raíces / base ensanchada
        caja(r, 0, 0.15f, 0, 0.75f, 0.3f, 0.75f, troncoOsc, R45);
        caja(r, 0, 0.15f, 0, 0.75f, 0.3f, 0.75f, troncoOsc, 0);
        // Tronco
        caja(r, 0, 0.9f, 0, 0.45f, 1.2f, 0.45f, tronco, 0);
        // Ramas hacia la copa
        caja(r, 0.35f, 1.55f, 0, 0.7f, 0.18f, 0.18f, troncoOsc, 0);
        caja(r, -0.3f, 1.7f, 0.1f, 0.6f, 0.18f, 0.18f, troncoOsc, 0);
        caja(r, 0, 1.65f, -0.35f, 0.18f, 0.18f, 0.7f, troncoOsc, 0);

        // Copa: capa base oscura (sombra inferior)
        caja(r, 0, 1.85f, 0, 2.3f, 0.5f, 2.3f, oscuro, 0);
        caja(r, 0, 1.85f, 0, 2.3f, 0.5f, 2.3f, oscuro, R45);
        // Cuerpo medio
        caja(r, 0, 2.5f, 0, 2.5f, 0.9f, 2.5f, medio, 0);
        caja(r, 0, 2.5f, 0, 2.5f, 0.9f, 2.5f, medio, R45);
        // Bultos laterales para romper la silueta cúbica
        caja(r, 1.2f, 2.2f, 0.3f, 1.0f, 0.9f, 1.0f, medio, R45);
        caja(r, -1.2f, 2.3f, -0.3f, 1.0f, 0.9f, 1.0f, oscuro, R45);
        caja(r, 0.2f, 2.3f, 1.2f, 1.0f, 0.8f, 1.0f, oscuro, R45);
        caja(r, -0.3f, 2.2f, -1.2f, 1.1f, 0.9f, 1.1f, medio, R45);
        // Parte alta clara (donde pega el sol)
        caja(r, 0, 3.25f, 0, 1.7f, 0.7f, 1.7f, claro, 0);
        caja(r, 0, 3.25f, 0, 1.7f, 0.7f, 1.7f, claro, R45);
        caja(r, 0.3f, 3.75f, -0.2f, 0.9f, 0.5f, 0.9f, claro, R45);
        // Destellos
        caja(r, 0.9f, 3.0f, 0.5f, 0.6f, 0.5f, 0.6f, claro, 0);
        caja(r, -0.8f, 2.95f, 0.8f, 0.6f, 0.4f, 0.6f, claro, R45);
    }

    private void dibujarPino(Renderer r, float[] tronco, float[] troncoOsc,
            float[] oscuro, float[] medio, float[] claro) {
        // Tronco con base
        caja(r, 0, 0.1f, 0, 0.55f, 0.2f, 0.55f, troncoOsc, 0);
        caja(r, 0, 0.6f, 0, 0.38f, 1.2f, 0.38f, tronco, 0);

        // Pisos que se estrechan; cada uno es una estrella de 8 puntas (caja + caja girada 45°)
        float[][] pisos = {
            // y, ancho, alto
            { 1.20f, 2.6f, 0.6f },
            { 1.75f, 2.2f, 0.6f },
            { 2.30f, 1.8f, 0.6f },
            { 2.85f, 1.4f, 0.6f },
            { 3.35f, 1.0f, 0.5f },
            { 3.80f, 0.6f, 0.5f },
        };
        for (int i = 0; i < pisos.length; i++) {
            float[] c = (i % 2 == 0) ? oscuro : medio;
            float y = pisos[i][0], w = pisos[i][1], h = pisos[i][2];
            caja(r, 0, y, 0, w, h, w, c, 0);
            caja(r, 0, y, 0, w, h + 0.02f, w, c, R45);
            // Borde superior claro, más pequeño para simular luz sobre cada rama
            caja(r, 0, y + h * 0.5f, 0, w * 0.6f, 0.12f, w * 0.6f, claro, R45);
        }
        // Punta
        caja(r, 0, 4.25f, 0, 0.3f, 0.5f, 0.3f, claro, 0);
    }

    // Abedul: tronco blanco con marcas oscuras, copa amarillo-verdosa y ligera
    private void dibujarAbedul(Renderer r, float[] marca, float[] medio, float[] claro) {
        float[] corteza = { 0.92f, 0.92f, 0.88f };
        float[] hoja = { 0.55f, 0.75f, 0.25f };
        float[] hojaClara = { 0.72f, 0.85f, 0.35f };
        caja(r, 0, 1.4f, 0, 0.3f, 2.8f, 0.3f, corteza, 0);
        // Marcas oscuras
        caja(r, 0, 0.5f, 0, 0.33f, 0.1f, 0.2f, marca, 0);
        caja(r, 0, 1.1f, 0, 0.2f, 0.1f, 0.33f, marca, 0);
        caja(r, 0, 1.7f, 0, 0.33f, 0.08f, 0.2f, marca, 0);
        caja(r, 0, 2.2f, 0, 0.2f, 0.1f, 0.33f, marca, 0);
        // Rama
        caja(r, 0.3f, 2.3f, 0, 0.5f, 0.12f, 0.12f, corteza, 0);
        // Copa alargada y desordenada
        caja(r, 0, 3.0f, 0, 1.5f, 1.0f, 1.5f, hoja, 0);
        caja(r, 0, 3.0f, 0, 1.5f, 1.0f, 1.5f, hoja, R45);
        caja(r, 0.5f, 2.6f, 0.2f, 0.9f, 0.7f, 0.9f, hoja, R45);
        caja(r, -0.5f, 3.4f, -0.2f, 0.9f, 0.8f, 0.9f, hojaClara, R45);
        caja(r, 0, 3.9f, 0, 0.9f, 0.6f, 0.9f, hojaClara, 0);
        caja(r, 0, 4.3f, 0, 0.4f, 0.4f, 0.4f, hojaClara, R45);
    }

    // Palmera: tronco segmentado que se curva y hojas en abanico
    private void dibujarPalmera(Renderer r, float[] tronco, float[] troncoOsc,
            float[] oscuro, float[] medio, float[] claro) {
        for (int i = 0; i < 6; i++) {
            float[] c = (i % 2 == 0) ? tronco : troncoOsc;
            float x = 0.04f * i * i * 0.5f; // curvatura suave
            caja(r, x, 0.4f + i * 0.7f, 0, 0.4f - i * 0.02f, 0.72f, 0.4f - i * 0.02f, c, 0);
        }
        float topX = 0.04f * 25 * 0.5f, topY = 4.6f;
        // Cocos
        float[] coco = { 0.35f, 0.22f, 0.1f };
        caja(r, topX + 0.2f, topY - 0.1f, 0.2f, 0.3f, 0.3f, 0.3f, coco, 0);
        caja(r, topX - 0.2f, topY - 0.1f, -0.2f, 0.3f, 0.3f, 0.3f, coco, 0);
        // 8 hojas: cada una es un segmento largo y otro que cae en la punta
        for (int i = 0; i < 8; i++) {
            float a = i * R45;
            float dx = (float) Math.cos(a), dz = (float) Math.sin(a);
            float[] c = (i % 2 == 0) ? medio : oscuro;
            float giro = -a;
            caja(r, topX + dx * 0.7f, topY + 0.15f, dz * 0.7f, 1.4f, 0.1f, 0.35f, c, giro);
            caja(r, topX + dx * 1.6f, topY - 0.05f, dz * 1.6f, 0.9f, 0.1f, 0.3f, claro, giro);
            caja(r, topX + dx * 2.2f, topY - 0.4f, dz * 2.2f, 0.5f, 0.1f, 0.22f, c, giro);
        }
        caja(r, topX, topY + 0.2f, 0, 0.5f, 0.3f, 0.5f, claro, R45);
    }

    // Cerezo: copa rosa con pétalos claros
    private void dibujarCerezo(Renderer r, float[] tronco, float[] troncoOsc, float t) {
        float[] rosa = tinte(0.95f, 0.55f, 0.70f, t);
        float[] rosaOsc = tinte(0.82f, 0.38f, 0.55f, t);
        float[] rosaClaro = tinte(1.0f, 0.78f, 0.86f, t);
        caja(r, 0, 0.15f, 0, 0.7f, 0.3f, 0.7f, troncoOsc, R45);
        caja(r, 0, 0.9f, 0, 0.4f, 1.2f, 0.4f, tronco, 0);
        caja(r, 0.45f, 1.7f, 0, 1.0f, 0.2f, 0.2f, troncoOsc, 0);
        caja(r, -0.45f, 1.8f, 0.1f, 0.9f, 0.2f, 0.2f, troncoOsc, 0);
        caja(r, 0, 1.9f, 0, 2.6f, 0.5f, 2.6f, rosaOsc, 0);
        caja(r, 0, 1.9f, 0, 2.6f, 0.52f, 2.6f, rosaOsc, R45);
        caja(r, 0, 2.5f, 0, 2.8f, 0.8f, 2.8f, rosa, 0);
        caja(r, 0, 2.5f, 0, 2.8f, 0.82f, 2.8f, rosa, R45);
        caja(r, 0, 3.15f, 0, 2.0f, 0.62f, 2.0f, rosaClaro, R45);
        caja(r, 0, 3.15f, 0, 2.0f, 0.6f, 2.0f, rosa, 0);
        caja(r, 1.3f, 2.3f, 0.4f, 0.9f, 0.8f, 0.9f, rosaClaro, R45);
        caja(r, -1.2f, 2.4f, -0.5f, 1.0f, 0.8f, 1.0f, rosaClaro, R45);
        // Pétalos caídos en el suelo
        caja(r, 1.1f, 0.03f, 0.9f, 0.3f, 0.05f, 0.3f, rosaClaro, 0.3f);
        caja(r, -0.9f, 0.03f, 1.2f, 0.25f, 0.05f, 0.25f, rosa, 0.8f);
        caja(r, 0.4f, 0.03f, -1.3f, 0.3f, 0.05f, 0.3f, rosaClaro, 1.2f);
    }

    // Ciprés: alto, estrecho y oscuro
    private void dibujarCipres(Renderer r, float[] tronco, float[] oscuro, float[] medio) {
        caja(r, 0, 0.2f, 0, 0.3f, 0.4f, 0.3f, tronco, 0);
        float[][] caps = {
            { 1.0f, 1.3f, 1.2f }, { 1.9f, 1.5f, 1.3f }, { 2.9f, 1.5f, 1.3f },
            { 3.9f, 1.3f, 1.2f }, { 4.8f, 1.0f, 1.0f }, { 5.6f, 0.6f, 0.8f }, { 6.2f, 0.3f, 0.5f },
        };
        for (int i = 0; i < caps.length; i++) {
            float[] c = (i % 2 == 0) ? oscuro : medio;
            caja(r, 0, caps[i][0], 0, caps[i][1], caps[i][2], caps[i][1], c, 0);
            caja(r, 0, caps[i][0], 0, caps[i][1], caps[i][2] + 0.02f, caps[i][1], c, R45);
        }
    }

    // Caja centrada con giro adicional en Y
    private void caja(Renderer r, float x, float y, float z, float sx, float sy, float sz,
            float[] color, float giroY) {
        dibujarCuboInclinado(r, x, y, z, sx, sy, sz, color, 0, giroY, 0);
    }

    // Método auxiliar con rotación en 3 ejes
    private void dibujarCuboInclinado(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] color, float angX, float angY, float angZ) {
        
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        float lx = xLocal * escala;
        float lz = zLocal * escala;
        
        float mundoX = posX + (coseno * lx) + (seno * lz);
        float mundoY = yLocal * escala;
        float mundoZ = posZ - (seno * lx) + (coseno * lz);
        
        float ancho = sx * escala;
        float alto = sy * escala;
        float prof = sz * escala;

        renderer.cajaInclinada(mundoX, mundoY, mundoZ, ancho, alto, prof, color[0], color[1], color[2], angulo + angY, angX, angZ);
    }
}
