package gamefield;

class Cell {
    private int x;
    private int y;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell[] getNeighbours() {
        Cell[] neighbours = {
            new Cell(x - 1, y - 1),
            new Cell(x, y - 1),
            new Cell(x + 1, y - 1),
            new Cell(x + 1, y),
            new Cell(x + 1, y + 1),
            new Cell(x, y + 1),
            new Cell(x - 1, y + 1),
            new Cell(x - 1, y)
        };
        return neighbours;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}