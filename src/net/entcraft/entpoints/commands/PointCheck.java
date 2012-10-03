package net.entcraft.entpoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.entcraft.entpoints.Main;
import net.entcraft.utils.IEntCommandHandler;

public class PointCheck extends BaseCMD implements IEntCommandHandler {
	
	private boolean checkOtherPlayer;
	
	public PointCheck(Main instance, boolean otherPlayer) {
		super(instance);
		checkOtherPlayer = otherPlayer;
	}

	@Override
	public boolean processCommand(CommandSender sender, String[] neededArgs) {
		
		sender.sendMessage("You made it to the command.");
		String pname;
		if (checkOtherPlayer) {
			Player player = plugin.getServer().getPlayerExact(neededArgs[0]);
			if (player != null) {
				pname = player.getName();
			} else {
				pname = neededArgs[0];
			}
		} else {
			if (isPlayer(sender)) {
				Player player = (Player)sender;
				pname = player.getName();
			} else {
				return true;
			}
		}
		
		if (plugin.pointGrabber.doesPlayerDataExist(pname)) {
			sender.sendMessage(ChatColor.GOLD + "============" + pname + "'s Points============");
			sender.sendMessage(ChatColor.GOLD + "Donated Points:" + plugin.pointGrabber.getDonatedPoints(pname));
			sender.sendMessage(ChatColor.GOLD + " Earned Points:" + plugin.pointGrabber.getEarnedPoints(pname));
			sender.sendMessage(ChatColor.GOLD + "Vouched Points:" + plugin.pointGrabber.getVouchedPoints(pname));
			sender.sendMessage(ChatColor.GOLD + "  Total Points:" + plugin.pointGrabber.getTotalPoints(pname));
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Player data not found!");
			return true;
		}
	}

	@Override
	public String getDescription() {
		return "Checks your Points" + (checkOtherPlayer ? " or another player's." : ".");
	}

}
