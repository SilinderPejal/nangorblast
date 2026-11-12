package nangor_blast.interfaces;

public interface moveable {

    double get_speed();

    void move(int dx, int dy, double delta_time);
}
