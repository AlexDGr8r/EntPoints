package net.entcraft.entpoints.listeners;

import net.entcraft.entpoints.Main;

import org.bukkit.event.Listener;

public class EntListener implements Listener {
	
	protected Main plugin;
	
	public EntListener(Main instance) {
		plugin = instance;
//		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}
