package net.entcraft.entpoints.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BlockListener extends EntListener implements Listener {
	
	private HashMap<String, BlockMemory> blockMemory = new HashMap<String, BlockMemory>();
	private HashMap<Material, Integer> loggedBlocks = new HashMap<Material, Integer>();

	public BlockListener(Main instance) {
		super(instance);
		
		Config config = plugin.config;
		//Valid blocks
		loggedBlocks.put(Material.COAL_ORE, 			config.get("Points.BlockBreak.COAL", 1));
		loggedBlocks.put(Material.DIAMOND_ORE, 			config.get("Points.BlockBreak.DIAMOND", 6));
		loggedBlocks.put(Material.EMERALD_ORE, 			config.get("Points.BlockBreak.EMERALD", 2));
		loggedBlocks.put(Material.GOLD_ORE, 			config.get("Points.BlockBreak.GOLD", 2));
		loggedBlocks.put(Material.IRON_ORE, 			config.get("Points.BlockBreak.IRON", 1));
		loggedBlocks.put(Material.LAPIS_ORE, 			config.get("Points.BlockBreak.LAPIS", 2));
		loggedBlocks.put(Material.MOB_SPAWNER, 			config.get("Points.BlockBreak.SPAWNER", 4));
		int redstonePoints = config.get("Points.BlockBreak.REDSTONE", 2);
		loggedBlocks.put(Material.REDSTONE_ORE, 		redstonePoints);
		loggedBlocks.put(Material.GLOWING_REDSTONE_ORE, redstonePoints);
		
		config.saveAllData();
		
		//Store separate values for each world
		for (World world : plugin.getServer().getWorlds()) {
			File f = new File(plugin.getDataFolder(), "block-" + world.getName() + ".bin");
			BlockMemory blockMem = new BlockMemory(f.toString());
			blockMem.load();
			blockMem.cleanup(world, loggedBlocks.keySet());
			blockMemory.put(world.getName(), blockMem);
		}
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
	
	// Used to prevent glitching with pistons when they are extended
	@EventHandler(priority = EventPriority.NORMAL)
	public void pistonExtend(BlockPistonExtendEvent event) {
		List<Block> bList = new ArrayList<Block>();
		BlockMemory blockMem = blockMemory.get(event.getBlock().getLocation().getWorld().getName());
		for (Block block : event.getBlocks()) {
			if (blockMem.blockPlaced(block.getLocation().toVector())) {
				bList.add(block);
			}
		}
		
		if (bList.size() > 0) {
			for (int i = 0; i < bList.size(); i++) {
				Block block = bList.get(i);
				Vector vec = block.getLocation().toVector();
				blockMem.remove(vec);
				vec.setX(vec.getBlockX() + event.getDirection().getModX());
				vec.setY(vec.getBlockY() + event.getDirection().getModY());
				vec.setZ(vec.getBlockZ() + event.getDirection().getModZ());
				blockMem.put(vec);
			}
		}
	}
	
	// Used to prevent glitching with pistons when they are retracted
	@EventHandler(priority = EventPriority.NORMAL)
	public void pistonRetract(BlockPistonRetractEvent event) {
		if (!event.isSticky()) return;
		BlockMemory blockMem =  blockMemory.get(event.getBlock().getLocation().getWorld().getName());
		Block block = event.getBlock().getRelative(event.getDirection(), 2);
		if (blockMem.blockPlaced(block.getLocation().toVector())) {
			blockMem.remove(block.getLocation().toVector());
			blockMem.put(event.getBlock().getRelative(event.getDirection(), 1).getLocation().toVector());
		}
	}

}
