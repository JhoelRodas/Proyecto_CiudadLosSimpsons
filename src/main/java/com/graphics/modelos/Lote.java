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

    public float ancho; // en X
    public float fondo; // en Z

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

        float hx = ancho / 2, hz = fondo / 2;

        // Pasto con franjas de corte
        pieza(r, 0, 0.05f, 0, ancho, 0.1f, fondo, pasto, 0);
        for (float z = -hz + 0.6f; z < hz; z += 2.4f) {
            float len = Math.min(1.2f, hz - (z - 0.6f));
            pieza(r, 0, 0.105f, z - 0.6f + len / 2, ancho, 0.01f, len, pastoOsc, 0);
        }

        // Acera frente al lote
        if (acera) {
            pieza(r, 0, 0.06f, hz + 0.5f, ancho, 0.12f, 1.0f, aceraCol, 0);
            pieza(r, 0, 0.13f, hz + 1.02f, ancho, 0.16f, 0.1f, juntas, 0); // cordón
            for (float x = -hx + 2; x < hx; x += 2) {
                pieza(r, x, 0.125f, hz + 0.5f, 0.05f, 0.01f, 1.0f, juntas, 0);
            }
        }

        // Camino a la puerta
        if (hayCamino) {
            float largo = hz - caminoZInicio;
            if (largo > 0) {
                pieza(r, caminoX, 0.13f, caminoZInicio + largo / 2, caminoAncho, 0.06f, largo, cemento, 0);
                for (float z = caminoZInicio + 1; z < hz; z += 1.0f) {
                    pieza(r, caminoX, 0.165f, z, caminoAncho, 0.01f, 0.05f, juntas, 0);
                }
            }
        }

        // Cochera
        if (hayCochera) {
            float largo = hz - cocheraZInicio;
            if (largo > 0) {
                pieza(r, cocheraX, 0.13f, cocheraZInicio + largo / 2, cocheraAncho, 0.06f, largo, cemento, 0);
                for (float z = cocheraZInicio + 1.2f; z < hz; z += 1.2f) {
                    pieza(r, cocheraX, 0.165f, z, cocheraAncho, 0.01f, 0.05f, juntas, 0);
                }
                // Rampa de acceso en la acera
                if (acera) {
                    pieza(r, cocheraX, 0.14f, hz + 0.5f, cocheraAncho + 0.4f, 0.1f, 1.0f, cemento, 0);
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

        // Cercas
        dibujarCercas(r);

        // Buzón
        if (buzon) {
            dibujarBuzon(r, buzonX, hz - 0.6f);
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

    private void dibujarBuzon(Renderer r, float x, float z) {
        float[] poste = { 0.40f, 0.28f, 0.18f };
        float[] caja = { 0.15f, 0.25f, 0.65f };
        float[] bandera = { 0.9f, 0.15f, 0.15f };
        pieza(r, x, 0.65f, z, 0.1f, 1.1f, 0.1f, poste, 0);
        pieza(r, x, 1.3f, z, 0.35f, 0.3f, 0.6f, caja, 0);
        pieza(r, x, 1.5f, z, 0.3f, 0.1f, 0.5f, caja, 0);
        pieza(r, x + 0.2f, 1.45f, z + 0.1f, 0.05f, 0.3f, 0.05f, bandera, 0);
        pieza(r, x + 0.2f, 1.6f, z + 0.1f, 0.05f, 0.1f, 0.2f, bandera, 0);
    }

    private void dibujarCercas(Renderer r) {
        float hx = ancho / 2, hz = fondo / 2;
        // Fondo y laterales
        if (cercaLados != SIN_CERCA) {
            cerca(r, cercaLados, -hx, hx, -hz, -hz);        // fondo
            cerca(r, cercaLados, -hx, -hx, -hz, hz);        // lateral izquierdo
            cerca(r, cercaLados, hx, hx, -hz, hz);          // lateral derecho
        }
        // Frente, con aberturas para camino y cochera
        if (cercaFrente != SIN_CERCA) {
            List<float[]> huecos = new ArrayList<>();
            if (hayCamino) huecos.add(new float[] { caminoX - caminoAncho / 2 - 0.2f, caminoX + caminoAncho / 2 + 0.2f });
            if (hayCochera) huecos.add(new float[] { cocheraX - cocheraAncho / 2 - 0.2f, cocheraX + cocheraAncho / 2 + 0.2f });
            huecos.sort((a, b) -> Float.compare(a[0], b[0]));
            float cursor = -hx;
            for (float[] h : huecos) {
                if (h[0] > cursor) cerca(r, cercaFrente, cursor, h[0], hz, hz);
                cursor = Math.max(cursor, h[1]);
            }
            if (cursor < hx) cerca(r, cercaFrente, cursor, hx, hz, hz);
        }
    }

    /** Segmento de cerca alineado a un eje. */
    private void cerca(Renderer r, int tipo, float x1, float x2, float z1, float z2) {
        boolean sobreX = Math.abs(x2 - x1) >= Math.abs(z2 - z1);
        float largo = sobreX ? Math.abs(x2 - x1) : Math.abs(z2 - z1);
        if (largo < 0.1f) return;
        float cx = (x1 + x2) / 2, cz = (z1 + z2) / 2;

        float[] blanco = { 0.95f, 0.95f, 0.93f };
        float[] madera = { 0.55f, 0.37f, 0.20f };
        float[] maderaOsc = { 0.45f, 0.30f, 0.16f };
        float[] hierro = { 0.08f, 0.08f, 0.10f };

        if (tipo == CERCA_MADERA) {
            // Paneles verticales alternados, con riel superior
            int n = Math.max(1, (int) (largo / 1.0f));
            float paso = largo / n;
            for (int i = 0; i < n; i++) {
                float px = sobreX ? Math.min(x1, x2) + paso * (i + 0.5f) : cx;
                float pz = sobreX ? cz : Math.min(z1, z2) + paso * (i + 0.5f);
                float[] c = (i % 2 == 0) ? madera : maderaOsc;
                pieza(r, px, 0.95f, pz, sobreX ? paso : 0.14f, 1.7f, sobreX ? 0.14f : paso, c, 0);
                pieza(r, px, 1.85f, pz, sobreX ? paso : 0.2f, 0.08f, sobreX ? 0.2f : paso, maderaOsc, 0);
            }
            return;
        }

        float alto = (tipo == CERCA_HIERRO) ? 1.8f : 0.8f;
        float sep = (tipo == CERCA_HIERRO) ? 0.5f : 0.4f;
        float[] col = (tipo == CERCA_HIERRO) ? hierro : blanco;
        float grosor = (tipo == CERCA_HIERRO) ? 0.1f : 0.12f;

        // Rieles
        if (sobreX) {
            pieza(r, cx, alto * 0.3f + 0.1f, cz, largo, 0.08f, 0.06f, col, 0);
            pieza(r, cx, alto * 0.8f, cz, largo, 0.08f, 0.06f, col, 0);
        } else {
            pieza(r, cx, alto * 0.3f + 0.1f, cz, 0.06f, 0.08f, largo, col, 0);
            pieza(r, cx, alto * 0.8f, cz, 0.06f, 0.08f, largo, col, 0);
        }
        int n = Math.max(1, (int) (largo / sep));
        for (int i = 0; i <= n; i++) {
            float t = (float) i / n;
            float px = sobreX ? x1 + (x2 - x1) * t : cx;
            float pz = sobreX ? cz : z1 + (z2 - z1) * t;
            pieza(r, px, alto / 2 + 0.1f, pz, grosor, alto, grosor, col, 0);
            pieza(r, px, alto + 0.17f, pz, grosor + 0.04f, 0.14f, grosor + 0.04f, col, 0);
        }
    }

    // ------------------------------------------------------------ Transformación a mundo
    private float[] aMundo(float lx, float lz) {
        float c = (float) Math.cos(angulo), s = (float) Math.sin(angulo);
        lx *= escala;
        lz *= escala;
        return new float[] { posX + c * lx + s * lz, posZ - s * lx + c * lz };
    }

    private void pieza(Renderer r, float x, float y, float z, float sx, float sy, float sz,
            float[] col, float giro) {
        float[] m = aMundo(x, z);
        r.cajaInclinada(m[0], y * escala, m[1], sx * escala, sy * escala, sz * escala,
                col[0], col[1], col[2], angulo + giro, 0, 0);
    }
}
