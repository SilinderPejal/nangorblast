
package nangor_blast.game.entities.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import nangor_blast.game.entities.bomb.bomb;
import nangor_blast.game.entities.bomb.explosion;
import nangor_blast.game.entities.entity;
import nangor_blast.game.environment.game_map;
import nangor_blast.game.system.bomb_manager;
import nangor_blast.game.system.event.game_event;
import nangor_blast.game.system.event.game_event.event_type;
import nangor_blast.game.system.event.game_event_manager;

public class player_respawn {

    private static final int[][] directions = {
        {0, -1},
        {1, 0},
        {0, 1},
        {-1, 0}
    };

    private final game_map map;
    private final bomb_manager bomb_manager;
    private final game_event_manager event_manager;

    private final List<player> players = new ArrayList<>();
    private final List<entity> blocking_entities = new ArrayList<>();

    private record tile_position(int x, int y) {}

    public player_respawn(
        game_map map,
        bomb_manager bomb_manager,
        game_event_manager event_manager
    ) {
        this.map = map;
        this.bomb_manager = bomb_manager;
        this.event_manager = event_manager;
    }

    public void add_player(player p) {
        if (p != null && !players.contains(p)) {
            players.add(p);
        }
    }

    public void remove_player(player p) {
        players.remove(p);
    }

    public void add_blocking_entity(entity e) {
        if (e != null && !blocking_entities.contains(e)) {
            blocking_entities.add(e);
        }
    }

    public void remove_blocking_entity(entity e) {
        blocking_entities.remove(e);
    }

    public void update() {

        for (player p : players) {

            if (!p.is_alive() || p.is_active()) {
                continue;
            }

            respawn_player(p);
        }
    }

    private boolean respawn_player(player p) {

        tile_position position = find_respawn_tile();

        if (position == null) {
            return false;
        }

        if (!p.respawn_at(position.x(), position.y())) {
            return false;
        }

        event_manager.notify_event(
            new game_event(
                event_type.player_respawned,
                p,
                p,
                position.x(),
                position.y(),
                p.get_lives()
            )
        );

        return true;
    }

    private tile_position find_respawn_tile() {

        int spawn_x = map.get_spawn_x();
        int spawn_y = map.get_spawn_y();

        if (!map.is_inside(spawn_x, spawn_y)) {
            return null;
        }

        boolean[][] visited = new boolean[
            map.get_height()
        ][
            map.get_width()
        ];

        Deque<tile_position> queue = new ArrayDeque<>();

        queue.add(new tile_position(spawn_x, spawn_y));
        visited[spawn_y][spawn_x] = true;

        while (!queue.isEmpty()) {

            tile_position current = queue.poll();

            if (is_safe_respawn_tile(current.x(), current.y())) {
                return current;
            }

            for (int[] direction : directions) {

                int next_x = current.x() + direction[0];
                int next_y = current.y() + direction[1];

                if (
                    !map.is_inside(next_x, next_y) ||
                    visited[next_y][next_x] ||
                    !map.is_walkable(next_x, next_y)
                ) {
                    continue;
                }

                visited[next_y][next_x] = true;

                queue.add(
                    new tile_position(next_x, next_y)
                );
            }
        }

        return null;
    }

    private boolean is_safe_respawn_tile(int x, int y) {

        if (!map.is_inside(x, y) || !map.is_walkable(x, y)) {
            return false;
        }

        for (bomb b : bomb_manager.get_bombs()) {

            if (
                !b.is_exploded() &&
                b.get_x() == x &&
                b.get_y() == y
            ) {
                return false;
            }
        }

        for (explosion e : bomb_manager.get_explosions()) {

            if (e.is_active() && e.covers(x, y)) {
                return false;
            }
        }

        for (player p : players) {

            if (!p.is_active()) {
                continue;
            }

            if (p.get_x() == x && p.get_y() == y) {
                return false;
            }
        }

        for (entity e : blocking_entities) {

            if (!e.is_active()) {
                continue;
            }

            if (e.get_x() == x && e.get_y() == y) {
                return false;
            }
        }

        return true;
    }
}
