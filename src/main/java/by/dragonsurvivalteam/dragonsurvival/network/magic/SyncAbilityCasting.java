package by.dragonsurvivalteam.dragonsurvival.network.magic;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.magic.common.active.ActiveDragonAbility;
import by.dragonsurvivalteam.dragonsurvival.network.IMessage;
import by.dragonsurvivalteam.dragonsurvival.network.client.ClientProxy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static by.dragonsurvivalteam.dragonsurvival.DragonSurvival.MODID;

public class SyncAbilityCasting implements IMessage<SyncAbilityCasting.Data> {
    public static void handleClient(final SyncAbilityCasting.Data message, final IPayloadContext context) {
        context.enqueueWork(() -> ClientProxy.handleSyncAbilityCasting(message));
    }

    public static void handleServer(final SyncAbilityCasting.Data message, final IPayloadContext context) {
        Player sender = context.player();
        context.enqueueWork(() -> {
            DragonStateProvider.getOptional(sender).ifPresent(handler -> {
                ActiveDragonAbility ability = handler.getMagicData().getAbilityFromSlot(message.abilitySlot);
                ability.loadNBT(message.nbt);
                handler.getMagicData().isCasting = message.isCasting;

                if (message.isCasting) {
                    ability.onKeyPressed(sender, () -> {
                    }, message.castStartTime, message.clientTime);
                } else {
                    ability.onKeyReleased(sender);
                }
            });
        }).thenRun(() -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(sender, message));
    }

    public record Data(int playerId, boolean isCasting, int abilitySlot, CompoundTag nbt, long castStartTime,
                    long clientTime) implements CustomPacketPayload {
        public static final Type<Data> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "ability_casting"));

        public static final StreamCodec<FriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                Data::playerId,
                ByteBufCodecs.BOOL,
                Data::isCasting,
                ByteBufCodecs.VAR_INT,
                Data::abilitySlot,
                ByteBufCodecs.COMPOUND_TAG,
                Data::nbt,
                ByteBufCodecs.VAR_LONG,
                Data::castStartTime,
                ByteBufCodecs.VAR_LONG,
                Data::clientTime,
                Data::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}