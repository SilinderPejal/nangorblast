package nangor_blast.interfaces;

import nangor_blast.game.entities.bomb.bomb;

// Milik F03 (explosion): yang dibutuhkan ledakan dari sebuah block (F04).
public interface blast_block {

    // true kalau bom ini menghancurkan block. Block yang tidak hancur tetap menghentikan ledakan.
    boolean is_destroyed_by(bomb source);

    void destroy();
}
