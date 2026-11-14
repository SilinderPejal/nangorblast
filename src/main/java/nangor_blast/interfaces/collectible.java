package nangor_blast.interfaces;

import nangor_blast.game.entities.player.player;

public interface collectible {

    // Mengembalikan true jika pemain berhasil mengambil item.
    boolean collect(player collector);
}
