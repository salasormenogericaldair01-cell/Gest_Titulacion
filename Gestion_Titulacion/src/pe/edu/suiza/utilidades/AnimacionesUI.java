package pe.edu.suiza.utilidades;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Clase utilitaria para animaciones visuales suaves (transiciones y modales).
 * Paquete: pe.edu.suiza.utilidades
 */
public class AnimacionesUI {
    
    public static void aplicarFadeIn(Node nodo, int millis) {
        if (nodo == null) return;
        FadeTransition ft = new FadeTransition(Duration.millis(millis), nodo);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
