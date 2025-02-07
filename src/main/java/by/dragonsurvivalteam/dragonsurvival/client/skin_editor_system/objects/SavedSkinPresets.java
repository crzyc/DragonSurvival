package by.dragonsurvivalteam.dragonsurvival.client.skin_editor_system.objects;

import java.util.HashMap;

public class SavedSkinPresets {
    /** The current 'saved_customizations.json' values */
    public final HashMap</* Dragon type */ String, HashMap</* Save slot */ Integer, SkinPreset>> skinPresets = new HashMap<>();
    public final HashMap</* Dragon type */ String, HashMap</* Dragon level */ String, /* Save slot */ Integer>> current = new HashMap<>();

    public int version;
}