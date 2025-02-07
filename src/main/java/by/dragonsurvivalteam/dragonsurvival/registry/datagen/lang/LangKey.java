package by.dragonsurvivalteam.dragonsurvival.registry.datagen.lang;

import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;

/** Translation keys which are used in multiple places */
public class LangKey {
    /** This is not assigned to one element but rather used at two places which dynamically create the translation key / translation */
    public static final String CATEGORY_PREFIX = Translation.Type.CONFIGURATION.prefix + "category.";

    // --- GUI --- //

    @Translation(type = Translation.Type.MISC, comments = "Cancel")
    public static final String GUI_CANCEL = Translation.Type.GUI.wrap("general.cancel");

    @Translation(type = Translation.Type.MISC, comments = "Confirm")
    public static final String GUI_CONFIRM = Translation.Type.GUI.wrap("general.confirm");

    @Translation(type = Translation.Type.MISC, comments = "Glowing")
    public static final String GUI_GLOWING = Translation.Type.GUI.wrap("general.glowing");

    @Translation(type = Translation.Type.MISC, comments = "DRAGON EDITOR")
    public static final String GUI_DRAGON_EDITOR = Translation.Type.GUI.wrap("general.dragon_editor");

    // --- GUI messages --- //

    @Translation(type = Translation.Type.MISC, comments = "Hunger has exhausted you, and you can't fly.")
    public static final String MESSAGE_NO_HUNGER = Translation.Type.GUI.wrap("message.no_hunger");

    // --- Abilities --- //

    @Translation(type = Translation.Type.MISC, comments = "§6■ Damage:§r %s")
    public static final String ABILITY_DAMAGE = Translation.Type.ABILITY.wrap("general.damage");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Range:§r %s blocks")
    public static final String ABILITY_RANGE = Translation.Type.ABILITY.wrap("general.range_blocks");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Radius:§r %s")
    public static final String ABILITY_AOE = Translation.Type.ABILITY.wrap("general.aoe");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Cooldown:§r %ss")
    public static final String ABILITY_COOLDOWN = Translation.Type.ABILITY.wrap("general.cooldown");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Chance: §c%s%%§r")
    public static final String ABILITY_CHANCE = Translation.Type.ABILITY.wrap("general.chance");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Mana cost:§r %s")
    public static final String ABILITY_MANA_COST = Translation.Type.ABILITY.wrap("general.mana_cost");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Cast time:§r %ss")
    public static final String ABILITY_CAST_TIME = Translation.Type.ABILITY.wrap("general.cast_time");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Duration:§r %ss")
    public static final String ABILITY_DURATION = Translation.Type.ABILITY.wrap("general.duration");

    @Translation(type = Translation.Type.MISC, comments = "§6■ Currently bound to:§r [%s]")
    public static final String ABILITY_KEYBIND = Translation.Type.ABILITY.wrap("general.keybind");

    // --- Misc --- //

    @Translation(type = Translation.Type.MISC, comments = "Kingdom Explorer Map")
    public static final String ITEM_KINGDOM_EXPLORER_MAP = Translation.Type.ITEM.wrap("kingdom_explorer_map");
}
