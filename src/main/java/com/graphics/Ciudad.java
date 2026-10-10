package com.graphics;

import com.graphics.modelos.*;

public class Ciudad {
    public static final int TAMANO_CELDA = 14;

    // 0: Calle
    // 1: Casa Genérica (Zonas residenciales continuas)
    // 2: Kwik-E-Mart
    // 3: Mansión Burns (Centro, 3x3)
    // 4: Parque
    // 5: Casa Simpson
    // 6: Casa Flanders
    // 8: Plaza Central (Centro, 3x3)
    // 9: Obstáculo (Pared invisible para acompañar edificios grandes)
    private final int[][] mapa = {
            // Row 0: Calle superior
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            // Row 1-3: KwikEMart, Casas, Parque Superior, Casas, Mansion Burns
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 4, 4, 4, 0, 1, 1, 1, 0, 9, 9, 9, 0 },
            { 0, 1, 2, 1, 0, 1, 1, 1, 0, 4, 4, 4, 0, 1, 1, 1, 0, 9, 3, 9, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 4, 4, 4, 0, 1, 1, 1, 0, 9, 9, 9, 0 },
            // Row 4: Calle
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            // Row 5-7: Manzanas residenciales
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            // Row 8: Calle
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            // Row 9-11: Parque Izquierdo, Casas, Plaza Central, Casas, Parque Derecho
            { 0, 4, 4, 4, 0, 1, 1, 1, 0, 9, 9, 9, 0, 1, 1, 1, 0, 4, 4, 4, 0 },
            { 0, 4, 4, 4, 0, 1, 1, 1, 0, 9, 8, 9, 0, 1, 1, 1, 0, 4, 4, 4, 0 },
            { 0, 4, 4, 4, 0, 1, 1, 1, 0, 9, 9, 9, 0, 1, 1, 1, 0, 4, 4, 4, 0 },
            // Row 12: Calle
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            // Row 13-15: Manzanas residenciales
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            // Row 16: Calle
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            // Row 17-19: Simpson/Flanders, Casas, Parque Inferior, Casas, Casas
            { 0, 5, 6, 1, 0, 1, 1, 1, 0, 4, 4, 4, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 4, 4, 4, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0, 4, 4, 4, 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            // Row 20: Calle inferior
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    };

    private CasaSimpson casaSimpson = new CasaSimpson(0, 0, 0);
    private Lote loteSimpson = Lote.paraSimpson(0, 0, 0);

    private CasaFlanders casaFlanders = new CasaFlanders(0, 0, 0);
    private Lote loteFlanders = Lote.paraFlanders(0, 0, 0);

    public Ciudad() {
        // El TERRENO de cada lote siempre llena la celda (TERRENO x TERRENO), así que
        // nunca se sale a la
        // calle ni choca con el vecino. La escala solo cambia el tamaño de la casa,
        // árboles y cercas.
        // Todas las casas comparten la misma unidad de medida (ESCALA_CASAS) para que
        // guarden proporción.
        casaSimpson.escala = ESCALA_CASAS;
        loteSimpson.escala = ESCALA_CASAS;
        casaFlanders.escala = ESCALA_CASAS;
        loteFlanders.escala = ESCALA_CASAS;
        loteSimpson.ajustarATerreno(TERRENO, TERRENO).acera = false;
        loteFlanders.ajustarATerreno(TERRENO, TERRENO).acera = false;

        for (int i = 0; i < 16; i++) {
            casasGen[i] = new CasaGenerica(0, 0, 0, i);
            lotesGen[i] = Lote.paraGenerica(0, 0, 0, casasGen[i]);
            lotesGen[i].ajustarATerreno(TERRENO, TERRENO).acera = false;
        }
    }

    // ---- Medidas de la ciudad (todas en unidades de mundo) ----
    private static final float TERRENO = TAMANO_CELDA - 1.0f; // lote: deja 0.5 libres a cada lado de la celda
    private static final float ESCALA_CASAS = 0.85f; // unidad común para Simpson, Flanders y genéricas
    private static final float ESCALA_FAROL = 1.3f;            // tamaño de los faroles (y de su luz)
    private static final float ANCHO_ACERA = 2.4f; // acera dentro de cada celda de calle

    private CasaGenerica[] casasGen = new CasaGenerica[16];
    private Lote[] lotesGen = new Lote[16];

    private KwikEMart kwik = new KwikEMart(0, 0, 0);
    private MansionBurns mansion = new MansionBurns(0, 0, 0);

    private Parque parqueGen = new Parque(0, 0, 0);
    private Plaza plaza = new Plaza(0, 0, 0, 2.8f);
    private Semaforo semaforo = new Semaforo(0, 0, 0);
    private Alumbrado alumbrado = new Alumbrado(0, 0, 0);

    // Guardaremos las posiciones mundiales de TODAS las farolas (X,Y,Z)
    private float[] posicionesFarolas = new float[600]; // Suficiente para 200 farolas
    private int totalFarolas = 0;
    private boolean farolasCalculadas = false;

    public void enviarLuces(Renderer r, float autoX, float autoZ) {
        if (!farolasCalculadas) {
            calcularFarolas();
            registrarFarolasPlaza();
            farolasCalculadas = true;
        }

        // Seleccionar las 25 farolas más cercanas al auto para pasarlas al shader
        // Usamos arreglos simples para no usar mucha memoria ni CPU en cada frame
        int MaxLuces = 25;
        float[] cercanasX = new float[MaxLuces];
        float[] cercanasY = new float[MaxLuces];
        float[] cercanasZ = new float[MaxLuces];
        float[] distancias = new float[MaxLuces];
        for (int i = 0; i < MaxLuces; i++) distancias[i] = Float.MAX_VALUE;

        for (int i = 0; i < totalFarolas; i++) {
            float fx = posicionesFarolas[i * 3];
            float fy = posicionesFarolas[i * 3 + 1];
            float fz = posicionesFarolas[i * 3 + 2];
            
            float dx = fx - autoX;
            float dz = fz - autoZ;
            float distSq = dx * dx + dz * dz;

            // Inserción ordenada simple en las 25 mejores
            if (distSq < distancias[MaxLuces - 1]) {
                for (int j = 0; j < MaxLuces; j++) {
                    if (distSq < distancias[j]) {
                        // Desplazar las demás
                        for (int k = MaxLuces - 1; k > j; k--) {
                            distancias[k] = distancias[k - 1];
                            cercanasX[k] = cercanasX[k - 1];
                            cercanasY[k] = cercanasY[k - 1];
                            cercanasZ[k] = cercanasZ[k - 1];
                        }
                        distancias[j] = distSq;
                        cercanasX[j] = fx;
                        cercanasY[j] = fy;
                        cercanasZ[j] = fz;
                        break;
                    }
                }
            }
        }

        int enviadas = Math.min(MaxLuces, totalFarolas);
        for (int i = 0; i < enviadas; i++) {
            r.vector("uLuces[" + i + "]", cercanasX[i], cercanasY[i], cercanasZ[i]);
        }
        r.entero("uNumLuces", enviadas);
    }

    private void calcularFarolas() {
        int filas = mapa.length;
        int cols = mapa[0].length;
        int offsetX = (cols * TAMANO_CELDA) / 2;
        int offsetZ = (filas * TAMANO_CELDA) / 2;
        final float T = TAMANO_CELDA;
        final float A = ANCHO_ACERA;
        final float mitad = T / 2f;

        for (int z = 0; z < filas; z++) {
            for (int x = 0; x < cols; x++) {
                if (mapa[z][x] == 0) { // CALLE
                    boolean cN = esCalle(x, z - 1), cS = esCalle(x, z + 1);
                    boolean cE = esCalle(x + 1, z), cO = esCalle(x - 1, z);
                    int numCalles = (cN ? 1 : 0) + (cS ? 1 : 0) + (cE ? 1 : 0) + (cO ? 1 : 0);
                    
                    float px = (x * T) - offsetX + mitad;
                    float pz = (z * T) - offsetZ + mitad;
                    
                    if (numCalles == 2 && cN && cS) { // Recta NS
                        if (!cO && z % 2 == 0) registrarFarola(px - mitad + A / 2, pz, 0f);
                        if (!cE && z % 2 == 1) registrarFarola(px + mitad - A / 2, pz, (float) Math.PI);
                    } else if (numCalles == 2 && cE && cO) { // Recta EO
                        if (!cN && x % 2 == 0) registrarFarola(px, pz - mitad + A / 2, (float) (-Math.PI / 2));
                        if (!cS && x % 2 == 1) registrarFarola(px, pz + mitad - A / 2, (float) (Math.PI / 2));
                    }
                }
            }
        }
    }

    /** Los 4 faroles de la plaza (los dibuja Plaza.java) también deben mandar luz al shader. */
    private void registrarFarolasPlaza() {
        int filas = mapa.length;
        int cols = mapa[0].length;
        int offsetX = (cols * TAMANO_CELDA) / 2;
        int offsetZ = (filas * TAMANO_CELDA) / 2;
        float e = plaza.escala;
        float[][] pf = { { -1.9f, -1.9f }, { 1.9f, -1.9f }, { 1.9f, 1.9f }, { -1.9f, 1.9f } }; // igual que en Plaza.java
        for (int z = 0; z < filas; z++) {
            for (int x = 0; x < cols; x++) {
                if (mapa[z][x] != 8) continue;
                float cx = (x * TAMANO_CELDA) - offsetX + TAMANO_CELDA / 2f;
                float cz = (z * TAMANO_CELDA) - offsetZ + TAMANO_CELDA / 2f;
                for (float[] p : pf) {
                    if (totalFarolas * 3 >= posicionesFarolas.length) return;
                    float ang = (float) Math.atan2(p[1], -p[0]); // el brazo apunta al centro de la plaza
                    float alcance = 1.2f * e;
                    posicionesFarolas[totalFarolas * 3] = cx + p[0] * e + (float) Math.cos(ang) * alcance;
                    // Los faroles de la plaza son muy altos; la luz se coloca más baja para que sí llegue al suelo
                    posicionesFarolas[totalFarolas * 3 + 1] = 4.28f * e * 0.6f;
                    posicionesFarolas[totalFarolas * 3 + 2] = cz + p[1] * e - (float) Math.sin(ang) * alcance;
                    totalFarolas++;
                }
            }
        }
    }

    /** Guarda la posición de la BOMBILLA (no del poste): la luz sale de la cabeza del farol, sobre la calle. */
    private void registrarFarola(float posteX, float posteZ, float ang) {
        if (totalFarolas * 3 >= posicionesFarolas.length) return; // Limite alcanzado
        float alcance = 1.2f * ESCALA_FAROL; // La cabeza del farol está 1.2 unidades (a escala 1) hacia la calle
        posicionesFarolas[totalFarolas * 3] = posteX + (float) Math.cos(ang) * alcance;
        posicionesFarolas[totalFarolas * 3 + 1] = 4.28f * ESCALA_FAROL;
        posicionesFarolas[totalFarolas * 3 + 2] = posteZ - (float) Math.sin(ang) * alcance;
        totalFarolas++;
    }

    public void dibujar(Renderer renderer, boolean esMinimapa) {
        // Suelo verde base para la ciudad
        renderer.caja(0, -0.2f, 0, 300, 0.4f, 300, 0.35f, 0.70f, 0.30f);

        int filas = mapa.length;
        int cols = mapa[0].length;
        int offsetX = (cols * TAMANO_CELDA) / 2;
        int offsetZ = (filas * TAMANO_CELDA) / 2;

        float tiempo = (float) org.lwjgl.glfw.GLFW.glfwGetTime();

        for (int z = 0; z < filas; z++) {
            for (int x = 0; x < cols; x++) {
                int tipo = mapa[z][x];
                float posX = (x * TAMANO_CELDA) - offsetX + (TAMANO_CELDA / 2.0f);
                float posZ = (z * TAMANO_CELDA) - offsetZ + (TAMANO_CELDA / 2.0f);

                if (tipo == 0) { // CALLE
                    dibujarCalle(renderer, x, z, posX, posZ, tiempo, esMinimapa);
                } else if (tipo != 9) {
                    float ang = obtenerAnguloHaciaCalle(x, z);

                    if (tipo == 1) { // CASA GENÉRICA CON LOTE
                        int indice = Math.abs(x * 7 + z * 13) % 16;
                        float esc = ESCALA_CASAS + 0.15f + ((x + z) % 4) * 0.05f;
                        casasGen[indice].escala = esc;
                        lotesGen[indice].escala = esc;
                        dibujarLoteYCasa(renderer, lotesGen[indice], casasGen[indice], posX, posZ, ang,
                                hayCalleAdelante(x, z, ang), esMinimapa);
                    } else if (tipo == 5) { // SIMPSON CON LOTE
                        dibujarLoteYCasa(renderer, loteSimpson, casaSimpson, posX, posZ, ang,
                                hayCalleAdelante(x, z, ang), esMinimapa);
                    } else if (tipo == 6) { // FLANDERS CON LOTE
                        dibujarLoteYCasa(renderer, loteFlanders, casaFlanders, posX, posZ, ang,
                                hayCalleAdelante(x, z, ang), esMinimapa);
                    } else if (tipo == 2) { // KWIK-E-MART
                        kwik.posX = posX;
                        kwik.posZ = posZ;
                        kwik.angulo = ang;
                        if (!esMinimapa)
                            kwik.dibujar(renderer);
                        else
                            renderer.cajaGirada(posX, 2f, posZ, 9f, 4f, 9f, 0.8f, 0.85f, 0.85f, ang); // Proxy
                    } else if (tipo == 3) { // MANSION BURNS
                        mansion.posX = posX;
                        mansion.posZ = posZ;
                        mansion.angulo = ang;
                        if (!esMinimapa)
                            mansion.dibujar(renderer);
                        else
                            renderer.cajaGirada(posX, 4f, posZ, 12f, 8f, 10f, 0.45f, 0.45f, 0.5f, ang); // Proxy
                    } else if (tipo == 8) { // PLAZA CENTRAL
                        plaza.posX = posX;
                        plaza.posZ = posZ;
                        if (!esMinimapa)
                            plaza.dibujar(renderer);
                        else
                            renderer.caja(posX, 0.1f, posZ, TAMANO_CELDA, 0.2f, TAMANO_CELDA, 0.5f, 0.5f, 0.5f); // Proxy
                    } else if (tipo == 4) { // PARQUE GENÉRICO
                        parqueGen.posX = posX;
                        parqueGen.posZ = posZ;
                        parqueGen.variante = (x + z * 5);
                        // Escalar a la par de la celda de la calle para que el parque cubra toda la
                        // celda
                        parqueGen.escala = TAMANO_CELDA / 10.0f;
                        if (!esMinimapa)
                            parqueGen.dibujar(renderer);
                        else
                            renderer.caja(posX, 0.1f, posZ, TAMANO_CELDA, 0.2f, TAMANO_CELDA, 0.2f, 0.4f, 0.22f); // Proxy
                    }
                }
            }
        }
    }

    /**
     * true si la celda (x, z) existe y es calle. Fuera del mapa cuenta como "no
     * calle" (se dibuja borde).
     */
    private boolean esCalle(int x, int z) {
        return z >= 0 && z < mapa.length && x >= 0 && x < mapa[0].length && mapa[z][x] == 0;
    }

    private static final float[] ASFALTO = { 0.25f, 0.25f, 0.28f };
    private static final float[] ACERA = { 0.74f, 0.73f, 0.70f };
    private static final float[] BORDILLO = { 0.55f, 0.54f, 0.52f };
    private static final float[] LINEA_BLANCA = { 0.92f, 0.92f, 0.92f };
    private static final float[] LINEA_AMARILLA = { 0.95f, 0.80f, 0.15f };

    /**
     * Calle con asfalto, aceras con bordillo en los lados que dan a edificios,
     * esquinas de acera en las
     * intersecciones, líneas de carril, pasos de cebra, semáforos en las esquinas y
     * faroles en las aceras.
     */
    private void dibujarCalle(Renderer r, int x, int z, float px, float pz, float tiempo, boolean esMinimapa) {
        final float T = TAMANO_CELDA;
        final float A = ANCHO_ACERA;
        final float mitad = T / 2f;
        final float ancho = T - 2 * A; // ancho de asfalto transitable entre aceras

        boolean cN = esCalle(x, z - 1), cS = esCalle(x, z + 1), cE = esCalle(x + 1, z), cO = esCalle(x - 1, z);
        int numCalles = (cN ? 1 : 0) + (cS ? 1 : 0) + (cE ? 1 : 0) + (cO ? 1 : 0);

        // Asfalto
        r.caja(px, 0.05f, pz, T, 0.1f, T, ASFALTO[0], ASFALTO[1], ASFALTO[2]);

        // Aceras en los lados que NO son calle
        // (el bordillo se recorta donde la acera vecina continúa, para no cruzarla)
        if (!cN)
            acera(r, px, pz - mitad + A / 2, T, A, true, !cO ? A : 0, !cE ? A : 0);
        if (!cS)
            acera(r, px, pz + mitad - A / 2, T, A, false, !cO ? A : 0, !cE ? A : 0);
        if (!cE)
            acera(r, px + mitad - A / 2, pz, A, T, false, !cN ? A : 0, !cS ? A : 0);
        if (!cO)
            acera(r, px - mitad + A / 2, pz, A, T, true, !cN ? A : 0, !cS ? A : 0);

        // Esquinas de acera: si las dos calles vecinas existen pero la diagonal es
        // edificio
        int[][] esquinas = { { -1, -1 }, { 1, -1 }, { 1, 1 }, { -1, 1 } }; // NO, NE, SE, SO
        for (int[] e : esquinas) {
            boolean ortoX = e[0] < 0 ? cO : cE;
            boolean ortoZ = e[1] < 0 ? cN : cS;
            boolean diag = esCalle(x + e[0], z + e[1]);
            if (ortoX && ortoZ && !diag) {
                float cx = px + e[0] * (mitad - A / 2);
                float cz = pz + e[1] * (mitad - A / 2);
                acera(r, cx, cz, A, A, false, 0, 0);
                // Bordillo en los dos bordes que dan al asfalto
                r.caja(cx, 0.1f, cz - e[1] * (A / 2 - 0.09f), A, 0.205f, 0.18f, BORDILLO[0], BORDILLO[1], BORDILLO[2]);
                r.caja(cx - e[0] * (A / 2 - 0.09f), 0.1f, cz, 0.18f, 0.205f, A, BORDILLO[0], BORDILLO[1], BORDILLO[2]);
            }
        }

        if (esMinimapa)
            return; // ¡OPTIMIZACIÓN EXTREMA! Saltamos los detalles si es vista aérea

        if (numCalles >= 3) {
            // INTERSECCIÓN: pasos de cebra en cada acceso que sea calle
            if (cN)
                cebra(r, px, pz - mitad + A * 0.75f, ancho, true);
            if (cS)
                cebra(r, px, pz + mitad - A * 0.75f, ancho, true);
            if (cE)
                cebra(r, px + mitad - A * 0.75f, pz, ancho, false);
            if (cO)
                cebra(r, px - mitad + A * 0.75f, pz, ancho, false);

            // Semáforos en las esquinas de acera. NO y SE controlan el eje Norte-Sur; NE y
            // SO el Este-Oeste.
            // angulo: el brazo (+X local) apunta sobre la calle y las luces (+Z local)
            // miran al tráfico que llega.
            if (!tieneSemaforo(x, z))
                return;
            float[] angulos = { 0f, (float) (-Math.PI / 2), (float) Math.PI, (float) (Math.PI / 2) };
            boolean[] ejeNS = { true, false, true, false };
            for (int i = 0; i < 4; i++) {
                int[] e = esquinas[i];
                boolean ortoX = e[0] < 0 ? cO : cE;
                boolean ortoZ = e[1] < 0 ? cN : cS;
                boolean diag = esCalle(x + e[0], z + e[1]);
                if (ortoX && ortoZ && !diag) {
                    semaforo.escala = 1.5f;
                    semaforo.posX = px + e[0] * (mitad - A / 2);
                    semaforo.posZ = pz + e[1] * (mitad - A / 2);
                    semaforo.angulo = angulos[i];
                    semaforo.estado = estadoSemaforo(tiempo, ejeNS[i]);
                    semaforo.dibujar(r);
                }
            }
        } else if (numCalles == 2 && cN && cS) {
            // CALLE RECTA NORTE-SUR
            lineasCarril(r, px, pz, true);
            if (!cO && z % 2 == 0)
                farol(r, px - mitad + A / 2, pz, 0f);
            if (!cE && z % 2 == 1)
                farol(r, px + mitad - A / 2, pz, (float) Math.PI);
        } else if (numCalles == 2 && cE && cO) {
            // CALLE RECTA ESTE-OESTE
            lineasCarril(r, px, pz, false);
            if (!cN && x % 2 == 0)
                farol(r, px, pz - mitad + A / 2, (float) (-Math.PI / 2));
            if (!cS && x % 2 == 1)
                farol(r, px, pz + mitad - A / 2, (float) (Math.PI / 2));
        }
    }

    /**
     * Acera elevada de tamaño sx x sz centrada en (cx, cz). "ladoNegativo" indica
     * de qué lado queda el exterior
     * (la calle queda del lado contrario). recIni/recFin recortan el bordillo en
     * los extremos.
     */
    private void acera(Renderer r, float cx, float cz, float sx, float sz, boolean ladoNegativo,
            float recIni, float recFin) {
        r.caja(cx, 0.1f, cz, sx, 0.2f, sz, ACERA[0], ACERA[1], ACERA[2]);
        float g = 0.18f;
        if (sx > sz) { // acera horizontal (norte o sur)
            float dz = (ladoNegativo ? 1 : -1) * (sz / 2 - g / 2);
            float largo = sx - recIni - recFin;
            if (largo > 0) {
                r.caja(cx + (recIni - recFin) / 2, 0.1f, cz + dz, largo, 0.205f, g, BORDILLO[0], BORDILLO[1],
                        BORDILLO[2]);
            }
        } else if (sz > sx) { // acera vertical (este u oeste)
            float dx = (ladoNegativo ? 1 : -1) * (sx / 2 - g / 2);
            float largo = sz - recIni - recFin;
            if (largo > 0) {
                r.caja(cx + dx, 0.1f, cz + (recIni - recFin) / 2, g, 0.205f, largo, BORDILLO[0], BORDILLO[1],
                        BORDILLO[2]);
            }
        }
    }

    /**
     * Paso de cebra: barras paralelas al tráfico repartidas a lo ancho de la calle.
     */
    private void cebra(Renderer r, float cx, float cz, float ancho, boolean cruzaEnX) {
        int n = 7;
        float paso = ancho / n;
        for (int i = 0; i < n; i++) {
            float off = -ancho / 2 + paso * (i + 0.5f);
            if (cruzaEnX) {
                r.caja(cx + off, 0.115f, cz, paso * 0.5f, 0.03f, 2.2f, LINEA_BLANCA[0], LINEA_BLANCA[1],
                        LINEA_BLANCA[2]);
            } else {
                r.caja(cx, 0.115f, cz + off, 2.2f, 0.03f, paso * 0.5f, LINEA_BLANCA[0], LINEA_BLANCA[1],
                        LINEA_BLANCA[2]);
            }
        }
    }

    /** Doble línea central amarilla discontinua y bordes de carril blancos. */
    private void lineasCarril(Renderer r, float px, float pz, boolean norteSur) {
        final float ancho = TAMANO_CELDA - 2 * ANCHO_ACERA;
        final float borde = ancho / 2 - 0.35f;
        for (int k = -1; k <= 1; k += 2) {
            float d = k * 3.5f; // dos rayas por celda, con 4 unidades de hueco
            if (norteSur) {
                r.caja(px - 0.12f, 0.115f, pz + d, 0.1f, 0.03f, 3f, LINEA_AMARILLA[0], LINEA_AMARILLA[1],
                        LINEA_AMARILLA[2]);
                r.caja(px + 0.12f, 0.115f, pz + d, 0.1f, 0.03f, 3f, LINEA_AMARILLA[0], LINEA_AMARILLA[1],
                        LINEA_AMARILLA[2]);
            } else {
                r.caja(px + d, 0.115f, pz - 0.12f, 3f, 0.03f, 0.1f, LINEA_AMARILLA[0], LINEA_AMARILLA[1],
                        LINEA_AMARILLA[2]);
                r.caja(px + d, 0.115f, pz + 0.12f, 3f, 0.03f, 0.1f, LINEA_AMARILLA[0], LINEA_AMARILLA[1],
                        LINEA_AMARILLA[2]);
            }
        }
        // Bordes de carril
        for (int k = -1; k <= 1; k += 2) {
            if (norteSur) {
                r.caja(px + k * borde, 0.115f, pz, 0.12f, 0.03f, TAMANO_CELDA, LINEA_BLANCA[0], LINEA_BLANCA[1],
                        LINEA_BLANCA[2]);
            } else {
                r.caja(px, 0.115f, pz + k * borde, TAMANO_CELDA, 0.03f, 0.12f, LINEA_BLANCA[0], LINEA_BLANCA[1],
                        LINEA_BLANCA[2]);
            }
        }
    }

    private void farol(Renderer r, float fx, float fz, float angulo) {
        alumbrado.escala = ESCALA_FAROL;
        alumbrado.posX = fx;
        alumbrado.posZ = fz;
        alumbrado.angulo = angulo;
        alumbrado.dibujar(r);
    }

    // Intersecciones con semáforo {columna, fila}. Las demás solo llevan pasos de
    // cebra.
    // Para agregar o quitar semáforos basta editar esta lista (las intersecciones
    // están en múltiplos de 4).
    private static final int[][] SEMAFOROS = {
            { 4, 4 }, // junto al Kwik-E-Mart
            { 16, 4 }, // junto a la Mansión Burns
            { 4, 16 }, // junto a la casa de los Simpson
            { 8, 8 }, // esquinas opuestas de la plaza central
            { 12, 12 },
    };

    private boolean tieneSemaforo(int x, int z) {
        for (int[] p : SEMAFOROS) {
            if (p[0] == x && p[1] == z)
                return true;
        }
        return false;
    }

    /**
     * Ciclo de 16 s. El eje contrario va desfasado 8 s, con un breve instante en
     * rojo para ambos.
     */
    private int estadoSemaforo(float tiempo, boolean ejeNS) {
        float c = (tiempo + (ejeNS ? 0f : 8f)) % 16f;
        if (c < 6f)
            return 2; // verde
        if (c < 7.5f)
            return 1; // amarillo
        return 0; // rojo
    }

    /**
     * true si la celda que queda delante del lote (hacia donde mira su frente) es
     * una calle.
     */
    private boolean hayCalleAdelante(int x, int z, float ang) {
        if (ang == 0f)
            return esCalle(x, z + 1);
        if (ang == (float) (Math.PI / 2))
            return esCalle(x + 1, z);
        if (ang == (float) Math.PI)
            return esCalle(x, z - 1);
        return esCalle(x - 1, z);
    }

    private float obtenerAnguloHaciaCalle(int x, int z) {
        if (z < mapa.length - 1 && mapa[z + 1][x] == 0)
            return 0f;
        if (x < mapa[0].length - 1 && mapa[z][x + 1] == 0)
            return (float) (Math.PI / 2);
        if (z > 0 && mapa[z - 1][x] == 0)
            return (float) Math.PI;
        if (x > 0 && mapa[z][x - 1] == 0)
            return (float) (-Math.PI / 2);
        return 0f;
    }

    private void dibujarLoteYCasa(Renderer r, Lote lote, Object casa, float x, float z, float ang, boolean hayCalle,
            boolean esMinimapa) {
        lote.posX = x;
        lote.posZ = z;
        lote.angulo = ang;
        lote.buzon = hayCalle;
        float[] p = lote.posCasa();

        if (esMinimapa) {
            // ¡OPTIMIZACIÓN EXTREMA! Solo el bloque del césped y una caja proxy de la casa.
            r.cajaGirada(x, 0.1f, z, TAMANO_CELDA, 0.2f, TAMANO_CELDA, 0.22f, 0.45f, 0.25f, ang); // Césped base plano
            r.cajaGirada(p[0], 2.5f, p[1], 7f, 5f, 5f, 0.8f, 0.6f, 0.5f, ang); // Base proxy de la casa
            return;
        }

        if (casa instanceof CasaSimpson) {
            CasaSimpson cs = (CasaSimpson) casa;
            cs.posX = p[0];
            cs.posZ = p[1];
            cs.angulo = ang;
            lote.dibujar(r);
            cs.dibujar(r);
        } else if (casa instanceof CasaFlanders) {
            CasaFlanders cf = (CasaFlanders) casa;
            cf.posX = p[0];
            cf.posZ = p[1];
            cf.angulo = ang;
            lote.dibujar(r);
            cf.dibujar(r);
        } else if (casa instanceof CasaGenerica) {
            CasaGenerica cg = (CasaGenerica) casa;
            cg.posX = p[0];
            cg.posZ = p[1];
            cg.angulo = ang;
            lote.dibujar(r);
            cg.dibujar(r);
        }
    }

    public void resolverColisiones(Auto auto) {
        // En lugar de una esfera gigante, usamos 2 esferas pequeñas (Cápsula de
        // colisión)
        float radioAuto = 1.0f; // Ancho del auto
        float distanciaEjes = 1.7f; // Distancia del centro a los parachoques

        float sen = (float) Math.sin(auto.angulo);
        float cos = (float) Math.cos(auto.angulo);

        // 1. Esfera DELANTERA (Capó)
        float[] esferaDelantera = { auto.posX + sen * distanciaEjes, auto.posZ + cos * distanciaEjes };
        empujarEsfera(auto, esferaDelantera, radioAuto);

        // 2. Esfera TRASERA (Maletero). Usamos la nueva posX/Z en caso de que la
        // delantera ya lo haya empujado
        float[] esferaTrasera = { auto.posX - sen * distanciaEjes, auto.posZ - cos * distanciaEjes };
        empujarEsfera(auto, esferaTrasera, radioAuto);
    }

    private void empujarEsfera(Auto auto, float[] esfera, float radio) {
        int filas = mapa.length;
        int cols = mapa[0].length;
        int offsetX = (cols * TAMANO_CELDA) / 2;
        int offsetZ = (filas * TAMANO_CELDA) / 2;

        int colEsfera = (int) Math.floor((esfera[0] + offsetX) / TAMANO_CELDA);
        int filaEsfera = (int) Math.floor((esfera[1] + offsetZ) / TAMANO_CELDA);

        for (int z = filaEsfera - 1; z <= filaEsfera + 1; z++) {
            for (int x = colEsfera - 1; x <= colEsfera + 1; x++) {
                if (z < 0 || z >= filas || x < 0 || x >= cols) {
                    resolverContacto(auto, esfera, radio, x, z, offsetX, offsetZ);
                    continue;
                }
                int tipo = mapa[z][x];
                // Los parques, casas y tiendas son obstáculos
                if (tipo != 0) {
                    resolverContacto(auto, esfera, radio, x, z, offsetX, offsetZ);
                }
            }
        }
    }

    private void resolverContacto(Auto auto, float[] esfera, float radio, int celdaX, int celdaZ, int offsetX,
            int offsetZ) {
        float centroCeldaX = (celdaX * TAMANO_CELDA) - offsetX + (TAMANO_CELDA / 2.0f);
        float centroCeldaZ = (celdaZ * TAMANO_CELDA) - offsetZ + (TAMANO_CELDA / 2.0f);

        // ¡LA MAGIA AQUÍ! Expandimos la pared invisible para que cubra exactamente la
        // acera.
        // TAMANO_CELDA/2 es el límite de la casa/césped. Le sumamos ANCHO_ACERA (2.4f).
        float mitad = (TAMANO_CELDA / 2.0f) + ANCHO_ACERA;

        float cercanoX = Math.max(centroCeldaX - mitad, Math.min(esfera[0], centroCeldaX + mitad));
        float cercanoZ = Math.max(centroCeldaZ - mitad, Math.min(esfera[1], centroCeldaZ + mitad));

        float distanciaX = esfera[0] - cercanoX;
        float distanciaZ = esfera[1] - cercanoZ;
        float distanciaCuadrada = (distanciaX * distanciaX) + (distanciaZ * distanciaZ);

        if (distanciaCuadrada < radio * radio) {
            float distancia = (float) Math.sqrt(distanciaCuadrada);
            if (distancia == 0) {
                distanciaX = 1;
                distanciaZ = 0;
                distancia = 1;
            }
            float nx = distanciaX / distancia;
            float nz = distanciaZ / distancia;

            float superposicion = radio - distancia;
            float pushX = nx * superposicion;
            float pushZ = nz * superposicion;

            // 1. Sacamos físicamente al auto y a la esfera del muro
            auto.posX += pushX;
            auto.posZ += pushZ;
            esfera[0] += pushX;
            esfera[1] += pushZ;

            // 2. MATEMÁTICA MAGNÍFICA DE DESLIZAMIENTO (Producto Punto)
            float dirAutoX = (float) Math.sin(auto.angulo);
            float dirAutoZ = (float) Math.cos(auto.angulo);

            // impacto = 1.0f (Golpe completamente de frente/perpendicular a la pared)
            // impacto = 0.0f (Golpe rozando la pared en paralelo, de costado)
            float impacto = Math.abs(nx * dirAutoX + nz * dirAutoZ);

            // Si roza, conserva el 98% de la velocidad. Si choca de frente, conserva solo
            // el 10%
            float conservacion = 0.98f - (0.88f * impacto);
            auto.velocidad *= conservacion;
        }
    }
}
