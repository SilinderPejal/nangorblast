
package nangor_blast.game.environment;

import java.util.Optional;

import nangor_blast.game.entities.block.block;
import nangor_blast.game.entities.block.destructible_block;
import nangor_blast.game.entities.block.indestructible_block;
import nangor_blast.interfaces.blast_world;
import nangor_blast.interfaces.blast_block;
import nangor_blast.game.entities.player.player;
import nangor_blast.game.entities.power_up.power_up;

public class game_map implements blast_world {

    private final int width = 14;
    private final int height = 10;

    private final int spawn_x = 1;
    private final int spawn_y = 1;

    private final int exit_x = 12;
    private final int exit_y = 1;

    private final block[][] blocks = new block[height][width];
    private final power_up[][] power_ups = new power_up[height][width];
    private final char[][] tiles = new char[height][width];

    public game_map() {

        String[] layout = map_layout.default_glacier;

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                char tile = layout[y].charAt(x);

                tiles[y][x] = tile;

                switch (tile) {

                    case '#':
                        blocks[y][x] = new indestructible_block(x, y);
                        break;

                    case 'D':
                        blocks[y][x] = new destructible_block(x, y);
                        break;

                    default:
                        blocks[y][x] = null;
                        break;
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

    public char get_tile_at(int x, int y) {

        if (!is_inside(x, y)) {
            return '\0';
        }

        return tiles[y][x];
    }

    public boolean is_inside(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean is_walkable(int x, int y) {

        if (!is_inside(x, y)) {
            return false;
        }

        char tile = get_tile_at(x, y);

        switch (tile) {

            case '#':
                return false;

            case 'D':
                return get_block_at(x, y).isEmpty();

            case 'I':
            case 'P':
            case 'E':
            case 'B':
            case 'X':
            case '.':
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean is_out_of_bounds(int x, int y) {
        return !is_inside(x, y);
    }

    @Override
    public Optional<blast_block> get_block_at(int x, int y) {

        if (!is_inside(x, y)) {
            return Optional.empty();
        }

        block tile_block = blocks[y][x];

        if (tile_block == null || !tile_block.is_active()) {
            return Optional.empty();
        }

        return Optional.of(tile_block);
    }

    // Simpan maksimal satu item pada setiap tile yang bisa dilalui.
    public boolean place_power_up(power_up item) {
        int x = item.get_x();
        int y = item.get_y();

        if (!item.is_active() || !is_walkable(x, y) || power_ups[y][x] != null) {
            return false;
        }

        power_ups[y][x] = item;
        return true;
    }

    // Cari item pada koordinat yang diminta, jika ada.
    public Optional<power_up> get_power_up_at(int x, int y) {
        if (!is_inside(x, y)) {
            return Optional.empty();
        }

        return Optional.ofNullable(power_ups[y][x]);
    }

    public void collect_power_up(player collector) {
        int x = collector.get_x();
        int y = collector.get_y();
        power_up item = get_power_up_at(x, y).orElse(null);

        // Hapus item dari map setelah efeknya berhasil diterapkan.
        if (item != null && item.collect(collector)) {
            power_ups[y][x] = null;
        }
    }

    public boolean is_spawn(int x, int y) {
        return x == spawn_x && y == spawn_y;
    }

    public boolean is_exit(int x, int y) {
        return x == exit_x && y == exit_y;
    }
}
