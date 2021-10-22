package managment;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import Main.guessMain;

public class commandManager implements CommandExecutor, TabCompleter {
	guessManager guessManager = guessMain.getInstance().getGuessManager();

	String prefix = (ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "GTN" + ChatColor.GOLD + "] ");

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return false;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			sendUsage(player);
			return false;	
		}
		
		switch (args[0].toLowerCase()) {
		case "start": {
			if(guessManager.isGuessing(player)) {
				player.sendMessage(prefix + ChatColor.GOLD + "You are already playing a game.");
				break;
			}
			guessManager.setGuessing(player, true);
			player.sendMessage(prefix + ChatColor.GREEN + "Started" + ChatColor.GOLD + " an \"guess the number\" game.");

			guessManager.generateRandomNumber(player);
			guessManager.setGuessTries(player, 0);
			break;
		}
		case "end": {
			if(!guessManager.isGuessing(player)) {
				player.sendMessage(prefix + ChatColor.GOLD + "You can't stop a game you aren't playing.");
				break;
			}
			guessManager.setGuessing(player, false);
			player.sendMessage(prefix + ChatColor.RED + "Ended" + ChatColor.GOLD + " an \"guess the number\" game.");
			break;
		}
		case "min": {
			if (args.length == 1) {
				player.sendMessage(prefix + "Your current minimum guess-number is " + ChatColor.AQUA + guessManager.getGuessMin(player));
				if(guessManager.isGuessing(player)) player.sendMessage(prefix + "which will be used next round.");
				break;
			}

			try {
				guessManager.setGuessMin(player, Integer.valueOf(args[1]));
			} catch (NumberFormatException e) {
				player.sendMessage(prefix + ChatColor.GOLD + "This is " + ChatColor.RED + "not" + ChatColor.GOLD + " a number!");
				break;
			}

			player.sendMessage(prefix + "You set your minimum guess-number to " + ChatColor.AQUA + guessManager.getGuessMin(player));

			break;
		}
		case "max": {
			if (args.length == 1) {
				player.sendMessage(prefix + "Your current maximum guess-number is " + ChatColor.AQUA + guessManager.getGuessMax(player));
				if(guessManager.isGuessing(player)) player.sendMessage(prefix + "which will be used next round.");
				break;
			}

			try {
				guessManager.setGuessMax(player, Integer.valueOf(args[1]));
			} catch (NumberFormatException e) {
				player.sendMessage(prefix + ChatColor.GOLD + "This is " + ChatColor.RED + "not" + ChatColor.GOLD + " a number!");
				break;
			}

			player.sendMessage(prefix + "You set your maximum guess-number to " + ChatColor.AQUA + guessManager.getGuessMax(player));

			break;
		}
		case "info": {

			if (guessManager.isGuessing(player)) {
				player.sendMessage(prefix + "You currently " + ChatColor.GREEN + "play" + ChatColor.GOLD + " a game of \"guess the number\"");
				player.sendMessage(prefix + "and guessing numbers from " + ChatColor.AQUA + guessManager.getGuessMin(player) + ChatColor.GOLD + " to " + ChatColor.AQUA + guessManager.getGuessMax(player) + ChatColor.GOLD + ".");
				player.sendMessage(prefix + "You have tried to guess the number " + ChatColor.AQUA + guessManager.getGuessTries(player) + ChatColor.GOLD + " times.");
			} else {
				player.sendMessage(prefix + "You currently " + ChatColor.RED + "don't play" + ChatColor.GOLD + " a game of \"guess the number\".");
				player.sendMessage(prefix + "and don't guess numbers from " + ChatColor.AQUA + guessManager.getGuessMin(player) + ChatColor.GOLD + " to " + ChatColor.AQUA + guessManager.getGuessMax(player) + ChatColor.GOLD + "");
			}
			break;
		} 
		case "<number>": {
			player.sendMessage(prefix + "What did you expected?");;
			break;
		}
		default:
			if (guessManager.isGuessing(player)) {
				int i;

				try {
					i = Integer.valueOf(args[0]);

					if (i >= guessManager.getGuessMin(player) && i <= guessManager.getGuessMax(player)) {
						if (i == guessManager.getRandomNumber(player)) {
							player.sendMessage(prefix + ChatColor.GREEN + "You guessed the right number " + ChatColor.YELLOW + "(" + ChatColor.AQUA + guessManager.getRandomNumber(player) + ChatColor.YELLOW + ")");
							guessManager.setGuessing(player, false);
							break;
						}
						if (i < guessManager.getRandomNumber(player)) {
							player.sendMessage(prefix + "You guessed " + ChatColor.AQUA + i + ChatColor.GOLD + ". You have to guess " + ChatColor.LIGHT_PURPLE + "LOWER" + ChatColor.GOLD + " to win.");
							guessManager.setGuessTries(player, guessManager.getGuessTries(player) + 1);
							break;
						}
						if (i > guessManager.getRandomNumber(player)) {
							player.sendMessage(prefix + "You guessed " + ChatColor.AQUA + i + ChatColor.GOLD + ". You have to guess " + ChatColor.DARK_PURPLE + "HIGHER" + ChatColor.GOLD + " to win.");
							guessManager.setGuessTries(player, guessManager.getGuessTries(player) + 1);
							break;
						}
					}
					break;
				} catch (NumberFormatException e) {
					sendUsage(player);
					break;
				}
			}
			sendUsage(player);
			break;
		}

		if (minBiggerMax(player)) {
			int min = guessManager.getGuessMin(player);

			guessManager.setGuessMin(player, guessManager.getGuessMax(player));
			guessManager.setGuessMax(player, min);

			player.sendMessage(prefix + ChatColor.RED + ChatColor.BOLD + "!!!" + ChatColor.GOLD + " Switched your min. guess-number" + ChatColor.YELLOW + "(" + ChatColor.AQUA + guessManager.getGuessMax(player) + ChatColor.YELLOW + ")"
					+ ChatColor.GOLD + "\nand your max. guess-number" + ChatColor.YELLOW + "(" + ChatColor.AQUA + guessManager.getGuessMin(player) + ChatColor.YELLOW + ")"
					+ ChatColor.GOLD + "\nbecause " + ChatColor.AQUA + guessManager.getGuessMax(player) + ChatColor.YELLOW + " > " + ChatColor.AQUA + guessManager.getGuessMin(player) + ChatColor.RED + ChatColor.BOLD + " !!!");
		}

		return true;
	}


	private boolean minBiggerMax(Player player) {
		int min = guessManager.getGuessMin(player);
		int max = guessManager.getGuessMax(player);

		if(min <= max) return false;
		if(min > max) return true;

		return false;
	}


	private void sendUsage(Player player) {	
		player.sendMessage(prefix + ChatColor.GRAY + "Usage" + ChatColor.DARK_GRAY + ": " + ChatColor.BLUE + "/guess start, /guess end, /guess min, /guess max, /guess info, /guess <" + ChatColor.ITALIC + "number"+ ChatColor.BLUE + ">");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1){
			if(guessManager.isGuessing((Player) sender)) {
				List<String> argument = new ArrayList<>();

				argument.add("end");
				argument.add("<number>");
				argument.add("info");

				return argument;
			}
			List<String> argument = new ArrayList<>();
			argument.add("start");
			argument.add("min");
			argument.add("max");
			argument.add("info");

			return argument;
		}
		return null;
	}
}