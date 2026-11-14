package nangor_blast.game.system;

import java.util.Optional;

import nangor_blast.game.environment.game_map;
import nangor_blast.interfaces.blast_block;
import nangor_blast.interfaces.blast_world;

// Adapter: membuat game_map milik Erin bisa dibaca explosion_resolver.
// game_map saat ini belum punya block, jadi get_block_at selalu kosong.
// Kalau F04/F07 sudah menambahkan block ke game_map, cukup ubah get_block_at di sini.
public class map_blast_world implements blast_world {

    private final game_map map;

    public map_blast_world(game_map map) {
        this.map = map;
    }

    @Override
    public boolean is_out_of_bounds(int x, int y) {
        return !map.is_inside(x, y);
    }

    @Override
    public Optional<blast_block> get_block_at(int x, int y) {
        return Optional.empty();
    }
}
