package nangor_blast.interfaces;

import java.util.Optional;

public interface blast_world {

    boolean is_out_of_bounds(int x, int y);

    boolean is_walkable(int x, int y);

    Optional<blast_block> get_block_at(int x, int y);
}
