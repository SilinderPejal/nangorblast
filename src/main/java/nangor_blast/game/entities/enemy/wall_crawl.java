package nangor_blast.game.entities.enemy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nangor_blast.game.entities.block.indestructible_block;
import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;

public class wall_crawl extends boss_attack {

    private static final int max_path_length = 12;
    private static final double crawl_duration = 1.0;

    private final List<int[]> path = new ArrayList<>();
    private int path_index;
    private double elapsed;

    @Override
    public String get_name() {
        return "Wall Crawl";
    }

    @Override
    public void prepare(boss_enemy boss, entity target, blast_world world) {

        path.clear();
        path_index = 0;
        elapsed = 0;

        if (target == null) {
            return;
        }

        path.addAll(find_path(boss, target, world));
    }

    @Override
    public void on_active_tick(boss_enemy boss, double delta_time, blast_world world) {

        if (path.isEmpty()) {
            return;
        }

        elapsed += delta_time;

        int should_be_at = Math.min(
            path.size(),
            (int) Math.floor(elapsed / crawl_duration * path.size())
        );

        while (path_index < should_be_at) {
            int[] tile = path.get(path_index++);
            boss.set_position(tile[0], tile[1]);
        }
    }

    @Override
    public void on_active_end(boss_enemy boss, blast_world world) {

        if (!path.isEmpty()) {
            int[] last = path.get(path.size() - 1);
            boss.set_position(last[0], last[1]);
            path_index = path.size();
        }
    }

    @Override
    public boss_attack get_follow_up(boss_enemy boss) {
        return boss.get_current_phase() == 2 ? new scythe_swipe() : null;
    }

    @Override
    public double get_active_duration(int phase) {
        return crawl_duration;
    }

    private List<int[]> find_path(boss_enemy boss, entity target, blast_world world) {

        int start_x = boss.get_x();
        int start_y = boss.get_y();

        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Integer> distance = new HashMap<>();
        Deque<int[]> queue = new ArrayDeque<>();

        int start_key = key(start_x, start_y);
        parent.put(start_key, -1);
        distance.put(start_key, 0);
        queue.add(new int[] { start_x, start_y });

        int best_key = start_key;
        int best_gap = manhattan(start_x, start_y, target.get_x(), target.get_y());

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        while (!queue.isEmpty()) {

            int[] current = queue.poll();
            int current_key = key(current[0], current[1]);
            int current_distance = distance.get(current_key);

            if (current_distance >= max_path_length) {
                continue;
            }

            for (int[] d : directions) {

                int nx = current[0] + d[0];
                int ny = current[1] + d[1];
                int next_key = key(nx, ny);

                if (
                    parent.containsKey(next_key)
                    || world.is_out_of_bounds(nx, ny)
                    || !world.is_walkable(nx, ny)
                    || !is_next_to_wall(world, nx, ny)
                    || (nx == target.get_x() && ny == target.get_y())
                ) {
                    continue;
                }

                parent.put(next_key, current_key);
                distance.put(next_key, current_distance + 1);
                queue.add(new int[] { nx, ny });

                int gap = manhattan(nx, ny, target.get_x(), target.get_y());

                if (
                    gap < best_gap
                    || (gap == best_gap && distance.get(next_key) < distance.get(best_key))
                ) {
                    best_gap = gap;
                    best_key = next_key;
                }
            }
        }

        List<int[]> result = new ArrayList<>();

        for (int k = best_key; k != start_key; k = parent.get(k)) {
            result.add(new int[] { k / 1000, k % 1000 });
        }

        Collections.reverse(result);
        return result;
    }

    private boolean is_next_to_wall(blast_world world, int x, int y) {
        return is_wall(world, x + 1, y) || is_wall(world, x - 1, y)
            || is_wall(world, x, y + 1) || is_wall(world, x, y - 1);
    }

    private boolean is_wall(blast_world world, int x, int y) {
        return world.get_block_at(x, y).orElse(null) instanceof indestructible_block;
    }

    private int key(int x, int y) {
        return x * 1000 + y;
    }

    private int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
