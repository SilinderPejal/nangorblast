package nangor_blast.game.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nangor_blast.game.entities.bomb.bomb;
import nangor_blast.game.entities.bomb.bomb_type;
import nangor_blast.game.entities.bomb.explosion;
import nangor_blast.game.entities.bomb.explosion_resolver;
import nangor_blast.game.entities.bomb.normal_bomb;
import nangor_blast.game.entities.bomb.pumped_bomb;
import nangor_blast.game.entities.bomb.remote_bomb;
import nangor_blast.game.entities.entity;
import nangor_blast.game.entities.player.player;
import nangor_blast.game.system.event.game_event;
import nangor_blast.game.system.event.game_event.event_type;
import nangor_blast.game.system.event.game_event_manager;
import nangor_blast.interfaces.blast_world;

public class bomb_manager {

    private static final double max_dt = 0.25;

    private final blast_world world;
    private final game_event_manager event_manager;

    private final List<bomb> bombs = new ArrayList<>();
    private final List<explosion> explosions = new ArrayList<>();
    private final List<entity> targets = new ArrayList<>();

    private long last_nano = -1;
    private bomb_type selected_type = bomb_type.normal;

    public bomb_manager(blast_world world) {
        this(world, new game_event_manager());
    }

    public bomb_manager(
        blast_world world,
        game_event_manager event_manager
    ) {
        this.world = world;
        this.event_manager = event_manager;
    }

    public void add_target(entity target) {

        if (target != null && !targets.contains(target)) {
            targets.add(target);
        }
    }

    public boolean select_type(player owner, bomb_type type) {

        if (owner == null || type == null) {
            return false;
        }

        if (
            type == bomb_type.remote &&
            !owner.has_remote_unlocked()
        ) {
            return false;
        }

        selected_type = type;
        return true;
    }

    public bomb_type get_selected_type() {
        return selected_type;
    }

    public boolean place_selected(player owner) {
        return place_bomb(owner, selected_type);
    }

    public boolean place_bomb(player owner, bomb_type type) {

        if (
            owner == null ||
            !owner.is_active() ||
            !owner.can_place_bomb() ||
            type == null
        ) {
            return false;
        }

        if (
            type == bomb_type.remote &&
            !owner.has_remote_unlocked()
        ) {
            return false;
        }

        int x = owner.get_x();
        int y = owner.get_y();

        if (!world.is_walkable(x, y)) {
            return false;
        }

        if (get_bomb_at(x, y) != null) {
            return false;
        }

        bomb created = switch (type) {
            case normal -> new normal_bomb(x, y, owner);
            case pumped -> new pumped_bomb(x, y, owner);
            case remote -> new remote_bomb(x, y, owner);
        };

        owner.place_bomb();
        bombs.add(created);

        event_manager.notify_event(
            new game_event(
                event_type.bomb_placed,
                created,
                owner,
                x,
                y,
                0
            )
        );

        return true;
    }

    public int detonate_remote(player owner) {

        if (owner == null || !owner.has_remote_unlocked()) {
            return 0;
        }

        int count = 0;

        for (bomb b : bombs) {

            if (
                b.get_owner() == owner &&
                b.get_type() == bomb_type.remote &&
                !b.is_triggered() &&
                !b.is_exploded()
            ) {

                b.trigger();
                count++;

                event_manager.notify_event(
                    new game_event(
                        event_type.bomb_detonated,
                        b,
                        owner,
                        b.get_x(),
                        b.get_y(),
                        0
                    )
                );
            }
        }

        return count;
    }

    public void update() {

        long now = System.nanoTime();

        double dt = last_nano < 0
            ? 0
            : (now - last_nano) / 1_000_000_000.0;

        last_nano = now;

        update(dt);
    }

    public void update(double dt) {

        if (dt < 0) {
            dt = 0;
        }

        if (dt > max_dt) {
            dt = max_dt;
        }

        for (bomb b : new ArrayList<>(bombs)) {
            b.update(dt);
        }

        for (explosion e : new ArrayList<>(explosions)) {

            e.update(dt);

            if (!e.is_active()) {

                event_manager.notify_event(
                    new game_event(
                        event_type.explosion_ended,
                        e.get_source(),
                        e,
                        e.get_x(),
                        e.get_y(),
                        0
                    )
                );
            }
        }

        explosions.removeIf(e -> !e.is_active());

        for (bomb b : new ArrayList<>(bombs)) {

            if (b.is_triggered() && !b.is_exploded()) {

                List<explosion> new_explosions =
                    explosion_resolver.detonate(
                        world,
                        bombs,
                        b
                    );

                for (explosion e : new_explosions) {

                    explosions.add(e);

                    event_manager.notify_event(
                        new game_event(
                            event_type.bomb_exploded,
                            e.get_source(),
                            e,
                            e.get_x(),
                            e.get_y(),
                            e.get_source().get_blast_range()
                        )
                    );

                    event_manager.notify_event(
                        new game_event(
                            event_type.explosion_created,
                            e.get_source(),
                            e,
                            e.get_x(),
                            e.get_y(),
                            0
                        )
                    );

                    explosion_resolver.apply_damage(
                        targets,
                        e,
                        event_manager
                    );
                }
            }
        }

        bombs.removeIf(bomb::is_exploded);
    }

    public bomb get_bomb_at(int x, int y) {

        for (bomb b : bombs) {

            if (b.get_x() == x && b.get_y() == y) {
                return b;
            }
        }

        return null;
    }

    public boolean has_activity() {
        return !bombs.isEmpty() || !explosions.isEmpty();
    }

    public List<bomb> get_bombs() {
        return Collections.unmodifiableList(bombs);
    }

    public List<explosion> get_explosions() {
        return Collections.unmodifiableList(explosions);
    }
}
