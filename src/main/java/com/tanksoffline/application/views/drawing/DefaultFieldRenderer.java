package com.tanksoffline.application.views.drawing;

import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.data.fields.FieldCell;
import com.tanksoffline.application.utils.BoundRenderer;
import com.tanksoffline.application.utils.Renderer;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class DefaultFieldRenderer implements Renderer<Field> {
    private BoundRenderer<FieldCell> cellRenderer;

    public DefaultFieldRenderer(BoundRenderer<FieldCell> cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    @Override
    public void render(Field field, GraphicsContext gc) {
        if (field == null) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            return;
        }

        double cellSize = Math.ceil(gc.getCanvas().getWidth() / field.getWidth());

        gc.setFill(Color.BURLYWOOD);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5.0);
        gc.setLineCap(StrokeLineCap.ROUND);

        FieldCell[][] cells = field.getFieldCells();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cellRenderer.render(cells[i][j], gc,
                        new BoundingBox(i * cellSize, j * cellSize, cellSize, cellSize));

//                gc.setLineWidth(1.0);
//                gc.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
//                gc.setLineWidth(5.0);
//
//                if (selectedCell.get() != null
//                        && i == selectedCell.get().getKey()
//                        && j == selectedCell.get().getValue()) {
//                    gc.setFill(Color.LIGHTGREEN);
//                }
//                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
//                gc.setFill(Color.BURLYWOOD);
//
//                FieldCell cell = cells[i][j];
//
//                if (cell.hasBorder(Direction.TOP)) {
//                    gc.strokeLine(i * cellSize, j * cellSize,
//                            (i + 1) * cellSize, j * cellSize);
//                }
//
//                if (cell.hasBorder(Direction.LEFT)) {
//                    gc.strokeLine(i * cellSize, j * cellSize,
//                            i * cellSize, (j + 1) * cellSize);
//                }
//
//                if (cell.hasBorder(Direction.BOTTOM)) {
//                    gc.strokeLine(i * cellSize, (j + 1) * cellSize,
//                            (i + 1) * cellSize, (j + 1) * cellSize);
//                }
//
//                if (cell.hasBorder(Direction.RIGHT)) {
//                    gc.strokeLine((i + 1) * cellSize, j * cellSize,
//                            (i + 1) * cellSize, (j + 1) * cellSize);
//                }
            }
        }
    }
}
