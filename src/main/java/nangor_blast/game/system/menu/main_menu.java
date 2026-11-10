package nangor_blast.game.system.menu;

public enum main_menu {
    start_game("Start Game"),
    how_to_play("How to Play"),
    exit("Exit");

    private final String label;

    main_menu(String label) {
        this.label = label;
    }

    public String get_label() {
        return label;
    }
}