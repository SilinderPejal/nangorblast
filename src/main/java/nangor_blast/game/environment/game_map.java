
package nangor_blast.game.environment;

public class game_map {

    private final int width = 14;
    private final int height = 10;

    private final int spawn_x = 1;
    private final int spawn_y = 1;

    private final int exit_x = 12;
    private final int exit_y = 1;

    public int get_width() {
        return width;
    }

    public int get_height() {
        return height;
    }

    public int get_spawn_x() {
        return spawn_x;
    }

    public int get_spawn_y() {
        return spawn_y;
    }

    public int get_exit_x() {
        return exit_x;
    }

    public int get_exit_y() {
        return exit_y;
    }

    // Mengecek apakah koordinat berada di dalam map
    public boolean is_inside(int x, int y) {
        return x >= 0 && x < width
            && y >= 0 && y < height;
    }

    // Mengecek apakah tile bisa dilewati
    public boolean is_walkable(int x, int y) {
        return is_inside(x, y);
    }

    // Mengecek apakah koordinat merupakan posisi spawn
    public boolean is_spawn(int x, int y) {
        return x == spawn_x && y == spawn_y;
    }

    // Mengecek apakah koordinat merupakan posisi exit
    public boolean is_exit(int x, int y) {
        return x == exit_x && y == exit_y;
    }
}
