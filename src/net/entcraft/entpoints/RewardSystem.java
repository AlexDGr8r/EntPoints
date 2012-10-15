package net.entcraft.entpoints;

import net.entcraft.entpoints.commands.*;
import net.entcraft.utils.EntCMD;

public class RewardSystem {
	
	private Main plugin;
	private EntCMD cmd;
	
	public RewardSystem(Main instance) {
		plugin = instance;
		cmd = new EntCMD(plugin, "rewards");
		cmd.addSubCommand("rewardsList", new RewardsList(plugin), "list");
		cmd.addSubCommand("rewardsList", new RewardsList(plugin), "list <page>", 1);
		cmd.addSubCommand("rewardInfo", new RewardInfo(plugin), "info <ID:Title>", true);
	}

}
