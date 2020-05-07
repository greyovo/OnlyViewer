package home.java.model;

import home.java.components.ImageBox;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

// FIXME: 2020/5/7 在FlowPane中失效
public class MultipleSelection {

    public double mouseAnchorX;
    public double mouseAnchorY;
    Rectangle rect;
    Pane root;

    public MultipleSelection(Pane root) {
        this.root = root;

        rect = new Rectangle(0, 0, 0, 0);

        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(0.6);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

//        root.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
//        root.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
//        root.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            mouseAnchorX = event.getX();
            mouseAnchorY = event.getY();
            System.out.println(mouseAnchorX + ", " + mouseAnchorY);
            rect.setX(mouseAnchorX);
            rect.setY(mouseAnchorY);
            rect.setWidth(0);
            rect.setHeight(0);

            root.getChildren().add(rect);
            event.consume();
        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (!event.isShiftDown() && !event.isControlDown()) {
                SelectionModel.clear();
            }
            for (Node node : root.getChildren()) {
                //instanceof决定选择的节点类型
                if (node instanceof ImageBox) {
//                    if (node.getBoundsInParent().intersects(rect.getBoundsInParent())) {
                        if (event.isShiftDown()) {
                            SelectionModel.add(node);
                        } else if (event.isControlDown()) {
                            if (SelectionModel.contains(node)) {
                                SelectionModel.remove(node);
                            } else {
                                SelectionModel.add(node);
                            }
                        } else {
                            SelectionModel.add(node);
                        }
//                    }
                }
            }
            SelectionModel.log();
            root.getChildren().remove(rect);
            event.consume();
        }
    };


    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            double offsetX = event.getX() - mouseAnchorX;
            double offsetY = event.getY() - mouseAnchorY;

            if (offsetX > 0)
                rect.setWidth(offsetX);
            else {
                rect.setX(event.getX());
                rect.setWidth(mouseAnchorX - rect.getX());
            }

            if (offsetY > 0) {
                rect.setHeight(offsetY);
            } else {
                rect.setY(event.getY());
                rect.setHeight(mouseAnchorY - rect.getY());
            }
            event.consume();
        }
    };
}
