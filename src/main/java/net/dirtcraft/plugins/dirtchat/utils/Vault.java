package net.dirtcraft.plugins.dirtchat.utils;

import net.dirtcraft.plugins.dirtchat.DirtChat;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

public class Vault {
	private static Economy econ;
	private static Permission perms;
	private static Chat chat;

	public static void init() {
		if (!setupEconomy() ) {
			Utilities.log(Level.SEVERE, "Disabled plugin due to missing vault dependency.");
			Utilities.disablePlugin();
			return;
		}
		setupPermissions();
		setupChat();
	}

	public static Permission getPerms() {
		return perms;
	}

	public static Chat getChat() {
		return chat;
	}

	public static Economy getEcon() {
		return econ;
	}

	private static boolean setupEconomy() {
		if (DirtChat.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = DirtChat.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private static void setupChat() {
		RegisteredServiceProvider<Chat> rsp = DirtChat.getPlugin().getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
	}

	private static void setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = DirtChat.getPlugin().getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
	}
}
