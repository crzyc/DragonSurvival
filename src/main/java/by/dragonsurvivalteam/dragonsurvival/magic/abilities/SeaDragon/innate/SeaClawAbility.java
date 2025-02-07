package by.dragonsurvivalteam.dragonsurvival.magic.abilities.SeaDragon.innate;

import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.DragonTypes;
import by.dragonsurvivalteam.dragonsurvival.magic.common.RegisterDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.magic.common.innate.DragonClawsAbility;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import net.minecraft.resources.ResourceLocation;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

@Translation(type = Translation.Type.ABILITY_DESCRIPTION, comments = {
        "■ Sea dragons §2can§r deal increased damage and can dig blocks that require shovels without tools. They grow stronger with age.\n",
        "■ §cCannot§r ride horses, or use certain items."
})
@Translation(type = Translation.Type.ABILITY, comments = "Claws and Teeth")
@RegisterDragonAbility
public class SeaClawAbility extends DragonClawsAbility {
    @Override
    public String getName() {
        return "sea_claws_and_teeth";
    }

    @Override
    public AbstractDragonType getDragonType() {
        return DragonTypes.SEA;
    }

    @Override
    public ResourceLocation[] getSkillTextures() {
        return new ResourceLocation[]{
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_0.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_1.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_2.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_3.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_4.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_5.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_6.png"),
                ResourceLocation.fromNamespaceAndPath(MODID, "textures/skills/sea/sea_claws_and_teeth_7.png")
        };
    }
}