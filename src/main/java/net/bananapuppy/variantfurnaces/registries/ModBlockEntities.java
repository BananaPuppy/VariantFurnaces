package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.blockentities.BaseVFurnaceBlockEntity;
import net.bananapuppy.variantfurnaces.registries.blockentities.testFurnaceBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<testFurnaceBlockEntity> TEST_FURNACE;

    public static void registerModBlockEntities(){
        TEST_FURNACE = registerBlockEntity("test_furnace", testFurnaceBlockEntity::new, ModBlocks.TEST_FURNACE);
//        TEST_FURNACE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
//                new Identifier(MainClass.MOD_ID, "test_furnace"),
//                FabricBlockEntityTypeBuilder.create(testFurnaceBlockEntity::new,
//                        ModBlocks.TEST_FURNACE).build(null));
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<? extends T> factory, Block block){
        return (BlockEntityType<T>) Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MainClass.MOD_ID, name),
                FabricBlockEntityTypeBuilder.create(factory, block).build()
        );
    }
}
