package nangor_blast.game.entities.bomb;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_block;
import nangor_blast.interfaces.blast_world;
import nangor_blast.interfaces.damageable;

public final class explosion_resolver {

    private static final int player_damage = 1;

    private explosion_resolver() {
    }

    // Meledakkan satu bom beserta seluruh rantainya. Iteratif (bukan rekursif),
    // dan tiap bom hanya diproses satu kali, jadi rantai pasti berhenti.
    public static List<explosion> detonate(blast_world world, List<bomb> all_bombs, bomb first) {
        List<explosion> result = new ArrayList<>();
        if (first.is_exploded()) {
            return result;
        }
        Set<bomb> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        Deque<bomb> queue = new ArrayDeque<>();
        first.trigger();
        seen.add(first);
        queue.add(first);

        while (!queue.isEmpty()) {
            bomb current = queue.poll();
            if (current.is_exploded()) {
                continue;
            }
            current.mark_exploded();
            current.get_owner().bomb_exploded();

            List<blast_block> to_destroy = new ArrayList<>();
            Set<blast_tile> area = propagate(world, current, to_destroy);
            result.add(new explosion(current, area));

            for (blast_block block : to_destroy) {
                block.destroy();
            }
            for (bomb other : all_bombs) {
                if (!seen.contains(other) && !other.is_exploded()
                        && area.contains(new blast_tile(other.get_x(), other.get_y()))) {
                    other.trigger();
                    seen.add(other);
                    queue.add(other);
                }
            }
        }
        return result;
    }

    // Memberi damage ke semua entity aktif yang bisa terkena damage dan berada di area ledakan.
    // Player yang sedang kebal tidak terkena karena can_be_damaged() bernilai false.
    public static void apply_damage(List<? extends entity> entities, explosion blast) {
        for (entity target : entities) {
            if (target.is_active() && target instanceof damageable d
                    && blast.covers(target.get_x(), target.get_y()) && d.can_be_damaged()) {
                d.take_damage(player_damage);
            }
        }
    }

    // Menghitung area satu ledakan: pusat + 4 arah. Tile block tidak pernah masuk area.
    static Set<blast_tile> propagate(blast_world world, bomb source, List<blast_block> to_destroy) {
        Set<blast_tile> area = new LinkedHashSet<>();
        area.add(new blast_tile(source.get_x(), source.get_y()));
        for (blast_direction d : blast_direction.values()) {
            int x = source.get_x();
            int y = source.get_y();
            for (int i = 0; i < source.get_blast_range(); i++) {
                x += d.get_dx();
                y += d.get_dy();
                if (world.is_out_of_bounds(x, y)) {
                    break;
                }
                Optional<blast_block> block = world.get_block_at(x, y);
                if (block.isPresent()) {
                    if (block.get().is_destroyed_by(source)) {
                        to_destroy.add(block.get());
                    }
                    break;
                }
                area.add(new blast_tile(x, y));
            }
        }
        return area;
    }
}
