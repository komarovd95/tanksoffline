package com.tanksoffline.application.data;

import com.tanksoffline.application.utils.Direction;

public interface Field {
    boolean hasBorder(int i, int j, Direction direction);
    void setBorder(int i, int j, Direction direction);
    void removeBorder(int i, int j, Direction direction);

    FieldCell[][] getFieldCells();
    FieldCell getFieldCell(int i, int j);

    int getWidth();
    int getHeight();

    String getName();
    void setName(String name);

    User getOwner();
}
