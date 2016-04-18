package com.tanksoffline.application.views.drawing;

import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.utils.BoundRenderer;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DefaultCellRenderer implements BoundRenderer<FieldCell> {
    @Override
    public void render(FieldCell fieldCell, GraphicsContext gc, Bounds bounds) {
        double x = bounds.getMinX();
        double y = bounds.getMinY();
        double w = bounds.getWidth();
        double h = bounds.getHeight();

        gc.setLineWidth(1.0);
        gc.strokeRect(x, y, w, h);
        gc.setLineWidth(5.0);

        gc.fillRect(x, y, w, h);
        gc.setFill(Color.BURLYWOOD);

        if (fieldCell.hasBorder(Direction.TOP)) {
            gc.strokeLine(x, y, x + w, y);
        }

        if (fieldCell.hasBorder(Direction.LEFT)) {
            gc.strokeLine(x, y, x, y + h);
        }

        if (fieldCell.hasBorder(Direction.BOTTOM)) {
            gc.strokeLine(x, y + h, x + w, y + h);
        }

        if (fieldCell.hasBorder(Direction.RIGHT)) {
            gc.strokeLine(x + w, y, x + w, y + h);
        }
    }
}
