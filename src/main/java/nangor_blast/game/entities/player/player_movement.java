
package nangor_blast.game.entities.player;

import nangor_blast.game.environment.game_map;
import nangor_blast.game.environment.ice_tile;

public class player_movement {

    private final game_map map;
    private final ice_tile ice;

    public player_movement(game_map map) {
        this.map = map;
        this.ice = new ice_tile(map);
    }

    public boolean move_player(player player, int dx, int dy) {

        if (!player.is_alive()) {
            return false;
        }

        // Hanya memperbolehkan gerakan satu tile
        if (Math.abs(dx) + Math.abs(dy) != 1) {
            return false;
        }

        int new_x = player.get_x() + dx;
        int new_y = player.get_y() + dy;

        // Mengecek batas map dan tile tujuan
        if (!map.is_walkable(new_x, new_y)) {
            return false;
        }

        // Memindahkan player ke tile tujuan
        player.move(dx, dy, 1.0);

        // Melanjutkan gerakan selama player berada di Ice Tile
        while (ice.is_on_ice(player.get_x(), player.get_y())) {

            int next_x = player.get_x() + dx;
            int next_y = player.get_y() + dy;

            // Berhenti jika terdapat obstacle
            if (!map.is_walkable(next_x, next_y)) {
                break;
            }

            player.move(dx, dy, 1.0);
        }

        return true;
    }
}
