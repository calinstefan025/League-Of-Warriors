public class Cell {
    public int x;
    public int y;
    public boolean visited;
    public CellEntityType type;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.visited = false;
    }

    public Cell(int x, int y, CellEntityType type) {
        this.x = x;
        this.y = y;
        this.visited = false;
        this.type = type;
    }

    // getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisited() {
        return visited;
    }

    public CellEntityType getType() {
        return type;
    }

    // setters
    public void setVisitedCell(boolean visited) {
        this.visited = visited;
    }

    public void setType(CellEntityType type) {
        this.type = type;
    }
}
