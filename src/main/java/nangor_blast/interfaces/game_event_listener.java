package nangor_blast.interfaces;

import nangor_blast.game.system.event.game_event;

public interface game_event_listener {

    void on_game_event(game_event event);
}