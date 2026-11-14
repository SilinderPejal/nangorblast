package nangor_blast.game.environment;

public class ice_tile {

    private final game_map map;

    public ice_tile(game_map map) {
        this.map = map;
    }

    public boolean is_on_ice(int x, int y) {
        return map.get_tile_at(x, y) == 'I';
    }
}