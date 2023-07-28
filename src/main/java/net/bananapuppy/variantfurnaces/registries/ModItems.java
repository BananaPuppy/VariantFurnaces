package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.registries.items.augments.BlastingAugment;
import net.bananapuppy.variantfurnaces.registries.items.augments.FuelAugment;
import net.bananapuppy.variantfurnaces.registries.items.augments.SmokeAugment;
import net.bananapuppy.variantfurnaces.registries.items.augments.SpeedAugment;
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
    public static Item TEST_FURNACE = new BlockItem(ModBlocks.TEST_FURNACE, new FabricItemSettings());
    public static RegistryKey<ItemGroup> VFurnacesItemGroup = registerItemGroup("Variant Furnaces", TEST_FURNACE);

    //Augments
    public static Item FUEL_AUGMENT = new FuelAugment(new FabricItemSettings());
    public static Item SPEED_AUGMENT = new SpeedAugment(new FabricItemSettings());
    public static Item BLASTING_AUGMENT = new BlastingAugment(new FabricItemSettings());
    public static Item SMOKE_AUGMENT = new SmokeAugment(new FabricItemSettings());

    public static void registerModItems(){
        //Furnaces
        TEST_FURNACE = registerItemToGroup(VFurnacesItemGroup, registerItem("test_furnace", TEST_FURNACE));
        //Augments
        FUEL_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("fuel_augment", FUEL_AUGMENT));
        SPEED_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("speed_augment", SPEED_AUGMENT));
        BLASTING_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("blasting_augment", BLASTING_AUGMENT));
        SMOKE_AUGMENT = registerItemToGroup(VFurnacesItemGroup, registerItem("smoke_augment", SMOKE_AUGMENT));
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
