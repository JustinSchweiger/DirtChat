package net.dirtcraft.plugins.dirtchat.utils;

import com.moandjiezana.toml.Toml;
import net.dirtcraft.plugins.dirtchat.DirtChat;
import net.dirtcraft.plugins.dirtchat.commands.GlobalCommand;
import net.dirtcraft.plugins.dirtchat.commands.StaffCommand;
import net.dirtcraft.plugins.dirtchat.config.Config;
import net.dirtcraft.plugins.dirtchat.listeners.ChatListener;
import net.dirtcraft.plugins.dirtchat.listeners.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class Utilities {
	public static Config config;

	public static void loadConfig() {
		if (!DirtChat.getPlugin().getDataFolder().exists()) {
			DirtChat.getPlugin().getDataFolder().mkdirs();
		}
		File file = new File(DirtChat.getPlugin().getDataFolder(), "config.toml");
		if (!file.exists()) {
			try {
				Files.copy(DirtChat.getPlugin().getResource("config.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		config = new Toml(new Toml().read(DirtChat.getPlugin().getResource("config.toml"))).read(file).to(Config.class);
	}

	public static void registerListener() {
		DirtChat.getPlugin().getServer().getPluginManager().registerEvents(new PlayerListener(), DirtChat.getPlugin());
		DirtChat.getPlugin().getServer().getPluginManager().registerEvents(new ChatListener(), DirtChat.getPlugin());
	}

	public static void registerCommands() {
		DirtChat.getPlugin().getCommand("staff").setExecutor(new StaffCommand());
		DirtChat.getPlugin().getCommand("staff").setTabCompleter(new StaffCommand());
		DirtChat.getPlugin().getCommand("global").setExecutor(new GlobalCommand());
		DirtChat.getPlugin().getCommand("global").setTabCompleter(new GlobalCommand());
	}

	public static void log(Level level, String msg) {
		String consoleMessage;
		if (Level.INFO.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.WHITE + msg;
		} else if (Level.WARNING.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.YELLOW + msg;
		} else if (Level.SEVERE.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.RED + msg;
		} else {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.GRAY + msg;
		}

		if (!config.general.coloredDebug) {
			consoleMessage = ChatColor.stripColor(msg);
		}

		DirtChat.getPlugin().getServer().getConsoleSender().sendMessage(consoleMessage);
	}

	public static void disablePlugin() {
		DirtChat.getPlugin().getServer().getPluginManager().disablePlugin(DirtChat.getPlugin());
	}

	public static String format(String message) {
		return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
	}

	private static String formatName(String s) {
		if (!s.contains("_")) {
			return capitalize(s);
		}
		String[] j = s.split("_");

		StringBuilder c = new StringBuilder();

		for (String f : j) {
			f = capitalize(f);
			c.append(c.toString().equalsIgnoreCase("") ? f : " " + f);
		}
		return c.toString();
	}

	private static String capitalize(String text) {
		String firstLetter = text.substring(0, 1).toUpperCase();
		String next = text.substring(1).toLowerCase();

		return firstLetter + next;
	}

	public static String getFriendlyName(ItemStack item) {
		return formatName(item.getType().getKey().getKey());
	}
}
