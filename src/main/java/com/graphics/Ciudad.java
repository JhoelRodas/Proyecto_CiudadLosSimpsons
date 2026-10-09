package com.graphics;
import com.graphics.modelos.*;
public class Ciudad {

    // 0 = Calle, 1 = Casa (Beige), 2 = Tienda (Verde), 3 = Planta (Gris), 4 =
    // Parque (Pasto)
    private final int[][] mapa = {
            { 1, 1, 0, 2, 2, 0, 4, 4, 0, 1, 1 },
            { 1, 1, 0, 2, 2, 0, 4, 4, 0, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 4, 4, 0, 1, 1, 0, 3, 3, 0, 2, 2 },
            { 4, 4, 0, 1, 1, 0, 3, 3, 0, 2, 2 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 0, 3, 3, 0, 1, 1, 0, 4, 4 },
            { 1, 1, 0, 3, 3, 0, 1, 1, 0, 4, 4 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 2, 2, 0, 4, 4, 0, 1, 1, 0, 3, 3 },
            { 2, 2, 0, 4, 4, 0, 1, 1, 0, 3, 3 }
    };

    public void dibujar(Renderer renderer) {
        // Suelo de asfalto gris (La base completa de la ciudad)
        renderer.caja(0, -0.25f, 0, 300, 0.5f, 300, 0.28f, 0.29f, 0.30f);
        // Parámetros: (posX, posY, posZ, anchoX, altoY, profundidadZ, rojo, verde,
        // azul)

        int tamanoCelda = 10;
        int offset = (11 * tamanoCelda) / 2; // -55

        for (int z = 0; z < 11; z++) {
            for (int x = 0; x < 11; x++) {
                int tipo = mapa[z][x];

                // La posición real en el mundo 3D
                float posX = (x * tamanoCelda) - offset + (tamanoCelda / 2.0f);
                float posZ = (z * tamanoCelda) - offset + (tamanoCelda / 2.0f);

                if (tipo == 1) {
                    // Casa: Beige
                    renderer.caja(posX, 5f, posZ, 9f, 10f, 9f, 0.95f, 0.92f, 0.82f);
                } else if (tipo == 2) {
                    // Tienda: Verde claro
                    renderer.caja(posX, 4f, posZ, 9f, 8f, 9f, 0.6f, 0.8f, 0.6f);
                } else if (tipo == 3) {
                    // Planta: Gris oscuro y más alta
                    renderer.caja(posX, 8f, posZ, 9f, 16f, 9f, 0.5f, 0.5f, 0.55f);
                } else if (tipo == 4) {
                    // Parque: Un bloque verde plano en el suelo
                    renderer.caja(posX, 0.5f, posZ, 9f, 1f, 9f, 0.3f, 0.7f, 0.3f);
                }
            }
        }
    }
}
