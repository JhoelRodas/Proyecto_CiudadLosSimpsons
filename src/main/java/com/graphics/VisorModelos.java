package com.graphics;
import com.graphics.modelos.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Visor de Modelos 3D (Estilo Menú de Simpsons Hit & Run)
 * Navegación: flechas del teclado (izq/der), A/D, o clic en las flechas de los
 * lados de la ventana.
 * Para agregar un modelo nuevo basta una línea en registrarModelos().
 * Ejecutar con: mvn compile exec:exec -DmainClass=com.graphics.VisorModelos
 */
public class VisorModelos {

    private long ventana;
    private int ancho = 1200;
    private int alto = 600;

    /**
     * Un modelo mostrable: nombre, distancia de cámara y cómo dibujarlo dado el
     * ángulo de giro.
     */
    private static class Entrada {
        final String nombre;
        final float distancia;
        final BiConsumer<Renderer, Float> dibujo;

        Entrada(String nombre, float distancia, BiConsumer<Renderer, Float> dibujo) {
            this.nombre = nombre;
            this.distancia = distancia;
            this.dibujo = dibujo;
        }
    }

    private Renderer renderer;
    private final List<Entrada> modelos = new ArrayList<>();
    private int actual = 0;
    private float angulo = 0;

    // Estado para detectar "recién presionado"
    private boolean izqPrev, derPrev, clicPrev;

    /**
     * AQUÍ se agregan los modelos nuevos. El ángulo `ang` es el giro automático.
     */
    private void registrarModelos() {
        String[] nombresArbol = { "Roble", "Pino", "Abedul", "Palmera", "Cerezo", "Ciprés" };
        for (int i = 0; i < nombresArbol.length; i++) {
            Arbol arbol = new Arbol(0, 0, 1.0f, i + 1);
            registrar("Árbol: " + nombresArbol[i], 12f, (r, ang) -> {
                arbol.angulo = ang;
                arbol.dibujar(r);
            });
        }

        Auto auto = new Auto();
        auto.posX = 0f;
        auto.posZ = 0f;
        registrar("Auto", 14f, (r, ang) -> {
            auto.angulo = ang;
            auto.dibujar(r);
        });

        Semaforo semaforo = new Semaforo(0, 0, 0);
        registrar("Semáforo", 10f, (r, ang) -> {
            semaforo.angulo = ang;
            // Ciclo: verde -> amarillo -> rojo
            float ciclo = ang % 7f;
            semaforo.estado = ciclo < 3f ? 2 : (ciclo < 4f ? 1 : 0);
            semaforo.dibujar(r);
        });

        Alumbrado alumbrado = new Alumbrado(0, 0, 0);
        registrar("Alumbrado público", 10f, (r, ang) -> {
            alumbrado.angulo = ang;
            alumbrado.dibujar(r);
        });

        Plaza plaza = new Plaza(0, 0, 0, 2.0f);
        registrar("Plaza de Springfield", 26f, (r, ang) -> {
            plaza.angulo = ang;
            plaza.dibujar(r);
        });

        String[] nombresGenericas = { "Cottage", "Dos pisos", "Moderna", "Piramidal" };
        for (int i = 0; i < nombresGenericas.length; i++) {
            CasaGenerica generica = new CasaGenerica(0, 0, 0, i, i * 2 + 1);
            registrar("Casa genérica: " + nombresGenericas[i], 16f, (r, ang) -> {
                generica.angulo = ang;
                generica.dibujar(r);
            });
        }

        KwikEMart kwik = new KwikEMart(0, 0, 0);
        registrar("Kwik-E-Mart", 18f, (r, ang) -> {
            kwik.angulo = ang;
            kwik.dibujar(r);
        });

        MansionBurns burns = new MansionBurns(0, 0, 0);
        registrar("Mansión del Sr. Burns", 38f, (r, ang) -> {
            burns.angulo = ang;
            burns.dibujar(r);
        });

        // ---- Casas con su lote (patio) ----
        Lote loteSimpson = Lote.paraSimpson(0, 0, 0);
        CasaSimpson simpsonConPatio = new CasaSimpson(0, 0, 0);
        registrar("Casa Simpson + patio", 30f, (r, ang) -> {
            loteSimpson.angulo = ang;
            float[] p = loteSimpson.posCasa();
            simpsonConPatio.posX = p[0];
            simpsonConPatio.posZ = p[1];
            simpsonConPatio.angulo = ang;
            loteSimpson.dibujar(r);
            simpsonConPatio.dibujar(r);
        });

        Lote loteFlanders = Lote.paraFlanders(0, 0, 0);
        CasaFlanders flandersConPatio = new CasaFlanders(0, 0, 0);
        registrar("Casa Flanders + patio", 30f, (r, ang) -> {
            loteFlanders.angulo = ang;
            float[] p = loteFlanders.posCasa();
            flandersConPatio.posX = p[0];
            flandersConPatio.posZ = p[1];
            flandersConPatio.angulo = ang;
            loteFlanders.dibujar(r);
            flandersConPatio.dibujar(r);
        });

        for (int i = 0; i < 4; i++) {
            CasaGenerica cg = new CasaGenerica(0, 0, 0, i, i * 2 + 1);
            Lote lg = Lote.paraGenerica(0, 0, 0, cg);
            registrar("Casa genérica + patio " + (i + 1), 18f, (r, ang) -> {
                lg.angulo = ang;
                float[] p = lg.posCasa();
                cg.posX = p[0];
                cg.posZ = p[1];
                cg.angulo = ang;
                lg.dibujar(r);
                cg.dibujar(r);
            });
        }

        CasaSimpson simpson = new CasaSimpson(0, 0, 0);
        registrar("Casa Simpson", 18f, (r, ang) -> {
            simpson.angulo = ang;
            simpson.dibujar(r);
        });

        CasaFlanders flanders = new CasaFlanders(0, 0, 0);
        registrar("Casa Flanders", 18f, (r, ang) -> {
            flanders.angulo = ang;
            flanders.dibujar(r);
        });
    }

    private void registrar(String nombre, float distancia, BiConsumer<Renderer, Float> dibujo) {
        modelos.add(new Entrada(nombre, distancia, dibujo));
    }

    public void run() {
        iniciar();

        renderer = new Renderer();
        registrarModelos();
        actualizarTitulo();

        loop();

        glfwDestroyWindow(ventana);
        glfwTerminate();
    }

    private void cambiar(int delta) {
        actual = (actual + delta + modelos.size()) % modelos.size();
        actualizarTitulo();
    }

    private void actualizarTitulo() {
        glfwSetWindowTitle(ventana, "Visor de Modelos 3D - " + modelos.get(actual).nombre
                + " (" + (actual + 1) + "/" + modelos.size() + ")  [<- -> o clic en las flechas]");
    }

    /** Flecha hecha de cajas apuntando a +X (dir = 1) o -X (dir = -1). */
    private void dibujarFlecha(float x, float s, int dir, boolean resaltada) {
        float c = resaltada ? 0.95f : 0.35f;
        float r = c, g = resaltada ? 0.75f : c, b = resaltada ? 0.1f : c + 0.1f;
        float y = 1.5f * s;
        renderer.caja(x - dir * 0.5f * s, y, 0, 1.2f * s, 0.45f * s, 0.45f * s, r, g, b);
        renderer.caja(x + dir * 0.35f * s, y, 0, 0.35f * s, 1.7f * s, 0.45f * s, r, g, b);
        renderer.caja(x + dir * 0.65f * s, y, 0, 0.3f * s, 1.1f * s, 0.45f * s, r, g, b);
        renderer.caja(x + dir * 0.9f * s, y, 0, 0.25f * s, 0.5f * s, 0.45f * s, r, g, b);
    }

    private void iniciar() {
        if (!glfwInit())
            throw new RuntimeException("No se pudo inicializar GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        ventana = glfwCreateWindow(ancho, alto, "Visor de Modelos 3D", 0, 0);
        glfwMakeContextCurrent(ventana);
        glfwSwapInterval(1);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    private void loop() {
        // Fondo blanco
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        int[] w = new int[1], h = new int[1];
        double[] mx = new double[1], my = new double[1];

        while (!glfwWindowShouldClose(ventana)) {
            if (glfwGetKey(ventana, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                glfwSetWindowShouldClose(ventana, true);
            }

            glfwGetWindowSize(ventana, w, h);
            glfwGetCursorPos(ventana, mx, my);
            float fx = w[0] > 0 ? (float) (mx[0] / w[0]) : 0.5f;
            boolean sobreIzq = fx < 0.15f;
            boolean sobreDer = fx > 0.85f;

            boolean izq = glfwGetKey(ventana, GLFW_KEY_LEFT) == GLFW_PRESS
                    || glfwGetKey(ventana, GLFW_KEY_A) == GLFW_PRESS;
            boolean der = glfwGetKey(ventana, GLFW_KEY_RIGHT) == GLFW_PRESS
                    || glfwGetKey(ventana, GLFW_KEY_D) == GLFW_PRESS;
            boolean clic = glfwGetMouseButton(ventana, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;

            if (izq && !izqPrev)
                cambiar(-1);
            if (der && !derPrev)
                cambiar(1);
            if (clic && !clicPrev) {
                if (sobreIzq)
                    cambiar(-1);
                else if (sobreDer)
                    cambiar(1);
            }
            izqPrev = izq;
            derPrev = der;
            clicPrev = clic;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderer.prepararFrame();

            Entrada e = modelos.get(actual);
            float d = e.distancia;
            renderer.actualizarCamara(0, d * 0.42f, d, 0, d * 0.08f, 0, (float) ancho / alto);

            // El modelo gira automáticamente poco a poco
            angulo += 0.015f;

            // Plataforma proporcional a la distancia de cámara
            float s = d / 12f;
            renderer.caja(0, -0.2f, 0, 8f * s, 0.4f, 8f * s, 0.1f, 0.1f, 0.15f);

            e.dibujo.accept(renderer, angulo);

            // Flechas de navegación a los lados
            dibujarFlecha(-6.2f * s, s, -1, sobreIzq);
            dibujarFlecha(6.2f * s, s, 1, sobreDer);

            glfwSwapBuffers(ventana);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new VisorModelos().run();
    }
}
