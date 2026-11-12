
package nangor_blast.game.entities.player;

import nangor_blast.game.environment.game_map;

public class player_movement {

    private final game_map map;

    public player_movement(game_map map) {
        this.map = map;
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

        player.move(dx, dy, 1.0);

        return true;
    }
}
