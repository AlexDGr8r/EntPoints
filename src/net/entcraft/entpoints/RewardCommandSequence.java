package net.entcraft.entpoints;

import java.util.HashMap;

import org.bukkit.Server;

public class RewardCommandSequence {
	
	public enum Options {
		
		Player("@p");
		
		private String sReplace;
		
		private Options(String s) {
			sReplace = s;
		}
		
		@Override
		public String toString() {
			return sReplace;
		}
		
	}
	
	public String[] cmds;
	public Rewards reward;
	
	public static HashMap<Rewards, RewardCommandSequence> rewardMapping = new HashMap<Rewards, RewardCommandSequence>();
	
	public RewardCommandSequence(Rewards r, String... commands) {
		cmds = commands;
		reward = r;
		rewardMapping.put(reward, this);
	}
	
	public void processCommands(Server server, String player) {
		for (int i = 0; i < cmds.length; i++) {
			cmds[i].replaceAll(Options.Player.toString(), player);
			server.dispatchCommand(server.getConsoleSender(), cmds[i]);
		}
	}

}
