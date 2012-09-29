package net.entcraft.tasks;

import org.bukkit.entity.Player;

import net.entcraft.entpoints.Main;

public class TaskTime implements Runnable {
	
	private Main plugin;
	private int ticks = 0;
	private int minutesPerCashout;
	private int pointsPerCashout;
	
	public TaskTime(Main instance) {
		plugin = instance;
		minutesPerCashout = plugin.config.get("Points.TimePlayed.MinutesPerCashout", 5);
		pointsPerCashout = plugin.config.get("Points.TimePlayed.PointsPerCashout", 1);
		plugin.config.saveAllData();
	}

	// Called every 15 seconds
	@Override
	public void run() {
		ticks++;
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			plugin.pointGrabber.addTime(player.getName(), 1);
			if (ticks / 4 >= minutesPerCashout) {
				int oldTime = plugin.timePlayedWhenLogin.get(player.getName());
				int newTime = plugin.pointGrabber.getTimePlayed(player.getName());
				int diff = newTime - oldTime;
				if (diff / 4 >= minutesPerCashout) {
					int points = diff / 4 / minutesPerCashout * pointsPerCashout;
					plugin.pointGrabber.addEarnedPoints(player.getName(), points);
					plugin.timePlayedWhenLogin.put(player.getName(), newTime - ((diff % 4) + (diff / 4 % minutesPerCashout) * 4));
				}
			}
		}
	}

}
