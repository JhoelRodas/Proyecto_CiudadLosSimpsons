package com.graphics.modelos;
import com.graphics.Renderer;
import static org.lwjgl.opengl.GL11.*;

public class CasaFlanders {

    public float posX;
    public float posZ;
    public float angulo;

    public CasaFlanders(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
    }

    public void dibujar(Renderer renderer) {
        // --- PALETA DE COLORES DE LA CASA DE LOS FLANDERS ---
        float[] colorPared = { 0.86f, 0.58f, 0.52f }; // Rosa/Salmón claro
        float[] colorTejado = { 0.55f, 0.42f, 0.65f }; // Morado
        float[] colorPuerta = { 0.50f, 0.35f, 0.60f }; // Morado oscuro
        float[] colorGaraje = { 0.80f, 0.40f, 0.35f }; // Salmón fuerte / Rojo apagado
        float[] colorChimenea = { 0.80f, 0.45f, 0.35f }; // Ladrillo rojo/naranja
        float[] colorVidrio = { 0.40f, 0.80f, 0.85f }; // Celeste
        float[] colorMarco = { 0.40f, 0.25f, 0.50f }; // Morado oscuro para marcos
        float[] colorEscalones = { 0.60f, 0.65f, 0.65f }; // Gris

        // =========================================================================
        // 1. ESTRUCTURAS BASE (Paredes)
        // =========================================================================
        // Casa Principal (Cuerpo largo de izquierda a derecha)
        // Centro X=1.0, Ancho=11.0 (Izquierda: -4.5, Derecha: 6.5)
        dibujarPieza(renderer, 1.0f, 2.4f, 0.0f, 11.0f, 4.8f, 6.0f, colorPared);

        // Garaje Inferior Derecho
        // Centro X=4.75, Ancho=3.5 (Izquierda: 3.0, Derecha: 6.5)
        // Centro Z=2.3, Profundidad=3.0 -> Va desde Z=0.8 hasta Z=3.8
        // Base más alta: Altura 3.0 (Centro Y = 1.5)
        dibujarPieza(renderer, 4.75f, 1.5f, 2.3f, 3.5f, 3.0f, 3.0f, colorPared);

        // =========================================================================
        // 2. TECHOS Y FRONTÓNS (Gables)
        // =========================================================================
        // Techo Casa Principal (Dos aguas adelante/atrás, rotX)
        dibujarPiezaInclinada(renderer, 1.0f, 5.6f, 1.5f, 11.5f, 0.2f, 3.6f, colorTejado, 0.45f, 0.0f); // Frente
        dibujarPiezaInclinada(renderer, 1.0f, 5.6f, -1.5f, 11.5f, 0.2f, 3.6f, colorTejado, -0.45f, 0.0f); // Atrás
        // Relleno lateral suave Casa Principal
        dibujarGable(renderer, -4.5f, 4.8f, 0.0f, 6.0f, 1.55f, 0.2f, colorPared, true); // Izquierdo
        dibujarGable(renderer, 6.5f, 4.8f, 0.0f, 6.0f, 1.55f, 0.2f, colorPared, true); // Derecho

        // Techo Garaje (Dos aguas izquierda/derecha, rotZ)
        // Empieza desde Y=3.0, Angulo 0.6
        dibujarPiezaInclinada(renderer, 3.76f, 3.67f, 2.3f, 2.4f, 0.2f, 3.4f, colorTejado, 0.0f, 0.6f); // Izquierda
        dibujarPiezaInclinada(renderer, 5.74f, 3.67f, 2.3f, 2.4f, 0.2f, 3.4f, colorTejado, 0.0f, -0.6f); // Derecha
        // Relleno frontal Garaje
        dibujarGable(renderer, 4.75f, 3.0f, 3.8f, 3.5f, 1.2f, 0.2f, colorPared, false);

        // =========================================================================
        // 3. PUERTAS Y PORCHE
        // =========================================================================
        // Puerta del garaje (En Z=3.82 para estar en el frente del garaje)
        float gZ = 3.82f;
        // Hacemos la puerta más alta (Centro Y=1.2, Alto=2.4) para un peldaño más
        dibujarPieza(renderer, 4.75f, 1.2f, gZ, 2.8f, 2.4f, 0.05f, colorGaraje);
        for (int i = 0; i < 6; i++) { // Ahora son 6 líneas en lugar de 5
            dibujarPieza(renderer, 4.75f, 0.3f + (i * 0.4f), gZ + 0.02f, 2.8f, 0.04f, 0.02f, colorMarco);
        }

        // Puerta principal (X = -0.5)
        dibujarPieza(renderer, -0.5f, 1.0f, 3.02f, 1.2f, 2.0f, 0.05f, colorPuerta);
        dibujarPieza(renderer, -0.1f, 1.0f, 3.05f, 0.1f, 0.1f, 0.05f, new float[] { 0.9f, 0.8f, 0.2f }); // Pomo

        // Tejadito de la puerta principal (Toldo)
        dibujarPiezaInclinada(renderer, -0.5f, 2.2f, 3.3f, 1.8f, 0.1f, 0.8f, colorTejado, 0.4f, 0.0f);

        // Escalones semicirculares (simulados con dos bloques escalonados anchos)
        dibujarPieza(renderer, -0.5f, 0.1f, 3.4f, 2.6f, 0.2f, 1.0f, colorEscalones);
        dibujarPieza(renderer, -0.5f, 0.3f, 3.2f, 1.8f, 0.2f, 0.6f, colorEscalones);

        // =========================================================================
        // 4. VENTANAS
        // =========================================================================
        // Ventanas salientes planta baja (Z=3.0)
        dibujarVentanaSaliente(renderer, -3.0f, 1.2f, 3.0f, colorMarco, colorVidrio); // Izquierda
        dibujarVentanaSaliente(renderer, 1.7f, 1.2f, 3.0f, colorMarco, colorVidrio); // Derecha

        // Ventanas superiores casa principal (Z=3.02) - Alineadas con las inferiores
        dibujarVentanaPlana(renderer, -3.0f, 3.6f, 3.02f, colorMarco, colorVidrio); // Izquierda (Alineada con ventanal
                                                                                    // izquierdo)
        dibujarVentanaPlana(renderer, 1.7f, 3.6f, 3.02f, colorMarco, colorVidrio); // Derecha (Alineada con ventanal
                                                                                   // derecho)

        // Ventana superior chiquita DENTRO del frontón del garaje (Bajada un poco)
        dibujarVentanaPlanaChica(renderer, 4.75f, 3.25f, 3.83f, colorMarco, colorVidrio);

        // =========================================================================
        // 5. CHIMENEA
        // =========================================================================
        // Chimenea en el lateral derecho de la casa (X=6.7)
        dibujarPieza(renderer, 6.7f, 1.5f, 0.0f, 0.8f, 3.0f, 1.6f, colorChimenea); // Base ancha
        dibujarPieza(renderer, 6.7f, 4.5f, 0.0f, 0.6f, 3.0f, 1.2f, colorChimenea); // Tubo
        dibujarPieza(renderer, 6.7f, 6.1f, 0.0f, 0.8f, 0.2f, 1.4f, colorChimenea); // Corona
    }

    // =========================================================================
    // MÉTODOS AUXILIARES (APLICAN ROTACIÓN Y POSICIÓN GLOBAL)
    // =========================================================================

    private void dibujarPieza(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb) {
        dibujarPiezaInclinada(renderer, xLocal, yLocal, zLocal, sx, sy, sz, rgb, 0.0f, 0.0f);
    }

    private void dibujarPiezaInclinada(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb, float anguloX, float anguloZ) {
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        // Transformación de la posición al espacio del mundo
        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);

        // Llamamos al método del renderer con todos los ángulos
        renderer.cajaInclinada(mundoX, yLocal, mundoZ, sx, sy, sz, rgb[0], rgb[1], rgb[2], angulo, anguloX, anguloZ);
    }

    private void dibujarGable(Renderer renderer, float x, float yBase, float zCenter, float anchoTotal, float altoTotal,
            float grosor, float[] color, boolean esEjeZ) {
        int steps = 15;
        float h = altoTotal / steps;
        for (int i = 0; i < steps; i++) {
            float currentY = yBase + (i * h) + (h / 2);
            float stepWidth = anchoTotal * (1.0f - ((float) i / steps));
            if (esEjeZ) {
                dibujarPieza(renderer, x, currentY, zCenter, grosor, h, stepWidth, color);
            } else {
                dibujarPieza(renderer, x, currentY, zCenter, stepWidth, h, grosor, color);
            }
        }
    }

    private void dibujarVentanaPlana(Renderer renderer, float x, float y, float z, float[] colorMarco,
            float[] colorVidrio) {
        dibujarPieza(renderer, x, y, z, 1.2f, 1.2f, 0.05f, colorVidrio);
        dibujarPieza(renderer, x, y + 0.6f, z + 0.05f, 1.3f, 0.1f, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y - 0.6f, z + 0.05f, 1.3f, 0.1f, 0.1f, colorMarco);
        dibujarPieza(renderer, x + 0.6f, y, z + 0.05f, 0.1f, 1.3f, 0.1f, colorMarco);
        dibujarPieza(renderer, x - 0.6f, y, z + 0.05f, 0.1f, 1.3f, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 1.2f, 0.1f, 0.05f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 0.1f, 1.2f, 0.05f, colorMarco);
    }

    private void dibujarVentanaPlanaChica(Renderer renderer, float x, float y, float z, float[] colorMarco,
            float[] colorVidrio) {
        float s = 0.5f; // Ventana más pequeña para el frontón
        dibujarPieza(renderer, x, y, z, 1.2f * s, 1.2f * s, 0.05f, colorVidrio);
        dibujarPieza(renderer, x, y + 0.6f * s, z + 0.05f, 1.3f * s, 0.1f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y - 0.6f * s, z + 0.05f, 1.3f * s, 0.1f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x + 0.6f * s, y, z + 0.05f, 0.1f * s, 1.3f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x - 0.6f * s, y, z + 0.05f, 0.1f * s, 1.3f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 1.2f * s, 0.1f * s, 0.05f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 0.1f * s, 1.2f * s, 0.05f, colorMarco);
    }

    private void dibujarVentanaSaliente(Renderer renderer, float x, float y, float z, float[] colorMarco,
            float[] colorVidrio) {
        dibujarPieza(renderer, x, y + 0.7f, z + 0.3f, 1.8f, 0.2f, 0.6f, colorMarco); // Tejadillo
        dibujarPieza(renderer, x, y - 0.7f, z + 0.3f, 1.8f, 0.2f, 0.6f, colorMarco); // Repisa
        dibujarPieza(renderer, x, y, z + 0.25f, 1.6f, 1.2f, 0.5f, colorVidrio); // Vidrio
        dibujarPieza(renderer, x - 0.4f, y, z + 0.55f, 0.1f, 1.2f, 0.1f, colorMarco); // Marco izq
        dibujarPieza(renderer, x + 0.4f, y, z + 0.55f, 0.1f, 1.2f, 0.1f, colorMarco); // Marco der
        dibujarPieza(renderer, x, y, z + 0.55f, 1.6f, 0.1f, 0.1f, colorMarco); // Cruz horizontal
    }
}
