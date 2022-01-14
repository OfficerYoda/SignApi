package de.officeryoda;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomSignEditEvent extends Event implements Cancellable {
	private Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private String[] lines;
    private Block block;
    private Material previousMaterial;

    public CustomSignEditEvent(Player player, Block block, String[] lines, Material previousMaterial){
        this.player = player;
        this.isCancelled = false;
        this.lines = lines;
        this.block = block;
        this.previousMaterial = previousMaterial;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

	public String[] getLines() {
		return lines;
	}
	
	public String getLine(int line) {
		return lines[line];
	}

	public Block getBlock() {
		return block;
	}
	
	public Material getPreviousMaterial() {
		return previousMaterial;
	}
	
	public void setPreviousMaterial(Material previousMaterial) {
		this.previousMaterial = previousMaterial;
	}
}