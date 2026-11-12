package nangor_blast.interfaces;

public interface damageable {

    boolean can_be_damaged();

    void take_damage(int damage);
}