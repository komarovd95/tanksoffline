package tanks.field;

import tanks.users.ManagerUser;
import tanks.utils.Direction;
import tanks.utils.persistence.DomainObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fields")
public class Field extends DomainObject implements Serializable {
    @ManyToOne(targetEntity = ManagerUser.class)
    @JoinColumn(name = "user_id")
    private ManagerUser user;

    @Column(name = "field_lob")
    @Lob
    private final FieldCell[][] fieldCells;

    @Column(name = "players_count")
    private int numberOfPlayers;

    @Column(name = "name", unique = true, length = 50)
    private String name;

    public Field(String name, int width, int height, int numberOfPlayers) {
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
        this.numberOfPlayers = numberOfPlayers;
    }

    public Field(String name) {
        this(name, 10, 10, 2);
    }

    public Field() {
        fieldCells = new FieldCell[0][0];
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

    public void setSpawnCell(int i, int j, int spawnGroup) {
        if (spawnGroup > numberOfPlayers) {
            throw new IllegalArgumentException("Spawn Group must be less or equal to players number");
        }
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
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int n) {
        this.numberOfPlayers = n;
    }

    @Override
    public String toString() {
        return String.format("[%s] Players: %d Size: (%d, %d)", name, numberOfPlayers, getWidth(), getHeight());
    }

    public String displayAscii() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString()).append("\r\n\r\n ");
        for (FieldCell[] ignored : fieldCells) {
            sb.append("_ ");
        }
        sb.append(" \r\n");
        for (int j = 0; j < getHeight(); j++) {
            sb.append("|");
            for (int i = 0; i < getWidth(); i++) {
                sb.append(fieldCells[i][j].hasBorder(Direction.BOTTOM) ? "_" : " ")
                        .append(fieldCells[i][j].hasBorder(Direction.RIGHT) ? "|" : " ");
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
