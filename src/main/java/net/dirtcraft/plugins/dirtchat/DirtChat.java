package net.dirtcraft.plugins.dirtchat;

import net.dirtcraft.plugins.dirtchat.utils.Utilities;
import net.dirtcraft.plugins.dirtchat.utils.Vault;
import org.bukkit.plugin.java.JavaPlugin;

public final class DirtChat extends JavaPlugin {
	private static DirtChat plugin;
	public static DirtChat getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {
		plugin = this;
		Utilities.loadConfig();
		Vault.init();
		Utilities.registerListener();
		Utilities.registerCommands();
	}

	@Override
	public void onDisable() {

	}
}
