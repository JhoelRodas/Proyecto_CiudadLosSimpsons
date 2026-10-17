package com.graphics.modelos;

import com.graphics.Renderer;

public class Auto {
    // Inicializamos en una calle (x=10, z=12)
    public float posX = 0;
    public float posZ = 28f;
    public float angulo = 0;
    public float velocidad = 0;
    public float anguloVolante = 0; // Para animación visual de ruedas
    public boolean farosOn = false; // Los faros delanteros brillan cuando están encendidos
    public float offsetY = 0; // Para animación de salto/rebote de suspensión o transformación

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
        // Desplazadas al extremo absoluto (X = 0.92f) para que formen el contorno del
        // coche
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
        if (farosOn) {
            renderer.entero("uEmision", 1); // Faro encendido: color fijo y brillante, sin oscurecerse
            colorFaro = new float[] { 1.0f, 0.96f, 0.75f };
        }
        dibujarPieza(renderer, 0.65f, 0.65f, 2.31f, 0.25f, 0.25f, 0.05f, colorFaro);
        dibujarPieza(renderer, -0.65f, 0.65f, 2.31f, 0.25f, 0.25f, 0.05f, colorFaro);
        if (farosOn) {
            renderer.entero("uEmision", 0);
        }

        // Antena clásica del juego (Fina en el lado izquierdo del capó)
        dibujarPieza(renderer, 0.75f, 1.15f, 1.5f, 0.02f, 0.8f, 0.02f, colorVolante);

        // =========================================================================
        // 6. RUEDAS CON DETALLE DE LLANTA DE PLATO (Animación de giro añadida)
        // =========================================================================
        // --- EJE DELANTERO --- (Giran con el volante)
        dibujarRueda(renderer, 1.0f, 0.35f, 1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta, true);
        dibujarRueda(renderer, 1.12f, 0.35f, 1.2f, anchoTapon, radioTapon, radioTapon, colorCromo, true);

        dibujarRueda(renderer, -1.0f, 0.35f, 1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta, true);
        dibujarRueda(renderer, -1.12f, 0.35f, 1.2f, anchoTapon, radioTapon, radioTapon, colorCromo, true);

        // --- EJE TRASERO --- (No giran con el volante)
        dibujarRueda(renderer, 1.0f, 0.35f, -1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta, false);
        dibujarRueda(renderer, 1.12f, 0.35f, -1.2f, anchoTapon, radioTapon, radioTapon, colorCromo, false);

        dibujarRueda(renderer, -1.0f, 0.35f, -1.2f, anchoRueda, radioRueda, radioRueda, colorLlanta, false);
        dibujarRueda(renderer, -1.12f, 0.35f, -1.2f, anchoTapon, radioTapon, radioTapon, colorCromo, false);

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

    protected void dibujarPieza(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb) {
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);

        renderer.cajaGirada(mundoX, yLocal + offsetY, mundoZ, sx, sy, sz, rgb[0], rgb[1], rgb[2], angulo);
    }

    /**
     * Traduce las coordenadas locales de la rueda circular a la posición global rotada
     */
    protected void dibujarRueda(Renderer renderer, float xLocal, float yLocal, float zLocal,
            float sx, float sy, float sz, float[] rgb, boolean esDelantera) {
        
        float anguloTotal = angulo;
        if (esDelantera) {
            anguloTotal += anguloVolante; // Ruedas delanteras giran localmente
        }
        
        float coseno = (float) Math.cos(angulo);
        float seno = (float) Math.sin(angulo);

        float mundoX = posX + (coseno * xLocal) + (seno * zLocal);
        float mundoZ = posZ - (seno * xLocal) + (coseno * zLocal);

        // Invocamos el renderizado del cilindro girado
        renderer.cilindroGirado(mundoX, yLocal + offsetY, mundoZ, sx, sy, sz, rgb, anguloTotal);
    }

    public void actualizarFisicas(float deltaTime, boolean acelerar, boolean frenar, boolean izq, boolean der) {
        // 1. Aceleración más realista (Estilo CiudadConduccion)
        float aceleradorCmd = 0;
        if (acelerar) aceleradorCmd += 1;
        if (frenar) aceleradorCmd -= 1;
        
        // Aceleración de motor más manejable (antes 25, ahora 12)
        velocidad += aceleradorCmd * 12.0f * deltaTime; 
        
        // Fricción adaptativa
        float resistencia = 0.5f; // Rodamiento libre (Pérdida mínima)
        if (!acelerar && !frenar) resistencia = 2.0f; // Freno de motor al soltar
        if (frenar && velocidad > 0) resistencia = 8.0f; // Freno fuerte adelante
        if (acelerar && velocidad < 0) resistencia = 8.0f; // Freno fuerte al cambiar a adelante

        // Fórmula exponencial de CiudadConduccion para fricción muy realista
        float factorFrenado = (float) Math.exp(-resistencia * deltaTime);
        velocidad *= factorFrenado;

        // Límites de velocidad suavizados
        velocidad = Math.max(-8.0f, Math.min(22.0f, velocidad));

        // 2. Dirección (Giro de volante)
        float direccionCmd = 0;
        if (izq) direccionCmd += 1.0f;
        if (der) direccionCmd -= 1.0f;

        // Animación visual del volante y las ruedas
        float volanteObjetivo = direccionCmd * 0.6f; // Max ~35 grados de giro de rueda visual
        anguloVolante += (volanteObjetivo - anguloVolante) * 10f * deltaTime; // Interpolación suave

        // El radio de giro de la física (suave como CiudadConduccion)
        float sensibilidadGiro = 0.11f;
        angulo += direccionCmd * velocidad * sensibilidadGiro * deltaTime;

        // 3. Posición
        posX += Math.sin(angulo) * velocidad * deltaTime;
        posZ += Math.cos(angulo) * velocidad * deltaTime;
    }
}
