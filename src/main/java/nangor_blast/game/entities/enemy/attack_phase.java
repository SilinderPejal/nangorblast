package nangor_blast.game.entities.enemy;

// Tahapan satu siklus serangan boss (F05.3.1):
// ready -> warning (1 detik) -> active -> cooldown (5 detik) -> ready
public enum attack_phase {
    ready,
    warning,
    active,
    cooldown
}
