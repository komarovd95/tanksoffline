package com.tanksoffline.application.entities;

import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.core.data.DomainObject;

public class FieldEntity extends DomainObject implements Field {
    private FieldCell[][] fieldCells;
    private String name;

    public FieldEntity(String name, int width, int height) {
        this.name = name;
        fieldCells = new FieldCell[width][];
        for (int i = 0; i < fieldCells.length; i++) {
            fieldCells[i] = new FieldCell[height];
            for (int j = 0; j < fieldCells[i].length; j++) {
                FieldCell cell = new FieldCell();
                if (i == 0) {
                    cell.setBorder(Direction.LEFT);
                }
                if (i == fieldCells.length - 1) {
                    cell.setBorder(Direction.RIGHT);
                }
                if (j == 0) {
                    cell.setBorder(Direction.TOP);
                }
                if (j == fieldCells[i].length - 1) {
                    cell.setBorder(Direction.BOTTOM);
                }
                fieldCells[i][j] = cell;
            }
        }
    }

    public FieldEntity(String name) {
        this(name, 10, 10);
    }

    public FieldEntity() {
        fieldCells = new FieldCell[0][0];
    }

    @Override
    public boolean hasBorder(int i, int j, Direction direction) {
        return fieldCells[i][j].hasBorder(direction);
    }

    @Override
    public void setBorder(int i, int j, Direction direction) {
        fieldCells[i][j].setBorder(direction);
        switch (direction) {
            case TOP:
                if (j > 0) {
                    fieldCells[i][j - 1].setBorder(Direction.BOTTOM);
                }
                break;
            case LEFT:
                if (i > 0) {
                    fieldCells[i - 1][j].setBorder(Direction.RIGHT);
                }
                break;
            case RIGHT:
                if (i < fieldCells.length - 1) {
                    fieldCells[i + 1][j].setBorder(Direction.LEFT);
                }
                break;
            case BOTTOM:
                if (j < fieldCells[i].length - 1) {
                    fieldCells[i][j + 1].setBorder(Direction.TOP);
                }
                break;
        }
    }

    @Override
    public void removeBorder(int i, int j, Direction direction) {
        if (i == 0 && direction == Direction.LEFT) return;
        if (i == getWidth() - 1 && direction == Direction.RIGHT) return;
        if (j == 0 && direction == Direction.TOP) return;
        if (j == getHeight() - 1 && direction == Direction.BOTTOM) return;

        fieldCells[i][j].removeBorder(direction);
        switch (direction) {
            case TOP:
                if (j > 0) {
                    fieldCells[i][j - 1].removeBorder(Direction.BOTTOM);
                }
                break;
            case LEFT:
                if (i > 0) {
                    fieldCells[i - 1][j].removeBorder(Direction.RIGHT);
                }
                break;
            case RIGHT:
                if (j < fieldCells[i].length - 1) {
                    fieldCells[i][j + 1].removeBorder(Direction.TOP);
                }
                break;
            case BOTTOM:
                if (i < fieldCells.length - 1) {
                    fieldCells[i + 1][j].removeBorder(Direction.LEFT);
                }
                break;
        }
    }

    @Override
    public FieldCell[][] getFieldCells() {
        return fieldCells;
    }

    @Override
    public FieldCell getFieldCell(int i, int j) {
        return fieldCells[i][j];
    }

    @Override
    public int getWidth() {
        return fieldCells.length;
    }

    @Override
    public int getHeight() {
        return (fieldCells.length == 0) ? 0 : fieldCells[0].length;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("[%s] Size: (%d, %d)", name, getWidth(), getHeight());
    }
}
