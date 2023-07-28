package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Block COPPER_FURNACE = new CopperFurnace(FabricBlockSettings.create());
    public static Block IRON_FURNACE = new IronFurnace(FabricBlockSettings.create());
    public static Block GOLD_FURNACE = new GoldFurnace(FabricBlockSettings.create());
    public static Block DIAMOND_FURNACE = new DiamondFurnace(FabricBlockSettings.create());
    public static Block EMERALD_FURNACE = new EmeraldFurnace(FabricBlockSettings.create());
    public static Block CRYSTAL_FURNACE = new CrystalFurnace(FabricBlockSettings.create().nonOpaque());
    public static Block OBSIDIAN_FURNACE = new ObsidianFurnace(FabricBlockSettings.create());
    public static Block NETHERITE_FURNACE = new NetheriteFurnace(FabricBlockSettings.create());
    //TODO: RAINBOW_FURNACE

    public static void registerModBlocks(){
        COPPER_FURNACE = registerBlock("copper_furnace", COPPER_FURNACE);
        IRON_FURNACE = registerBlock("iron_furnace", IRON_FURNACE);
        GOLD_FURNACE = registerBlock("gold_furnace", GOLD_FURNACE);
        DIAMOND_FURNACE = registerBlock("diamond_furnace", DIAMOND_FURNACE);
        EMERALD_FURNACE = registerBlock("emerald_furnace", EMERALD_FURNACE);
        CRYSTAL_FURNACE = registerBlock("crystal_furnace", CRYSTAL_FURNACE);
        OBSIDIAN_FURNACE = registerBlock("obsidian_furnace", OBSIDIAN_FURNACE);
        NETHERITE_FURNACE = registerBlock("netherite_furnace", NETHERITE_FURNACE);
    }

    public static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MainClass.MOD_ID, name), block);
    }

//    public static Block registerBlockToGroup(RegistryKey<ItemGroup> group, Block block){
//        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(block));
//        return block;
//    }

}
