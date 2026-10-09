package com.graphics.modelos;
import com.graphics.Renderer;
/** Plaza central estilo Springfield: fuente, estatua de Jebediah, bancas, faroles, árboles y jardineras. */
public class Plaza {
    public float posX;
    public float posZ;
    public float angulo;
    public float escala = 1.0f; // 1.0 = 15x15 unidades; 2.0 = 30x30, etc.

    private static final float R45 = (float) (Math.PI / 4);
    private final Arbol[] arboles = new Arbol[4];
    private final Alumbrado[] faroles = new Alumbrado[4];

    public Plaza(float posX, float posZ, float angulo, float escala) {
        this(posX, posZ, angulo);
        this.escala = escala;
    }

    public Plaza(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
        int[] tipos = { 1, 5, 2, 3 };
        for (int i = 0; i < arboles.length; i++) {
            arboles[i] = new Arbol(0, 0, 1.0f, tipos[i]);
        }
        for (int i = 0; i < faroles.length; i++) {
            faroles[i] = new Alumbrado(0, 0, 0);
        }
    }

    public void dibujar(Renderer r) {
        float[] pasto = { 0.35f, 0.65f, 0.28f };
        float[] pastoOsc = { 0.30f, 0.58f, 0.24f };
        float[] cemento = { 0.80f, 0.77f, 0.70f };
        float[] cementoOsc = { 0.68f, 0.65f, 0.58f };
        float[] borde = { 0.55f, 0.53f, 0.50f };

        // --- Terreno y senderos ---
        pieza(r, 0, 0.05f, 0, 15f, 0.1f, 15f, borde, 0);
        pieza(r, 0, 0.07f, 0, 14.4f, 0.1f, 14.4f, pasto, 0);
        // Parches de pasto más oscuro para dar textura
        pieza(r, -3.6f, 0.125f, -3.6f, 5.4f, 0.02f, 5.4f, pastoOsc, 0);
        pieza(r, 3.6f, 0.125f, 3.6f, 5.4f, 0.02f, 5.4f, pastoOsc, 0);
        // Senderos en cruz
        pieza(r, 0, 0.1f, 0, 2.6f, 0.12f, 14.4f, cemento, 0);
        pieza(r, 0, 0.1f, 0, 14.4f, 0.12f, 2.6f, cemento, 0);
        // Círculo central (octágono hecho con dos cajas)
        pieza(r, 0, 0.12f, 0, 6.4f, 0.12f, 6.4f, cementoOsc, 0);
        pieza(r, 0, 0.12f, 0, 6.4f, 0.12f, 6.4f, cementoOsc, R45);
        pieza(r, 0, 0.14f, 0, 5.4f, 0.12f, 5.4f, cemento, 0);
        pieza(r, 0, 0.14f, 0, 5.4f, 0.12f, 5.4f, cemento, R45);

        dibujarFuente(r);
        dibujarEstatua(r, 0, -5.2f);

        // --- Bancas mirando a la fuente ---
        banca(r, 0, 3.9f, 0f);
        banca(r, 0, -3.9f, (float) Math.PI);
        banca(r, 3.9f, 0, (float) (Math.PI / 2));
        banca(r, -3.9f, 0, (float) (-Math.PI / 2));

        // --- Jardineras con flores en cada cuadrante ---
        jardinera(r, -4.2f, -4.2f);
        jardinera(r, 4.2f, -4.2f);
        jardinera(r, -4.2f, 4.2f);
        jardinera(r, 4.2f, 4.2f);

        // --- Árboles en las esquinas ---
        float[][] pa = { { -5.8f, -5.8f }, { 5.8f, -5.8f }, { 5.8f, 5.8f }, { -5.8f, 5.8f } };
        for (int i = 0; i < arboles.length; i++) {
            float[] m = aMundo(pa[i][0], pa[i][1]);
            arboles[i].posX = m[0];
            arboles[i].posZ = m[1];
            arboles[i].angulo = angulo;
            arboles[i].escala = escala;
            arboles[i].dibujar(r);
        }

        // --- Faroles junto a los senderos, con el brazo apuntando al centro ---
        float[][] pf = { { -1.9f, -1.9f }, { 1.9f, -1.9f }, { 1.9f, 1.9f }, { -1.9f, 1.9f } };
        for (int i = 0; i < faroles.length; i++) {
            float[] m = aMundo(pf[i][0], pf[i][1]);
            faroles[i].posX = m[0];
            faroles[i].posZ = m[1];
            faroles[i].angulo = angulo + (float) Math.atan2(pf[i][1], -pf[i][0]);
            faroles[i].escala = escala;
            faroles[i].dibujar(r);
        }
    }

    private void dibujarFuente(Renderer r) {
        float[] piedra = { 0.74f, 0.72f, 0.68f };
        float[] piedraOsc = { 0.60f, 0.58f, 0.55f };
        float[] agua = { 0.30f, 0.65f, 0.90f };
        float[] aguaClara = { 0.65f, 0.88f, 1.0f };

        // Estanque: agua y muro
        pieza(r, 0, 0.3f, 0, 3.6f, 0.2f, 3.6f, agua, 0);
        pieza(r, 0, 0.3f, 0, 3.6f, 0.2f, 3.6f, agua, R45);
        pieza(r, 0, 0.3f, 1.85f, 4.1f, 0.5f, 0.35f, piedra, 0);
        pieza(r, 0, 0.3f, -1.85f, 4.1f, 0.5f, 0.35f, piedra, 0);
        pieza(r, 1.85f, 0.3f, 0, 0.35f, 0.5f, 4.1f, piedra, 0);
        pieza(r, -1.85f, 0.3f, 0, 0.35f, 0.5f, 4.1f, piedra, 0);
        // Tapa del muro más oscura
        pieza(r, 0, 0.58f, 1.85f, 4.2f, 0.08f, 0.45f, piedraOsc, 0);
        pieza(r, 0, 0.58f, -1.85f, 4.2f, 0.08f, 0.45f, piedraOsc, 0);
        pieza(r, 1.85f, 0.58f, 0, 0.45f, 0.08f, 4.2f, piedraOsc, 0);
        pieza(r, -1.85f, 0.58f, 0, 0.45f, 0.08f, 4.2f, piedraOsc, 0);

        // Pedestal central y platos
        pieza(r, 0, 0.8f, 0, 0.7f, 1.1f, 0.7f, piedra, 0);
        pieza(r, 0, 1.4f, 0, 2.2f, 0.18f, 2.2f, piedra, 0);
        pieza(r, 0, 1.4f, 0, 2.2f, 0.18f, 2.2f, piedra, R45);
        pieza(r, 0, 1.55f, 0, 1.8f, 0.1f, 1.8f, aguaClara, 0);
        pieza(r, 0, 1.55f, 0, 1.8f, 0.1f, 1.8f, aguaClara, R45);
        pieza(r, 0, 1.85f, 0, 0.35f, 0.7f, 0.35f, piedraOsc, 0);
        pieza(r, 0, 2.3f, 0, 1.0f, 0.12f, 1.0f, piedra, R45);
        // Chorro
        pieza(r, 0, 2.7f, 0, 0.12f, 0.8f, 0.12f, aguaClara, 0);
        pieza(r, 0, 3.15f, 0, 0.3f, 0.12f, 0.3f, aguaClara, R45);
    }

    // Estatua de bronce sobre pedestal (estilo Jebediah Springfield)
    private void dibujarEstatua(Renderer r, float x, float z) {
        float[] piedra = { 0.70f, 0.68f, 0.63f };
        float[] piedraOsc = { 0.55f, 0.53f, 0.50f };
        float[] bronce = { 0.45f, 0.55f, 0.42f };
        float[] bronceOsc = { 0.32f, 0.42f, 0.32f };
        float[] cara = { 0.52f, 0.62f, 0.48f };

        pieza(r, x, 0.3f, z, 2.2f, 0.4f, 2.2f, piedraOsc, 0);
        pieza(r, x, 0.9f, z, 1.6f, 0.8f, 1.6f, piedra, 0);
        pieza(r, x, 1.4f, z, 1.9f, 0.2f, 1.9f, piedraOsc, 0);
        // Placa frontal
        pieza(r, x, 0.9f, z + 0.82f, 0.9f, 0.4f, 0.06f, bronceOsc, 0);

        // Piernas
        pieza(r, x - 0.2f, 1.9f, z, 0.3f, 0.8f, 0.3f, bronceOsc, 0);
        pieza(r, x + 0.2f, 1.9f, z, 0.3f, 0.8f, 0.3f, bronceOsc, 0);
        // Abrigo largo
        pieza(r, x, 2.6f, z, 0.85f, 0.9f, 0.5f, bronce, 0);
        // Brazos
        pieza(r, x - 0.55f, 2.6f, z, 0.22f, 0.8f, 0.25f, bronce, 0);
        pieza(r, x + 0.55f, 2.9f, z + 0.15f, 0.22f, 0.25f, 0.5f, bronce, 0);
        // Cabeza y sombrero de ala ancha
        pieza(r, x, 3.3f, z, 0.45f, 0.45f, 0.45f, cara, 0);
        pieza(r, x, 3.55f, z, 0.9f, 0.08f, 0.9f, bronceOsc, 0);
        pieza(r, x, 3.75f, z, 0.5f, 0.35f, 0.5f, bronceOsc, 0);
        // Rifle en la mano
        pieza(r, x - 0.6f, 2.5f, z + 0.1f, 0.1f, 1.8f, 0.1f, bronceOsc, 0);
    }

    // Banca de madera con respaldo; "giro" decide hacia dónde mira
    private void banca(Renderer r, float cx, float cz, float giro) {
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
            float lx = cx + c * p[0] + s * p[2];
            float lz = cz - s * p[0] + c * p[2];
            pieza(r, lx, p[1] + 0.1f, lz, p[3], p[4], p[5], p[6] == 0 ? madera : hierro, giro);
        }
    }

    private void jardinera(Renderer r, float x, float z) {
        float[] muro = { 0.62f, 0.60f, 0.56f };
        float[] tierra = { 0.35f, 0.22f, 0.12f };
        float[] hoja = { 0.15f, 0.50f, 0.18f };
        float[][] flores = { { 0.95f, 0.25f, 0.30f }, { 1.0f, 0.85f, 0.2f }, { 0.95f, 0.5f, 0.75f }, { 1f, 1f, 1f } };
        pieza(r, x, 0.25f, z, 2.2f, 0.3f, 2.2f, muro, 0);
        pieza(r, x, 0.32f, z, 1.9f, 0.3f, 1.9f, tierra, 0);
        pieza(r, x, 0.5f, z, 1.8f, 0.12f, 1.8f, hoja, 0);
        int k = 0;
        for (float dx = -0.65f; dx <= 0.66f; dx += 0.65f) {
            for (float dz = -0.65f; dz <= 0.66f; dz += 0.65f) {
                float[] f = flores[k++ % flores.length];
                pieza(r, x + dx, 0.68f, z + dz, 0.22f, 0.22f, 0.22f, f, 0.4f * k);
            }
        }
    }

    private float[] aMundo(float lx, float lz) {
        float c = (float) Math.cos(angulo), s = (float) Math.sin(angulo);
        lx *= escala;
        lz *= escala;
        return new float[] { posX + c * lx + s * lz, posZ - s * lx + c * lz };
    }

    private void pieza(Renderer r, float x, float y, float z, float sx, float sy, float sz, float[] col, float giro) {
        float[] m = aMundo(x, z);
        r.cajaInclinada(m[0], y * escala, m[1], sx * escala, sy * escala, sz * escala, col[0], col[1], col[2], angulo + giro, 0, 0);
    }
}
