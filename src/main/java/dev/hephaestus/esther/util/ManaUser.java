package dev.hephaestus.esther.util;

public interface ManaUser {
    int getManaLevel();
    void setMana(int mana);
    void useMana(int mana);
    void resetMana();
}
