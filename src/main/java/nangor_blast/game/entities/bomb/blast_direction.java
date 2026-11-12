package nangor_blast.game.entities.bomb;

public enum blast_direction {

    up(0, -1),
    down(0, 1),
    left(-1, 0),
    right(1, 0);

    private final int dx;
    private final int dy;

    blast_direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int get_dx() {
        return dx;
    }

    public int get_dy() {
        return dy;
    }
}
