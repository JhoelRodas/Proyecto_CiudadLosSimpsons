package com.graphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Tarjeta del juego (abajo a la izquierda): velocidad en km/h con números grandes y estado de las misiones.
 * Colores al estilo Springfield: azul cielo, amarillo Simpson y blanco.
 *
 * No hay texto en el renderer, así que las letras son una fuente de píxeles 3x5 dibujada con cajas.
 * La tarjeta se dibuja en su propio recuadro de pantalla (viewport) usando el modo uMapa = 2.
 */
public class HUD {

    // ---- Paleta ----
    private static final float[] BLANCO = { 0.97f, 0.97f, 0.97f };
    private static final float[] AMARILLO = { 1.00f, 0.84f, 0.10f };
    private static final float[] NARANJA = { 1.00f, 0.55f, 0.10f };
    private static final float[] ROJO = { 0.92f, 0.22f, 0.18f };
    private static final float[] VERDE = { 0.40f, 0.95f, 0.50f };
    private static final float[] AZUL_FONDO = { 0.12f, 0.28f, 0.60f };
    private static final float[] AZUL_OSCURO = { 0.07f, 0.17f, 0.40f };
    private static final float[] AZUL_TENUE = { 0.22f, 0.40f, 0.72f };
    private static final float[] CELESTE = { 0.62f, 0.82f, 1.00f };
    private static final float[] SOMBRA = { 0.03f, 0.06f, 0.16f };

    // Medidas de diseño (se escalan al tamaño real de la tarjeta)
    private static final float W = 320f, H = 238f;

    private Renderer r;
    private float escala;   // píxeles por unidad de diseño
    private float anchoPx, altoPx;

    /**
     * @param velocidad velocidad del auto en unidades por segundo (1 unidad = 1 metro)
     */
    public void dibujar(Renderer renderer, int anchoVentana, int altoVentana, float velocidad, Misiones m,
            float autoX, float autoZ) {
        this.r = renderer;
        anchoPx = Math.max(230f, Math.min(320f, anchoVentana * 0.27f));
        escala = anchoPx / W;
        altoPx = H * escala;
        int margen = 16;

        glDisable(GL_SCISSOR_TEST);
        glDisable(GL_DEPTH_TEST);                       // se dibuja por capas, sin profundidad
        glViewport(margen, margen, (int) anchoPx, (int) altoPx);
        r.entero("uMapa", 2);
        r.entero("uEmision", 1);
        r.decimal("uAspecto", 1.0f);                    // el recuadro ya es de las proporciones de la tarjeta

        int kmh = Math.round(Math.abs(velocidad) * 3.6f);

        fondo();
        velocimetro(kmh);
        misiones(m, autoX, autoZ);

        // Restaurar el estado de dibujo normal
        r.entero("uEmision", 0);
        r.entero("uMapa", 0);
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, anchoVentana, altoVentana);
    }

    // ------------------------------------------------------------ Secciones
    private void fondo() {
        rect(0, 0, W, H, BLANCO);                         // borde blanco
        rect(4, 4, W - 8, H - 8, AZUL_OSCURO);            // contorno oscuro
        rect(7, 7, W - 14, H - 14, AZUL_FONDO);           // panel
        rect(7, 7, W - 14, 5, AZUL_TENUE);                // brillo superior
        rect(7, 108, W - 14, H - 115, AZUL_OSCURO);           // zona de misiones, más oscura
        rect(7, 104, W - 14, 4, AMARILLO);                // línea divisoria
    }

    private void velocimetro(int kmh) {
        String s = String.format("%03d", Math.min(kmh, 999));
        float celda = 12f;
        float x = 22f, y = 18f;
        boolean significativo = false;
        for (int i = 0; i < 3; i++) {
            char c = s.charAt(i);
            if (c != '0' || i == 2) significativo = true;
            float[] col = significativo ? AMARILLO : AZUL_TENUE;     // ceros a la izquierda apagados
            textoConSombra(String.valueOf(c), x + i * 4 * celda, y, celda, col);
        }
        textoConSombra("KM/H", 186f, y + 5 * celda - 5 * 5f, 5f, BLANCO);

        // Barra de velocidad: 12 segmentos, amarillo -> naranja -> rojo
        int segmentos = 12;
        int encendidos = Math.min(segmentos, Math.round(kmh / 70f * segmentos));
        for (int i = 0; i < segmentos; i++) {
            float[] col = AZUL_TENUE;
            if (i < encendidos) {
                col = i < 7 ? AMARILLO : (i < 10 ? NARANJA : ROJO);
            }
            rect(18f + i * 24f, 86f, 20f, 9f, col);
        }
    }

    private void misiones(Misiones m, float autoX, float autoZ) {
        // Progreso: cuadritos a la derecha del título
        for (int i = 0; i < m.total(); i++) {
            float[] col = AZUL_TENUE;
            if (i < m.completadas()) col = AMARILLO;
            else if (i == m.completadas() && !m.terminado()) col = BLANCO;
            rect(236f + i * 18f, 121f, 12f, 12f, SOMBRA);
            rect(237f + i * 18f, 122f, 10f, 10f, col);
        }

        if (m.terminado()) {
            textoConSombra("GANASTE!", 18f, 118f, 5f, VERDE);
            textoConSombra("TIEMPO " + (int) m.tiempo() + " S", 18f, 156f, 4f, BLANCO);
            textoConSombra("R REINICIA", 18f, 184f, 4f, CELESTE);
            return;
        }

        if (m.recienEntregado()) {
            textoConSombra("ENTREGADO!", 18f, 118f, 5f, VERDE);
        } else {
            textoConSombra("ENTREGA " + (m.completadas() + 1) + "/" + m.total(), 18f, 118f, 5f, AMARILLO);
        }
        textoConSombra(m.nombreActual(), 18f, 154f, 4f, BLANCO);
        textoConSombra("DIST " + (int) m.distanciaActual(autoX, autoZ) + " M", 18f, 180f, 4f, CELESTE);
        textoConSombra("TIEMPO " + (int) m.tiempo() + " S", 18f, 206f, 4f, CELESTE);
    }

    // ------------------------------------------------------------ Primitivas (coordenadas de diseño, origen arriba-izquierda)
    private void rect(float x, float y, float w, float h, float[] c) {
        float cx = (x + w / 2) / W * 2f - 1f;
        float cy = 1f - (y + h / 2) / H * 2f;
        r.caja(cx, cy, 0f, w / W * 2f, h / H * 2f, 0.01f, c[0], c[1], c[2]);
    }

    private void textoConSombra(String s, float x, float y, float celda, float[] col) {
        texto(s, x + celda * 0.3f, y + celda * 0.3f, celda, SOMBRA);
        texto(s, x, y, celda, col);
    }

    /** Dibuja texto con la fuente 3x5. Une los píxeles contiguos de cada fila para usar menos cajas. */
    private void texto(String s, float x, float y, float celda, float[] col) {
        float cursor = x;
        for (int i = 0; i < s.length(); i++) {
            String[] g = glifo(s.charAt(i));
            for (int fila = 0; fila < 5; fila++) {
                int c = 0;
                while (c < 3) {
                    if (g[fila].charAt(c) == '1') {
                        int inicio = c;
                        while (c < 3 && g[fila].charAt(c) == '1') c++;
                        rect(cursor + inicio * celda, y + fila * celda, (c - inicio) * celda, celda, col);
                    } else {
                        c++;
                    }
                }
            }
            cursor += 4 * celda;
        }
    }

    private static String[] glifo(char c) {
        switch (Character.toUpperCase(c)) {
            case 'A': return new String[] { "010", "101", "111", "101", "101" };
            case 'B': return new String[] { "110", "101", "110", "101", "110" };
            case 'C': return new String[] { "011", "100", "100", "100", "011" };
            case 'D': return new String[] { "110", "101", "101", "101", "110" };
            case 'E': return new String[] { "111", "100", "110", "100", "111" };
            case 'F': return new String[] { "111", "100", "110", "100", "100" };
            case 'G': return new String[] { "011", "100", "101", "101", "011" };
            case 'H': return new String[] { "101", "101", "111", "101", "101" };
            case 'I': return new String[] { "111", "010", "010", "010", "111" };
            case 'J': return new String[] { "001", "001", "001", "101", "010" };
            case 'K': return new String[] { "101", "101", "110", "101", "101" };
            case 'L': return new String[] { "100", "100", "100", "100", "111" };
            case 'M': return new String[] { "101", "111", "111", "101", "101" };
            case 'N': return new String[] { "110", "101", "101", "101", "101" };
            case 'O': return new String[] { "010", "101", "101", "101", "010" };
            case 'P': return new String[] { "110", "101", "110", "100", "100" };
            case 'Q': return new String[] { "010", "101", "101", "111", "011" };
            case 'R': return new String[] { "110", "101", "110", "101", "101" };
            case 'S': return new String[] { "011", "100", "010", "001", "110" };
            case 'T': return new String[] { "111", "010", "010", "010", "010" };
            case 'U': return new String[] { "101", "101", "101", "101", "111" };
            case 'V': return new String[] { "101", "101", "101", "101", "010" };
            case 'W': return new String[] { "101", "101", "101", "111", "101" };
            case 'X': return new String[] { "101", "101", "010", "101", "101" };
            case 'Y': return new String[] { "101", "101", "010", "010", "010" };
            case 'Z': return new String[] { "111", "001", "010", "100", "111" };
            case '0': return new String[] { "111", "101", "101", "101", "111" };
            case '1': return new String[] { "010", "110", "010", "010", "111" };
            case '2': return new String[] { "111", "001", "111", "100", "111" };
            case '3': return new String[] { "111", "001", "111", "001", "111" };
            case '4': return new String[] { "101", "101", "111", "001", "001" };
            case '5': return new String[] { "111", "100", "111", "001", "111" };
            case '6': return new String[] { "111", "100", "111", "101", "111" };
            case '7': return new String[] { "111", "001", "010", "010", "010" };
            case '8': return new String[] { "111", "101", "111", "101", "111" };
            case '9': return new String[] { "111", "101", "111", "001", "111" };
            case '/': return new String[] { "001", "001", "010", "100", "100" };
            case ':': return new String[] { "000", "010", "000", "010", "000" };
            case '-': return new String[] { "000", "000", "111", "000", "000" };
            case '.': return new String[] { "000", "000", "000", "000", "010" };
            case '!': return new String[] { "010", "010", "010", "000", "010" };
            default:  return new String[] { "000", "000", "000", "000", "000" }; // espacio
        }
    }
}
