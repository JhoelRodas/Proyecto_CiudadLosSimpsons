package com.graphics;

import com.graphics.modelos.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import org.lwjgl.opengl.GL;

public class App {

    private long ventana;
    private int ancho = 1200;
    private int alto = 720;

    // Nuestros manejadores organizados
    private Renderer renderer;
    private Ciudad ciudad;
    private Auto auto;
    private HUD hud;

    public void run() {
        iniciar();

        // Inicializamos los componentes DESPUÉS de GL.createCapabilities()
        renderer = new Renderer();
        ciudad = new Ciudad();
        auto = new AutoCohete();
        hud = new HUD();

        loop();

        // Cuando termine el loop (al apretar ESC), limpiamos memoria
        glfwDestroyWindow(ventana);
        glfwTerminate();
    }

    private void iniciar() {
        if (!glfwInit()) {
            throw new RuntimeException("No se pudo inicializar GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        ventana = glfwCreateWindow(ancho, alto, "Springfield City", 0, 0);
        if (ventana == 0) {
            throw new RuntimeException("No se pudo crear la ventana");
        }

        glfwMakeContextCurrent(ventana);
        glfwSwapInterval(1); // Activar V-Sync

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    private float camaraAngulo = 0; // Guardar el ángulo actual de la cámara
    private boolean mostrarMapa = true; // Control del minimapa
    private boolean mPresionada = false; // Evitar rebotes de la tecla M

    // Controles de Iluminación
    private boolean esNoche = true; // Arranca de noche (N alterna)
    private boolean nPresionada = false;
    private boolean farosEncendidos = true; // F alterna
    private boolean fPresionada = false;
    
    // Misiones de entrega
    private final Misiones misiones = new Misiones();

    private void loop() {
        glClearColor(0.35f, 0.60f, 0.90f, 1.0f);

        // Reloj del sistema para el DeltaTime
        double tiempoAnterior = glfwGetTime();

        while (!glfwWindowShouldClose(ventana)) {
            // --- ACTUALIZAR TAMAÑO DE VENTANA ---
            int[] pAncho = new int[1], pAlto = new int[1];
            glfwGetFramebufferSize(ventana, pAncho, pAlto);
            ancho = pAncho[0];
            alto = pAlto[0];

            // 1. Calcular el DeltaTime
            double tiempoActual = glfwGetTime();
            float deltaTime = (float) Math.min(tiempoActual - tiempoAnterior, 0.05);
            tiempoAnterior = tiempoActual;

            if (glfwGetKey(ventana, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                glfwSetWindowShouldClose(ventana, true);
            }

            // --- TECLA M (MINIMAPA) ---
            boolean mActual = glfwGetKey(ventana, org.lwjgl.glfw.GLFW.GLFW_KEY_M) == GLFW_PRESS;
            if (mActual && !mPresionada)
                mostrarMapa = !mostrarMapa;
            mPresionada = mActual;

            // --- TECLA N (DÍA/NOCHE) ---
            boolean nActual = glfwGetKey(ventana, org.lwjgl.glfw.GLFW.GLFW_KEY_N) == GLFW_PRESS;
            if (nActual && !nPresionada)
                esNoche = !esNoche;
            nPresionada = nActual;

            // --- TECLA F (FAROS) ---
            boolean fActual = glfwGetKey(ventana, org.lwjgl.glfw.GLFW.GLFW_KEY_F) == GLFW_PRESS;
            if (fActual && !fPresionada)
                farosEncendidos = !farosEncendidos;
            fPresionada = fActual;
            // 2. Leer estado del teclado de conducción
            boolean acelerar = glfwGetKey(ventana, GLFW_KEY_UP) == GLFW_PRESS
                    || glfwGetKey(ventana, GLFW_KEY_W) == GLFW_PRESS;
            boolean frenar = glfwGetKey(ventana, GLFW_KEY_DOWN) == GLFW_PRESS
                    || glfwGetKey(ventana, GLFW_KEY_S) == GLFW_PRESS;
            boolean izq = glfwGetKey(ventana, GLFW_KEY_LEFT) == GLFW_PRESS
                    || glfwGetKey(ventana, GLFW_KEY_A) == GLFW_PRESS;
            boolean der = glfwGetKey(ventana, GLFW_KEY_RIGHT) == GLFW_PRESS
                    || glfwGetKey(ventana, GLFW_KEY_D) == GLFW_PRESS;

            // Funcionalidad de reinicio
            if (glfwGetKey(ventana, org.lwjgl.glfw.GLFW.GLFW_KEY_R) == GLFW_PRESS) {
                auto.posX = 0;
                auto.posZ = 28f;
                auto.angulo = 0;
                auto.velocidad = 0;
                camaraAngulo = 0;
                misiones.reiniciar();
            }

            // 3. Mover el auto
            auto.actualizarFisicas(deltaTime, acelerar, frenar, izq, der);
            ciudad.resolverColisiones(auto);
            
            misiones.actualizar(deltaTime, auto.posX, auto.posZ, auto.velocidad);

            // 4. Configurar Cámara
            float distCamara = 8.5f;
            float alturaCamara = 3.8f;

            float diferenciaAngulo = auto.angulo - camaraAngulo;
            while (diferenciaAngulo > Math.PI)
                diferenciaAngulo -= Math.PI * 2;
            while (diferenciaAngulo < -Math.PI)
                diferenciaAngulo += Math.PI * 2;

            camaraAngulo += diferenciaAngulo * 5.0f * deltaTime;

            float ojoX = auto.posX - (float) Math.sin(camaraAngulo) * distCamara;
            float ojoZ = auto.posZ - (float) Math.cos(camaraAngulo) * distCamara;

            // =========================================================
            // 5. DIBUJAR VISTA PRINCIPAL (Perspectiva)
            // =========================================================
            org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST); // Apagar recorte
            org.lwjgl.opengl.GL11.glViewport(0, 0, ancho, alto); // Toda la ventana

            if (esNoche)
                glClearColor(0.04f, 0.04f, 0.12f, 1.0f); // Cielo nocturno
            else
                glClearColor(0.35f, 0.60f, 0.90f, 1.0f); // Cielo azul

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderer.prepararFrame();
            renderer.entero("uMapa", 0); // Shader en modo Perspectiva

            // Configurar variables de iluminación
            renderer.entero("uNoche", esNoche ? 1 : 0);
            renderer.entero("uFaros", farosEncendidos ? 1 : 0);
            auto.farosOn = farosEncendidos;
            renderer.vector("uAuto", auto.posX, 0.8f, auto.posZ); // altura de los faros
            renderer.vector("uFrente", (float) Math.sin(auto.angulo), 0, (float) Math.cos(auto.angulo));
            ciudad.enviarLuces(renderer, auto.posX, auto.posZ);

            // La cámara mira hacia la cabeza de Homero
            renderer.actualizarCamara(ojoX, alturaCamara, ojoZ, auto.posX, 1.8f, auto.posZ, (float) ancho / alto);

            ciudad.dibujar(renderer, false); // <--- Vista principal con todos los detalles
            misiones.dibujarDestino(renderer, false);
            auto.dibujar(renderer);

            // =========================================================
            // 6. DIBUJAR MINIMAPA (Ortográfica)
            // =========================================================
            if (mostrarMapa) {
                int dimensionMenor = Math.min(ancho, alto);
                int lado = Math.min(260, dimensionMenor / 3); // Límite de tamaño
                int margen = Math.min(18, dimensionMenor / 20);
                int xMapa = ancho - lado - margen;
                int yMapa = alto - lado - margen; // Esquina superior derecha

                org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST);

                // Marco blanco/cian del minimapa
                org.lwjgl.opengl.GL11.glScissor(xMapa - 3, yMapa - 3, lado + 6, lado + 6);
                glClearColor(0.8f, 0.87f, 0.94f, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                // Área interior del minimapa
                org.lwjgl.opengl.GL11.glScissor(xMapa, yMapa, lado, lado);
                glClearColor(0.06f, 0.10f, 0.15f, 1); // Fondo oscuro para resaltar calles
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                // Cambiar viewport al cuadrito
                org.lwjgl.opengl.GL11.glViewport(xMapa, yMapa, lado, lado);

                renderer.entero("uMapa", 1); // Shader en modo Vista Superior Ortográfica

                ciudad.dibujar(renderer, true); // <--- Minimapa activado (LOD)

                // Dibujar indicador del Auto en el minimapa (Triángulo/Flecha brillante)
                float flechaX = auto.posX - (float) Math.sin(auto.angulo) * 3f;
                float flechaZ = auto.posZ - (float) Math.cos(auto.angulo) * 3f;

                misiones.dibujarDestino(renderer, true);

                // Base del auto cian
                renderer.cajaGirada(auto.posX, 50, auto.posZ, 4.0f, 0.5f, 5.0f, 0.1f, 0.8f, 0.9f, auto.angulo);
                // Punta de flecha blanca indicando dirección
                renderer.cajaGirada(flechaX, 51, flechaZ, 2.5f, 0.5f, 2.5f, 1.0f, 1.0f, 1.0f, auto.angulo);
            }

            // =========================================================
            // 7. DIBUJAR HUD (Velocímetro y misiones)
            // =========================================================
            org.lwjgl.opengl.GL11.glDisable(org.lwjgl.opengl.GL11.GL_SCISSOR_TEST); // Apagar recorte por si acaso
            org.lwjgl.opengl.GL11.glViewport(0, 0, ancho, alto); // Restaurar viewport a pantalla completa
            
            // Le pasamos la velocidad, las entregas completadas, y el total
            hud.dibujar(renderer, ancho, alto, auto.velocidad, misiones, auto.posX, auto.posZ);

            glfwSwapBuffers(ventana);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new App().run();
        System.out.println("¡Springfield City Project Base listo!");
    }
}
