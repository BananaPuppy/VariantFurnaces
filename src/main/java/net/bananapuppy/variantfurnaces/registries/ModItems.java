package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.items.augments.BlastingAugment;
import net.bananapuppy.variantfurnaces.registries.items.augments.FuelAugment;
import net.bananapuppy.variantfurnaces.registries.items.augments.SmokeAugment;
import net.bananapuppy.variantfurnaces.registries.items.augments.SpeedAugment;
import net.bananapuppy.variantfurnaces.registries.items.upgrades.StoneCopperUpgrade;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    //Furnaces
    public static Item COPPER_FURNACE = new BlockItem(ModBlocks.COPPER_FURNACE, new FabricItemSettings());
    public static Item IRON_FURNACE = new BlockItem(ModBlocks.IRON_FURNACE, new FabricItemSettings());
    public static Item GOLD_FURNACE = new BlockItem(ModBlocks.GOLD_FURNACE, new FabricItemSettings());
    public static Item DIAMOND_FURNACE = new BlockItem(ModBlocks.DIAMOND_FURNACE, new FabricItemSettings());
    public static Item EMERALD_FURNACE = new BlockItem(ModBlocks.EMERALD_FURNACE, new FabricItemSettings());
    public static Item CRYSTAL_FURNACE = new BlockItem(ModBlocks.CRYSTAL_FURNACE, new FabricItemSettings());
    public static Item OBSIDIAN_FURNACE = new BlockItem(ModBlocks.OBSIDIAN_FURNACE, new FabricItemSettings());
    public static Item NETHERITE_FURNACE = new BlockItem(ModBlocks.NETHERITE_FURNACE, new FabricItemSettings().fireproof());
    public static RegistryKey<ItemGroup> VFurnacesItemGroup = registerItemGroup("Variant Furnaces", IRON_FURNACE);

    //Augments
    public static Item FUEL_AUGMENT = new FuelAugment(new FabricItemSettings());
    public static Item SPEED_AUGMENT = new SpeedAugment(new FabricItemSettings());
    public static Item BLASTING_AUGMENT = new BlastingAugment(new FabricItemSettings());
    public static Item SMOKE_AUGMENT = new SmokeAugment(new FabricItemSettings());

    //Upgrades
    public static Item STONE_TO_COPPER_UPGRADE = new StoneCopperUpgrade(new FabricItemSettings());

    public static void registerModItems(){
        //Furnaces
        COPPER_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("copper_furnace", COPPER_FURNACE));
        IRON_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("iron_furnace", IRON_FURNACE));
        GOLD_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("gold_furnace", GOLD_FURNACE));
        DIAMOND_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("diamond_furnace", DIAMOND_FURNACE));
        EMERALD_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("emerald_furnace", EMERALD_FURNACE));
        CRYSTAL_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("crystal_furnace", CRYSTAL_FURNACE));
        OBSIDIAN_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("obsidian_furnace", OBSIDIAN_FURNACE));
        NETHERITE_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("netherite_furnace", NETHERITE_FURNACE));
        //Augments
        FUEL_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("fuel_augment", FUEL_AUGMENT));
        SPEED_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("speed_augment", SPEED_AUGMENT));
        BLASTING_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("blasting_augment", BLASTING_AUGMENT));
        SMOKE_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("smoke_augment", SMOKE_AUGMENT));
        //Upgrades
        STONE_TO_COPPER_UPGRADE = registerItemToGroup(VFurnacesItemGroup, registerItem("stone_to_copper_upgrade", STONE_TO_COPPER_UPGRADE));
    }

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MainClass.MOD_ID, name), item);
    }
    public static Item registerItemToGroup(RegistryKey<ItemGroup> group, Item item){
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item));
        return item;
    }

    public static RegistryKey<ItemGroup> registerItemGroup(String name, Item icon){
        RegistryKey<ItemGroup> ITEMGROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MainClass.MOD_ID + name.toLowerCase().replaceAll("\\s+", "_")));
        Registry.register(Registries.ITEM_GROUP, ITEMGROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(icon))
                .displayName(Text.translatable(name)).build());
        return ITEMGROUP;
    }

}
