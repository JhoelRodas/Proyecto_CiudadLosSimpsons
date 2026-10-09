package com.graphics.modelos;
import com.graphics.Renderer;
/** Kwik-E-Mart: tienda de Apu. El frente mira hacia +Z. Mide unas 11 x 10 unidades con escala 1.0. */
public class KwikEMart {
    public float posX;
    public float posZ;
    public float angulo;
    public float escala = 1.0f;

    // Fuente de píxeles 3x5 para el letrero (cada String es una fila)
    private static final String LETRAS = "KWIK-E-MART";
    private static String[] glifo(char c) {
        switch (c) {
            case 'K': return new String[] { "101", "101", "110", "101", "101" };
            case 'W': return new String[] { "101", "101", "101", "111", "101" };
            case 'I': return new String[] { "111", "010", "010", "010", "111" };
            case 'E': return new String[] { "111", "100", "110", "100", "111" };
            case 'M': return new String[] { "101", "111", "111", "101", "101" };
            case 'A': return new String[] { "010", "101", "111", "101", "101" };
            case 'R': return new String[] { "110", "101", "110", "101", "101" };
            case 'T': return new String[] { "111", "010", "010", "010", "010" };
            default:  return new String[] { "000", "000", "111", "000", "000" }; // '-'
        }
    }

    public KwikEMart(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
    }

    public KwikEMart(float posX, float posZ, float angulo, float escala) {
        this(posX, posZ, angulo);
        this.escala = escala;
    }

    public void dibujar(Renderer r) {
        float[] pared = { 0.96f, 0.94f, 0.88f };
        float[] zocalo = { 0.55f, 0.55f, 0.58f };
        float[] rojo = { 0.85f, 0.15f, 0.15f };
        float[] azul = { 0.15f, 0.25f, 0.65f };
        float[] amarillo = { 0.98f, 0.85f, 0.15f };
        float[] vidrio = { 0.55f, 0.82f, 0.92f };
        float[] marco = { 0.85f, 0.85f, 0.88f };
        float[] asfalto = { 0.30f, 0.31f, 0.33f };
        float[] acera = { 0.75f, 0.74f, 0.70f };
        float[] techo = { 0.45f, 0.45f, 0.48f };
        float[] blanco = { 0.97f, 0.97f, 0.97f };

        float w = 9f, d = 6.5f, h = 4.2f;
        float yb = 0.25f; // el edificio se apoya sobre la acera

        // ---------------- Terreno: estacionamiento, acera y rayas ----------------
        pieza(r, 0, 0.05f, 1.5f, 11f, 0.1f, 10f, asfalto);
        pieza(r, 0, 0.12f, d / 2 + 0.9f, 11f, 0.25f, 1.8f, acera);
        for (int i = -2; i <= 2; i++) {
            pieza(r, i * 2.0f, 0.11f, d / 2 + 3.3f, 0.12f, 0.02f, 1.6f, blanco);
        }
        pieza(r, 0, 0.12f, 0, w + 0.6f, 0.25f, d + 0.6f, acera);

        // ---------------- Cuerpo del edificio ----------------
        pieza(r, 0, yb + h / 2, 0, w, h, d, pared);
        pieza(r, 0, yb + 0.25f, 0, w + 0.1f, 0.5f, d + 0.1f, zocalo);

        // Franja roja y azul bajo el techo
        pieza(r, 0, yb + h - 0.45f, 0, w + 0.12f, 0.5f, d + 0.12f, rojo);
        pieza(r, 0, yb + h - 0.85f, 0, w + 0.12f, 0.22f, d + 0.12f, azul);

        // Techo plano con parapeto
        pieza(r, 0, yb + h + 0.1f, 0, w + 0.4f, 0.2f, d + 0.4f, techo);
        pieza(r, 0, yb + h + 0.35f, d / 2 + 0.1f, w + 0.4f, 0.3f, 0.2f, blanco);
        pieza(r, 0, yb + h + 0.35f, -d / 2 - 0.1f, w + 0.4f, 0.3f, 0.2f, blanco);
        pieza(r, w / 2 + 0.1f, yb + h + 0.35f, 0, 0.2f, 0.3f, d + 0.4f, blanco);
        pieza(r, -w / 2 - 0.1f, yb + h + 0.35f, 0, 0.2f, 0.3f, d + 0.4f, blanco);

        // ---------------- Fachada: ventanales y puerta ----------------
        float zf = d / 2;
        float yv = yb + 2.0f;
        ventanal(r, -2.7f, yv, zf, 3.0f, 2.2f, vidrio, marco);
        ventanal(r, 2.7f, yv, zf, 3.0f, 2.2f, vidrio, marco);

        // Carteles en los vidrios (promociones)
        pieza(r, -3.4f, yv + 0.3f, zf + 0.08f, 0.6f, 0.8f, 0.03f, amarillo);
        pieza(r, -2.5f, yv - 0.2f, zf + 0.08f, 0.7f, 0.5f, 0.03f, rojo);
        pieza(r, -1.9f, yv + 0.4f, zf + 0.08f, 0.5f, 0.6f, 0.03f, blanco);
        // Vaso de Squishee en el ventanal derecho
        pieza(r, 3.3f, yv - 0.35f, zf + 0.1f, 0.55f, 0.7f, 0.05f, azul);
        pieza(r, 3.3f, yv + 0.1f, zf + 0.1f, 0.65f, 0.2f, 0.05f, rojo);
        pieza(r, 3.5f, yv + 0.55f, zf + 0.1f, 0.08f, 0.5f, 0.05f, rojo);
        pieza(r, 2.2f, yv + 0.2f, zf + 0.08f, 0.8f, 0.5f, 0.03f, amarillo);

        // Puerta de vidrio doble
        pieza(r, 0, yb + 1.25f, zf + 0.03f, 1.9f, 2.5f, 0.06f, vidrio);
        pieza(r, 0, yb + 1.25f, zf + 0.07f, 0.08f, 2.5f, 0.06f, marco);
        pieza(r, 0, yb + 2.5f, zf + 0.07f, 2.0f, 0.1f, 0.08f, marco);
        pieza(r, -0.95f, yb + 1.25f, zf + 0.07f, 0.1f, 2.5f, 0.08f, marco);
        pieza(r, 0.95f, yb + 1.25f, zf + 0.07f, 0.1f, 2.5f, 0.08f, marco);
        pieza(r, -0.15f, yb + 1.2f, zf + 0.12f, 0.06f, 0.6f, 0.06f, zocalo);
        pieza(r, 0.15f, yb + 1.2f, zf + 0.12f, 0.06f, 0.6f, 0.06f, zocalo);

        // Techito de entrada
        pieza(r, 0, yb + 2.85f, zf + 0.5f, 2.6f, 0.15f, 1.0f, rojo);
        pieza(r, -1.2f, yb + 1.4f, zf + 0.95f, 0.1f, 2.8f, 0.1f, marco);
        pieza(r, 1.2f, yb + 1.4f, zf + 0.95f, 0.1f, 2.8f, 0.1f, marco);

        // ---------------- Letrero del techo ----------------
        letrero(r, yb + h + 0.45f, zf - 0.2f, rojo, azul, blanco);

        // ---------------- Detalles laterales ----------------
        // Máquina de hielo y bote de basura
        pieza(r, w / 2 + 0.55f, yb + 0.75f, 1.2f, 0.8f, 1.5f, 1.2f, blanco);
        pieza(r, w / 2 + 0.55f, yb + 1.2f, 1.2f, 0.7f, 0.5f, 0.05f, azul);
        pieza(r, -w / 2 - 0.55f, yb + 0.5f, 2.0f, 0.6f, 1.0f, 0.6f, zocalo);
        // Ventana lateral
        pieza(r, w / 2 + 0.02f, yb + 2.2f, -1.0f, 0.05f, 1.2f, 1.6f, vidrio);
        pieza(r, -w / 2 - 0.02f, yb + 2.2f, -1.0f, 0.05f, 1.2f, 1.6f, vidrio);
        // Unidad de aire en el techo
        pieza(r, -2.0f, yb + h + 0.7f, -1.0f, 1.4f, 0.7f, 1.2f, zocalo);
    }

    private void ventanal(Renderer r, float x, float y, float zf, float ancho, float alto, float[] vidrio,
            float[] marco) {
        pieza(r, x, y, zf + 0.02f, ancho, alto, 0.05f, vidrio);
        pieza(r, x, y + alto / 2, zf + 0.06f, ancho + 0.2f, 0.12f, 0.1f, marco);
        pieza(r, x, y - alto / 2, zf + 0.08f, ancho + 0.3f, 0.14f, 0.16f, marco);
        pieza(r, x - ancho / 2, y, zf + 0.06f, 0.12f, alto, 0.1f, marco);
        pieza(r, x + ancho / 2, y, zf + 0.06f, 0.12f, alto, 0.1f, marco);
        pieza(r, x, y, zf + 0.05f, 0.08f, alto, 0.06f, marco);
    }

    /** Letrero con "KWIK-E-MART" hecho de píxeles (cajas). */
    private void letrero(Renderer r, float yBase, float z, float[] letra, float[] borde, float[] fondo) {
        float celda = 0.2f;
        float anchoChar = 3 * celda + celda; // 3 columnas + espacio
        float total = LETRAS.length() * anchoChar - celda;
        float alto = 5 * celda;
        float yCentro = yBase + 0.9f + alto / 2;

        // Patas y tablero
        pieza(r, -total / 2 + 0.6f, yBase + 0.45f, z, 0.15f, 0.9f, 0.15f, borde);
        pieza(r, total / 2 - 0.6f, yBase + 0.45f, z, 0.15f, 0.9f, 0.15f, borde);
        pieza(r, 0, yCentro, z - 0.1f, total + 0.7f, alto + 0.6f, 0.2f, fondo);
        pieza(r, 0, yCentro + (alto + 0.6f) / 2, z - 0.1f, total + 0.9f, 0.14f, 0.28f, borde);
        pieza(r, 0, yCentro - (alto + 0.6f) / 2, z - 0.1f, total + 0.9f, 0.14f, 0.28f, borde);
        pieza(r, -(total + 0.7f) / 2, yCentro, z - 0.1f, 0.14f, alto + 0.74f, 0.28f, borde);
        pieza(r, (total + 0.7f) / 2, yCentro, z - 0.1f, 0.14f, alto + 0.74f, 0.28f, borde);

        for (int i = 0; i < LETRAS.length(); i++) {
            String[] g = glifo(LETRAS.charAt(i));
            float x0 = -total / 2 + i * anchoChar;
            // la "E" central del guion y la "A" van en azul para dar ritmo
            float[] col = (i == 5) ? borde : letra;
            for (int fila = 0; fila < 5; fila++) {
                for (int c = 0; c < 3; c++) {
                    if (g[fila].charAt(c) == '1') {
                        pieza(r, x0 + c * celda + celda / 2, yCentro + alto / 2 - fila * celda - celda / 2,
                                z + 0.05f, celda, celda, 0.1f, col);
                    }
                }
            }
        }
    }

    private void pieza(Renderer r, float x, float y, float z, float sx, float sy, float sz, float[] c) {
        float cos = (float) Math.cos(angulo);
        float sen = (float) Math.sin(angulo);
        float lx = x * escala;
        float lz = z * escala;
        float mundoX = posX + cos * lx + sen * lz;
        float mundoZ = posZ - sen * lx + cos * lz;
        r.cajaInclinada(mundoX, y * escala, mundoZ, sx * escala, sy * escala, sz * escala,
                c[0], c[1], c[2], angulo, 0f, 0f);
    }
}
