package com.tanksoffline.application.data.fields;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldCell implements Serializable {
    private static final Logger logger = Logger.getLogger(FieldCell.class.getName());
    private static final byte TOP_BORDER = 8;
    private static final byte LEFT_BORDER = 4;
    private static final byte RIGHT_BORDER = 2;
    private static final byte BOTTOM_BORDER = 1;


    private transient boolean hasTopBorder;
    private transient boolean hasLeftBorder;
    private transient boolean hasRightBorder;
    private transient boolean hasBottomBorder;

    private transient int spawnGroup;

    public FieldCell(boolean hasTopBorder,
                     boolean hasLeftBorder,
                     boolean hasRightBorder,
                     boolean hasBottomBorder,
                     int spawnGroup) {
        if (hasTopBorder) setBorder(Direction.TOP);
        if (hasLeftBorder) setBorder(Direction.LEFT);
        if (hasRightBorder) setBorder(Direction.RIGHT);
        if (hasBottomBorder) setBorder(Direction.BOTTOM);
        setSpawnGroup(spawnGroup);
    }

    public FieldCell(boolean hasTopBorder,
                     boolean hasLeftBorder,
                     boolean hasRightBorder,
                     boolean hasBottomBorder) {
        this(hasTopBorder, hasLeftBorder, hasRightBorder, hasBottomBorder, 0);
    }

    public FieldCell(Direction... borders) {
        for (Direction border : borders) {
            setBorder(border);
        }
    }

    public boolean hasBorder(Direction direction) {
        boolean hasBorder = false;
        switch (direction) {
            case TOP:
                hasBorder = hasTopBorder;
                break;
            case LEFT:
                hasBorder = hasLeftBorder;
                break;
            case RIGHT:
                hasBorder = hasRightBorder;
                break;
            case BOTTOM:
                hasBorder = hasBottomBorder;
                break;
        }
        return hasBorder;
    }

    public void setBorder(Direction direction) {
        switch (direction) {
            case TOP:
                hasTopBorder = true;
                break;
            case LEFT:
                hasLeftBorder = true;
                break;
            case RIGHT:
                hasRightBorder = true;
                break;
            case BOTTOM:
                hasBottomBorder = true;
                break;
        }
    }

    public boolean isSpawnCell() {
        return spawnGroup > 0;
    }

    public int getSpawnGroup() {
        return spawnGroup;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %d) SpawnGroup : %d",
                (hasTopBorder) ? 1 : 0,
                (hasLeftBorder) ? 1 : 0,
                (hasRightBorder) ? 1 : 0,
                (hasBottomBorder) ? 1 : 0,
                spawnGroup);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 0;
        for (Direction dir : Direction.values()) {
            hashCode += dir.toString().hashCode();
            hashCode *= prime;
        }
        hashCode += spawnGroup;
        hashCode *= prime;
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof FieldCell) {
            FieldCell obj = (FieldCell) o;
            return  this.hasTopBorder == obj.hasTopBorder &&
                    this.hasLeftBorder == obj.hasLeftBorder &&
                    this.hasRightBorder == obj.hasRightBorder &&
                    this.hasBottomBorder == obj.hasBottomBorder &&
                    this.spawnGroup == obj.spawnGroup;
        }
        return false;
    }

    public void setSpawnGroup(int spawnGroup) {
        if (spawnGroup != 0 && spawnGroup != 1) {
            logger.log(Level.SEVERE, "Spawn group's limits exceeded : " + spawnGroup);
            throw   new IllegalArgumentException("Spawn group must be in [0..1] interval");
        }
        this.spawnGroup = spawnGroup;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        byte value = 0;
        value |= (hasTopBorder) ? TOP_BORDER : value;
        value |= (hasLeftBorder) ? LEFT_BORDER : value;
        value |= (hasRightBorder) ? RIGHT_BORDER : value;
        value |= (hasBottomBorder) ? BOTTOM_BORDER : value;
        value |= (isSpawnCell()) ? spawnGroup << 4 : value;
        out.writeByte(value);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        byte value = in.readByte();
        hasTopBorder = (value & TOP_BORDER) != 0;
        hasLeftBorder = (value & LEFT_BORDER) != 0;
        hasRightBorder = (value & RIGHT_BORDER) != 0;
        hasBottomBorder = (value & BOTTOM_BORDER) != 0;
        setSpawnGroup(value >> 4);
    }
}
