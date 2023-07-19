package net.bananapuppy.templatemodid.util;

import net.bananapuppy.templatemodid.registries.commands.ReloadConfigCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModRegistries {

    public static void registerModCommands(){
        CommandRegistrationCallback.EVENT.register(ReloadConfigCommand::register);
    }
}
