package net.entcraft.entpoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.entcraft.entpoints.Main;
import net.entcraft.entpoints.Rewards;
import net.entcraft.utils.IEntCommandHandler;

public class RewardInfo extends BaseCMD implements IEntCommandHandler {

	public RewardInfo(Main instance) {
		super(instance);
	}

	@Override
	public boolean processCommand(CommandSender sender, String[] neededArgs) {
		
		Player player;
		if (isPlayer(sender)) {
			player = (Player)sender;
		} else {
			return true;
		}
		
		Rewards reward;
		if (isInteger(sender, neededArgs[0])) {
			reward = Rewards.getReward(Integer.parseInt(neededArgs[0]));
			if (reward != null) {
				return detailReward(player, reward);
			} else {
				player.sendMessage(ChatColor.RED + "Reward not found!");
			}
		} else {
			String rt = neededArgs[0];
			for (int i = 1; i < neededArgs.length; i++) {
				rt += " " + neededArgs[i];
			}
			reward = Rewards.getReward(rt);
			if (reward != null) {
				return detailReward(player, reward);
			} else {
				player.sendMessage(ChatColor.RED + "Reward not found!");
			}
		}
		
		return true;
	}
	
	private boolean detailReward(Player player, Rewards reward) {
		
		player.sendMessage(ChatColor.GOLD + "--------------------------------------------------");
		player.sendMessage(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + reward.getID());
		player.sendMessage(ChatColor.YELLOW + "Title: " + ChatColor.WHITE + reward.getTitle());
		player.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.WHITE + reward.getDescription());
		int pointsNeeded = reward.getPointsNeeded();
		int pointsToGo;
		String pointType;
		if (reward.donatedPointsOnly()) {
			pointType = "Donated";
			pointsToGo = pointsNeeded - plugin.pointGrabber.getDonatedPoints(player.getName());
		} else {
			pointType = "Earned";
			pointsToGo = pointsNeeded - plugin.pointGrabber.getEarnedPoints(player.getName());
		}
		if (pointsToGo < 0) pointsToGo = 0;
		player.sendMessage(ChatColor.YELLOW + pointType + " Points Needed: " + ChatColor.WHITE + pointsNeeded);
		player.sendMessage(ChatColor.YELLOW + pointType + " Points to go: " + ChatColor.WHITE + pointsToGo);
		if (reward.getRequired() != null) {
			player.sendMessage(ChatColor.YELLOW + "Reward Required: " + ChatColor.WHITE + reward.getRequired().getTitle());
		}
		player.sendMessage(ChatColor.GREEN + "To obtain this reward use the command /rewards obtain " + reward.getID());
		player.sendMessage(ChatColor.GOLD + "--------------------------------------------------");
		
		return true;
	}

	@Override
	public String getDescription() {
		return "Get more info on a reward";
	}

}
