package net.bananapuppy.variantfurnaces.registries;

import net.bananapuppy.variantfurnaces.registries.commands.ReloadConfigCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {

    public static void registerModCommands(){
        CommandRegistrationCallback.EVENT.register(ReloadConfigCommand::register);
    }
}
