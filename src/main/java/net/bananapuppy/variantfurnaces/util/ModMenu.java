package net.bananapuppy.variantfurnaces.util;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.bananapuppy.variantfurnaces.Config;
import net.bananapuppy.variantfurnaces.MainClass;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenu implements ModMenuApi {

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            //Return the screen here with the one you created from Cloth Config Builder
            return genConfigScreen(parent);
        };
    }

    @SuppressWarnings("Convert2MethodRef")
    private Screen genConfigScreen(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("modmenu.variantfurnaces.configtitle"));
        builder.setSavingRunnable(() -> {
            //Serialize the config into the config file.
            //Called after all of the variables are updated
            MainClass.CONFIG_MANAGER.writeFieldsToFile();
        });
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        //TODO: Defined option categories in Config.class
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("modmenu.variantfurnaces.category.general"));
        //noinspection CodeBlock2Expr
        general.addEntry(entryBuilder.startBooleanToggle(
                Text.translatable("modmenu.variantfurnaces.category.general.options.fuelScalesWithSpeed"),
                Config.fuelScalesWithSpeed
            )
                .setDefaultValue(false)
                //TODO: Defined option tooltip in Config.class
                //.setTooltip(Text.translatable(""))
                .setSaveConsumer(newValue -> {
                    Config.fuelScalesWithSpeed = newValue;
                })
                .build()
        );
        return builder.build();
    }
}
