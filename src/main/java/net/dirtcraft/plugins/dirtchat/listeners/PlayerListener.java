package net.dirtcraft.plugins.dirtchat.listeners;

import net.dirtcraft.plugins.dirtchat.data.ChatManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		ChatManager.join(uuid);
	}
}
