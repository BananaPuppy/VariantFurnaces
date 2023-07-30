package net.bananapuppy.variantfurnaces.registries.blocks;

import net.bananapuppy.variantfurnaces.registries.blockentities.AbstractVFurnaceBlockEntity;
import net.bananapuppy.variantfurnaces.registries.items.upgrades.AbstractUpgrade;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.ToIntFunction;

public abstract class AbstractVFurnaceBlock
        extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LIT = Properties.LIT;

    public static final BooleanProperty FUEL_AUGMENT = BooleanProperty.of("fuel_augment");
    public static final BooleanProperty SPEED_AUGMENT = BooleanProperty.of("speed_augment");
    public static final BooleanProperty BLASTING_AUGMENT = BooleanProperty.of("blasting_augment");
    public static final BooleanProperty SMOKE_AUGMENT = BooleanProperty.of("smoke_augment");
    //TODO: SPOOKY

    protected AbstractVFurnaceBlock(FabricBlockSettings settings, MapColor color, @Nullable Instrument instrument, Float hardness, Float resistance, int litLuminance, BlockSoundGroup soundGroup, Class<?>... upgradeItem) {
        super(settings.mapColor(color).instrument(instrument).requiresTool().strength(hardness, resistance).luminance(createLightLevelFromLitBlockState(litLuminance)).requiresTool().sounds(soundGroup));
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(LIT, false)
                .with(FUEL_AUGMENT, false)
                .with(SPEED_AUGMENT, false)
                .with(BLASTING_AUGMENT, false)
                .with(SMOKE_AUGMENT, false)
        );
        this.upgradeItem.addAll(Arrays.asList(upgradeItem));
    }
    private final ArrayList<Class<?>> upgradeItem = new ArrayList<>();

    @SuppressWarnings("SameParameterValue")
    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        for(Class<?> clazz : this.upgradeItem){
            if(clazz.isInstance(player.getMainHandStack().getItem())){
                return ActionResult.FAIL;
            }
            if(clazz.isInstance(player.getOffHandStack().getItem())){
                return ActionResult.FAIL;
            }
        }
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        this.openScreen(world, pos, player);
        return ActionResult.CONSUME;
    }

    protected abstract void openScreen(World var1, BlockPos var2, PlayerEntity var3);

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof AbstractVFurnaceBlockEntity) {
            ((AbstractVFurnaceBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AbstractVFurnaceBlockEntity) {
            if (world instanceof ServerWorld) {
                ItemScatterer.spawn(world, pos, (AbstractVFurnaceBlockEntity)blockEntity);
                ((AbstractVFurnaceBlockEntity)blockEntity).getRecipesUsedAndDropExperience((ServerWorld)world, Vec3d.ofCenter(pos));
            }
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
        builder.add(FUEL_AUGMENT, SPEED_AUGMENT, BLASTING_AUGMENT, SMOKE_AUGMENT);
    }

    @SuppressWarnings("unused")
    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends AbstractVFurnaceBlockEntity> expectedType) {
        return world.isClient ? null : AbstractFurnaceBlock.checkType(givenType, expectedType, AbstractVFurnaceBlockEntity::tick);
    }
}