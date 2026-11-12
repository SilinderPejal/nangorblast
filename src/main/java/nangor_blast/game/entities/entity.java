package nangor_blast.game.entities;

public abstract class entity {
    
    protected int x;
    protected int y;
    protected boolean active;

    public entity(int x, int y) {
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public void set_position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean is_active() {
        return active;
    }

    public void set_active(boolean active) {
        this.active = active;
    }
}