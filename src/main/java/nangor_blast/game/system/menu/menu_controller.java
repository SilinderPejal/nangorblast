package nangor_blast.game.system.menu;

public class menu_controller {
    private int selected_index;

    public menu_controller() {
        selected_index = 0;
    }

    public void move_selection(int direction) {
        int option_count = main_menu.values().length;

        selected_index = Math.floorMod(
            selected_index + direction,
            option_count
        );
    }

    public main_menu select() {
        return main_menu.values()[selected_index];
    }

    public int get_selected_index() {
        return selected_index;
    }
}
