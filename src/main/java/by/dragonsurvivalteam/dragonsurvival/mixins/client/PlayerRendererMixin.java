package by.dragonsurvivalteam.dragonsurvival.mixins.client;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.common.handlers.magic.HunterHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Render the human player translucent in first person if they have hunter stacks */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
    @WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
    private void dragonSurvival$renderTranslucent(final ModelPart instance, final PoseStack poseStack, final VertexConsumer buffer, int packedLight, int packedOverlay, final Operation<Void> original, @Local(argsOnly = true) final MultiBufferSource bufferSource, @Local(argsOnly = true) final AbstractClientPlayer player) {
        DragonStateHandler data = DragonStateProvider.getData(player);

        if (data.hasHunterStacks() && !data.isBeingRenderedInInventory) {
            VertexConsumer translucentBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(player.getSkin().texture()));
            instance.render(poseStack, translucentBuffer, packedLight, packedOverlay, HunterHandler.modifyAlpha(player, -1));
        } else {
            original.call(instance, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
