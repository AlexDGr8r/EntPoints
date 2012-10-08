package net.entcraft.entpoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.entcraft.entpoints.Main;
import net.entcraft.utils.IEntCommandHandler;

public class TimeCheck extends BaseCMD implements IEntCommandHandler {

	public TimeCheck(Main instance) {
		super(instance);
	}

	// Tells the player how long they've played the game.
	@Override
	public boolean processCommand(CommandSender sender, String[] neededArgs) {
		
		if (!isPlayer(sender)) return false;
		Player player = (Player)sender;
		long timePlayed = plugin.pointGrabber.getTimePlayed(player.getName());
		int seconds = 0, minutes = 0, hours = 0, days = 0;
		if (timePlayed >= 1000) {
			seconds = (int)(timePlayed / 1000.0);
			if (seconds >= 60) {
				minutes = seconds / 60;
				seconds = seconds % 60;
				if (minutes >= 60) {
					hours = minutes / 60;
					minutes = minutes % 60;
					if (hours >= 24) {
						days = hours / 24;
						hours = hours % 24;
					}
				}
			}
		}
		
		String message = "Time Played: ";
		if (days > 0) {
			message += days + "D " + hours + "h " + minutes + "m " + seconds + "s";
		} else if (hours > 0) {
			message += hours + "h " + minutes + "m " + seconds + "s";
		} else if (minutes > 0) {
			message += minutes + "m " + seconds + "s";
		} else if (seconds > 0) {
			message += seconds + "s";
		} else {
			message += "You are newb";
		}
		player.sendMessage(ChatColor.GREEN + message);
		
		return true;
	}

	@Override
	public String getDescription() {
		return "Check the amount of time you've played.";
	}

}
