
package nangor_blast.game.system.state;

import nangor_blast.interfaces.game_state;

import nangor_blast.game.system.game_session;
import nangor_blast.game.environment.game_map;
import nangor_blast.game.entities.player.player;
import nangor_blast.game.entities.player.player_movement;

import nangor_blast.game.system.event.game_event;
import nangor_blast.game.system.event.game_event.event_type;

public class playing_state implements game_state {

    private final game_state_manager state_manager;
    private final game_session session;

    private player_movement movement;

    private boolean game_over_notified;
    private boolean finished;

    public playing_state(
        game_state_manager state_manager,
        game_session session
    ) {
        this.state_manager = state_manager;
        this.session = session;

        game_over_notified = false;
        finished = false;
    }

    @Override
    public void enter() {

        game_over_notified = false;
        finished = false;

        movement = new player_movement(
            session.get_current_map(),
            session.get_bomb_manager(),
            session.get_event_manager()
        );
    }

    @Override
    public void update() {

        if (finished) {
            return;
        }

        // Update bomb dan proses ledakan.
        session.get_bomb_manager().update();

        // Update proses respawn player.
        session.get_player_respawn().update();

        player current_player = session.get_player();

        // Update durasi invulnerability.
        current_player.update_invulnerability();

        // Periksa apakah player sudah kehabisan lives.
        if (!current_player.is_alive()) {

            if (!game_over_notified) {

                session.get_event_manager().notify_event(
                    new game_event(
                        event_type.game_over,
                        session,
                        current_player,
                        current_player.get_x(),
                        current_player.get_y(),
                        0
                    )
                );

                game_over_notified = true;
            }

            return;
        }

        // Periksa apakah player sudah mencapai exit.
        if (session.get_current_map().is_exit(
            current_player.get_x(),
            current_player.get_y()
        )) {

            finished = true;

            session.set_game_state(game_state_type.win);

            session.get_event_manager().notify_event(
                new game_event(
                    event_type.game_won,
                    session,
                    current_player,
                    current_player.get_x(),
                    current_player.get_y(),
                    0
                )
            );

            state_manager.change_state(new win_state());
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public game_state_type get_type() {
        return game_state_type.playing;
    }

    public player get_player_1() {
        return session.get_player();
    }

    public player_movement get_movement() {
        return movement;
    }

    public game_session get_session() {
        return session;
    }

    public game_map get_map() {
        return session.get_current_map();
    }
}
