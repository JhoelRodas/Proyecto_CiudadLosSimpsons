package com.graphics;

/**
 * Misiones de entrega: el jugador debe llegar a cada destino y detenerse sobre la marca dorada.
 * Los destinos se definen por celda del mapa (columna, fila) para que siempre caigan sobre una calle.
 */
public class Misiones {

    /** Un destino: nombre que ve el jugador y celda de calle donde está la marca. */
    private static class Destino {
        final String nombre;
        final int columna, fila;

        Destino(String nombre, int columna, int fila) {
            this.nombre = nombre;
            this.columna = columna;
            this.fila = fila;
        }
    }

    // Todas son celdas de calle recta, frente al lugar que nombran. Para agregar una misión basta una línea.
    private static final Destino[] DESTINOS = {
        new Destino("PLAZA", 10, 8),              // al norte de la plaza central
        new Destino("MANSION BURNS", 18, 4),      // frente a la mansión
        new Destino("KWIK-E-MART", 2, 4),         // frente a la tienda
        new Destino("CASA SIMPSON", 1, 16),       // frente a los Simpson
    };

    private static final float RADIO_LLEGADA = 4.0f;       // unidades
    private static final float VELOCIDAD_PARA_ENTREGAR = 1.5f; // unidades por segundo (~5 km/h)

    private int actual = 0;
    private float tiempo = 0;          // segundos desde que empezó la partida
    private float reloj = 0;           // para animar la baliza
    private float avisoEntrega = 0;    // segundos restantes del aviso "ENTREGADO!"

    public int total() { return DESTINOS.length; }
    public int completadas() { return actual; }
    public boolean terminado() { return actual >= DESTINOS.length; }
    public float tiempo() { return tiempo; }
    public boolean recienEntregado() { return avisoEntrega > 0 && !terminado(); }
    public String nombreActual() { return terminado() ? "" : DESTINOS[actual].nombre; }

    public void reiniciar() {
        actual = 0;
        tiempo = 0;
        avisoEntrega = 0;
    }

    private float x(Destino d) {
        int n = 21; // columnas del mapa
        return d.columna * Ciudad.TAMANO_CELDA - (n * Ciudad.TAMANO_CELDA) / 2f + Ciudad.TAMANO_CELDA / 2f;
    }

    private float z(Destino d) {
        int n = 21; // filas del mapa
        return d.fila * Ciudad.TAMANO_CELDA - (n * Ciudad.TAMANO_CELDA) / 2f + Ciudad.TAMANO_CELDA / 2f;
    }

    /** Distancia en línea recta del auto al destino activo (0 si ya terminó). */
    public float distanciaActual(float autoX, float autoZ) {
        if (terminado()) return 0;
        float dx = autoX - x(DESTINOS[actual]);
        float dz = autoZ - z(DESTINOS[actual]);
        return (float) Math.sqrt(dx * dx + dz * dz);
    }

    /** Avanza el cronómetro y completa la entrega si el auto llegó y está casi detenido. */
    public void actualizar(float deltaTime, float autoX, float autoZ, float velocidad) {
        reloj += deltaTime;
        if (avisoEntrega > 0) avisoEntrega -= deltaTime;
        if (terminado()) return;
        tiempo += deltaTime;
        if (distanciaActual(autoX, autoZ) < RADIO_LLEGADA && Math.abs(velocidad) < VELOCIDAD_PARA_ENTREGAR) {
            actual++;
            avisoEntrega = 2.0f;
        }
    }

    /** Dibuja la marca del destino activo: plataforma, baliza giratoria y rayo de luz (vista principal) o cuadro (minimapa). */
    public void dibujarDestino(Renderer r, boolean minimapa) {
        if (terminado()) return;
        Destino d = DESTINOS[actual];
        float px = x(d), pz = z(d);

        r.entero("uEmision", 1); // brilla incluso de noche
        if (minimapa) {
            float pulso = 1.0f + 0.25f * (float) Math.sin(reloj * 4);
            r.caja(px, 48f, pz, 20f * pulso, 0.5f, 20f * pulso, 1.0f, 0.82f, 0.10f);
            r.caja(px, 49f, pz, 9f * pulso, 0.5f, 9f * pulso, 1.0f, 1.0f, 1.0f);
        } else {
            // Plataforma dorada con borde blanco
            r.caja(px, 0.16f, pz, 7.4f, 0.06f, 7.4f, 1.0f, 1.0f, 1.0f);
            r.caja(px, 0.19f, pz, 6.6f, 0.06f, 6.6f, 1.0f, 0.80f, 0.10f);
            r.caja(px, 0.22f, pz, 4.2f, 0.06f, 4.2f, 1.0f, 0.92f, 0.45f);
            // Rayo de luz visible desde lejos
            r.caja(px, 30f, pz, 0.7f, 60f, 0.7f, 1.0f, 0.85f, 0.25f);
            // Baliza que gira y flota
            float altura = 4.0f + (float) Math.sin(reloj * 2) * 0.4f;
            r.cajaGirada(px, altura, pz, 1.4f, 1.4f, 1.4f, 1.0f, 0.82f, 0.10f, reloj);
            r.cajaGirada(px, altura, pz, 1.4f, 1.4f, 1.4f, 1.0f, 1.0f, 1.0f, reloj + (float) (Math.PI / 4));
        }
        r.entero("uEmision", 0);
    }
}
