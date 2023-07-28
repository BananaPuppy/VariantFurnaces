package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.blocks.testFurnace;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Block TEST_FURNACE = new testFurnace(FabricBlockSettings.create());

    public static void registerModBlocks(){
        TEST_FURNACE = registerBlock("test_furnace", TEST_FURNACE);
    }

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MainClass.MOD_ID, name), block);
    }

//    public static Block registerBlockToGroup(RegistryKey<ItemGroup> group, Block block){
//        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(block));
//        return block;
//    }

}
