package nangor_blast.interfaces;

import java.util.Optional;

// Milik F03 (explosion): yang dibutuhkan ledakan dari peta.
// game_map belum punya block, jadi nanti game_map (atau adapter kecil) mengimplementasikan ini.
// Koordinat 0-based seperti game_map: x 0..13, y 0..9.
public interface blast_world {

    boolean is_out_of_bounds(int x, int y);

    // block aktif di tile itu, atau kosong kalau tile bebas (Ice Tile dianggap bebas).
    Optional<blast_block> get_block_at(int x, int y);
}
