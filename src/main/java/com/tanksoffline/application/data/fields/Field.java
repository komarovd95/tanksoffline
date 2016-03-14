package com.tanksoffline.application.data.fields;

import com.tanksoffline.core.data.ActiveRecord;
import com.tanksoffline.application.data.users.User;

@SuppressWarnings("JpaAttributeTypeInspection")
public class Field extends ActiveRecord {
    private User owner;
    private final FieldCell[][] fieldCells;
    private String name;

    public Field(User owner, String name, int width, int height) {
        if (!owner.isManager()) {
            throw new RuntimeException("Only manager can be an owner of field");
        }
        this.owner = owner;
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

    public Field(User owner, String name) {
        this(owner, name, 10, 10);
    }

    public Field() {
        fieldCells = new FieldCell[0][0];
    }

    public User getOwner() {
        return owner;
    }

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
                if (j < fieldCells[i].length - 1) {
                    fieldCells[i][j + 1].setBorder(Direction.TOP);
                }
                break;
            case BOTTOM:
                if (i < fieldCells.length - 1) {
                    fieldCells[i + 1][j].setBorder(Direction.LEFT);
                }
                break;
        }
    }

    public boolean hasBorder(int i, int j, Direction direction) {
        return fieldCells[i][j].hasBorder(direction);
    }

    public void addCell(int i, int j, FieldCell cell) {
        for (Direction dir : Direction.values()) {
            if (cell.hasBorder(dir)) {
                setBorder(i, j, dir);
            }
        }
        setSpawnCell(i, j, cell.getSpawnGroup());
    }

    public void setSpawnCell(int i, int j, int spawnGroup) {
        fieldCells[i][j].setSpawnGroup(spawnGroup);
    }

    public int getWidth() {
        return fieldCells.length;
    }

    public int getHeight() {
        return (fieldCells.length == 0) ? 0 : fieldCells[0].length;
    }

    public FieldCell getCell(int i, int j) {
        return fieldCells[i][j];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfPlayers() {
        return 2;
    }

    @Override
    public String toString() {
        return String.format("[%s] Size: (%d, %d)", name, getWidth(), getHeight());
    }

//    public String displayAscii() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(toString()).append("\r\n\r\n ");
//        for (FieldCell[] ignored : fieldCells) {
//            sb.append("_ ");
//        }
//        sb.append(" \r\n");
//        for (int j = 0; j < getHeight(); j++) {
//            sb.append("|");
//            for (int i = 0; i < getWidth(); i++) {
//                sb.append(fieldCells[i][j].hasBorder(Direction.BOTTOM) ? "_" : " ")
//                        .append(fieldCells[i][j].hasBorder(Direction.RIGHT) ? "|" : " ");
//            }
//            sb.append("\r\n");
//        }
//        return sb.toString();
//    }
}