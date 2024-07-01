package com.ashkiano.blockcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BlockCommands extends JavaPlugin implements Listener {

    private List<String> blockedCommands;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadBlockedCommands();
        getCommand("blockcommandsreload").setExecutor((sender, command, label, args) -> {
            if (sender.hasPermission("blockcommands.reload")) {
                reloadConfig();
                reloadBlockedCommands();
                sender.sendMessage("Config reloaded!");
            } else {
                sender.sendMessage("You do not have permission to execute this command.");
            }
            return true;
        });
        getServer().getPluginManager().registerEvents(this, this);
        Metrics metrics = new Metrics(this, 22251);
    }

    private void reloadBlockedCommands() {
        this.blockedCommands = getConfig().getStringList("blocked-commands");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1).split(" ")[0].toLowerCase();

        if (blockedCommands.contains(command)) {
            Player player = event.getPlayer();
            if (!player.hasPermission("blockcommands.bypass")) {
                player.sendMessage("This command is blocked.");
                event.setCancelled(true);
            }
        }
    }
}