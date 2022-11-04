package net.dirtcraft.plugins.dirtchat.listeners;

import net.dirtcraft.plugins.dirtchat.DirtChat;
import net.dirtcraft.plugins.dirtchat.data.ChatManager;
import net.dirtcraft.plugins.dirtchat.utils.Permissions;
import net.dirtcraft.plugins.dirtchat.utils.Utilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();

		boolean isInStaffChat = ChatManager.isInStaffChat(player.getUniqueId());
		if (message.startsWith("###global ") && player.hasPermission(Permissions.WRITE_GLOBAL)) {
			isInStaffChat = false;
			message = message.replace("###global ", "");
			event.setMessage(message);
		}

		if (message.startsWith("###staff ") && player.hasPermission(Permissions.WRITE_STAFF)) {
			isInStaffChat = true;
			message = message.replace("###staff ", "");
			event.setMessage(message);
		}

		Set<Player> recipients = new HashSet<>(event.getRecipients());
		event.getRecipients().clear();

		if (isInStaffChat) {
			event.setCancelled(true);
		}

		if (event.isCancelled()) {
			event.setCancelled(true);
		}

		Set<String> playerNamesToPing = new HashSet<>();

		if (player.hasPermission(Permissions.PING)) {
			for (Player recipient : recipients) {
				if (message.contains(recipient.getName())) {
					playerNamesToPing.add(recipient.getName());
				}
			}
		}

		if (isInStaffChat) {
			ComponentBuilder chatComponent = ChatManager.getChatComponent(player, message, true);
			chatStaff(recipients, chatComponent);

			CommandSender console = Bukkit.getConsoleSender();
			console.spigot().sendMessage(chatComponent.create());
		} else {
			ComponentBuilder chatComponent = ChatManager.getChatComponent(player, message, false);
			chatGlobal(recipients, ChatManager.getChatComponent(player, message, false), playerNamesToPing, player.getName());

			CommandSender console = Bukkit.getConsoleSender();
			console.spigot().sendMessage(chatComponent.create());
		}
	}

	private static void chatGlobal(Set<Player> recipients, ComponentBuilder component, Set<String> playersToPing, String sender) {
		Bukkit.getScheduler().runTask(DirtChat.getPlugin(), () -> {
			for (Player recipient : recipients) {
				if (recipient.hasPermission(Permissions.READ_GLOBAL)) {
					recipient.spigot().sendMessage(component.create());

					if (playersToPing.size() == 0) continue;
					if (recipient.hasPermission(Permissions.PING_BYPASS)) continue;

					if (playersToPing.contains(recipient.getName())) {
						if (Utilities.config.sound.playPingSound) {
							String sound = Utilities.config.sound.pingSound;
							if (sound == null) {
								sound = "block.note_block.pling";
							}
							recipient.playSound(recipient.getLocation(), sound, 1, 1);
						}

						if (Utilities.config.general.sendTitleOnPing) {
							recipient.sendTitle("", ChatColor.GOLD + sender + ChatColor.GRAY + " pinged you in chat!", 10, 40, 20);
						}
					}
				}
			}
		});
	}

	private static void chatStaff(Set<Player> recipients, ComponentBuilder component) {
		Bukkit.getScheduler().runTask(DirtChat.getPlugin(), () -> {
			int onlineStaff = 0;
			for (Player recipient : recipients) {
				if (recipient.hasPermission(Permissions.READ_STAFF)) {
					recipient.spigot().sendMessage(component.create());
					onlineStaff++;
				}
			}
			if (onlineStaff == 1) {
				for (Player recipient : recipients) {
					if (recipient.hasPermission(Permissions.READ_STAFF)) {
						recipient.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "You are the only online staff!"));
					}
				}
			}
		});
	}
}
