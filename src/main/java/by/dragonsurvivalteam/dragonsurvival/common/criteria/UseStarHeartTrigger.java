package by.dragonsurvivalteam.dragonsurvival.common.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UseStarHeartTrigger extends SimpleCriterionTrigger<UseStarHeartTrigger.UseStarHeartInstance> {
    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }

    @Override
    public @NotNull Codec<UseStarHeartTrigger.UseStarHeartInstance> codec() {
        return UseStarHeartTrigger.UseStarHeartInstance.CODEC;
    }

    public record UseStarHeartInstance(
            Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<UseStarHeartTrigger.UseStarHeartInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(UseStarHeartTrigger.UseStarHeartInstance::player)
        ).apply(instance, UseStarHeartTrigger.UseStarHeartInstance::new));
    }
}
