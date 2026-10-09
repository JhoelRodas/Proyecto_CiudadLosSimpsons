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

    public void run() {
        iniciar();

        // Inicializamos los componentes DESPUÉS de GL.createCapabilities()
        renderer = new Renderer();
        ciudad = new Ciudad();
        auto = new Auto();

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

    private void loop() {
        glClearColor(0.35f, 0.60f, 0.90f, 1.0f);
        
        // Reloj del sistema para el DeltaTime
        double tiempoAnterior = glfwGetTime();

        while (!glfwWindowShouldClose(ventana)) {
            // 1. Calcular el DeltaTime (Tiempo transcurrido desde el último frame)
            double tiempoActual = glfwGetTime();
            float deltaTime = (float) Math.min(tiempoActual - tiempoAnterior, 0.05);
            tiempoAnterior = tiempoActual;

            if (glfwGetKey(ventana, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                glfwSetWindowShouldClose(ventana, true);
            }
            
            // 2. Leer estado del teclado
            boolean acelerar = glfwGetKey(ventana, GLFW_KEY_UP) == GLFW_PRESS || glfwGetKey(ventana, GLFW_KEY_W) == GLFW_PRESS;
            boolean frenar = glfwGetKey(ventana, GLFW_KEY_DOWN) == GLFW_PRESS || glfwGetKey(ventana, GLFW_KEY_S) == GLFW_PRESS;
            boolean izq = glfwGetKey(ventana, GLFW_KEY_LEFT) == GLFW_PRESS || glfwGetKey(ventana, GLFW_KEY_A) == GLFW_PRESS;
            boolean der = glfwGetKey(ventana, GLFW_KEY_RIGHT) == GLFW_PRESS || glfwGetKey(ventana, GLFW_KEY_D) == GLFW_PRESS;

            // 3. Mover el auto
            auto.actualizarFisicas(deltaTime, acelerar, frenar, izq, der);
            
            // 4. Configurar Cámara en 3ra Persona (Persiguiendo al auto - Estilo Hit & Run)
            float distCamara = 8.5f;   // Más cerca del auto
            float alturaCamara = 3.8f; // Cámara más baja, casi a nivel del conductor
            
            // Posicionamos el "Ojo" matemáticamente detrás del auto (-Z local)
            float ojoX = auto.posX - (float)Math.sin(auto.angulo) * distCamara;
            float ojoZ = auto.posZ - (float)Math.cos(auto.angulo) * distCamara;
            
            // 5. Dibujar
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderer.prepararFrame();
            
            // La cámara mira hacia la cabeza de Homero (auto.posX, 1.8f, auto.posZ)
            renderer.actualizarCamara(ojoX, alturaCamara, ojoZ, auto.posX, 1.8f, auto.posZ, (float) ancho / alto);

            ciudad.dibujar(renderer);
            auto.dibujar(renderer);

            glfwSwapBuffers(ventana);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new App().run();
        System.out.println("¡Springfield City Project Base listo!");
    }
}
