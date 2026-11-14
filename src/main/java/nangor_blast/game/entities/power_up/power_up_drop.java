package nangor_blast.game.entities.power_up;

import java.util.concurrent.ThreadLocalRandom;

import nangor_blast.game.environment.game_map;

public final class power_up_drop {

    private static final int drop_chance_percent = 30;

    private power_up_drop() {
    }

    public static boolean try_spawn(game_map map, int x, int y) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Dari 100 kemungkinan, hanya angka 0 sampai 29 yang memicu drop.
        if (random.nextInt(100) >= drop_chance_percent) {
            return false;
        }

        // Setelah drop berhasil, pilih satu dari lima jenis Power-Up.
        power_up item = switch (random.nextInt(5)) {
            case 0 -> new bomb_up(x, y);
            case 1 -> new fire_up(x, y);
            case 2 -> new speed_up(x, y);
            case 3 -> new remote_detonator(x, y);
            default -> new heart(x, y);
        };

        return map.place_power_up(item);
    }
}
