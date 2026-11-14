package nangor_blast.interfaces;

public interface game_state {

    enum game_state_type {
        menu,
        playing,
        win,
        lose
    }

    void enter();

    void update();

    void exit();

    game_state_type get_type();
}