package dev.hephaestus.esther.spells;

import dev.hephaestus.esther.Esther;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.HashMap;

public class SpellRegistry {
    private HashMap<String, Spell> registeredIncantations = new HashMap<>();
    private HashMap<Identifier, Spell> registeredSpells = new HashMap<>();

    public SpellRegistry() {};

    public Spell register(Spell spell, String incantation) {
        Esther.log("Registered " + spell.getId() + " with incantation \"" + incantation + "\"");
        registeredIncantations.put(incantation.toLowerCase(), spell);
        registeredSpells.put(spell.getId(), spell);
        return spell;
    }

    public Spell register(Identifier id, String incantation) {
        if (registeredSpells.containsKey(id)) {
            Esther.log("Added incantation \"" + incantation + "\" to spell " + id);
            registeredIncantations.put(incantation.toLowerCase(), registeredSpells.get(id));
            return registeredSpells.get(id);
        } else {
            Esther.log("Could not register incantation; spell \"" + id + "\" does not exist");
            return null;
        }
    }

    public TypedActionResult<Spell> get(String incantation) {
        return registeredIncantations.containsKey(incantation.toLowerCase()) ?
                new TypedActionResult<>(ActionResult.SUCCESS, registeredIncantations.get(incantation.toLowerCase())) :
                TypedActionResult.pass(null);
    }

    public TypedActionResult<Spell> get(Identifier id) {
        return registeredSpells.containsKey(id) ?
                new TypedActionResult<>(ActionResult.SUCCESS, registeredSpells.get(id)) :
                TypedActionResult.pass(null);
    }
}
