package by.dragonsurvivalteam.dragonsurvival.mixins;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SleepStatus.class)
public class SleepStatusMixin {
    // This is done here so that the dragon rests without the player sleep UI appearing or the camera view changing.
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSleeping()Z"))
    public boolean dragonSurvival$isSleeping(ServerPlayer target) {
        DragonStateHandler handler = DragonStateProvider.getData(target);
        return target.isSleeping() || handler.treasureResting;
    }
}