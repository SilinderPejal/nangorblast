package nangor_blast.game.entities.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nangor_blast.game.entities.entity;
import nangor_blast.interfaces.blast_world;
import nangor_blast.interfaces.damageable;

// Induk semua boss (F05.3). Siklus serangan dijalankan di update():
// pilih serangan acak (tidak sama dua kali berturut-turut) -> warning 1 detik
// -> active -> cooldown 5 detik. Rentan 4 detik pertama cooldown, hanya jika
// serangan terakhir memberi kerentanan.
public abstract class boss_enemy extends enemy {

    protected static final double warning_duration = 1.0;
    protected static final double cooldown_duration = 5.0;
    protected static final double vulnerable_window = 4.0;
    protected static final int attack_damage = 1;

    protected final List<boss_attack> attacks = new ArrayList<>();
    private final List<enemy> pending_spawns = new ArrayList<>();
    private final Random random = new Random();

    protected int current_phase;
    protected int facing_dx;
    protected int facing_dy;

    private attack_phase attack_state;
    private boss_attack current_attack;
    private boss_attack last_attack;
    private double timer;
    private boolean vulnerable;
    private boolean vulnerable_eligible;
    private boolean hit_applied;

    protected boss_enemy(int x, int y, int max_health, double speed, int score_value) {
        super(x, y, max_health, speed, score_value);

        this.current_phase = 1;
        this.facing_dx = 0;
        this.facing_dy = 1;
        this.attack_state = attack_phase.ready;
    }

    @Override
    public void update(double delta_time, blast_world world, entity target) {

        if (!is_active()) {
            return;
        }

        switch (attack_state) {

            case ready -> start_next_attack(world, target);

            case warning -> {
                timer -= delta_time;

                if (timer <= 0) {
                    attack_state = attack_phase.active;
                    timer = current_attack.get_active_duration(current_phase);
                    hit_applied = false;
                    current_attack.on_active_start(this, target, world);
                }
            }

            case active -> {
                apply_attack_damage(target);
                timer -= delta_time;

                if (timer <= 0) {
                    finish_attack();
                }
            }

            case cooldown -> {
                timer -= delta_time;

                vulnerable = vulnerable_eligible
                    && timer > cooldown_duration - vulnerable_window;

                if (timer <= 0) {
                    vulnerable = false;
                    attack_state = attack_phase.ready;
                }
            }
        }
    }

    private void start_next_attack(blast_world world, entity target) {

        List<boss_attack> candidates = new ArrayList<>();

        for (boss_attack attack : attacks) {
            if (attack.can_use(this, target, world)) {
                candidates.add(attack);
            }
        }

        // Jangan pilih serangan yang sama dua kali berturut-turut jika ada pilihan lain.
        if (candidates.size() > 1) {
            candidates.remove(last_attack);
        }

        if (candidates.isEmpty()) {
            return;
        }

        current_attack = candidates.get(random.nextInt(candidates.size()));
        current_attack.get_affected_tiles().clear();
        current_attack.prepare(this, target, world);

        attack_state = attack_phase.warning;
        timer = warning_duration;
    }

    // Player di area serangan saat aktif kehilangan 1 nyawa, sekali per aktivasi.
    private void apply_attack_damage(entity target) {

        if (hit_applied || target == null || !target.is_active()) {
            return;
        }

        if (
            target instanceof damageable d
            && current_attack.covers(target.get_x(), target.get_y())
            && d.can_be_damaged()
        ) {
            d.take_damage(attack_damage);
            hit_applied = true;
        }
    }

    private void finish_attack() {

        vulnerable_eligible = current_attack.grants_vulnerability();
        last_attack = current_attack;
        current_attack.get_affected_tiles().clear();
        current_attack = null;

        attack_state = attack_phase.cooldown;
        timer = cooldown_duration;
        vulnerable = vulnerable_eligible;
    }

    // Boss hanya menerima damage saat rentan (bomb: 1 normal/remote, 2 pumped).
    @Override
    public boolean can_be_damaged() {
        return is_active() && vulnerable;
    }

    @Override
    public void take_damage(int damage) {

        if (!can_be_damaged() || damage <= 0) {
            return;
        }

        health -= damage;

        if (current_phase == 1 && health <= max_health / 2) {
            current_phase = 2;
        }

        if (health <= 0) {
            health = 0;
            vulnerable = false;
            set_active(false);
        }
    }

    // Tile yang perlu digambar sebagai '!' (warning) atau '*' (aktif) oleh renderer.
    public List<int[]> get_danger_tiles() {

        if (current_attack == null) {
            return List.of();
        }

        return current_attack.get_affected_tiles();
    }

    public attack_phase get_attack_state() {
        return attack_state;
    }

    public boolean is_vulnerable() {
        return vulnerable;
    }

    public int get_current_phase() {
        return current_phase;
    }

    public int get_facing_dx() {
        return facing_dx;
    }

    public int get_facing_dy() {
        return facing_dy;
    }

    public void set_facing(int dx, int dy) {
        this.facing_dx = dx;
        this.facing_dy = dy;
    }

    // Musuh baru (mis. Hatchling) menunggu didaftarkan oleh game session.
    public void request_spawn(enemy spawned) {
        pending_spawns.add(spawned);
    }

    public List<enemy> drain_pending_spawns() {
        List<enemy> result = new ArrayList<>(pending_spawns);
        pending_spawns.clear();
        return result;
    }
}
