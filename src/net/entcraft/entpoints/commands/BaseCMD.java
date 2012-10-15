package net.entcraft.entpoints.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.entcraft.entpoints.Main;

public class BaseCMD {
	
	protected Main plugin;
	
	protected BaseCMD(Main instance) {
		plugin = instance;
	}
	
	protected boolean isPlayer(CommandSender sender) {
		if (sender instanceof Player) {
			return true;
		} else {
			sender.sendMessage("This command can only be performed by a player.");
			return false;
		}
	}
	
	protected boolean isInteger(CommandSender sender, String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
//			sender.sendMessage(ChatColor.RED + "Error NumberFormatException: Incorrect value entered.");
			return false;
		}
		return true;
	}
	
}
