package net.bananapuppy.variantfurnaces.registries.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.bananapuppy.variantfurnaces.MainClass;
import net.bananapuppy.variantfurnaces.util.ConfigManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ReloadConfigCommand {

    public static int run(CommandContext<ServerCommandSource> context) {
        MainClass.CONFIG_MANAGER.writeFileToFields();
        return 1;
    }

    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
            CommandManager.literal("reloadconfig")
            .then(CommandManager.literal(MainClass.MOD_ID)
                .executes(ReloadConfigCommand::run
        )));
    }
}
