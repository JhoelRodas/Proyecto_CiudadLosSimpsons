package com.graphics.modelos;

import com.graphics.Renderer;

public class AutoCohete extends Auto {

    @Override
    public void dibujar(Renderer renderer) {
        float[] colorGris = { 0.75f, 0.75f, 0.78f }; // Cuerpo plateado/gris
        float[] colorNegro = { 0.15f, 0.15f, 0.15f }; // Nariz y aletas
        float[] colorCyan = { 0.1f, 0.8f, 0.9f }; // Parabrisas
        float[] colorPurpura = { 0.5f, 0.3f, 0.7f }; // Asientos y "texto"
        float[] colorLlanta = { 0.4f, 0.4f, 0.4f };
        float[] colorPlata = { 0.9f, 0.9f, 0.95f };
        float[] colorFuego = { 0.9f, 0.4f, 0.1f };
        float[] colorAmarillo = { 0.9f, 0.8f, 0.2f }; // Pegatina amarilla

        // 1. Cuerpo Principal (Gris, largo y tubular)
        dibujarPieza(renderer, 0.0f, 0.55f, -0.6f, 1.4f, 0.7f, 4.6f, colorGris);

        // 2. Nariz Negra (Escalonada para simular el cono aerodinámico)
        dibujarPieza(renderer, 0.0f, 0.5f, 2.0f, 1.3f, 0.6f, 1.0f, colorNegro);
        dibujarPieza(renderer, 0.0f, 0.45f, 2.6f, 1.1f, 0.5f, 0.6f, colorNegro);
        dibujarPieza(renderer, 0.0f, 0.35f, 3.0f, 0.8f, 0.3f, 0.4f, colorNegro);
        dibujarPieza(renderer, 0.0f, 0.28f, 3.25f, 0.5f, 0.2f, 0.3f, colorNegro);

        // 3. Parabrisas (Cyan) - Inclinado y puntiagudo
        dibujarPieza(renderer, 0.0f, 1.0f, 0.8f, 1.0f, 0.4f, 0.6f, colorCyan);
        dibujarPieza(renderer, 0.0f, 0.9f, 1.2f, 1.2f, 0.3f, 0.5f, colorCyan);

        // 4. Cabina Abierta y Asientos Púrpura
        // Interior (Agujero oscuro para sentarse)
        dibujarPieza(renderer, 0.0f, 0.8f, -0.6f, 1.1f, 0.1f, 2.2f, new float[] { 0.3f, 0.3f, 0.3f });

        // Asiento Conductor
        dibujarPieza(renderer, 0.0f, 0.9f, 0.0f, 0.8f, 0.15f, 0.5f, colorPurpura); // base
        dibujarPieza(renderer, 0.0f, 1.15f, -0.2f, 0.8f, 0.5f, 0.15f, colorPurpura); // respaldo

        // Asiento Pasajero (Atrás)
        dibujarPieza(renderer, 0.0f, 0.9f, -1.1f, 0.8f, 0.15f, 0.5f, colorPurpura); // base
        dibujarPieza(renderer, 0.0f, 1.15f, -1.3f, 0.8f, 0.5f, 0.15f, colorPurpura); // respaldo

        // 5. Detalles Laterales (Simulando el texto "HONOR ROLLER" y pegatinas)
        dibujarPieza(renderer, 0.71f, 0.6f, -0.2f, 0.05f, 0.15f, 1.8f, colorPurpura); // Raya morada izq
        dibujarPieza(renderer, -0.71f, 0.6f, -0.2f, 0.05f, 0.15f, 1.8f, colorPurpura); // Raya morada der
        // Pegatina amarilla trasera
        dibujarPieza(renderer, 0.71f, 0.7f, -1.8f, 0.05f, 0.25f, 0.4f, colorAmarillo);
        dibujarPieza(renderer, -0.71f, 0.7f, -1.8f, 0.05f, 0.25f, 0.4f, colorAmarillo);

        // 6. Aletas (Negras)
        // Aletas delanteras pequeñas (cerca de las ruedas)
        dibujarPieza(renderer, 0.9f, 0.3f, 1.8f, 1.2f, 0.05f, 0.8f, colorNegro);
        dibujarPieza(renderer, -0.9f, 0.3f, 1.8f, 1.2f, 0.05f, 0.8f, colorNegro);

        // Aletas traseras grandes
        dibujarPieza(renderer, 1.4f, 0.4f, -2.2f, 2.2f, 0.08f, 1.6f, colorNegro);
        dibujarPieza(renderer, -1.4f, 0.4f, -2.2f, 2.2f, 0.08f, 1.6f, colorNegro);

        // Aleta vertical trasera (Cola de avión)
        dibujarPieza(renderer, 0.0f, 1.25f, -2.6f, 0.15f, 1.2f, 0.8f, colorNegro);
        dibujarPieza(renderer, 0.0f, 1.9f, -2.8f, 0.16f, 0.4f, 0.4f, colorPlata); // Punta plateada

        // 7. Propulsor Trasero (Cilindro plateado)
        // Rotamos un cilindro a lo largo del eje Z para que sirva de tubo de escape
        // Nota: dibujarRueda dibuja un cilindro en el eje X, así que tenemos que rotar
        // la caja manualmente
        // Pero dibujarRueda usa cilindroGirado que aplica rotaciones.
        // Para simplificar, dibujamos un tubo cuadrado o usamos dibujarPieza
        dibujarPieza(renderer, 0.0f, 0.55f, -3.1f, 0.6f, 0.6f, 0.6f, colorPlata);

        // Fuego del propulsor al acelerar (vinculado a la velocidad)
        if (Math.abs(velocidad) > 1.0f) {
            renderer.entero("uEmision", 1);
            // Hacer que el fuego varíe de tamaño
            float vibracion = (float) Math.random() * 0.2f;
            float largoFuego = 1.0f + (Math.abs(velocidad) / 15f) + vibracion;

            dibujarPieza(renderer, 0.0f, 0.55f, -3.1f - (largoFuego / 2f), 0.45f, 0.45f, largoFuego, colorFuego);

            // Fuego interior amarillo
            dibujarPieza(renderer, 0.0f, 0.55f, -3.1f - (largoFuego / 2f) - 0.2f, 0.25f, 0.25f, largoFuego * 0.8f,
                    new float[] { 1f, 0.9f, 0.2f });
            renderer.entero("uEmision", 0);
        }

        // 8. Ruedas (Escondidas debajo, pequeñas)
        float radioRueda = 0.45f;
        float anchoRueda = 0.15f;
        // Delanteras
        dibujarRueda(renderer, 0.6f, 0.25f, 1.5f, anchoRueda, radioRueda, radioRueda, colorLlanta, true);
        dibujarRueda(renderer, -0.6f, 0.25f, 1.5f, anchoRueda, radioRueda, radioRueda, colorLlanta, true);
        // Traseras
        dibujarRueda(renderer, 0.6f, 0.25f, -1.5f, anchoRueda, radioRueda, radioRueda, colorLlanta, false);
        dibujarRueda(renderer, -0.6f, 0.25f, -1.5f, anchoRueda, radioRueda, radioRueda, colorLlanta, false);

        // Faros delanteros
        if (farosOn) {
            renderer.entero("uEmision", 1);
            dibujarPieza(renderer, 0.4f, 0.4f, 3.4f, 0.2f, 0.15f, 0.05f, new float[] { 1, 1, 0.8f });
            dibujarPieza(renderer, -0.4f, 0.4f, 3.4f, 0.2f, 0.15f, 0.05f, new float[] { 1, 1, 0.8f });
            renderer.entero("uEmision", 0);
        }
    }

    @Override
    public void actualizarFisicas(float deltaTime, boolean acelerar, boolean frenar, boolean izq, boolean der) {
        // Aceleración extrema para el coche cohete
        float aceleradorCmd = 0;
        if (acelerar)
            aceleradorCmd += 1;
        if (frenar)
            aceleradorCmd -= 1;

        // Aceleración de motor brutal (cohete!)
        velocidad += aceleradorCmd * 20.0f * deltaTime;

        // Fricción adaptativa
        float resistencia = 0.3f; // Rodamiento muy libre, aerodinámico
        if (!acelerar && !frenar)
            resistencia = 1.0f; // Tarda más en frenar por sí solo
        if (frenar && velocidad > 0)
            resistencia = 12.0f; // Frenos de disco super potentes
        if (acelerar && velocidad < 0)
            resistencia = 12.0f;

        float factorFrenado = (float) Math.exp(-resistencia * deltaTime);
        velocidad *= factorFrenado;

        // Límite de velocidad ultra alto (es un cohete)
        velocidad = Math.max(-15.0f, Math.min(45.0f, velocidad));

        // Dirección (Giro de volante)
        float direccionCmd = 0;
        if (izq)
            direccionCmd += 1.0f;
        if (der)
            direccionCmd -= 1.0f;

        float volanteObjetivo = direccionCmd * 0.6f;
        anguloVolante += (volanteObjetivo - anguloVolante) * 12f * deltaTime;

        // Gira muy rápido
        float sensibilidadGiro = 0.08f; // Un poco más duro de girar a altas velocidades
        angulo += direccionCmd * velocidad * sensibilidadGiro * deltaTime;

        // Posición
        posX += Math.sin(angulo) * velocidad * deltaTime;
        posZ += Math.cos(angulo) * velocidad * deltaTime;
    }
}
