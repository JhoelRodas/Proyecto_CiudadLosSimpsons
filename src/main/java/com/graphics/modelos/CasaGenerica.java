package com.graphics.modelos;
import com.graphics.Renderer;
/**
 * Casa genérica para rellenar la ciudad. El frente mira hacia +Z (igual que CasaSimpson).
 *
 * Hay 4 estilos de construcción y 6 paletas de color. Con el constructor de 4 parámetros
 * basta un número de semilla (por ejemplo el índice de la celda) y se obtiene una casa distinta:
 * new CasaGenerica(x, z, angulo, semilla)
 *
 * Estilos: 0 = Cottage con porche, 1 = Dos pisos, 2 = Moderna con garaje, 3 = Techo piramidal.
 * Tamaño aproximado: 7 x 6 unidades (cabe en una celda de 9x9). Usa "escala" para agrandar o achicar.
 */
public class CasaGenerica {
    public static final int ESTILOS = 4;

    public float posX;
    public float posZ;
    public float angulo;
    public float escala = 1.0f;
    public int estilo;
    public int variante;

    private static final float[][] PAREDES = {
        { 0.95f, 0.88f, 0.70f }, // Crema
        { 0.80f, 0.90f, 0.95f }, // Celeste pálido
        { 0.93f, 0.80f, 0.80f }, // Rosa pálido
        { 0.78f, 0.88f, 0.72f }, // Verde menta
        { 0.96f, 0.93f, 0.55f }, // Amarillo suave
        { 0.85f, 0.85f, 0.88f }, // Gris perla
    };
    private static final float[][] TECHOS = {
        { 0.65f, 0.25f, 0.15f }, // Teja
        { 0.30f, 0.32f, 0.40f }, // Pizarra
        { 0.45f, 0.30f, 0.20f }, // Madera
        { 0.25f, 0.45f, 0.35f }, // Verde oscuro
    };

    public CasaGenerica(float posX, float posZ, float angulo, int estilo, int variante) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
        this.estilo = Math.floorMod(estilo, ESTILOS);
        this.variante = Math.abs(variante);
    }

    /** Todo sale de una sola semilla: estilo y colores. */
    public CasaGenerica(float posX, float posZ, float angulo, int semilla) {
        this(posX, posZ, angulo, semilla, semilla * 7 + semilla / ESTILOS);
    }

    public void dibujar(Renderer r) {
        float[] pared = PAREDES[variante % PAREDES.length];
        float[] techo = TECHOS[(variante / 2 + variante) % TECHOS.length];
        float[] madera = { 0.42f, 0.26f, 0.15f };
        float[] vidrio = { 0.40f, 0.70f, 0.90f };
        float[] blanco = { 0.95f, 0.95f, 0.95f };
        float[] base = { 0.55f, 0.55f, 0.55f };

        switch (estilo) {
            case 0: cottage(r, pared, techo, madera, vidrio, blanco, base); break;
            case 1: dosPisos(r, pared, techo, madera, vidrio, blanco, base); break;
            case 2: moderna(r, pared, techo, madera, vidrio, blanco, base); break;
            default: piramidal(r, pared, techo, madera, vidrio, blanco, base); break;
        }
    }

    // ---------------------------------------------------------------- Estilo 0
    private void cottage(Renderer r, float[] pared, float[] techo, float[] madera, float[] vidrio,
            float[] blanco, float[] base) {
        float w = 7f, d = 5.5f, h = 3.2f;
        pieza(r, 0, 0.1f, 0, w + 0.3f, 0.2f, d + 0.3f, base);
        pieza(r, 0, h / 2 + 0.2f, 0, w, h, d, pared);
        techoDosAguas(r, w, d, h + 0.2f, techo, pared);

        // Chimenea de ladrillo
        float[] ladrillo = { 0.6f, 0.3f, 0.25f };
        pieza(r, -w / 2 + 1.0f, h + 1.4f, -0.8f, 0.8f, 2.4f, 0.9f, ladrillo);
        pieza(r, -w / 2 + 1.0f, h + 2.65f, -0.8f, 1.0f, 0.15f, 1.1f, base);

        // Frente: puerta con porche y dos ventanas
        puerta(r, 0, 0.2f, d / 2, madera);
        pieza(r, 0, 2.45f, d / 2 + 0.7f, 2.4f, 0.15f, 1.6f, techo);
        pieza(r, -1.05f, 1.3f, d / 2 + 1.3f, 0.15f, 2.2f, 0.15f, blanco);
        pieza(r, 1.05f, 1.3f, d / 2 + 1.3f, 0.15f, 2.2f, 0.15f, blanco);
        pieza(r, 0, 0.25f, d / 2 + 0.7f, 2.4f, 0.1f, 1.6f, base);
        ventana(r, -2.4f, 1.8f, d / 2, vidrio, blanco);
        ventana(r, 2.4f, 1.8f, d / 2, vidrio, blanco);
        // Ventana trasera/lateral
        ventanaLateral(r, w / 2, 1.8f, 0, vidrio, blanco);
    }

    // ---------------------------------------------------------------- Estilo 1
    private void dosPisos(Renderer r, float[] pared, float[] techo, float[] madera, float[] vidrio,
            float[] blanco, float[] base) {
        float w = 6.5f, d = 5.5f, h = 5.4f;
        pieza(r, 0, 0.1f, 0, w + 0.3f, 0.2f, d + 0.3f, base);
        pieza(r, 0, h / 2 + 0.2f, 0, w, h, d, pared);
        // Cinta entre pisos
        pieza(r, 0, 2.9f, 0, w + 0.1f, 0.15f, d + 0.1f, blanco);
        techoDosAguas(r, w, d, h + 0.2f, techo, pared);

        puerta(r, 1.5f, 0.2f, d / 2, madera);
        pieza(r, 1.5f, 2.35f, d / 2 + 0.35f, 1.8f, 0.12f, 0.7f, techo);
        ventana(r, -1.7f, 1.8f, d / 2, vidrio, blanco);
        // Piso superior
        ventana(r, -1.7f, 4.1f, d / 2, vidrio, blanco);
        ventana(r, 0.2f, 4.1f, d / 2, vidrio, blanco);
        ventana(r, 2.1f, 4.1f, d / 2, vidrio, blanco);
        // Balconcito
        pieza(r, -1.7f, 2.95f, d / 2 + 0.4f, 1.8f, 0.12f, 0.8f, blanco);
        ventanaLateral(r, w / 2, 1.8f, 0, vidrio, blanco);
        ventanaLateral(r, w / 2, 4.1f, 0, vidrio, blanco);
        ventanaLateral(r, -w / 2, 4.1f, 0, vidrio, blanco);

        float[] ladrillo = { 0.6f, 0.3f, 0.25f };
        pieza(r, w / 2 - 1.0f, h + 1.5f, -0.8f, 0.8f, 2.6f, 0.9f, ladrillo);
    }

    // ---------------------------------------------------------------- Estilo 2
    private void moderna(Renderer r, float[] pared, float[] techo, float[] madera, float[] vidrio,
            float[] blanco, float[] base) {
        float w = 5.5f, d = 6f, h = 3.6f;
        float[] oscuro = { 0.25f, 0.27f, 0.30f };
        pieza(r, 0.7f, 0.1f, 0, w + 3.6f, 0.2f, d + 0.3f, base);
        // Volumen principal (más alto) y garaje bajo
        pieza(r, -0.8f, h / 2 + 0.2f, 0, w, h, d, pared);
        pieza(r, 3.0f, 1.3f, 0.6f, 3.0f, 2.2f, d - 1.2f, pared);
        // Techos planos con borde
        pieza(r, -0.8f, h + 0.3f, 0, w + 0.3f, 0.2f, d + 0.3f, techo);
        pieza(r, 3.0f, 2.5f, 0.6f, 3.3f, 0.2f, d - 0.9f, techo);
        // Tragaluz y tanque
        pieza(r, -1.5f, h + 0.65f, -1.0f, 1.2f, 0.5f, 1.2f, oscuro);
        // Ventanal grande
        pieza(r, -1.9f, 2.2f, d / 2 + 0.01f, 2.6f, 2.0f, 0.08f, vidrio);
        pieza(r, -1.9f, 2.2f, d / 2 + 0.05f, 0.08f, 2.0f, 0.06f, oscuro);
        pieza(r, -1.9f, 2.2f, d / 2 + 0.05f, 2.6f, 0.08f, 0.06f, oscuro);
        // Puerta y ventana alta
        puerta(r, 0.7f, 0.2f, d / 2, oscuro);
        ventana(r, 0.7f, 3.0f, d / 2, vidrio, oscuro);
        // Garaje
        pieza(r, 3.0f, 1.1f, 0.6f + (d - 1.2f) / 2 + 0.02f, 2.4f, 1.9f, 0.06f, blanco);
        for (int i = 0; i < 5; i++) {
            pieza(r, 3.0f, 0.4f + i * 0.38f, 0.6f + (d - 1.2f) / 2 + 0.05f, 2.4f, 0.04f, 0.03f, oscuro);
        }
    }

    // ---------------------------------------------------------------- Estilo 3
    private void piramidal(Renderer r, float[] pared, float[] techo, float[] madera, float[] vidrio,
            float[] blanco, float[] base) {
        float w = 6f, d = 6f, h = 3.4f;
        pieza(r, 0, 0.1f, 0, w + 0.3f, 0.2f, d + 0.3f, base);
        pieza(r, 0, h / 2 + 0.2f, 0, w, h, d, pared);
        // Techo escalonado en 6 niveles
        int pasos = 6;
        float alturaPaso = 0.45f;
        for (int i = 0; i < pasos; i++) {
            float f = 1.0f - (float) i / pasos;
            float tw = (w + 0.8f) * f, td = (d + 0.8f) * f;
            pieza(r, 0, h + 0.2f + alturaPaso * i + alturaPaso / 2, 0, tw, alturaPaso, td, techo);
        }
        pieza(r, 0, h + 0.2f + alturaPaso * pasos + 0.2f, 0, 0.15f, 0.5f, 0.15f, blanco);

        puerta(r, 0, 0.2f, d / 2, madera);
        pieza(r, 0, 2.45f, d / 2 + 0.3f, 1.8f, 0.12f, 0.6f, blanco);
        ventana(r, -2.0f, 1.9f, d / 2, vidrio, blanco);
        ventana(r, 2.0f, 1.9f, d / 2, vidrio, blanco);
        ventanaLateral(r, w / 2, 1.9f, 0, vidrio, blanco);
        ventanaLateral(r, -w / 2, 1.9f, 0, vidrio, blanco);
        // Escalones de entrada
        pieza(r, 0, 0.2f, d / 2 + 0.5f, 1.6f, 0.2f, 0.6f, base);
    }

    // ---------------------------------------------------------------- Piezas comunes
    /** Techo a dos aguas con la cumbrera en el eje X y frontones laterales rellenos. */
    private void techoDosAguas(Renderer r, float w, float d, float yBase, float[] techo, float[] pared) {
        float ang = 0.45f;
        float mitad = d / 2;
        float subida = mitad * (float) Math.tan(ang);
        float largo = mitad / (float) Math.cos(ang) + 0.3f;
        float yc = yBase + subida / 2 + 0.05f;
        inclinada(r, 0, yc, mitad / 2, w + 0.6f, 0.2f, largo, techo, ang);
        inclinada(r, 0, yc, -mitad / 2, w + 0.6f, 0.2f, largo, techo, -ang);
        gable(r, -w / 2, yBase, d, subida, pared);
        gable(r, w / 2, yBase, d, subida, pared);
    }

    private void gable(Renderer r, float x, float yBase, float anchoTotal, float altoTotal, float[] color) {
        int pasos = 12;
        float h = altoTotal / pasos;
        for (int i = 0; i < pasos; i++) {
            float y = yBase + i * h + h / 2;
            float ancho = anchoTotal * (1.0f - (float) i / pasos);
            pieza(r, x, y, 0, 0.2f, h, ancho, color);
        }
    }

    private void puerta(Renderer r, float x, float yBase, float zFrente, float[] color) {
        pieza(r, x, yBase + 1.0f, zFrente + 0.03f, 1.1f, 2.0f, 0.06f, color);
        pieza(r, x + 0.35f, yBase + 1.0f, zFrente + 0.08f, 0.1f, 0.1f, 0.05f, new float[] { 0.9f, 0.8f, 0.2f });
    }

    private void ventana(Renderer r, float x, float y, float zFrente, float[] vidrio, float[] marco) {
        pieza(r, x, y, zFrente + 0.02f, 1.1f, 1.1f, 0.05f, vidrio);
        pieza(r, x, y + 0.55f, zFrente + 0.05f, 1.25f, 0.1f, 0.08f, marco);
        pieza(r, x, y - 0.55f, zFrente + 0.07f, 1.35f, 0.1f, 0.14f, marco);
        pieza(r, x + 0.55f, y, zFrente + 0.05f, 0.1f, 1.2f, 0.08f, marco);
        pieza(r, x - 0.55f, y, zFrente + 0.05f, 0.1f, 1.2f, 0.08f, marco);
        pieza(r, x, y, zFrente + 0.05f, 0.08f, 1.1f, 0.06f, marco);
        pieza(r, x, y, zFrente + 0.05f, 1.1f, 0.08f, 0.06f, marco);
    }

    private void ventanaLateral(Renderer r, float xPared, float y, float z, float[] vidrio, float[] marco) {
        float dir = Math.signum(xPared);
        pieza(r, xPared + dir * 0.02f, y, z, 0.05f, 1.1f, 1.1f, vidrio);
        pieza(r, xPared + dir * 0.05f, y, z, 0.08f, 1.2f, 0.1f, marco);
        pieza(r, xPared + dir * 0.05f, y, z, 0.08f, 0.1f, 1.2f, marco);
        pieza(r, xPared + dir * 0.05f, y + 0.55f, z, 0.08f, 0.1f, 1.25f, marco);
        pieza(r, xPared + dir * 0.05f, y - 0.55f, z, 0.08f, 0.1f, 1.25f, marco);
    }

    // ---------------------------------------------------------------- Transformación a mundo
    private void pieza(Renderer r, float x, float y, float z, float sx, float sy, float sz, float[] c) {
        inclinada(r, x, y, z, sx, sy, sz, c, 0f);
    }

    private void inclinada(Renderer r, float x, float y, float z, float sx, float sy, float sz,
            float[] c, float angX) {
        float cos = (float) Math.cos(angulo);
        float sen = (float) Math.sin(angulo);
        float lx = x * escala;
        float lz = z * escala;
        float mundoX = posX + cos * lx + sen * lz;
        float mundoZ = posZ - sen * lx + cos * lz;
        r.cajaInclinada(mundoX, y * escala, mundoZ, sx * escala, sy * escala, sz * escala,
                c[0], c[1], c[2], angulo, angX, 0f);
    }
}
