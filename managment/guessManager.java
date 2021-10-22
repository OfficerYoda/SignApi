package managment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class guessManager implements Listener{

	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playername = player.getName();
		guessList.put(playername, false);
	}

	String prefix = (ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "GTN" + ChatColor.GOLD + "] ");
	private Map<String, Boolean> guessList;
	private Map<String, Integer> guessMin;
	private Map<String, Integer> guessMax;
	private Map<String, Integer> guessTries;
	private Map<String, Integer> randomNumber;

	public guessManager() {
		guessList = new HashMap<>();
		guessMin = new HashMap<>();
		guessMax = new HashMap<>();
		randomNumber = new HashMap<>();
		guessTries = new HashMap<>();

		String playername;
		for (Player player : Bukkit.getOnlinePlayers()) {
			playername = player.getName();
			guessList.put(playername, false);
			guessMin.put(playername, 0);
			guessMax.put(playername, 100);
		}
	}

	public boolean isGuessing(Player player) {
		String playername = player.getName();

		return guessList.get(playername);
	}

	public void setGuessing(Player player, Boolean b) {
		String playername = player.getName();

		guessList.put(playername, b);
	}



	public int getGuessMin(Player player) {
		String playername = player.getName();
		try {
			if(guessMin.containsKey(playername)) {
				return guessMin.get(playername);
			}
		} catch (Exception e) {
			guessMin.put(playername, 0);
			return 0;
		}
		guessMin.put(playername, 0);
		return 0;
	}

	public void setGuessMin(Player player, int i) {
		String playername = player.getName();

		guessMin.put(playername, i);
	}



	public int getGuessMax(Player player) {
		String playername = player.getName();
		try {
			if(guessMax.containsKey(playername)) {
				return guessMax.get(playername);
			}
		} catch (Exception e) {
			guessMax.put(playername, 100);
			return 100;
		}
		guessMax.put(playername, 100);
		return 100;
	}

	public void setGuessMax(Player player, int i) {
		String playername = player.getName();

		guessMax.put(playername, i);
	}

	public int getRandomNumber(Player player) {
		String playername = player.getName();

		return randomNumber.get(playername);
	}

	public void generateRandomNumber(Player player) {
		String playername = player.getName();

		int min = getGuessMin(player);
		int max = getGuessMax(player);

		Random randomNum = new Random();
		if (max - min == 0) {
			randomNumber.put(playername, min);
		} else {
			int randomInt = min + randomNum.nextInt(max - min);
			randomNumber.put(playername, randomInt);
		}
	}
	public int getGuessTries(Player player) {
		String playername = player.getName();
		try {
			if(guessTries.containsKey(playername)) {
				return guessTries.get(playername);
			}
		} catch (Exception e) {
			guessTries.put(playername, 0);
			return guessTries.get(playername);
		}
		return 0;
	}

	public void setGuessTries(Player player, int i) {
		String playername = player.getName();

		guessTries.put(playername, i);
	}
}