package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.blockentities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<CopperFurnaceBlockEntity> COPPER_FURNACE;
    public static BlockEntityType<IronFurnaceBlockEntity> IRON_FURNACE;
    public static BlockEntityType<GoldFurnaceBlockEntity> GOLD_FURNACE;
    public static BlockEntityType<DiamondFurnaceBlockEntity> DIAMOND_FURNACE;
    public static BlockEntityType<EmeraldFurnaceBlockEntity> EMERALD_FURNACE;
    public static BlockEntityType<CrystalFurnaceBlockEntity> CRYSTAL_FURNACE;
    public static BlockEntityType<ObsidianFurnaceBlockEntity> OBSIDIAN_FURNACE;
    public static BlockEntityType<NetheriteFurnaceBlockEntity> NETHERITE_FURNACE;

    public static void registerModBlockEntities(){
        COPPER_FURNACE = registerBlockEntity("copper_furnace", CopperFurnaceBlockEntity::new, ModBlocks.COPPER_FURNACE);
        IRON_FURNACE = registerBlockEntity("iron_furnace", IronFurnaceBlockEntity::new, ModBlocks.IRON_FURNACE);
        GOLD_FURNACE = registerBlockEntity("gold_furnace", GoldFurnaceBlockEntity::new, ModBlocks.GOLD_FURNACE);
        DIAMOND_FURNACE = registerBlockEntity("diamond_furnace", DiamondFurnaceBlockEntity::new, ModBlocks.DIAMOND_FURNACE);
        EMERALD_FURNACE = registerBlockEntity("emerald_furnace", EmeraldFurnaceBlockEntity::new, ModBlocks.EMERALD_FURNACE);
        CRYSTAL_FURNACE = registerBlockEntity("crystal_furnace", CrystalFurnaceBlockEntity::new, ModBlocks.CRYSTAL_FURNACE);
        OBSIDIAN_FURNACE = registerBlockEntity("obsidian_furnace", ObsidianFurnaceBlockEntity::new, ModBlocks.OBSIDIAN_FURNACE);
        NETHERITE_FURNACE = registerBlockEntity("netherite_furnace", NetheriteFurnaceBlockEntity::new, ModBlocks.NETHERITE_FURNACE);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> factory, Block block){
        return (BlockEntityType<T>) Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MainClass.MOD_ID, name),
                FabricBlockEntityTypeBuilder.create(factory, block).build()
        );
    }
}
