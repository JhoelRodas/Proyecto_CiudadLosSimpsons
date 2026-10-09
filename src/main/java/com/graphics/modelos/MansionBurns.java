package com.graphics.modelos;
import com.graphics.Renderer;
/**
 * Mansión del Sr. Burns: casona gótica de piedra oscura con torre central, alas laterales,
 * reja de hierro con puerta, jardín y cipreses. El frente mira hacia +Z.
 * Mide unos 27 x 21 unidades con escala 1.0 (usa "escala" para achicarla).
 */
public class MansionBurns {
    public float posX;
    public float posZ;
    public float angulo;
    public float escala = 1.0f;

    private final Arbol[] cipreses = new Arbol[4];

    public MansionBurns(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
        for (int i = 0; i < cipreses.length; i++) {
            cipreses[i] = new Arbol(0, 0, 1.0f, 6);
        }
    }

    public MansionBurns(float posX, float posZ, float angulo, float escala) {
        this(posX, posZ, angulo);
        this.escala = escala;
    }

    public void dibujar(Renderer r) {
        float[] piedra = { 0.52f, 0.50f, 0.48f };
        float[] piedraClara = { 0.66f, 0.64f, 0.60f };
        float[] piedraOsc = { 0.38f, 0.37f, 0.37f };
        float[] pizarra = { 0.20f, 0.21f, 0.27f };
        float[] madera = { 0.30f, 0.18f, 0.10f };
        float[] vidrio = { 0.95f, 0.85f, 0.45f }; // ventanas encendidas
        float[] pasto = { 0.25f, 0.45f, 0.22f };
        float[] camino = { 0.72f, 0.68f, 0.60f };
        float[] seto = { 0.12f, 0.33f, 0.14f };
        float[] hierro = { 0.08f, 0.08f, 0.10f };

        // ---------------- Terreno ----------------
        pieza(r, 0, 0.05f, 1.5f, 27.6f, 0.1f, 21.6f, pasto);
        pieza(r, 0, 0.12f, 9.3f, 3.2f, 0.06f, 3.8f, camino);   // camino de la reja a la escalera
        pieza(r, 0, 0.12f, 7.5f, 5.0f, 0.06f, 0.8f, camino);

        // ---------------- Cuerpo principal ----------------
        float hMain = 6.5f, dMain = 7f, wMain = 10f;
        pieza(r, 0, hMain / 2, 0, wMain, hMain, dMain, piedra);
        zocalo(r, 0, wMain, dMain, piedraOsc);
        franja(r, 0, 3.4f, wMain, dMain, piedraClara);
        franja(r, 0, hMain, wMain, dMain, piedraClara);
        techoDosAguas(r, 0, wMain, dMain, hMain, pizarra, piedra);

        // ---------------- Alas laterales ----------------
        float hAla = 5f, dAla = 6f, wAla = 6f;
        for (int lado = -1; lado <= 1; lado += 2) {
            float cx = lado * 8f;
            pieza(r, cx, hAla / 2, 0, wAla, hAla, dAla, piedra);
            zocalo(r, cx, wAla, dAla, piedraOsc);
            franja(r, cx, 2.8f, wAla, dAla, piedraClara);
            franja(r, cx, hAla, wAla, dAla, piedraClara);
            techoDosAguas(r, cx, wAla, dAla, hAla, pizarra, piedra);
            // Ventanas en el frente (2 pisos, 2 columnas)
            for (int col = -1; col <= 1; col += 2) {
                float vx = cx + col * 1.5f;
                ventanaAlta(r, vx, 1.7f, dAla / 2, vidrio, piedraClara);
                ventanaAlta(r, vx, 3.9f, dAla / 2, vidrio, piedraClara);
            }
            // Ventanas laterales externas
            ventanaLateral(r, cx + lado * wAla / 2, 1.8f, 0, lado, vidrio, piedraClara);
            ventanaLateral(r, cx + lado * wAla / 2, 3.8f, 0, lado, vidrio, piedraClara);
            // Chimenea
            chimenea(r, cx + lado * 1.8f, hAla + 1.6f, -1.0f, piedraOsc, piedraClara);
            // Seto frente al ala
            pieza(r, cx, 0.5f, dAla / 2 + 0.7f, wAla - 0.4f, 0.8f, 0.8f, seto);
        }

        // Ventanas del cuerpo principal (a los lados de la torre)
        for (int lado = -1; lado <= 1; lado += 2) {
            ventanaAlta(r, lado * 3.7f, 1.9f, dMain / 2, vidrio, piedraClara);
            ventanaAlta(r, lado * 3.7f, 4.9f, dMain / 2, vidrio, piedraClara);
            chimenea(r, lado * 3.3f, hMain + 2.0f, -1.2f, piedraOsc, piedraClara);
        }

        // ---------------- Torre de entrada ----------------
        float wT = 4.6f, dT = 2.6f, hT = 9.5f;
        float zT = dMain / 2 + dT / 2 - 0.3f;
        float zFrente = zT + dT / 2;
        pieza(r, 0, hT / 2, zT, wT, hT, dT, piedra);
        zocalo(r, 0, wT, dT, piedraOsc, zT);
        franja(r, 0, 4.3f, wT, dT, piedraClara, zT);
        franja(r, 0, hT, wT, dT, piedraClara, zT);
        // Aguja escalonada (flecha gótica)
        int pasos = 10;
        for (int i = 0; i < pasos; i++) {
            float f = 1.0f - (float) i / pasos;
            pieza(r, 0, hT + 0.25f + i * 0.5f, zT, (wT + 0.6f) * f, 0.5f, (dT + 0.6f) * f, pizarra);
        }
        pieza(r, 0, hT + 0.25f + pasos * 0.5f + 0.4f, zT, 0.12f, 1.0f, 0.12f, hierro);
        pieza(r, 0, hT + 0.25f + pasos * 0.5f + 0.7f, zT, 0.5f, 0.1f, 0.1f, hierro);

        // Puerta doble, marco y escalones
        pieza(r, 0, 1.7f, zFrente + 0.03f, 2.3f, 3.4f, 0.1f, madera);
        pieza(r, 0, 1.7f, zFrente + 0.1f, 0.08f, 3.4f, 0.06f, hierro);
        pieza(r, -0.35f, 1.6f, zFrente + 0.13f, 0.12f, 0.12f, 0.06f, new float[] { 0.9f, 0.75f, 0.2f });
        pieza(r, 0.35f, 1.6f, zFrente + 0.13f, 0.12f, 0.12f, 0.06f, new float[] { 0.9f, 0.75f, 0.2f });
        pieza(r, 0, 3.55f, zFrente + 0.03f, 2.6f, 0.2f, 0.18f, piedraClara);
        pieza(r, -1.3f, 1.8f, zFrente + 0.03f, 0.2f, 3.6f, 0.18f, piedraClara);
        pieza(r, 1.3f, 1.8f, zFrente + 0.03f, 0.2f, 3.6f, 0.18f, piedraClara);
        for (int i = 0; i < 3; i++) {
            pieza(r, 0, 0.12f + i * 0.14f, zFrente + 0.8f - i * 0.25f, 3.6f, 0.14f, 0.7f, piedraClara);
        }
        // Portal con columnas
        pieza(r, 0, 4.2f, zFrente + 0.9f, 4.4f, 0.3f, 2.0f, piedraClara);
        pieza(r, -1.9f, 2.1f, zFrente + 1.7f, 0.4f, 4.0f, 0.4f, piedraClara);
        pieza(r, 1.9f, 2.1f, zFrente + 1.7f, 0.4f, 4.0f, 0.4f, piedraClara);
        pieza(r, -1.9f, 0.2f, zFrente + 1.7f, 0.65f, 0.3f, 0.65f, piedraOsc);
        pieza(r, 1.9f, 0.2f, zFrente + 1.7f, 0.65f, 0.3f, 0.65f, piedraOsc);
        pieza(r, 0, 4.5f, zFrente + 1.7f, 4.4f, 0.2f, 0.2f, piedraClara);
        // Ventanas de la torre
        ventanaAlta(r, 0, 5.8f, zFrente, vidrio, piedraClara);
        ventanaAlta(r, 0, 7.6f, zFrente, vidrio, piedraClara);
        ventanaAlta(r, -1.4f, 6.7f, zFrente, vidrio, piedraClara);
        ventanaAlta(r, 1.4f, 6.7f, zFrente, vidrio, piedraClara);

        // ---------------- Reja de hierro con puerta ----------------
        float zReja = 11f, xReja = 13f, zFondo = -8f;
        float abertura = 2.6f;
        reja(r, -xReja, -abertura, zReja, zReja, hierro, piedraOsc);   // frente izquierda
        reja(r, abertura, xReja, zReja, zReja, hierro, piedraOsc);     // frente derecha
        reja(r, -xReja, xReja, zFondo, zFondo, hierro, piedraOsc);     // fondo
        reja(r, -xReja, -xReja, zFondo, zReja, hierro, piedraOsc);     // lateral izquierdo
        reja(r, xReja, xReja, zFondo, zReja, hierro, piedraOsc);       // lateral derecho

        // Pilares de la entrada con farol
        for (int lado = -1; lado <= 1; lado += 2) {
            float px = lado * (abertura + 0.4f);
            pieza(r, px, 1.8f, zReja, 0.9f, 3.6f, 0.9f, piedra);
            pieza(r, px, 3.7f, zReja, 1.1f, 0.2f, 1.1f, piedraClara);
            pieza(r, px, 4.05f, zReja, 0.5f, 0.5f, 0.5f, vidrio);
            pieza(r, px, 4.4f, zReja, 0.65f, 0.15f, 0.65f, piedraOsc);
        }
        // Hojas de la puerta
        for (float bx = -abertura + 0.2f; bx <= abertura - 0.19f; bx += 0.3f) {
            pieza(r, bx, 1.2f, zReja, 0.08f, 2.4f, 0.08f, hierro);
            pieza(r, bx, 2.5f, zReja, 0.16f, 0.25f, 0.16f, hierro);
        }
        pieza(r, 0, 0.5f, zReja, abertura * 2, 0.1f, 0.1f, hierro);
        pieza(r, 0, 2.0f, zReja, abertura * 2, 0.1f, 0.1f, hierro);
        // Arco superior de la puerta con la inicial "B" hecha de cajas
        pieza(r, 0, 3.0f, zReja, abertura * 2 + 0.4f, 0.12f, 0.12f, hierro);
        pieza(r, -0.2f, 3.35f, zReja, 0.1f, 0.7f, 0.1f, hierro);
        pieza(r, 0.0f, 3.65f, zReja, 0.45f, 0.1f, 0.1f, hierro);
        pieza(r, 0.0f, 3.35f, zReja, 0.45f, 0.1f, 0.1f, hierro);
        pieza(r, 0.0f, 3.05f, zReja, 0.45f, 0.1f, 0.1f, hierro);
        pieza(r, 0.2f, 3.5f, zReja, 0.1f, 0.3f, 0.1f, hierro);
        pieza(r, 0.2f, 3.2f, zReja, 0.1f, 0.3f, 0.1f, hierro);

        // Setos a los lados del camino
        pieza(r, -2.4f, 0.5f, 9.0f, 0.8f, 0.9f, 3.0f, seto);
        pieza(r, 2.4f, 0.5f, 9.0f, 0.8f, 0.9f, 3.0f, seto);

        // ---------------- Cipreses ----------------
        float[][] pc = { { -4.5f, 8.0f }, { 4.5f, 8.0f }, { -10.5f, 8.5f }, { 10.5f, 8.5f } };
        for (int i = 0; i < cipreses.length; i++) {
            float[] m = aMundo(pc[i][0], pc[i][1]);
            cipreses[i].posX = m[0];
            cipreses[i].posZ = m[1];
            cipreses[i].angulo = angulo;
            cipreses[i].escala = escala;
            cipreses[i].dibujar(r);
        }
    }

    // ---------------------------------------------------------------- Partes
    private void zocalo(Renderer r, float cx, float w, float d, float[] c) {
        zocalo(r, cx, w, d, c, 0f);
    }

    private void zocalo(Renderer r, float cx, float w, float d, float[] c, float cz) {
        pieza(r, cx, 0.4f, cz, w + 0.25f, 0.8f, d + 0.25f, c);
    }

    private void franja(Renderer r, float cx, float y, float w, float d, float[] c) {
        franja(r, cx, y, w, d, c, 0f);
    }

    private void franja(Renderer r, float cx, float y, float w, float d, float[] c, float cz) {
        pieza(r, cx, y, cz, w + 0.2f, 0.22f, d + 0.2f, c);
    }

    /** Techo a dos aguas con cumbrera en el eje X y frontones rellenos con el color de la pared. */
    private void techoDosAguas(Renderer r, float cx, float w, float d, float yBase, float[] techo,
            float[] pared) {
        float ang = 0.55f;
        float mitad = d / 2;
        float subida = mitad * (float) Math.tan(ang);
        float largo = mitad / (float) Math.cos(ang) + 0.35f;
        float yc = yBase + subida / 2 + 0.05f;
        inclinada(r, cx, yc, mitad / 2, w + 0.7f, 0.25f, largo, techo, ang);
        inclinada(r, cx, yc, -mitad / 2, w + 0.7f, 0.25f, largo, techo, -ang);
        pieza(r, cx, yBase + subida + 0.18f, 0, w + 0.7f, 0.18f, 0.3f, techo);
        int pasos = 14;
        float h = subida / pasos;
        for (int lado = -1; lado <= 1; lado += 2) {
            for (int i = 0; i < pasos; i++) {
                float y = yBase + i * h + h / 2;
                float ancho = d * (1.0f - (float) i / pasos);
                pieza(r, cx + lado * w / 2, y, 0, 0.25f, h, ancho, pared);
            }
        }
    }

    private void chimenea(Renderer r, float x, float y, float z, float[] c, float[] tapa) {
        pieza(r, x, y, z, 0.9f, 3.0f, 1.0f, c);
        pieza(r, x, y + 1.6f, z, 1.15f, 0.2f, 1.25f, tapa);
        pieza(r, x - 0.2f, y + 1.9f, z, 0.25f, 0.4f, 0.25f, c);
        pieza(r, x + 0.2f, y + 1.9f, z, 0.25f, 0.4f, 0.25f, c);
    }

    /** Ventana vertical con dintel, repisa y cruz de marco. */
    private void ventanaAlta(Renderer r, float x, float y, float zf, float[] vidrio, float[] marco) {
        pieza(r, x, y, zf + 0.02f, 0.95f, 1.8f, 0.06f, vidrio);
        pieza(r, x, y + 0.98f, zf + 0.08f, 1.25f, 0.2f, 0.16f, marco);
        pieza(r, x, y + 1.15f, zf + 0.08f, 0.6f, 0.2f, 0.16f, marco);
        pieza(r, x, y - 0.98f, zf + 0.12f, 1.3f, 0.16f, 0.24f, marco);
        pieza(r, x - 0.52f, y, zf + 0.06f, 0.1f, 1.9f, 0.1f, marco);
        pieza(r, x + 0.52f, y, zf + 0.06f, 0.1f, 1.9f, 0.1f, marco);
        pieza(r, x, y, zf + 0.06f, 0.08f, 1.8f, 0.08f, marco);
        pieza(r, x, y + 0.25f, zf + 0.06f, 0.95f, 0.08f, 0.08f, marco);
    }

    private void ventanaLateral(Renderer r, float xPared, float y, float z, int lado, float[] vidrio,
            float[] marco) {
        pieza(r, xPared + lado * 0.02f, y, z, 0.06f, 1.8f, 0.95f, vidrio);
        pieza(r, xPared + lado * 0.07f, y + 0.98f, z, 0.16f, 0.2f, 1.25f, marco);
        pieza(r, xPared + lado * 0.07f, y, z, 0.1f, 1.9f, 0.08f, marco);
        pieza(r, xPared + lado * 0.07f, y - 0.98f, z, 0.2f, 0.16f, 1.3f, marco);
    }

    /** Segmento de reja alineado a un eje: barrotes con punta, dos rieles y zócalo de piedra. */
    private void reja(Renderer r, float x1, float x2, float z1, float z2, float[] hierro, float[] base) {
        boolean sobreX = Math.abs(x2 - x1) >= Math.abs(z2 - z1);
        float largo = sobreX ? Math.abs(x2 - x1) : Math.abs(z2 - z1);
        float cx = (x1 + x2) / 2, cz = (z1 + z2) / 2;
        if (sobreX) {
            pieza(r, cx, 0.2f, cz, largo, 0.4f, 0.3f, base);
            pieza(r, cx, 0.9f, cz, largo, 0.1f, 0.1f, hierro);
            pieza(r, cx, 2.1f, cz, largo, 0.1f, 0.1f, hierro);
        } else {
            pieza(r, cx, 0.2f, cz, 0.3f, 0.4f, largo, base);
            pieza(r, cx, 0.9f, cz, 0.1f, 0.1f, largo, hierro);
            pieza(r, cx, 2.1f, cz, 0.1f, 0.1f, largo, hierro);
        }
        int n = Math.max(2, (int) (largo / 0.9f));
        for (int i = 0; i <= n; i++) {
            float t = (float) i / n;
            float px = sobreX ? x1 + (x2 - x1) * t : cx;
            float pz = sobreX ? cz : z1 + (z2 - z1) * t;
            pieza(r, px, 1.4f, pz, 0.12f, 2.2f, 0.12f, hierro);
            pieza(r, px, 2.6f, pz, 0.2f, 0.3f, 0.2f, hierro);
        }
    }

    // ---------------------------------------------------------------- Transformación a mundo
    private float[] aMundo(float lx, float lz) {
        float cos = (float) Math.cos(angulo);
        float sen = (float) Math.sin(angulo);
        lx *= escala;
        lz *= escala;
        return new float[] { posX + cos * lx + sen * lz, posZ - sen * lx + cos * lz };
    }

    private void pieza(Renderer r, float x, float y, float z, float sx, float sy, float sz, float[] c) {
        inclinada(r, x, y, z, sx, sy, sz, c, 0f);
    }

    private void inclinada(Renderer r, float x, float y, float z, float sx, float sy, float sz,
            float[] c, float angX) {
        float[] m = aMundo(x, z);
        r.cajaInclinada(m[0], y * escala, m[1], sx * escala, sy * escala, sz * escala,
                c[0], c[1], c[2], angulo, angX, 0f);
    }
}
