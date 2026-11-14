package nangor_blast.game.entities.block;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_block;

public abstract class block extends entity implements blast_block {

    protected block(int x, int y) {
        super(x, y);
    }
}
