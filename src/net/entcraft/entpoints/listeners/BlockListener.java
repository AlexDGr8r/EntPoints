package net.entcraft.entpoints.listeners;

import java.io.File;
import java.util.HashMap;
import net.entcraft.entpoints.*;
import net.entcraft.utils.Config;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends EntListener implements Listener {
	
	private HashMap<String, BlockMemory> blockMemory = new HashMap<String, BlockMemory>();
	private HashMap<Material, Integer> loggedBlocks = new HashMap<Material, Integer>();

	public BlockListener(Main instance) {
		super(instance);
		
		//Store separate values for each world
		for (World world : plugin.getServer().getWorlds()) {
			File f = new File(plugin.getDataFolder(), "block-" + world.getName() + ".bin");
			BlockMemory blockMem = new BlockMemory(f.toString());
			blockMem.load();
			blockMemory.put(world.getName(), blockMem);
		}
		
		Config config = plugin.config;
		//Valid blocks
		loggedBlocks.put(Material.COAL_ORE, 	config.get("Points.BlockBreak.COAL", 1));
		loggedBlocks.put(Material.DIAMOND_ORE, 	config.get("Points.BlockBreak.DIAMOND", 6));
		loggedBlocks.put(Material.EMERALD_ORE, 	config.get("Points.BlockBreak.EMERALD", 2));
		loggedBlocks.put(Material.GOLD_ORE, 	config.get("Points.BlockBreak.GOLD", 2));
		loggedBlocks.put(Material.IRON_ORE, 	config.get("Points.BlockBreak.IRON", 1));
		loggedBlocks.put(Material.LAPIS_ORE, 	config.get("Points.BlockBreak.LAPIS", 2));
		loggedBlocks.put(Material.MOB_SPAWNER, 	config.get("Points.BlockBreak.SPAWNER", 4));
		loggedBlocks.put(Material.REDSTONE_ORE, config.get("Points.BlockBreak.REDSTONE", 2));
		
		config.saveAllData();
	}
	
	public void saveBlocks() {
		for (String s : blockMemory.keySet()) {
			blockMemory.get(s).save();
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void blockPlaced(BlockPlaceEvent event) {
		if (loggedBlocks.containsKey(event.getBlock().getType())) {
			Location l = event.getBlock().getLocation();
			blockMemory.get(l.getWorld().getName()).put(l.toVector());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void blockBreak(BlockBreakEvent event) {
		if(loggedBlocks.containsKey(event.getBlock().getType())) {
			
			BlockMemory blockMem = blockMemory.get(event.getBlock().getLocation().getWorld().getName());
			if (blockMem.blockPlaced(event.getBlock().getLocation().toVector())) {
				blockMem.remove(event.getBlock().getLocation().toVector());
			} else {
				Block block = event.getBlock();
				int points = loggedBlocks.get(block.getType());
				plugin.pointGrabber.addEarnedPoints(event.getPlayer().getName(), points);
				event.getPlayer().sendMessage(ChatColor.GOLD + "You earned " + points + " points!");
			}
			
		}
	}

}
