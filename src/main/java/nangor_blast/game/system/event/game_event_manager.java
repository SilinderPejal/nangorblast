package nangor_blast.game.system.event;

import java.util.ArrayList;
import java.util.List;

import nangor_blast.interfaces.game_event_listener;

public class game_event_manager {

    private final List<game_event_listener> listeners = new ArrayList<>();

    public void add_listener(game_event_listener listener) {

        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void remove_listener(game_event_listener listener) {
        listeners.remove(listener);
    }

    public void notify_event(game_event event) {

        if (event == null) {
            return;
        }

        for (game_event_listener listener : new ArrayList<>(listeners)) {
            listener.on_game_event(event);
        }
    }
}