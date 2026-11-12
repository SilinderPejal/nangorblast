package nangor_blast.interfaces;

// Milik F02 (bomb): objek yang bisa dipicu lalu meledak.
public interface explodable {

    void trigger();

    boolean is_triggered();
}
