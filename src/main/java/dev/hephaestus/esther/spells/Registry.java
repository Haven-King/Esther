package dev.hephaestus.esther.spells;

import com.google.common.collect.ImmutableList;
import dev.hephaestus.esther.Esther;
import dev.hephaestus.esther.spells.aura.Aura;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Registry {
    private HashMap<String, Spell> registeredIncantations = new HashMap<>();
    private HashMap<Identifier, Spell> registeredSpells = new HashMap<>();
    private ArrayList<Aura> registeredAura = new ArrayList<>();
    
    // The spells are going here because I don't like a cluttered main class
    // and I like being able to access them from Esther.SPELLS.<SPELL_NAME>

    public Registry() {
        // Adding more incantations goes here cause ¯\_(ツ)_/¯
    }

    public Spell register(Spell spell, String incantation) {
        Esther.log("Registered " + spell.getId() + " with incantation \"" + incantation + "\"");
        registeredIncantations.put(incantation.toLowerCase(), spell);
        registeredSpells.put(spell.getId(), spell);
        return spell;
    }

    public Identifier register(Identifier id, String incantation) {
        if (registeredSpells.containsKey(id)) {
            Esther.log("Added incantation \"" + incantation + "\" to spell " + id);
            registeredIncantations.put(incantation.toLowerCase(), registeredSpells.get(id));
            return id;
        } else {
            Esther.log("Could not register incantation; spell \"" + id + "\" does not exist");
            return null;
        }
    }

    public Spell register(Aura aura, String activate_incantation, String deactivate_incantation) {
        Spell spell = register(aura, activate_incantation);
        register(spell.getId(), deactivate_incantation);
        registeredAura.add(aura);
        return spell;
    }

    public Collection<Aura> getRegisteredAura() {
        return ImmutableList.copyOf(registeredAura);
    }

    public TypedActionResult<Spell> get(String incantation) {
        return registeredIncantations.containsKey(incantation.toLowerCase()) ?
                new TypedActionResult<>(ActionResult.SUCCESS, registeredIncantations.get(incantation.toLowerCase())) :
                TypedActionResult.pass(null);
    }

    public Spell get(Identifier id) {
        return registeredSpells.get(id);
    }
}
