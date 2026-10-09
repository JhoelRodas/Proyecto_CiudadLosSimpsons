package com.graphics.modelos;
import com.graphics.Renderer;
public class Auto {
    public float posX = 0;
    public float posZ = 0;
    public float angulo = 0;
    public float velocidad = 0;

    public void dibujar(Renderer renderer) {
        // --- PALETA DE COLORES REFINADA (HIT & RUN) ---
        float[] colorRosa = { 0.91f, 0.44f, 0.54f };
        float[] colorCromo = { 0.85f, 0.85f, 0.85f };
        float[] colorVidrio = { 0.40f, 0.75f, 0.65f }; // Verde menta/celeste semitransparente del juego
        float[] colorLlanta = { 0.15f, 0.15f, 0.15f };
        float[] colorFaro = { 0.20f, 0.60f, 0.80f }; // Faros ovalados azul/celeste del juego
        float[] colorInterior = { 0.45f, 0.22f, 0.15f }; // Cuero marrón de los asientos
        float[] colorVolante = { 0.10f, 0.10f, 0.10f };
        float[] colorFreno = { 0.90f, 0.10f, 0.10f }; // Rojo brillante

        float radioRueda = 0.70f;
        float anchoRueda = 0.20f;
        float radioTapon = 0.36f;
        float anchoTapon = 0.04f;

        // =========================================================================
        // 1. CARROCERÍA PRINCIPAL (RECALIBRADA PARA TRASERA RECTILÍNEA)
        // =========================================================================
        // Chasis base inferior
        dibujarPieza(renderer, 0.0f, 0.35f, 0.0f, 2.0f, 0.3f, 4.8f, colorRosa);

        // Paneles laterales (Paredes izquierda y derecha de la cabina abierta)
        dibujarPieza(renderer, 0.95f, 0.75f, -0.2f, 0.1f, 0.5f, 2.4f, colorRosa);
        dibujarPieza(renderer, -0.95f, 0.75f, -0.2f, 0.1f, 0.5f, 2.4f, colorRosa);

        // Capó delantero (Morro del coche)
        dibujarPieza(renderer, 0.0f, 0.65f, 1.6f, 1.8f, 0.3f, 1.4f, colorRosa);

        // Maletero trasero central (Más alto y ancho para cerrar el hueco estilo sedán)
        dibujarPieza(renderer, 0.0f, 0.62f, -1.8f, 1.68f, 0.30f, 1.0f, colorRosa);

        // Intersección trasera central (Debajo de los faros)
        dibujarPieza(renderer, 0.0f, 0.55f, -2.28f, 1.68f, 0.12f, 0.04f, colorRosa);

        // =========================================================================
        // 2. DETALLES ICÓNICOS: ALETAS TRASERAS EXTREMAS
        // =========================================================================
        // Desplazadas al extremo absoluto (X = 0.92f) para que formen el contorno del coche
        dibujarPieza(renderer, 0.92f, 0.70f, -1.8f, 0.16f, 0.30f, 1.0f, colorRosa);
        dibujarPieza(renderer, -0.92f, 0.70f, -1.8f, 0.16f, 0.30f, 1.0f, colorRosa);

        // Rejilla del motor frontal (Radiador gris)
        dibujarPieza(renderer, 0.0f, 0.60f, 2.31f, 1.2f, 0.25f, 0.05f, colorCromo);

        // Parachoques Delantero estándar
        dibujarPieza(renderer, 0.0f, 0.35f, 2.42f, 2.1f, 0.2f, 0.15f, colorCromo);

        // ¡PARACHOQUES TRASERO ULTRA ANCHO Y ALTO! (Como el de la foto del juego)
        // Subimos su centro a Y = 0.45f y le damos un altoY de 0.35f para que cubra
        // toda la parte baja y tape el chasis
        dibujarPieza(renderer, 0.0f, 0.42f, -2.32f, 2.02f, 0.35f, 0.12f, colorCromo);

        // Placa de matrícula blanca empotrada en el centro del parachoques
        dibujarPieza(renderer, 0.0f, 0.40f, -2.40f, 0.45f, 0.18f, 0.02f, new float[] { 0.95f, 0.95f, 0.95f });

        // =========================================================================
        // 3. INTERIOR DE LA CABINA (EXpuesta)
        // =========================================================================
        // Suelo interior oscuro
        dibujarPieza(renderer, 0.0f, 0.55f, -0.2f, 1.8f, 0.1f, 2.2f, colorInterior);

        // Asiento Delantero (Base + Respaldo más delgado)
        dibujarPieza(renderer, 0.0f, 0.65f, 0.35f, 1.7f, 0.15f, 0.4f, colorInterior); // Base (donde te sientas)
        dibujarPieza(renderer, 0.0f, 0.80f, 0.10f, 1.7f, 0.45f, 0.15f, colorInterior); // Respaldo delgado

        // Asiento Trasero (Base + Respaldo)
        dibujarPieza(renderer, 0.0f, 0.65f, -0.6f, 1.7f, 0.15f, 0.4f, colorInterior); // Base
        dibujarPieza(renderer, 0.0f, 0.80f, -0.85f, 1.7f, 0.45f, 0.15f, colorInterior); // Respaldo delgado

        // =========================================================================
        // 3.5 HOMERO SIMPSON (CONDUCTOR)
        // =========================================================================
        float[] colorPiel = { 1.0f, 0.85f, 0.0f }; // Amarillo Simpson
        float[] colorCamisa = { 1.0f, 1.0f, 1.0f }; // Blanco

        float hX = 0.45f; // Posición X (Lado izquierdo, asiento del conductor)
        float hZ = 0.25f; // Posición Z (Ligeramente atrás del volante)
        
        // Torso (Camisa blanca)
        dibujarPieza(renderer, hX, 0.85f, hZ, 0.45f, 0.40f, 0.35f, colorCamisa);
        
        // Cuello
        dibujarPieza(renderer, hX, 1.08f, hZ, 0.12f, 0.10f, 0.12f, colorPiel);
        
        // Cabeza (Bloque amarillo)
        dibujarPieza(renderer, hX, 1.25f, hZ + 0.02f, 0.25f, 0.28f, 0.28f, colorPiel);
        
        // Manga derecha (Hacia el centro del coche)
        dibujarPieza(renderer, hX - 0.25f, 0.90f, hZ, 0.15f, 0.20f, 0.15f, colorCamisa);
        // Brazo derecho (Llegando al volante)
        dibujarPieza(renderer, hX - 0.25f, 0.90f, hZ + 0.18f, 0.10f, 0.10f, 0.25f, colorPiel);

        // Manga izquierda (Hacia la ventana exterior)
        dibujarPieza(renderer, hX + 0.25f, 0.90f, hZ, 0.15f, 0.20f, 0.15f, colorCamisa);
        // Brazo izquierdo (Llegando al volante)
        dibujarPieza(renderer, hX + 0.25f, 0.90f, hZ + 0.18f, 0.10f, 0.10f, 0.25f, colorPiel);

        // Volante (Caja pequeña flotante a la izquierda)
        dibujarPieza(renderer, 0.4f, 0.85f, 0.6f, 0.3f, 0.05f, 0.3f, colorVolante);

        // =========================================================================
        // 4. LUCES DE FRENO DETALLADAS (ALINEADAS AL EXTREMO EN VOLADIZO)
        // =========================================================================
        // Colocadas exactamente en X = 0.92f de forma que "abracen" las aletas y queden
        // pegadas al borde
        float posicionX_Faro = 0.92f;
        float profundidadZ_Faro = -2.33f; // Sobresale sutilmente un milímetro del parachoques

        // Faro Trasero Izquierdo
        dibujarPieza(renderer, posicionX_Faro, 0.70f, profundidadZ_Faro, 0.12f, 0.30f, 0.05f, colorFreno); // Superior
                                                                                                           // rojo
        dibujarPieza(renderer, posicionX_Faro, 0.50f, profundidadZ_Faro, 0.12f, 0.10f, 0.05f,
                new float[] { 0.98f, 0.65f, 0.15f }); // Inferior naranja

        // Faro Trasero Derecho
        dibujarPieza(renderer, -posicionX_Faro, 0.70f, profundidadZ_Faro, 0.12f, 0.30f, 0.05f, colorFreno); // Superior
                                                                                                            // rojo
        dibujarPieza(renderer, -posicionX_Faro, 0.50f, profundidadZ_Faro, 0.12f, 0.10f, 0.05f,
                new float[] { 0.98f, 0.65f, 0.15f }); // Inferior naranja

        // =========================================================================
        // 5. FAROS SIMPSON (OVALADOS Y CELESTES)
        // =========================================================================
        dibujarPieza(renderer, 0.65f, 0.65f, 2.31f, 0.25f, 0.25f, 0.05f, colorFaro);
        dibujarPieza(renderer, -0.65f, 0.65f, 2.31f, 0.25f, 0.25f, 0.05f, colorFaro);

        // Antena clásica del juego (Fina en el lado izquierdo del capó)
        dibujarPieza(renderer, 0.75f, 1.15f, 1.5f, 0.02f, 0.8f, 0.02f, colorVolante);

        // =========================================================================
        // 6. RUEDAS CON DETALLE DE LLANTA DE PLATO
        // =========================================================================
        // Rueda Izquierda (Neumático + Tapón Metálico sobresaliente)
        dibujarRueda(renderer, 1.0f, 0.35f, 1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta);
        dibujarRueda(renderer, 1.12f, 0.35f, 1.2f, anchoTapon, radioTapon, radioTapon, colorCromo);

        // Rueda Derecha
        dibujarRueda(renderer, -1.0f, 0.35f, 1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta);
        dibujarRueda(renderer, -1.12f, 0.35f, 1.2f, anchoTapon, radioTapon, radioTapon, colorCromo);

        // --- EJE TRASERO ---
        // Rueda Izquierda
        dibujarRueda(renderer, 1.0f, 0.35f, -1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta);
        dibujarRueda(renderer, 1.12f, 0.35f, -1.2f, anchoTapon, radioTapon, radioTapon, colorCromo);

        // Rueda Derecha
        dibujarRueda(renderer, -1.0f, 0.35f, -1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta);
        dibujarRueda(renderer, -1.12f, 0.35f, -1.2f, anchoTapon, radioTapon, radioTapon, colorCromo);

        // =========================================================================
        // 7. ESPEJOS RETROVISORES CELESTES LATERALES
        // =========================================================================
        // Palitos cromados que sostienen los espejos
        dibujarPieza(renderer, 1.02f, 0.85f, 0.80f, 0.15f, 0.04f, 0.04f, colorCromo);
        dibujarPieza(renderer, -1.02f, 0.85f, 0.80f, 0.15f, 0.04f, 0.04f, colorCromo);
        // Los espejos de color vidrio celeste
        dibujarPieza(renderer, 1.12f, 0.88f, 0.80f, 0.04f, 0.12f, 0.18f, colorVidrio);
        dibujarPieza(renderer, -1.12f, 0.88f, 0.80f, 0.04f, 0.12f, 0.18f, colorVidrio);
    }

    private void dibujarPieza(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb) {
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);

        renderer.cajaGirada(mundoX, yLocal, mundoZ, sx, sy, sz, rgb[0], rgb[1], rgb[2], angulo);
    }

    /**
     * Traduce las coordenadas locales de la rueda circular a la posición global
     * rotada
     * y ejecuta el dibujo del cilindro en el Renderer.
     */
    private void dibujarRueda(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb) {
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);

        // Invocamos el renderizado del cilindro girado
        renderer.cilindroGirado(mundoX, yLocal, mundoZ, sx, sy, sz, rgb, angulo);
    }

    public void actualizarFisicas(float deltaTime, boolean acelerar, boolean frenar, boolean izq, boolean der) {
        float aceleracion = 25.0f;
        float friccion = 10.0f;
        float velMax = 45.0f;
        float velGiro = 2.5f;

        // 1. Aceleración y Freno
        if (acelerar) {
            velocidad += aceleracion * deltaTime;
        } else if (frenar) {
            velocidad -= aceleracion * deltaTime;
        } else {
            // Si no presionas nada, el auto frena poco a poco por la fricción
            if (velocidad > 0)
                velocidad = Math.max(0, velocidad - friccion * deltaTime);
            if (velocidad < 0)
                velocidad = Math.min(0, velocidad + friccion * deltaTime);
        }

        // Limitar velocidad máxima (hacia adelante y reversa)
        if (velocidad > velMax)
            velocidad = velMax;
        if (velocidad < -velMax / 2)
            velocidad = -velMax / 2; // La reversa es más lenta

        // 2. Dirección (Giro de volante) - Solo giramos si el auto se está moviendo
        if (Math.abs(velocidad) > 0.1f) {
            float multiplicadorGiro = (velocidad > 0) ? 1.0f : -1.0f; // Invertir volante en reversa
            if (izq)
                angulo += velGiro * deltaTime * multiplicadorGiro;
            if (der)
                angulo -= velGiro * deltaTime * multiplicadorGiro;
        }

        // 3. Aplicar el movimiento a las coordenadas del mundo (Trigonometría)
        posX += Math.sin(angulo) * velocidad * deltaTime;
        posZ += Math.cos(angulo) * velocidad * deltaTime;
    }
}
