package nangor_blast.game.system;

public class score_tracker {
    private static final int POINTS_PER_LIFE = 100;
    private static final int POINTS_PER_SECOND = 10;

    private int score;
    private boolean win_bonus_added;

    // Panggil sekali ketika musuh dikalahkan. Poin mengikuti jenis musuh.
    public void add_enemy_score(int points) {
        score += points;
    }

    public void add_win_bonus(int remaining_lives, int remaining_seconds) {
        // Cegah bonus dihitung dua kali jika alur menang memanggil method ini lagi.
        if (win_bonus_added) {
            return;
        }

        score += remaining_lives * POINTS_PER_LIFE
                + remaining_seconds * POINTS_PER_SECOND;
        win_bonus_added = true;
    }

    public int get_score() {
        return score;
    }

    public void reset() {
        score = 0;
        // Pertandingan baru bisa mendapat bonus kemenangan lagi.
        win_bonus_added = false;
    }
}
