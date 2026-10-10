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
                uniform int uMapa; // <-- Variable para activar el minimapa

                out vec3 vNormal; // Enviamos la normal rotada al fragment shader
                out vec3 vMundo;  // Posición del vértice en el mundo para la iluminación

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

                    vMundo = giroTotal * (aPos * uEscala) + uPos;

                    // Rotamos la normal para que la luz impacte correctamente
                    vNormal = giroTotal * aNormal;

                    if (uMapa == 1) {
                        // --- PROYECCIÓN ORTOGRÁFICA (MINIMAPA) ---
                        // Nuestra ciudad mide 21 x 14 = 294 de ancho total.
                        // Dividimos entre 160 para abarcar desde el centro (-147 a 147).
                        float escalaMapa = 1.0 / 160.0;
                        float pantallaX = vMundo.x * escalaMapa;
                        float pantallaY = -vMundo.z * escalaMapa; // Norte hacia arriba (-Z)
                        float profundidad = -vMundo.y / 100.0; // Edificios encima de la calle
                        gl_Position = vec4(pantallaX, pantallaY, profundidad, 1.0);
                    } else if (uMapa == 2) {
                        // --- PROYECCIÓN HUD 2D (PANTALLA DIRECTA) ---
                        // vMundo ya tiene uPos y uEscala. Lo usamos como coordenadas directas de pantalla [-1 a 1]
                        // Ajustamos X según el aspecto para que los cuadrados no se deformen
                        gl_Position = vec4(vMundo.x / uAspecto, vMundo.y, 0.0, 1.0);
                    } else {
                        // --- PROYECCIÓN PERSPECTIVA (VISTA PRINCIPAL) ---
                        vec3 frente = normalize(uObjetivo - uOjo);
                        vec3 derecha = normalize(cross(frente, vec3(0.0, 1.0, 0.0)));
                        vec3 arriba = cross(derecha, frente);
                        vec3 diferencia = vMundo - uOjo;

                        float vistaX = dot(diferencia, derecha);
                        float vistaY = dot(diferencia, arriba);
                        float vistaZ = -dot(diferencia, frente);

                        float factor = 1.0 / tan(radians(55.0) * 0.5);
                        float cerca = 0.1;
                        float lejos = 350.0; // Distancia de visión ampliada

                        float clipX = vistaX * factor / uAspecto;
                        float clipY = vistaY * factor;
                        float clipZ = (lejos + cerca) / (cerca - lejos) * vistaZ;
                        clipZ += 2.0 * lejos * cerca / (cerca - lejos);

                        gl_Position = vec4(clipX, clipY, clipZ, -vistaZ);
                    }
                }
                """;

        String fragmentCode = """
                #version 330 core
                in vec3 vMundo;
                in vec3 vNormal;
                out vec4 FragColor;
                uniform vec3 uColor;

                // --- UNIFORMS DE LUZ ---
                uniform int uNoche;
                uniform int uFaros;
                uniform int uEmision;
                uniform int uMapa;

                uniform vec3 uAuto;
                uniform vec3 uFrente;
                
                uniform vec3 uLuces[25];
                uniform int uNumLuces;

                void main() {
                    if (uEmision == 1 || uMapa >= 1) { // Bombillas, ventanas y minimapa sin sombras
                        FragColor = vec4(uColor, 1.0);
                        return;
                    }

                    vec3 normal = normalize(vNormal);

                    // ==== Día / Noche Ambiental ====
                    // Valores bajos a propósito: ambiente + sol no deben pasar de ~1.0 o el color se "quema".
                    vec3 luz = vec3(0.42, 0.42, 0.44);        // Día: gris neutro
                    vec3 colorSol = vec3(1.0, 0.96, 0.85);    // Sol cálido
                    float intensidadSol = 0.55;

                    if (uNoche == 1) {
                        luz = vec3(0.07, 0.08, 0.17);         // Noche: índigo oscuro
                        colorSol = vec3(0.22, 0.25, 0.42);    // Luna tenue
                        intensidadSol = 0.09;
                    }

                    // Luz direccional base (Sol/Luna)
                    vec3 direccionSol = normalize(vec3(0.5, 1.0, 0.3));
                    float incidenciaSol = max(dot(normal, direccionSol), 0.0);
                    luz += colorSol * incidenciaSol * intensidadSol;

                    // ==== Farolas de la calle (solo las 25 más cercanas al auto llegan aquí) ====
                    if (uNoche == 1) {
                        for (int i = 0; i < uNumLuces; i++) {
                            vec3 haciaLuz = uLuces[i] - vMundo;
                            float distancia = length(haciaLuz);
                            if (distancia < 26.0) { // Fuera de este radio la farola no aporta nada
                                float difusa = max(dot(normal, normalize(haciaLuz)), 0.0);
                                float atenuacion = 1.0 + 0.10 * distancia + 0.030 * distancia * distancia;
                                float desvanece = 1.0 - smoothstep(14.0, 26.0, distancia); // Sin corte brusco
                                vec3 colorFarola = vec3(1.0, 0.74, 0.34); // Lámpara de sodio (naranja suave)
                                luz += colorFarola * difusa * 3.2 * desvanece / atenuacion;
                            }
                        }
                    }

                    // ==== Faros del Auto ====
                    if (uFaros == 1) {
                        for (int i = 0; i < 2; i++) { // Faro izquierdo y derecho
                            vec3 lateral = vec3(-uFrente.z, 0.0, uFrente.x);
                            float separacion = (i == 0) ? -0.65 : 0.65;
                            vec3 origen = uAuto + uFrente * 2.3 + lateral * separacion; // En el parachoques delantero

                            vec3 haciaFoco = vMundo - origen;
                            float distancia = length(haciaFoco);
                            if (distancia < 36.0) {
                                vec3 eje = normalize(uFrente + vec3(0.0, -0.07, 0.0)); // Apunta un poco hacia abajo
                                float alineacion = dot(normalize(haciaFoco), eje);
                                float cono = smoothstep(0.86, 0.97, alineacion);
                                float difusa = max(dot(normal, -normalize(haciaFoco)), 0.0);
                                float atenuacion = 1.0 + 0.030 * distancia * distancia;
                                float desvanece = 1.0 - smoothstep(22.0, 36.0, distancia);
                                vec3 colorFaro = vec3(1.0, 0.93, 0.70); // Blanco amarillento
                                luz += colorFaro * cono * difusa * 7.0 * desvanece / atenuacion;
                            }
                        }
                    }

                    FragColor = vec4(min(uColor * luz, vec3(1.0)), 1.0);
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

    // Estado de iluminación que el Renderer recuerda (se actualiza solo al enviar estos uniforms)
    private boolean noche = false;
    private boolean modoMapa = false;
    private int emisionActual = 0;

    public void entero(String nombre, int valor) {
        switch (nombre) {
            case "uNoche": noche = valor == 1; break;
            case "uMapa": modoMapa = valor >= 1; break;
            case "uEmision": emisionActual = valor; break;
            default: break;
        }
        glUniform1i(uniform(nombre), valor);
    }

    // ---- Ventanas encendidas de noche ----
    // Colores de vidrio de los modelos de edificio. Si creas un modelo con otro color de vidrio
    // y quieres que brille de noche, agrégalo aquí.
    private static final float[][] VIDRIOS_CASA = {
        { 0.40f, 0.70f, 0.90f },  // CasaSimpson y CasaGenerica
        { 0.40f, 0.80f, 0.85f },  // CasaFlanders
        { 0.95f, 0.85f, 0.45f },  // MansionBurns
    };
    private static final float[] VIDRIO_TIENDA = { 0.55f, 0.82f, 0.92f }; // KwikEMart: siempre encendida

    private static boolean parecido(float r, float g, float b, float[] c) {
        return Math.abs(r - c[0]) < 0.015f && Math.abs(g - c[1]) < 0.015f && Math.abs(b - c[2]) < 0.015f;
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
        boolean brilla = false;
        if (noche && !modoMapa && emisionActual == 0) {
            if (parecido(r, g, b, VIDRIO_TIENDA)) {
                // La tienda siempre tiene la luz prendida
                r = 0.80f; g = 0.74f; b = 0.50f;
                brilla = true;
            } else {
                for (float[] v : VIDRIOS_CASA) {
                    if (parecido(r, g, b, v)) {
                        // Mismo vidrio => siempre la misma decisión (depende solo de dónde está la ventana)
                        int h = (int) Math.floor(x * 3.1f) * 73856093 ^ (int) Math.floor(y * 1.7f) * 19349663
                                ^ (int) Math.floor(z * 3.1f) * 83492791;
                        int azar = (h >>> 8) & 255;
                        if (azar < 150) { // ~60% de las ventanas encendidas
                            float tono = 0.72f + (azar % 5) * 0.05f;
                            r = 0.78f * tono; g = 0.58f * tono; b = 0.26f * tono; // luz cálida y suave
                            brilla = true;
                        } else {
                            r = 0.07f; g = 0.09f; b = 0.14f; // ventana apagada
                        }
                        break;
                    }
                }
            }
        }
        if (brilla) glUniform1i(uniform("uEmision"), 1);

        vector("uPos", x, y, z);
        vector("uEscala", sx, sy, sz);
        vector("uColor", r, g, b);
        decimal("uGiro", anguloY);
        decimal("uGiroX", anguloX);
        decimal("uGiroZ", anguloZ);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        if (brilla) glUniform1i(uniform("uEmision"), 0);
    }
}
