package Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import managment.commandManager;
import managment.guessManager;


public class guessMain extends JavaPlugin {
	
	private static guessMain instance;
	
	private commandManager commandManager;
	private guessManager guessManager;


	@Override
	public void onLoad() {
		instance = this;
	}
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Guess My Number");
		
		commandManager = new commandManager();
		guessManager = new guessManager();
		
		getCommand("guess").setExecutor(new commandManager());
		
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(new guessManager(), this);
	}
	
	public static guessMain getInstance() {		 
		return instance;
	}

	public commandManager getCommandManager() {
		if(commandManager == null)
			commandManager = new commandManager();
		return commandManager;
	}

	public guessManager getGuessManager() {
		return guessManager;
	}
}