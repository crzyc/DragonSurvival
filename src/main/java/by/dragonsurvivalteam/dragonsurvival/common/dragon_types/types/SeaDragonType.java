package by.dragonsurvivalteam.dragonsurvival.common.dragon_types.types;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.dragon_types.AbstractDragonType;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.DragonTraitHandler;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.config.server.dragon.SeaDragonConfig;
import by.dragonsurvivalteam.dragonsurvival.magic.DragonAbilities;
import by.dragonsurvivalteam.dragonsurvival.magic.abilities.SeaDragon.passive.WaterAbility;
import by.dragonsurvivalteam.dragonsurvival.network.player.SyncDragonType;
import by.dragonsurvivalteam.dragonsurvival.registry.DSDamageTypes;
import by.dragonsurvivalteam.dragonsurvival.registry.DSEffects;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.tags.DSBlockTags;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class SeaDragonType extends AbstractDragonType {
    public double timeWithoutWater;

    public SeaDragonType() {
        slotForBonus = 3;
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("timeWithoutWater", timeWithoutWater);
        return tag;
    }

    @Override
    public void readNBT(CompoundTag base) {
        timeWithoutWater = base.getDouble("timeWithoutWater");
    }

    @Override
    public String getTypeName() {
        return "sea";
    }

    @Override
    public void onPlayerUpdate(Player player, DragonStateHandler dragonStateHandler) {
        Level level = player.level();
        boolean isInSeaBlock = DragonTraitHandler.isInCauldron(player, Blocks.WATER_CAULDRON) || player.getBlockStateOn().is(DSBlockTags.HYDRATES_SEA_DRAGON);
        int maxTicksOutofWater = SeaDragonConfig.seaTicksWithoutWater;
        Optional<WaterAbility> waterAbility = DragonAbilities.getAbility(player, WaterAbility.class);

        if (waterAbility.isPresent()) {
            maxTicksOutofWater += Functions.secondsToTicks(waterAbility.get().getDuration());
        }

        double oldWaterTime = timeWithoutWater;

        if (!level.isClientSide()) {
            if ((player.hasEffect(DSEffects.PEACE) || player.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) && player.getAirSupply() < player.getMaxAirSupply()) {
                player.setAirSupply(player.getMaxAirSupply());
            }
        }

        if (ServerConfig.penaltiesEnabled && maxTicksOutofWater > 0 && !player.isCreative() && !player.isSpectator()) {
            if (!level.isClientSide()) {
                if (player.hasEffect(DSEffects.PEACE)) {
                    timeWithoutWater = 0;
                } else {
                    if (!player.isInWaterRainOrBubble() && !isInSeaBlock) {
                        Biome biome = level.getBiome(player.blockPosition()).value();
                        boolean hotBiome = biome.getPrecipitationAt(player.blockPosition()) == Precipitation.NONE && biome.getBaseTemperature() > 1.0;
                        double timeIncrement = (level.isNight() ? 0.5F : 1.0) * (hotBiome ? biome.getBaseTemperature() : 1F);
                        timeWithoutWater += SeaDragonConfig.seaTicksBasedOnTemperature ? timeIncrement : 1;
                    }

                    if (player.isInWaterRainOrBubble() || isInSeaBlock) {
                        timeWithoutWater = Math.max(timeWithoutWater - (int) Math.ceil(maxTicksOutofWater * 0.005F), 0);
                    }

                    timeWithoutWater = Math.min(timeWithoutWater, maxTicksOutofWater * 2);


                    if (!player.level().isClientSide()) {
                        float hydrationDamage = SeaDragonConfig.seaDehydrationDamage.floatValue();

                        if (timeWithoutWater > maxTicksOutofWater && timeWithoutWater < maxTicksOutofWater * 2) {
                            if (player.tickCount % 40 == 0) {
                                player.hurt(new DamageSource(DSDamageTypes.get(player.level(), DSDamageTypes.DEHYDRATION)), hydrationDamage);
                            }

                        } else if (timeWithoutWater >= maxTicksOutofWater * 2) {
                            if (player.tickCount % 20 == 0) {
                                player.hurt(new DamageSource(DSDamageTypes.get(player.level(), DSDamageTypes.DEHYDRATION)), hydrationDamage);
                            }
                        }
                    }
                }

                if (oldWaterTime != timeWithoutWater) {
                    PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new SyncDragonType.Data(player.getId(), dragonStateHandler.getType().writeNBT()));
                }
            }

            if (level.isClientSide() && !player.isCreative() && !player.isSpectator()) {
                if (!player.hasEffect(DSEffects.PEACE) && timeWithoutWater >= maxTicksOutofWater) {
                    level.addParticle(ParticleTypes.WHITE_ASH, player.getX() + level.random.nextDouble() * (level.random.nextBoolean() ? 1 : -1), player.getY() + 0.5F, player.getZ() + level.random.nextDouble() * (level.random.nextBoolean() ? 1 : -1), 0, 0, 0);
                }
            }
        }
    }


    @Override
    public boolean isInManaCondition(final Player player, final DragonStateHandler cap) {
        return player.isInWaterRainOrBubble() || player.hasEffect(DSEffects.CHARGED) || player.hasEffect(DSEffects.PEACE);
    }

    @Override
    public void onPlayerDeath() {
        timeWithoutWater = 0;
    }

    @Override
    public List<Pair<ItemStack, FoodData>> validFoods(Player player, DragonStateHandler handler) {
        return null;
    }

    @Override
    public List<TagKey<Block>> mineableBlocks() {
        return List.of(BlockTags.MINEABLE_WITH_SHOVEL);
    }
}