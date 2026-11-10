package nangor_blast;

import nangor_blast.exception.cli_exception;
import nangor_blast.game.system.display.cli.cli_application;

public class Main {
    public static void main(String[] args) {
        cli_application application = new cli_application();

        try {
            application.start();
        } catch (cli_exception e) {
            e.printStackTrace();
        } finally {
            try {
                application.stop();
            } catch (cli_exception e) {
                e.printStackTrace();
            }
        }
    }
}
