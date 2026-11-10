package nangor_blast.game.system.menu;

import nangor_blast.interfaces.game_state;

public class menu_state implements game_state {

    private final menu_controller menu_controller;

    public menu_state() {
        this.menu_controller = new menu_controller();
    }

    @Override
    public void enter() {
    }

    @Override
    public void update() {
    }

    @Override
    public void exit() {
    }

    @Override
    public game_state.game_state_type get_type() {
        return game_state.game_state_type.menu;
    }

    public void move_selection(int direction) {
        menu_controller.move_selection(direction);
    }

    public main_menu select() {
        return menu_controller.select();
    }

    public int get_selected_index() {
        return menu_controller.get_selected_index();
    }
}