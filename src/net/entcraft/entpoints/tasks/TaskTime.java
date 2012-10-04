package net.entcraft.entpoints.tasks;

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
			plugin.pointGrabber.addTime(player.getName(), 15 * 1000);
			if (ticks / 4 >= minutesPerCashout) {
				long oldTime = plugin.timePlayedWhenLogin.get(player.getName());
				long newTime = plugin.pointGrabber.getTimePlayed(player.getName());
				long diff = newTime - oldTime;
				if (diff / 4 * 15 * 1000 >= minutesPerCashout) {
					long points = diff / (4 * 15 * 1000) / minutesPerCashout * pointsPerCashout;
					plugin.pointGrabber.addEarnedPoints(player.getName(), (int)points);
					plugin.timePlayedWhenLogin.put(player.getName(), newTime - ((diff % (4 * 15 * 1000)) + (diff / (4 * 15 * 1000) % minutesPerCashout) * (4 * 15 * 1000)));
				}
			}
		}
	}

}
