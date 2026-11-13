
package nangor_blast.game.environment;

import java.util.Optional;

import nangor_blast.game.entities.block.block;
import nangor_blast.game.entities.block.destructible_block;
import nangor_blast.game.entities.block.indestructible_block;
import nangor_blast.interfaces.blast_block;
import nangor_blast.interfaces.blast_world;

public class game_map implements blast_world {

    private final int width = 14;
    private final int height = 10;

    private final int spawn_x = 1;
    private final int spawn_y = 1;

    private final int exit_x = 12;
    private final int exit_y = 1;

    private final block[][] blocks = new block[height][width];

    public game_map() {
        String[] layout = {
            "##############",
            "#P..I...D...X#",
            "#.##I##.##.E.#",
            "#.E.I....I...#",
            "###.###.###..#",
            "#D..I...D.E..#",
            "#.##.###.##..#",
            "#...I....B...#",
            "#E..D....I..E#",
            "##############"
        };

        // Array memakai urutan [baris][kolom], yaitu [y][x].
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (layout[y].charAt(x)) {
                    case '#' -> blocks[y][x] = new indestructible_block(x, y);
                    case 'D' -> blocks[y][x] = new destructible_block(x, y);
                }
            }
        }
    }

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

    // Mengecek apakah tile berada di dalam map dan bebas dari block aktif.
    public boolean is_walkable(int x, int y) {
        return is_inside(x, y) && get_block_at(x, y).isEmpty();
    }

    @Override
    public boolean is_out_of_bounds(int x, int y) {
        return !is_inside(x, y);
    }

    @Override
    public Optional<blast_block> get_block_at(int x, int y) {
        if (is_out_of_bounds(x, y)) {
            return Optional.empty();
        }

        block tile_block = blocks[y][x];

        // Block yang sudah hancur diperlakukan sebagai tile kosong.
        if (tile_block == null || !tile_block.is_active()) {
            return Optional.empty();
        }

        return Optional.of(tile_block);
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
