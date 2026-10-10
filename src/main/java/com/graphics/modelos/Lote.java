package com.graphics.modelos;
import com.graphics.Renderer;
import java.util.ArrayList;
import java.util.List;

/**
 * Lote (terreno) para una casa: pasto, acera, camino a la puerta, cochera, cercas, árboles,
 * flores y buzón. La casa NO está dentro: se dibuja aparte, en la posición que da posCasa().
 *
 * El lote está centrado en (posX, posZ). Su frente (la calle) mira hacia +Z, igual que las casas.
 *
 * Uso:
 *   Lote lote = Lote.paraSimpson(x, z, angulo);
 *   float[] p = lote.posCasa();
 *   CasaSimpson casa = new CasaSimpson(p[0], p[1], lote.angulo);
 *   lote.dibujar(r);
 *   casa.dibujar(r);
 *
 * Nota: CasaSimpson y CasaFlanders no tienen escala, por eso "escala" del lote solo sirve de
 * forma coherente con CasaGenerica (que sí la tiene: casa.escala = lote.escala).
 */
public class Lote {
    public static final int SIN_CERCA = 0;
    public static final int CERCA_BLANCA = 1; // baja, de estacas
    public static final int CERCA_MADERA = 2; // alta, de paneles
    public static final int CERCA_HIERRO = 3; // barrotes con punta

    public float posX;
    public float posZ;
    public float angulo;
    public float escala = 1.0f;

    public float ancho; // en X (medida de diseño)
    public float fondo; // en Z (medida de diseño)

    // Tamaño REAL del terreno en el mundo (0 = usar ancho*escala / fondo*escala).
    // Con ajustarATerreno() el patio se estira para llenar la celda SIN agrandar casa, árboles ni cercas:
    // "escala" controla el tamaño de los objetos y el terreno controla cuánto pasto hay.
    public float anchoReal = 0;
    public float fondoReal = 0;

    public int cercaFrente = SIN_CERCA;
    public int cercaLados = SIN_CERCA;
    public boolean acera = true;
    public boolean buzon = true;
    public float buzonX = 0;

    // Posición de la casa dentro del lote (coordenadas locales del lote)
    public float casaX = 0;
    public float casaZ = 0;

    // Camino a la puerta (x local, z donde empieza junto a la casa, ancho)
    public boolean hayCamino = true;
    public float caminoX = 0, caminoZInicio = 0, caminoAncho = 1.5f;

    // Cochera (x local, z donde empieza, ancho)
    public boolean hayCochera = false;
    public float cocheraX = 0, cocheraZInicio = 0, cocheraAncho = 3.2f;

    private static class ArbolLote {
        float x, z, esc;
        Arbol arbol;
    }

    private final List<ArbolLote> arboles = new ArrayList<>();
    private final List<float[]> floresBeds = new ArrayList<>();  // x, z, ancho, fondo
    private final List<float[]> terrazas = new ArrayList<>();    // x, z, ancho, fondo

    public Lote(float posX, float posZ, float angulo, float ancho, float fondo) {
        this.posX = posX;
        this.posZ = posZ;
        this.angulo = angulo;
        this.ancho = ancho;
        this.fondo = fondo;
        this.buzonX = ancho / 2 - 1.2f;
    }

    // ------------------------------------------------------------ Contenido opcional
    /** tipo = tipo de Arbol (1..6); esc = escala relativa del árbol. */
    public Lote agregarArbol(float x, float z, int tipo, float esc) {
        ArbolLote a = new ArbolLote();
        a.x = x;
        a.z = z;
        a.esc = esc;
        a.arbol = new Arbol(0, 0, esc, tipo);
        arboles.add(a);
        return this;
    }

    public Lote agregarFlores(float x, float z, float anchoBed, float fondoBed) {
        floresBeds.add(new float[] { x, z, anchoBed, fondoBed });
        return this;
    }

    public Lote agregarTerraza(float x, float z, float anchoT, float fondoT) {
        terrazas.add(new float[] { x, z, anchoT, fondoT });
        return this;
    }

    /** Hace que el terreno mida exactamente anchoMundo x fondoMundo, sin cambiar el tamaño de los objetos. */
    public Lote ajustarATerreno(float anchoMundo, float fondoMundo) {
        this.anchoReal = anchoMundo;
        this.fondoReal = fondoMundo;
        return this;
    }

    private float fx() { return anchoReal > 0 ? anchoReal / ancho : escala; }
    private float fz() { return fondoReal > 0 ? fondoReal / fondo : escala; }

    // ------------------------------------------------------------ Posición de la casa
    /** Posición mundial {x, z} donde hay que poner la casa (usa lote.angulo como su ángulo). */
    public float[] posCasa() {
        return aMundo(casaX, casaZ);
    }

    // ------------------------------------------------------------ Presets
    public static Lote paraSimpson(float x, float z, float angulo) {
        Lote l = new Lote(x, z, angulo, 18f, 16f);
        l.casaX = -1.5f;
        l.casaZ = -2.5f;
        l.cercaFrente = SIN_CERCA;
        l.cercaLados = CERCA_MADERA;
        l.caminoX = -1.75f;
        l.caminoZInicio = 2.0f;
        l.hayCochera = true;
        l.cocheraX = 4.0f;
        l.cocheraZInicio = 1.3f;
        l.buzonX = -5.0f;
        l.agregarArbol(-6.8f, 4.5f, 1, 1.1f);   // el roble del jardín delantero
        l.agregarArbol(6.5f, -6.0f, 1, 1.0f);
        l.agregarFlores(-3.8f, 1.5f, 2.2f, 0.8f);
        l.agregarTerraza(-1.5f, -6.4f, 5.5f, 2.2f);
        return l;
    }

    public static Lote paraFlanders(float x, float z, float angulo) {
        Lote l = new Lote(x, z, angulo, 18f, 16f);
        l.casaX = -1.0f;
        l.casaZ = -2.5f;
        l.cercaFrente = CERCA_BLANCA;
        l.cercaLados = CERCA_BLANCA;
        l.caminoX = -1.5f;
        l.caminoZInicio = 2.0f;
        l.hayCochera = true;
        l.cocheraX = 3.75f;
        l.cocheraZInicio = 1.3f;
        l.buzonX = -6.0f;
        l.agregarArbol(-7.0f, 4.0f, 1, 1.0f);
        l.agregarArbol(7.2f, -5.5f, 5, 1.0f);
        l.agregarFlores(-4.5f, 1.6f, 2.8f, 0.8f);
        l.agregarFlores(1.5f, 1.6f, 2.2f, 0.8f);
        return l;
    }

    /** Lote de 9x9 para una CasaGenerica (cabe en una celda de la ciudad). Usa su estilo y semilla. */
    public static Lote paraGenerica(float x, float z, float angulo, CasaGenerica casa) {
        Lote l = new Lote(x, z, angulo, 9f, 9f);
        l.casaZ = -1.0f;
        float frente;
        float puertaX;
        switch (casa.estilo) {
            case 0: frente = 3.6f; puertaX = 0f; break;     // cottage con porche
            case 1: frente = 2.9f; puertaX = 1.5f; break;   // dos pisos
            case 2:
                frente = 3.1f;
                puertaX = 0.7f;
                l.casaX = -0.4f;
                l.hayCochera = true;
                l.cocheraX = 3.0f - 0.4f;
                l.cocheraZInicio = 3.0f - 1.0f;
                l.cocheraAncho = 2.6f;
                break;
            default: frente = 3.5f; puertaX = 0f; break;    // piramidal
        }
        l.caminoX = puertaX + l.casaX;
        l.caminoZInicio = frente + l.casaZ;
        l.caminoAncho = 1.2f;
        l.buzonX = 3.7f;
        int s = casa.variante;
        l.cercaFrente = (s % 3 == 0) ? CERCA_BLANCA : SIN_CERCA;
        l.cercaLados = (s % 2 == 0) ? CERCA_BLANCA : SIN_CERCA;
        l.agregarArbol(l.hayCochera ? -3.4f : 3.3f, 3.0f, (s % 2 == 0) ? 5 : 3, 0.75f);
        l.agregarFlores(l.hayCochera ? -1.2f : -2.6f, l.caminoZInicio + 0.1f, 1.8f, 0.6f);
        return l;
    }

    // ------------------------------------------------------------ Dibujo
    public void dibujar(Renderer r) {
        float[] pasto = { 0.35f, 0.65f, 0.28f };
        float[] pastoOsc = { 0.31f, 0.58f, 0.25f };
        float[] cemento = { 0.80f, 0.78f, 0.72f };
        float[] juntas = { 0.62f, 0.60f, 0.56f };
        float[] aceraCol = { 0.76f, 0.75f, 0.72f };

        float e = escala;
        float W = ancho * fx();          // ancho real del terreno
        float F = fondo * fz();          // fondo real del terreno
        float hx = W / 2, hz = F / 2;

        // Pasto con franjas de corte
        w(r, 0, 0.05f * e, 0, W, 0.1f * e, F, pasto, 0);
        for (float z = -hz + 0.6f * e; z < hz; z += 2.4f * e) {
            float len = Math.min(1.2f * e, hz - (z - 0.6f * e));
            w(r, 0, 0.105f * e, z - 0.6f * e + len / 2, W, 0.01f * e, len, pastoOsc, 0);
        }

        // Acera frente al lote (si la calle ya trae su propia acera, desactívala con acera = false)
        if (acera) {
            w(r, 0, 0.06f * e, hz + 0.5f * e, W, 0.12f * e, 1.0f * e, aceraCol, 0);
            w(r, 0, 0.13f * e, hz + 1.02f * e, W, 0.16f * e, 0.1f * e, juntas, 0);
            for (float x = -hx + 2 * e; x < hx; x += 2 * e) {
                w(r, x, 0.125f * e, hz + 0.5f * e, 0.05f * e, 0.01f * e, 1.0f * e, juntas, 0);
            }
        }

        // Camino a la puerta (posición relativa a la casa, para que siempre toque la puerta)
        float camX = casaRelX(caminoX);
        float camZ0 = casaRelZ(caminoZInicio);
        float camAncho = caminoAncho * e;
        if (hayCamino) {
            float largo = hz - camZ0;
            if (largo > 0) {
                w(r, camX, 0.13f * e, camZ0 + largo / 2, camAncho, 0.06f * e, largo, cemento, 0);
                for (float z = camZ0 + e; z < hz; z += e) {
                    w(r, camX, 0.165f * e, z, camAncho, 0.01f * e, 0.05f * e, juntas, 0);
                }
            }
        }

        // Cochera
        float cocX = casaRelX(cocheraX);
        float cocZ0 = casaRelZ(cocheraZInicio);
        float cocAncho = cocheraAncho * e;
        if (hayCochera) {
            float largo = hz - cocZ0;
            if (largo > 0) {
                w(r, cocX, 0.13f * e, cocZ0 + largo / 2, cocAncho, 0.06f * e, largo, cemento, 0);
                for (float z = cocZ0 + 1.2f * e; z < hz; z += 1.2f * e) {
                    w(r, cocX, 0.165f * e, z, cocAncho, 0.01f * e, 0.05f * e, juntas, 0);
                }
                if (acera) {
                    w(r, cocX, 0.14f * e, hz + 0.5f * e, cocAncho + 0.4f * e, 0.1f * e, 1.0f * e, cemento, 0);
                }
            }
        }

        // Terrazas de madera
        float[] madera = { 0.62f, 0.42f, 0.24f };
        float[] maderaOsc = { 0.52f, 0.34f, 0.19f };
        for (float[] t : terrazas) {
            pieza(r, t[0], 0.17f, t[1], t[2], 0.14f, t[3], madera, 0);
            for (float dx = -t[2] / 2 + 0.4f; dx < t[2] / 2; dx += 0.8f) {
                pieza(r, t[0] + dx, 0.245f, t[1], 0.04f, 0.01f, t[3], maderaOsc, 0);
            }
        }

        // Flores
        for (float[] f : floresBeds) {
            dibujarFlores(r, f[0], f[1], f[2], f[3]);
        }

        // Cercas (en unidades de mundo)
        dibujarCercas(r, hx, hz, camX, camAncho, cocX, cocAncho);

        // Buzón junto al borde del frente
        if (buzon) {
            dibujarBuzon(r, buzonX * fx(), hz - 0.6f * e);
        }

        // Árboles
        for (ArbolLote a : arboles) {
            float[] m = aMundo(a.x, a.z);
            a.arbol.posX = m[0];
            a.arbol.posZ = m[1];
            a.arbol.angulo = angulo;
            a.arbol.escala = a.esc * escala;
            a.arbol.dibujar(r);
        }
    }

    /** x de un elemento anclado a la casa (puerta, garaje): la casa se coloca con fx, su contenido con escala. */
    private float casaRelX(float xNominal) {
        return casaX * fx() + (xNominal - casaX) * escala;
    }

    private float casaRelZ(float zNominal) {
        return casaZ * fz() + (zNominal - casaZ) * escala;
    }

    private void dibujarFlores(Renderer r, float x, float z, float w, float d) {
        float[] muro = { 0.60f, 0.58f, 0.54f };
        float[] tierra = { 0.35f, 0.22f, 0.12f };
        float[] hoja = { 0.16f, 0.50f, 0.18f };
        float[][] flores = { { 0.95f, 0.25f, 0.30f }, { 1.0f, 0.85f, 0.2f }, { 0.95f, 0.5f, 0.75f },
                { 1f, 1f, 1f }, { 0.6f, 0.4f, 0.9f } };
        pieza(r, x, 0.2f, z, w, 0.2f, d, muro, 0);
        pieza(r, x, 0.27f, z, w - 0.2f, 0.12f, d - 0.2f, tierra, 0);
        pieza(r, x, 0.36f, z, w - 0.25f, 0.08f, d - 0.25f, hoja, 0);
        int k = 0;
        for (float dx = -w / 2 + 0.35f; dx <= w / 2 - 0.3f; dx += 0.5f) {
            float[] c = flores[k++ % flores.length];
            pieza(r, x + dx, 0.5f, z + ((k % 2 == 0) ? 0.1f : -0.1f), 0.18f, 0.2f, 0.18f, c, 0.4f * k);
        }
    }

    /** x, z en unidades de mundo (relativas al centro del lote). */
    private void dibujarBuzon(Renderer r, float x, float z) {
        float e = escala;
        float[] poste = { 0.40f, 0.28f, 0.18f };
        float[] caja = { 0.15f, 0.25f, 0.65f };
        float[] bandera = { 0.9f, 0.15f, 0.15f };
        w(r, x, 0.65f * e, z, 0.1f * e, 1.1f * e, 0.1f * e, poste, 0);
        w(r, x, 1.3f * e, z, 0.35f * e, 0.3f * e, 0.6f * e, caja, 0);
        w(r, x, 1.5f * e, z, 0.3f * e, 0.1f * e, 0.5f * e, caja, 0);
        w(r, x + 0.2f * e, 1.45f * e, z + 0.1f * e, 0.05f * e, 0.3f * e, 0.05f * e, bandera, 0);
        w(r, x + 0.2f * e, 1.6f * e, z + 0.1f * e, 0.05f * e, 0.1f * e, 0.2f * e, bandera, 0);
    }

    private void dibujarCercas(Renderer r, float hx, float hz, float camX, float camAncho,
            float cocX, float cocAncho) {
        float e = escala;
        if (cercaLados != SIN_CERCA) {
            cerca(r, cercaLados, -hx, hx, -hz, -hz);
            cerca(r, cercaLados, -hx, -hx, -hz, hz);
            cerca(r, cercaLados, hx, hx, -hz, hz);
        }
        if (cercaFrente != SIN_CERCA) {
            List<float[]> huecos = new ArrayList<>();
            if (hayCamino) huecos.add(new float[] { camX - camAncho / 2 - 0.2f * e, camX + camAncho / 2 + 0.2f * e });
            if (hayCochera) huecos.add(new float[] { cocX - cocAncho / 2 - 0.2f * e, cocX + cocAncho / 2 + 0.2f * e });
            huecos.sort((a, b) -> Float.compare(a[0], b[0]));
            float cursor = -hx;
            for (float[] h : huecos) {
                if (h[0] > cursor) cerca(r, cercaFrente, cursor, h[0], hz, hz);
                cursor = Math.max(cursor, h[1]);
            }
            if (cursor < hx) cerca(r, cercaFrente, cursor, hx, hz, hz);
        }
    }

    /** Segmento de cerca alineado a un eje. Todo en unidades de mundo: los postes miden lo mismo
     *  aunque el terreno sea más grande (solo se añaden más postes). */
    private void cerca(Renderer r, int tipo, float x1, float x2, float z1, float z2) {
        float e = escala;
        boolean sobreX = Math.abs(x2 - x1) >= Math.abs(z2 - z1);
        float largo = sobreX ? Math.abs(x2 - x1) : Math.abs(z2 - z1);
        if (largo < 0.1f * e) return;
        float cx = (x1 + x2) / 2, cz = (z1 + z2) / 2;

        float[] blanco = { 0.95f, 0.95f, 0.93f };
        float[] madera = { 0.55f, 0.37f, 0.20f };
        float[] maderaOsc = { 0.45f, 0.30f, 0.16f };
        float[] hierro = { 0.08f, 0.08f, 0.10f };

        if (tipo == CERCA_MADERA) {
            int n = Math.max(1, (int) (largo / (1.0f * e)));
            float paso = largo / n;
            for (int i = 0; i < n; i++) {
                float px = sobreX ? Math.min(x1, x2) + paso * (i + 0.5f) : cx;
                float pz = sobreX ? cz : Math.min(z1, z2) + paso * (i + 0.5f);
                float[] c = (i % 2 == 0) ? madera : maderaOsc;
                w(r, px, 0.95f * e, pz, sobreX ? paso : 0.14f * e, 1.7f * e, sobreX ? 0.14f * e : paso, c, 0);
                w(r, px, 1.85f * e, pz, sobreX ? paso : 0.2f * e, 0.08f * e, sobreX ? 0.2f * e : paso, maderaOsc, 0);
            }
            return;
        }

        float alto = ((tipo == CERCA_HIERRO) ? 1.8f : 0.8f) * e;
        float sep = ((tipo == CERCA_HIERRO) ? 0.5f : 0.4f) * e;
        float[] col = (tipo == CERCA_HIERRO) ? hierro : blanco;
        float grosor = ((tipo == CERCA_HIERRO) ? 0.1f : 0.12f) * e;

        if (sobreX) {
            w(r, cx, alto * 0.3f + 0.1f * e, cz, largo, 0.08f * e, 0.06f * e, col, 0);
            w(r, cx, alto * 0.8f, cz, largo, 0.08f * e, 0.06f * e, col, 0);
        } else {
            w(r, cx, alto * 0.3f + 0.1f * e, cz, 0.06f * e, 0.08f * e, largo, col, 0);
            w(r, cx, alto * 0.8f, cz, 0.06f * e, 0.08f * e, largo, col, 0);
        }
        int n = Math.max(1, (int) (largo / sep));
        for (int i = 0; i <= n; i++) {
            float t = (float) i / n;
            float px = sobreX ? x1 + (x2 - x1) * t : cx;
            float pz = sobreX ? cz : z1 + (z2 - z1) * t;
            w(r, px, alto / 2 + 0.1f * e, pz, grosor, alto, grosor, col, 0);
            w(r, px, alto + 0.17f * e, pz, grosor + 0.04f * e, 0.14f * e, grosor + 0.04f * e, col, 0);
        }
    }

    // ------------------------------------------------------------ Transformación a mundo
    /** Posición de diseño del lote -> mundo (la posición se estira con el terreno). */
    private float[] aMundo(float lx, float lz) {
        return rotar(lx * fx(), lz * fz());
    }

    /** Desplazamiento ya en unidades de mundo -> posición mundial rotada por el ángulo del lote. */
    private float[] rotar(float ox, float oz) {
        float c = (float) Math.cos(angulo), s = (float) Math.sin(angulo);
        return new float[] { posX + c * ox + s * oz, posZ - s * ox + c * oz };
    }

    /** Pieza en coordenadas de DISEÑO: la posición se estira con el terreno, el tamaño usa "escala". */
    private void pieza(Renderer r, float x, float y, float z, float sx, float sy, float sz,
            float[] col, float giro) {
        float e = escala;
        w(r, x * fx(), y * e, z * fz(), sx * e, sy * e, sz * e, col, giro);
    }

    /** Pieza con todo ya en unidades de mundo (offset desde el centro del lote). */
    private void w(Renderer r, float ox, float y, float oz, float sx, float sy, float sz,
            float[] col, float giro) {
        float[] m = rotar(ox, oz);
        r.cajaInclinada(m[0], y, m[1], sx, sy, sz, col[0], col[1], col[2], angulo + giro, 0, 0);
    }
}
