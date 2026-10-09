package com.graphics;

import static org.lwjgl.opengl.GL33.*;
import java.util.HashMap;
import java.util.Map;

public class Renderer {

    private int vaoCubo;
    private int vboCubo;
    private int vaoCilindro;
    private int vboCilindro;
    private int totalVerticesCilindro;

    private int shaderPrograma;
    private final Map<String, Integer> uniforms = new HashMap<>();

    public Renderer() {
        crearCubo();
        crearCilindro(16);
        crearPrograma();
        glUseProgram(shaderPrograma); // Activamos el programa por defecto
        glBindVertexArray(vaoCubo); // Activamos el VAO por defecto
    }

    public void prepararFrame() {
        glUseProgram(shaderPrograma);
        glBindVertexArray(vaoCubo);
    }

    public void actualizarCamara(float ojoX, float ojoY, float ojoZ, float objX, float objY, float objZ,
            float aspecto) {
        vector("uOjo", ojoX, ojoY, ojoZ);
        vector("uObjetivo", objX, objY, objZ);
        decimal("uAspecto", aspecto);
    }

    private void crearCubo() {
        float[] vertices = {
                // Cara Frontal (+Z)
                -0.5f, -0.5f, 0.5f, 0, 0, 1,
                0.5f, -0.5f, 0.5f, 0, 0, 1,
                0.5f, 0.5f, 0.5f, 0, 0, 1,
                0.5f, 0.5f, 0.5f, 0, 0, 1,
                -0.5f, 0.5f, 0.5f, 0, 0, 1,
                -0.5f, -0.5f, 0.5f, 0, 0, 1,
                // Cara Trasera (-Z)
                -0.5f, -0.5f, -0.5f, 0, 0, -1,
                -0.5f, 0.5f, -0.5f, 0, 0, -1,
                0.5f, 0.5f, -0.5f, 0, 0, -1,
                0.5f, 0.5f, -0.5f, 0, 0, -1,
                0.5f, -0.5f, -0.5f, 0, 0, -1,
                -0.5f, -0.5f, -0.5f, 0, 0, -1,
                // Cara Izquierda (-X)
                -0.5f, 0.5f, 0.5f, -1, 0, 0,
                -0.5f, 0.5f, -0.5f, -1, 0, 0,
                -0.5f, -0.5f, -0.5f, -1, 0, 0,
                -0.5f, -0.5f, -0.5f, -1, 0, 0,
                -0.5f, -0.5f, 0.5f, -1, 0, 0,
                -0.5f, 0.5f, 0.5f, -1, 0, 0,
                // Cara Derecha (+X)
                0.5f, 0.5f, 0.5f, 1, 0, 0,
                0.5f, -0.5f, 0.5f, 1, 0, 0,
                0.5f, -0.5f, -0.5f, 1, 0, 0,
                0.5f, -0.5f, -0.5f, 1, 0, 0,
                0.5f, 0.5f, -0.5f, 1, 0, 0,
                0.5f, 0.5f, 0.5f, 1, 0, 0,
                // Cara Superior (+Y)
                -0.5f, 0.5f, -0.5f, 0, 1, 0,
                -0.5f, 0.5f, 0.5f, 0, 1, 0,
                0.5f, 0.5f, 0.5f, 0, 1, 0,
                0.5f, 0.5f, 0.5f, 0, 1, 0,
                0.5f, 0.5f, -0.5f, 0, 1, 0,
                -0.5f, 0.5f, -0.5f, 0, 1, 0,
                // Cara Inferior (-Y)
                -0.5f, -0.5f, -0.5f, 0, -1, 0,
                0.5f, -0.5f, -0.5f, 0, -1, 0,
                0.5f, -0.5f, 0.5f, 0, -1, 0,
                0.5f, -0.5f, 0.5f, 0, -1, 0,
                -0.5f, -0.5f, 0.5f, 0, -1, 0,
                -0.5f, -0.5f, -0.5f, 0, -1, 0
        };

        vaoCubo = glGenVertexArrays();
        glBindVertexArray(vaoCubo);
        vboCubo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboCubo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);
        
        // Habilitamos las normales para el cubo (location = 1)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    private void crearCilindro(int segmentos) {
        // Cada segmento tiene: 2 triángulos para la tapa izq, 2 para la der, y 2 para
        // el cuerpo lateral.
        // 6 vértices por segmento para el contorno + tapas. Lo hacemos con un array
        // flotante directo.
        int caras = segmentos;
        totalVerticesCilindro = caras * 36; // Geometría optimizada indexada en triángulos planos
        float[] vertices = new float[totalVerticesCilindro * 6]; // 6 componentes: X,Y,Z, NX,NY,NZ

        float radio = 0.5f; // Radio base 1.0 de diámetro para escalar fácilmente
        float ancho = 1.0f; // Ancho base de -0.5 a +0.5

        int idx = 0;
        for (int i = 0; i < caras; i++) {
            float angulo1 = (float) (i * 2.0 * Math.PI / caras);
            float angulo2 = (float) ((i + 1) * 2.0 * Math.PI / caras);

            float cos1 = (float) Math.cos(angulo1);
            float sin1 = (float) Math.sin(angulo1);
            float cos2 = (float) Math.cos(angulo2);
            float sin2 = (float) Math.sin(angulo2);

            // --- CUERPO LATERAL (2 Triángulos = 6 Vértices) ---
            // Triángulo 1
            idx = añadirVertice(vertices, idx, -0.5f, radio * cos1, radio * sin1, 0, cos1, sin1);
            idx = añadirVertice(vertices, idx, 0.5f, radio * cos1, radio * sin1, 0, cos1, sin1);
            idx = añadirVertice(vertices, idx, 0.5f, radio * cos2, radio * sin2, 0, cos2, sin2);
            // Triángulo 2
            idx = añadirVertice(vertices, idx, -0.5f, radio * cos1, radio * sin1, 0, cos1, sin1);
            idx = añadirVertice(vertices, idx, 0.5f, radio * cos2, radio * sin2, 0, cos2, sin2);
            idx = añadirVertice(vertices, idx, -0.5f, radio * cos2, radio * sin2, 0, cos2, sin2);

            // --- TAPA DERECHA (+X) (1 Triángulo = 3 Vértices) ---
            idx = añadirVertice(vertices, idx, 0.5f, 0, 0, 1, 0, 0);
            idx = añadirVertice(vertices, idx, 0.5f, radio * cos2, radio * sin2, 1, 0, 0);
            idx = añadirVertice(vertices, idx, 0.5f, radio * cos1, radio * sin1, 1, 0, 0);

            // --- TAPA IZQUIERDA (-X) (1 Triángulo = 3 Vértices) ---
            idx = añadirVertice(vertices, idx, -0.5f, 0, 0, -1, 0, 0);
            idx = añadirVertice(vertices, idx, -0.5f, radio * cos1, radio * sin1, -1, 0, 0);
            idx = añadirVertice(vertices, idx, -0.5f, radio * cos2, radio * sin2, -1, 0, 0);
        }

        vaoCilindro = glGenVertexArrays();
        glBindVertexArray(vaoCilindro);
        vboCilindro = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboCilindro);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // Mismos atributos de tu Shader (Location 0 = Pos, Location 1 = Normales)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    private int añadirVertice(float[] arr, int idx, float x, float y, float z, float nx, float ny, float nz) {
        arr[idx++] = x;
        arr[idx++] = y;
        arr[idx++] = z;
        arr[idx++] = nx;
        arr[idx++] = ny;
        arr[idx++] = nz;
        return idx;
    }

    public void cilindroGirado(float x, float y, float z, float anchoX, float radioY, float radioZ, float[] rgb,
            float angulo) {
        // Cambiamos el pipeline temporalmente al buffer del cilindro
        glBindVertexArray(vaoCilindro);

        // Pasamos los datos de posición, escala y rotación a tus Uniforms existentes
        vector("uPos", x, y, z);
        vector("uEscala", anchoX, radioY, radioZ);
        decimal("uGiro", angulo);
        decimal("uGiroX", 0.0f);
        decimal("uGiroZ", 0.0f);
        vector("uColor", rgb[0], rgb[1], rgb[2]);

        // Dibujamos el cilindro completo con 1 sola llamada de hardware
        glDrawArrays(GL_TRIANGLES, 0, totalVerticesCilindro);

        // Restauramos el VAO del cubo para que la ciudad se siga dibujando normalmente
        glBindVertexArray(vaoCubo);
    }

    private void crearPrograma() {
        String vertexCode = """
                #version 330 core
                layout (location = 0) in vec3 aPos;
                layout (location = 1) in vec3 aNormal; // Recibimos la normal

                uniform vec3 uPos;
                uniform vec3 uEscala;
                uniform vec3 uOjo;
                uniform vec3 uObjetivo;
                uniform float uGiro;
                uniform float uGiroX; // Rotación en X (Inclinación hacia adelante/atrás)
                uniform float uGiroZ; // Rotación en Z (Inclinación lateral)
                uniform float uAspecto;

                out vec3 vNormal; // Enviamos la normal rotada al fragment shader

                void main() {
                    // Matriz de rotación en Y (Izquierda / Derecha)
                    mat3 rotY = mat3(
                        cos(uGiro), 0.0, -sin(uGiro),
                        0.0, 1.0, 0.0,
                        sin(uGiro), 0.0, cos(uGiro)
                    );
                    
                    // Matriz de rotación en X (Arriba / Abajo)
                    mat3 rotX = mat3(
                        1.0, 0.0, 0.0,
                        0.0, cos(uGiroX), sin(uGiroX),
                        0.0, -sin(uGiroX), cos(uGiroX)
                    );
                    
                    // Matriz de rotación en Z (Inclinación lateral)
                    mat3 rotZ = mat3(
                        cos(uGiroZ), sin(uGiroZ), 0.0,
                        -sin(uGiroZ), cos(uGiroZ), 0.0,
                        0.0, 0.0, 1.0
                    );

                    // Rotación combinada (Y * X * Z)
                    mat3 giroTotal = rotY * rotX * rotZ;
                    
                    vec3 vMundo = giroTotal * (aPos * uEscala) + uPos;
                    
                    // Rotamos la normal para que la luz impacte correctamente
                    vNormal = giroTotal * aNormal;

                    vec3 frente = normalize(uObjetivo - uOjo);
                    vec3 derecha = normalize(cross(frente, vec3(0.0, 1.0, 0.0)));
                    vec3 arriba = cross(derecha, frente);
                    vec3 diferencia = vMundo - uOjo;

                    float vistaX = dot(diferencia, derecha);
                    float vistaY = dot(diferencia, arriba);
                    float vistaZ = -dot(diferencia, frente);

                    float factor = 1.0 / tan(radians(55.0) * 0.5);
                    float cerca = 0.1;
                    float lejos = 250.0;

                    float clipX = vistaX * factor / uAspecto;
                    float clipY = vistaY * factor;
                    float clipZ = (lejos + cerca) / (cerca - lejos) * vistaZ;
                    clipZ += 2.0 * lejos * cerca / (cerca - lejos);

                    gl_Position = vec4(clipX, clipY, clipZ, -vistaZ);
                }
                """;

        String fragmentCode = """
                #version 330 core
                in vec3 vNormal;
                out vec4 FragColor;
                uniform vec3 uColor;
                
                void main() {
                    // Luz direccional que viene de arriba y un poco de lado (estilo Sol)
                    vec3 luzDir = normalize(vec3(0.6, 1.0, 0.4));
                    
                    // Aseguramos que la normal esté normalizada
                    vec3 normal = normalize(vNormal);
                    
                    // Intensidad según el ángulo con la luz (Sombreado Lambert básico)
                    float intensidad = max(dot(normal, luzDir), 0.0);
                    
                    // Luz ambiente para que las partes oscuras no sean negras del todo
                    float luzAmbiente = 0.45;
                    
                    // Multiplicamos el color base por la luz calculada
                    vec3 colorFinal = uColor * (luzAmbiente + intensidad * 0.55);
                    
                    FragColor = vec4(colorFinal, 1.0);
                }
                """;

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexCode);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentCode);
        glCompileShader(fragmentShader);

        shaderPrograma = glCreateProgram();
        glAttachShader(shaderPrograma, vertexShader);
        glAttachShader(shaderPrograma, fragmentShader);
        glLinkProgram(shaderPrograma);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private int uniform(String nombre) {
        if (!uniforms.containsKey(nombre)) {
            int ubicacion = glGetUniformLocation(shaderPrograma, nombre);
            uniforms.put(nombre, ubicacion);
        }
        return uniforms.get(nombre);
    }

    public void vector(String nombre, float x, float y, float z) {
        glUniform3f(uniform(nombre), x, y, z);
    }

    public void decimal(String nombre, float valor) {
        glUniform1f(uniform(nombre), valor);
    }

    public void caja(float x, float y, float z, float sx, float sy, float sz, float r, float g, float b) {
        cajaGirada(x, y, z, sx, sy, sz, r, g, b, 0);
    }

    public void cajaGirada(float x, float y, float z, float sx, float sy, float sz, float r, float g, float b,
            float angulo) {
        cajaInclinada(x, y, z, sx, sy, sz, r, g, b, angulo, 0.0f, 0.0f);
    }

    public void cajaInclinada(float x, float y, float z, float sx, float sy, float sz, float r, float g, float b,
            float anguloY, float anguloX, float anguloZ) {
        vector("uPos", x, y, z);
        vector("uEscala", sx, sy, sz);
        vector("uColor", r, g, b);
        decimal("uGiro", anguloY);
        decimal("uGiroX", anguloX);
        decimal("uGiroZ", anguloZ);
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }
}
