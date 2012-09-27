package net.entcraft.entpoints.listeners;

import net.entcraft.entpoints.Main;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends EntListener implements Listener {

	public PlayerJoinListener(Main instance) {
		super(instance);
	}
	
	public void playerJoin(PlayerJoinEvent event) {
		if (!plugin.pointGrabber.doesPlayerDataExist(event.getPlayer().getName())) {
			plugin.sql.standardQuery("INSERT INTO " + Main.tableName + " VALUES (" + event.getPlayer().getName() + ", 0, 0, 0);");
		}
	}

}
