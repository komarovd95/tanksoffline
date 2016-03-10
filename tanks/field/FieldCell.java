package tanks.field;

import tanks.utils.Direction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldCell implements Serializable {
    private static final byte TOP_BORDER = 8;
    private static final byte LEFT_BORDER = 4;
    private static final byte RIGHT_BORDER = 2;
    private static final byte BOTTOM_BORDER = 1;

    private static final Logger logger = Logger.getLogger("FieldCell");

    private transient boolean hasTopBorder;
    private transient boolean hasLeftBorder;
    private transient boolean hasRightBorder;
    private transient boolean hasBottomBorder;
    private transient int spawnGroup;

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

    public void setSpawnGroup(int spawnGroup) {
        if (spawnGroup < 0 || spawnGroup > 15) {
            logger.log(Level.SEVERE, "Spawn group's limits exceeded : " + spawnGroup);
            throw new IllegalArgumentException("Spawn group must be in [0..15] interval");
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
