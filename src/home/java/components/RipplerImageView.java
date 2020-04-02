package home.java.components;

import com.jfoenix.controls.JFXRippler;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class RipplerImageView extends JFXRippler {

    {
        super.setRipplerFill(Color.WHITE);
    }

    public RipplerImageView() {
    }

    public RipplerImageView(Node control) {
        super(control);
    }

    public RipplerImageView(Node control, RipplerPos pos) {
        super(control, pos);
    }

    public RipplerImageView(Node control, RipplerMask mask) {
        super(control, mask);
    }

    public RipplerImageView(Node control, RipplerMask mask, RipplerPos pos) {
        super(control, mask, pos);
    }
}
