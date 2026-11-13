package nangor_blast.game.entities.block;

import nangor_blast.game.entities.bomb.bomb;

public class indestructible_block extends block {

    public indestructible_block(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean is_destroyed_by(bomb source) {
        return false;
    }

    @Override
    public void destroy() {
        // Block ini tetap aktif ketika terkena ledakan.
    }
}
