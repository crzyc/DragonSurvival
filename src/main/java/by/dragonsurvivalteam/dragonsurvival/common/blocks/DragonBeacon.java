package by.dragonsurvivalteam.dragonsurvival.common.blocks;

import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler;
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;
import by.dragonsurvivalteam.dragonsurvival.config.ConfigHandler;
import by.dragonsurvivalteam.dragonsurvival.config.ServerConfig;
import by.dragonsurvivalteam.dragonsurvival.registry.DSBlocks;
import by.dragonsurvivalteam.dragonsurvival.registry.DSSounds;
import by.dragonsurvivalteam.dragonsurvival.registry.DSTileEntities;
import by.dragonsurvivalteam.dragonsurvival.server.tileentity.DragonBeaconTileEntity;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import by.dragonsurvivalteam.dragonsurvival.util.MobEffectUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

public class DragonBeacon extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public DragonBeacon(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(LIT, false).setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState1, @NotNull LevelAccessor world, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos1) {
        if (blockState.getValue(WATERLOGGED)) {
            world.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(blockState, direction, blockState1, world, blockPos, blockPos1);
    }

    private List<String> getEffectsForBeacon(Block block, Level pLevel) {
        if (block == DSBlocks.FOREST_DRAGON_BEACON.get()) {
            return ServerConfig.forestDragonBeaconEffects;
        } else if (block == DSBlocks.SEA_DRAGON_BEACON.get()) {
            return ServerConfig.seaDragonBeaconEffects;
        } else if (block == DSBlocks.CAVE_DRAGON_BEACON.get()) {
            return ServerConfig.caveDragonBeaconEffects;
        }

        return null;
    }

    private boolean tryAddEffectsForBeacon(Block block, Level pLevel, Player pPlayer) {
        List<String> effects = getEffectsForBeacon(block, pLevel);
        if(!pLevel.isClientSide() && effects != null) {
            ConfigHandler.getResourceElements(MobEffect.class, effects).forEach(effect -> {
                if (effect != null) {
                    pPlayer.addEffect(new MobEffectInstance(MobEffectUtils.getHolder(effect), Functions.minutesToTicks(ServerConfig.minutesOfDragonEffect)));
                }
            });
            return true;
        }

        return false;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHitResult) {
        Optional<DragonStateHandler> dragonState = DragonStateProvider.getOptional(pPlayer);

        if (dragonState.isPresent()) {
            DragonStateHandler dragonStateHandler = dragonState.orElse(null);

            if (dragonStateHandler.isDragon() && (pPlayer.totalExperience >= 60 || pPlayer.isCreative())) {
                if(tryAddEffectsForBeacon(pState.getBlock(), pLevel, pPlayer)) {
                    if(!pPlayer.isCreative()) {
                        pPlayer.giveExperiencePoints(-60);
                    }
                    pLevel.playSound(pPlayer, pPos, DSSounds.APPLY_EFFECT.get(), SoundSource.PLAYERS, 1, 1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }

    private static DragonBeaconTileEntity.Type itemToBeaconType(Item item) {
        if (item == Items.GOLD_BLOCK) {
            return DragonBeaconTileEntity.Type.PEACE;
        } else if (item == Items.DIAMOND_BLOCK) {
            return DragonBeaconTileEntity.Type.MAGIC;
        } else if (item == Items.NETHERITE_INGOT) {
            return DragonBeaconTileEntity.Type.FIRE;
        }

        return null;
    }

    private static Block beaconTypeToBlock(DragonBeaconTileEntity.Type type) {
        return switch (type) {
            case DragonBeaconTileEntity.Type.PEACE -> DSBlocks.SEA_DRAGON_BEACON.get();
            case DragonBeaconTileEntity.Type.MAGIC -> DSBlocks.FOREST_DRAGON_BEACON.get();
            case DragonBeaconTileEntity.Type.FIRE -> DSBlocks.CAVE_DRAGON_BEACON.get();
            case DragonBeaconTileEntity.Type.NONE -> null;
        };
    }

    private boolean tryUpgradeBeacon(Level level, BlockPos blockPos, Player player, Item item) {
        DragonBeaconTileEntity old = (DragonBeaconTileEntity) level.getBlockEntity(blockPos);
        if(old == null) return false;

        DragonBeaconTileEntity.Type type = itemToBeaconType(item);
        if(old.type == type) return false;
        if(type == null) return false;

        Block beaconBlock = beaconTypeToBlock(type);
        if(beaconBlock == null) return false;

        level.setBlockAndUpdate(blockPos, beaconBlock.defaultBlockState());
        DragonBeaconTileEntity dragonBeaconEntity = (DragonBeaconTileEntity) level.getBlockEntity(blockPos);
        if(dragonBeaconEntity == null) return false;

        dragonBeaconEntity.type = type;
        dragonBeaconEntity.tick = old.tick;
        if(!player.isCreative()) {
            player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
        }
        level.playSound(player, blockPos, DSSounds.UPGRADE_BEACON.get(), SoundSource.BLOCKS, 1, 1);
        return true;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        Item item = itemStack.getItem();

        if (tryUpgradeBeacon(pLevel, pPos, pPlayer, item)) {
            return ItemInteractionResult.SUCCESS;
        }


        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public boolean triggerEvent(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity != null && blockentity.triggerEvent(pId, pParam);
    }

    // Methods below are required for the waterlogged property to work
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    @Nullable public MenuProvider getMenuProvider(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos position, @NotNull BlockState state) {
        return DSTileEntities.DRAGON_BEACON.get().create(position, state);
    }

    @Override
    @Nullable public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : BaseEntityBlock.createTickerHelper(type, DSTileEntities.DRAGON_BEACON.get(), DragonBeaconTileEntity::serverTick);
    }
}