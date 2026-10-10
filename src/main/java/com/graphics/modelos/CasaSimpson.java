package com.graphics.modelos;
import com.graphics.Renderer;
public class CasaSimpson {
    // Posición de la casa en la ciudad y su orientación
    public float posX;
    public float posZ;
    public float angulo; // En radianes (0 por defecto, usa Math.PI/2 para girar 90 grados)
    public float escala = 0.65f;


    public CasaSimpson(float posX, float posZ, float angulo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
    }

    public void dibujar(Renderer renderer) {
        // --- PALETA DE COLORES MEJORADA ---
        float[] colorPared = { 0.94f, 0.76f, 0.62f }; // Melocotón claro
        float[] colorTejado = { 0.75f, 0.30f, 0.15f }; // Naranja teja
        float[] colorMadera = { 0.45f, 0.25f, 0.15f }; // Marrón oscuro
        float[] colorGaraje = { 0.85f, 0.70f, 0.50f }; // Beige
        float[] colorChimenea = { 0.60f, 0.60f, 0.60f }; // Gris
        float[] colorVidrio = { 0.40f, 0.70f, 0.90f }; // Celeste brillante
        float[] colorBlanco = { 0.95f, 0.95f, 0.95f }; // Blanco

        // =========================================================================
        // 1. ESTRUCTURAS BASE (Paredes)
        // =========================================================================
        // Casa Principal Izquierda (Bajamos alto a 4.8)
        dibujarPieza(renderer, -0.25f, 2.4f, 0.0f, 8.0f, 4.8f, 6.0f, colorPared);
        
        // Dormitorio Superior Derecho (Bajamos alto a 4.4)
        dibujarPieza(renderer, 5.5f, 2.2f, 0.0f, 3.5f, 4.4f, 6.0f, colorPared);

        // Garaje Inferior Derecho (Altura 2.4)
        dibujarPieza(renderer, 5.5f, 1.2f, 2.3f, 3.5f, 2.4f, 3.0f, colorPared);

        // =========================================================================
        // 2. TECHOS Y FRONTÓNS (Gables)
        // =========================================================================
        // Techo Casa Principal (Elevamos a 5.6)
        dibujarPiezaInclinada(renderer, -0.25f, 5.6f, 1.5f, 8.5f, 0.2f, 3.6f, colorTejado, 0.45f, 0.0f); // Frente
        dibujarPiezaInclinada(renderer, -0.25f, 5.6f, -1.5f, 8.5f, 0.2f, 3.6f, colorTejado, -0.45f, 0.0f); // Atrás
        // Relleno lateral suave Casa Principal
        dibujarGable(renderer, -4.25f, 4.8f, 0.0f, 6.0f, 1.55f, 0.2f, colorPared, true); // Izquierdo
        dibujarGable(renderer, 3.75f, 4.8f, 0.0f, 6.0f, 1.55f, 0.2f, colorPared, true);  // Derecho

        // Techo Dormitorio (Elevamos a 5.2)
        dibujarPiezaInclinada(renderer, 5.5f, 5.2f, 1.5f, 4.0f, 0.2f, 3.6f, colorTejado, 0.45f, 0.0f); // Frente
        dibujarPiezaInclinada(renderer, 5.5f, 5.2f, -1.5f, 4.0f, 0.2f, 3.6f, colorTejado, -0.45f, 0.0f); // Atrás
        // Relleno lateral suave Dormitorio
        dibujarGable(renderer, 7.25f, 4.4f, 0.0f, 6.0f, 1.55f, 0.2f, colorPared, true); // Derecho

        // Techo Garaje (Elevamos a 3.1)
        dibujarPiezaInclinada(renderer, 4.6f, 3.1f, 2.3f, 2.0f, 0.2f, 3.4f, colorTejado, 0.0f, 0.5f); // Izquierda
        dibujarPiezaInclinada(renderer, 6.4f, 3.1f, 2.3f, 2.0f, 0.2f, 3.4f, colorTejado, 0.0f, -0.5f); // Derecha
        // Relleno frontal suave Garaje
        dibujarGable(renderer, 5.5f, 2.4f, 3.8f, 3.5f, 1.1f, 0.2f, colorPared, false);

        // =========================================================================
        // 3. PUERTAS Y PORCHE
        // =========================================================================
        // Puerta del garaje (En Z=3.82 para estar en el frente del garaje)
        float gZ = 3.82f;
        dibujarPieza(renderer, 5.5f, 1.0f, gZ, 2.8f, 2.0f, 0.05f, colorGaraje);
        for (int i = 0; i < 7; i++) {
            dibujarPieza(renderer, 5.5f, 0.15f + (i * 0.28f), gZ + 0.02f, 2.8f, 0.04f, 0.02f, colorMadera);
        }

        // Puerta principal (Z=3.02)
        dibujarPieza(renderer, -0.25f, 1.0f, 3.02f, 1.2f, 2.0f, 0.05f, colorMadera);
        dibujarPieza(renderer, 0.15f, 1.0f, 3.05f, 0.1f, 0.1f, 0.05f, new float[] { 0.9f, 0.8f, 0.2f }); // Pomo
        // Porche
        dibujarPieza(renderer, -0.25f, 2.2f, 3.6f, 2.2f, 0.2f, 1.4f, colorTejado);
        dibujarPieza(renderer, -1.15f, 1.1f, 4.1f, 0.15f, 2.2f, 0.15f, colorBlanco);
        dibujarPieza(renderer, 0.65f, 1.1f, 4.1f, 0.15f, 2.2f, 0.15f, colorBlanco);

        // =========================================================================
        // 4. VENTANAS
        // =========================================================================
        // Ventanas salientes planta baja (Z=3.0)
        dibujarVentanaSaliente(renderer, -2.75f, 1.2f, 3.0f, colorMadera, colorVidrio); // Salón
        dibujarVentanaSaliente(renderer, 2.25f, 1.2f, 3.0f, colorMadera, colorVidrio);  // Comedor

        // Ventanas superiores casa principal (Z=3.02)
        dibujarVentanaPlana(renderer, -2.25f, 3.6f, 3.02f, colorMadera, colorVidrio); // Cuarto Maggie
        dibujarVentanaPlana(renderer, 1.75f, 3.6f, 3.02f, colorMadera, colorVidrio);  // Cuarto Homero/Marge
        
        // Ventana superior del dormitorio derecho (Z=3.02, por encima del garaje)
        // Subida a Y=4.0 para evitar bloqueo por el techo
        dibujarVentanaPlanaChica(renderer, 5.5f, 4.0f, 3.03f, colorMadera, colorVidrio); 

        // =========================================================================
        // 5. CHIMENEA
        // =========================================================================
        dibujarPieza(renderer, -2.75f, 4.5f, -1.0f, 0.8f, 3.0f, 1.0f, colorChimenea);
        dibujarPieza(renderer, -2.75f, 6.1f, -1.0f, 1.0f, 0.2f, 1.2f, colorChimenea);
    }

    private void dibujarGable(Renderer renderer, float x, float yBase, float zCenter, float anchoTotal, float altoTotal, float grosor, float[] color, boolean esEjeZ) {
        int steps = 15; // 15 pasos para que se vea súper suave
        float h = altoTotal / steps;
        for (int i = 0; i < steps; i++) {
            float currentY = yBase + (i * h) + (h / 2);
            float stepWidth = anchoTotal * (1.0f - ((float) i / steps));
            if (esEjeZ) {
                // Frontón lateral (se despliega en Z)
                dibujarPieza(renderer, x, currentY, zCenter, grosor, h, stepWidth, color);
            } else {
                // Frontón frontal (se despliega en X)
                dibujarPieza(renderer, x, currentY, zCenter, stepWidth, h, grosor, color);
            }
        }
    }

    private void dibujarVentanaPlanaChica(Renderer renderer, float x, float y, float z, float[] colorMarco,
            float[] colorVidrio) {
        float s = 0.75f; // Escala más pequeña
        dibujarPieza(renderer, x, y, z, 1.2f * s, 1.2f * s, 0.05f, colorVidrio);
        dibujarPieza(renderer, x, y + 0.6f * s, z + 0.05f, 1.3f * s, 0.1f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y - 0.6f * s, z + 0.05f, 1.3f * s, 0.1f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x + 0.6f * s, y, z + 0.05f, 0.1f * s, 1.3f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x - 0.6f * s, y, z + 0.05f, 0.1f * s, 1.3f * s, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 1.2f * s, 0.1f * s, 0.05f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 0.1f * s, 1.2f * s, 0.05f, colorMarco);
    }

    private void dibujarVentanaPlana(Renderer renderer, float x, float y, float z, float[] colorMarco,
            float[] colorVidrio) {
        // Fondo (Vidrio)
        dibujarPieza(renderer, x, y, z, 1.2f, 1.2f, 0.05f, colorVidrio);
        // Marco perimetral superior/inferior
        dibujarPieza(renderer, x, y + 0.6f, z + 0.05f, 1.3f, 0.1f, 0.1f, colorMarco);
        dibujarPieza(renderer, x, y - 0.6f, z + 0.05f, 1.3f, 0.1f, 0.1f, colorMarco);
        // Marco lateral
        dibujarPieza(renderer, x + 0.6f, y, z + 0.05f, 0.1f, 1.3f, 0.1f, colorMarco);
        dibujarPieza(renderer, x - 0.6f, y, z + 0.05f, 0.1f, 1.3f, 0.1f, colorMarco);
        // Cruces interiores
        dibujarPieza(renderer, x, y, z + 0.05f, 1.2f, 0.1f, 0.05f, colorMarco);
        dibujarPieza(renderer, x, y, z + 0.05f, 0.1f, 1.2f, 0.05f, colorMarco);
    }

    private void dibujarVentanaSaliente(Renderer renderer, float x, float y, float z, float[] colorMarco,
            float[] colorVidrio) {
        // Tejadillo marrón que sobresale
        dibujarPieza(renderer, x, y + 0.7f, z + 0.3f, 1.8f, 0.2f, 0.6f, colorMarco);
        // Repisa inferior
        dibujarPieza(renderer, x, y - 0.7f, z + 0.3f, 1.8f, 0.2f, 0.6f, colorMarco);

        // Bloque de vidrio grueso que simula los 3 paneles
        dibujarPieza(renderer, x, y, z + 0.25f, 1.6f, 1.2f, 0.5f, colorVidrio);

        // Marcos separadores para dar la ilusión de paneles curvos
        dibujarPieza(renderer, x - 0.4f, y, z + 0.55f, 0.1f, 1.2f, 0.1f, colorMarco);
        dibujarPieza(renderer, x + 0.4f, y, z + 0.55f, 0.1f, 1.2f, 0.1f, colorMarco);

        // Cruz horizontal central
        dibujarPieza(renderer, x, y, z + 0.55f, 1.6f, 0.1f, 0.1f, colorMarco);
    }

    /**
     * Dibuja y traslada los componentes de la casa aplicando rotación 2D en el
     * mapa.
     */
    private void dibujarPieza(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb) {
        dibujarPiezaInclinada(renderer, xLocal, yLocal, zLocal, sx, sy, sz, rgb, 0.0f, 0.0f);
    }

    /**
     * Dibuja una pieza que además de seguir la rotación de la casa, puede
     * inclinarse
     * localmente hacia adelante/atrás (anguloX) o hacia los lados (anguloZ).
     */
    private void dibujarPiezaInclinada(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb, float anguloX, float anguloZ) {
        xLocal *= escala; yLocal *= escala; zLocal *= escala;
        sx *= escala; sy *= escala; sz *= escala;
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        // Transformación de la posición al espacio del mundo
        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);

        // Llamamos al nuevo método del renderer con todos los ángulos
        renderer.cajaInclinada(mundoX, yLocal, mundoZ, sx, sy, sz, rgb[0], rgb[1], rgb[2], angulo, anguloX, anguloZ);
    }
}
