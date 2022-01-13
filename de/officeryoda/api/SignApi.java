package de.officeryoda.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SignApi extends JavaPlugin implements Listener{
	static SignApi instance;
	static Map<Location, Material> signs;

	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		signs = new HashMap<>();
		Bukkit.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	public static Map<Location, Material> openNewSign(Player player, String line0, String line1, String line2, String line3) throws InterruptedException {
		Map<Location, Material> returnValue = new HashMap<>();
		Location goodLoc = getSaveLocation(player); //get good location

		Block block = goodLoc.getWorld().getBlockAt(goodLoc);
		Material material = block.getType();
		block.setType(Material.OAK_SIGN);
		Sign sign = (Sign) block.getLocation().getBlock().getState();

		//add lines and wait a bit before opening
		sign.setLine(0, line0);
		sign.setLine(1, line1);
		sign.setLine(2, line2);
		sign.setLine(3, line3);
		sign.update();

		//wait two ticks (2/20 sec == 100 milliseconds) before opening
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
		    @Override
		    public void run() {
		    	player.openSign(sign); //open sign-gui
		    	addSignMaterial(goodLoc, material);
		    }
		}, 2L);
		returnValue.put(goodLoc, material);
		return returnValue;
	}

	public static Map<Location, Material> openNewSign(Player player, String[] lines) throws InterruptedException {
		Map<Location, Material> returnValue = new HashMap<>();
		Location goodLoc = getSaveLocation(player); //get good location

		Block block = goodLoc.getWorld().getBlockAt(goodLoc);
		Material material = block.getType();
		block.setType(Material.OAK_SIGN);
		Sign sign = (Sign) block.getLocation().getBlock().getState();

		//add lines and wait a bit before opening
		sign.setLine(0, lines[0] == null ? "" : lines[0]);
		sign.setLine(1, lines[1] == null ? "" : lines[1]);
		sign.setLine(2, lines[2] == null ? "" : lines[2]);
		sign.setLine(3, lines[3] == null ? "" : lines[3]);
		sign.update();

		//wait two ticks (2/20 sec == 100 milliseconds) before opening
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
		    @Override
		    public void run() {
		    	player.openSign(sign); //open sign-gui
		    	addSignMaterial(goodLoc, material);
		    }
		}, 2L);
		returnValue.put(goodLoc, material);
		return returnValue;
	}

	public static void openSign(Player player, Sign sign) {
		player.openSign(sign);
	}

	private static Location getSaveLocation(Player player) {
		Location goodLoc = searchForGoodLocation(player.getLocation(), player.getClientViewDistance());
		if(goodLoc == null || goodLoc.equals(null)) {
			Location playerLoc = player.getLocation();
			playerLoc.setX(playerLoc.getBlockX());
			playerLoc.setY(-64);	//if save location was found near the player its using block directly under the player
			playerLoc.setZ(playerLoc.getBlockZ());
			
			goodLoc.setYaw(0);
			goodLoc.setPitch(0);
			
			return playerLoc;
		}
		return goodLoc;
	}

	private static Location searchForGoodLocation(Location startLocation, int viewDistance) {
		Location location = startLocation;
		int renderDistance = (viewDistance * 16) - 10 > 100 ? (viewDistance * 16) - 10 : 100; //-10 for safety reasons
		
		int startX = location.getBlockX();
		int startZ = location.getBlockZ();

		int minX = startX - renderDistance;
		int minZ = startZ - renderDistance;

		int maxX = startX + renderDistance;
		int maxZ = startZ + renderDistance;

		location.setY(-63);
		for(int z = minZ; z < maxZ; z++) {
			location.setZ(z);	
			for(int x = minX; x < maxX; x++) {
				location.setX(x);
				if(location.getWorld().getBlockAt(location).getType() == Material.BEDROCK) {
					location.setY(-64);
					return location;
				}
			}
		}
		return null;
	}
	
	@EventHandler
	public void onSignLeave(SignChangeEvent event) {
		Location location = event.getBlock().getLocation();
		if(!signs.containsKey(location))
			return;

		location.getWorld().getBlockAt(location).setType(signs.get(location));
		signs.remove(location);
	}
	
	private static void addSignMaterial(Location location, Material material) {
		signs.put(location, material);
	}
}