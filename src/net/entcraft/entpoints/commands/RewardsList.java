package net.entcraft.entpoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.entcraft.entpoints.Main;
import net.entcraft.entpoints.Rewards;
import net.entcraft.utils.IEntCommandHandler;

public class RewardsList extends BaseCMD implements IEntCommandHandler {

	public RewardsList(Main instance) {
		super(instance);
	}

	@Override
	public boolean processCommand(CommandSender sender, String[] neededArgs) {
		
		int page;
		if (neededArgs == null) {
			page = 1;
		} else {
			if (isInteger(sender, neededArgs[0])) {
				page = Integer.parseInt(neededArgs[0]);
			} else {
				return true;
			}
		}
		
		return displayRewards(sender, page);
	}
	
	public boolean displayRewards(CommandSender sender, int page) {
		
		Player player;
		if (isPlayer(sender)) {
			player = (Player)sender;
		} else {
			return true;
		}
		
		int l = Rewards.values().length;
		int maxPages = l / 3 + (l % 3 == 0 ? 0 : 1);
		if (page > maxPages) {
			page = maxPages;
		} else if (page < 1) {
			page = 1;
		}
		
		int startIndex = (page - 1) * 3;
		int endIndex = page * 3 - 1 - (page == maxPages && l % 3 != 0 ? 3 - (l % 3) : 0);
		String pName = player.getName();
		player.sendMessage(ChatColor.GOLD + "============ Rewards List | Page " + page + " of " + maxPages + " ============");
		for (int i = startIndex; i <= endIndex; i++) {
			Rewards reward = Rewards.values()[i];
			String title = "(" + reward.getID() + ") " + ChatColor.BOLD + reward.getTitle() + ":";
			String desc = "    Desc: " + reward.getDescription();
			if (desc.length() > 55) {
				desc = desc.substring(0, 54) + "...";
			}
			ChatColor hasPoints = ChatColor.RED;
			if (reward.donatedPointsOnly()) {
				if (plugin.pointGrabber.getDonatedPoints(pName) >= reward.getPointsNeeded()) {
					hasPoints = ChatColor.GREEN;
				} else {
					hasPoints = ChatColor.YELLOW;
				}
			} else if (plugin.pointGrabber.getEarnedPoints(pName) >= reward.getPointsNeeded()) {
				hasPoints = ChatColor.GREEN;
			}
			String pointPrice = hasPoints + "    " + (reward.donatedPointsOnly() ? "Donated " : "Earned ") + "points: " + reward.getPointsNeeded();
			player.sendMessage(title);
			player.sendMessage(desc);
			player.sendMessage(pointPrice);
			if (reward.getRequired() != null) {
				player.sendMessage("    Requires: " + reward.getRequired().getTitle());
			}
			player.sendMessage(ChatColor.GOLD + "------------------------------------------------");
		}
		
		return true;
	}

	@Override
	public String getDescription() {
		return "Lists all available rewards";
	}

}
