package nangor_blast.interfaces;

public interface playable {

    int get_player_id();

    int get_lives();

    void lose_life();

    boolean is_alive();
}