package net.dirtcraft.plugins.dirtchat.data;

import net.dirtcraft.plugins.dirtchat.utils.ItemStackToJson;
import net.dirtcraft.plugins.dirtchat.utils.Permissions;
import net.dirtcraft.plugins.dirtchat.utils.Utilities;
import net.dirtcraft.plugins.dirtchat.utils.Vault;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatManager {
	private static final HashMap<UUID, Channel> tracker = new HashMap<>();

	public static void join(UUID uuid) {
		if (!tracker.containsKey(uuid)) {
			tracker.put(uuid, Channel.GLOBAL);
		}
	}

	public static void joinStaff(UUID uuid) {
		tracker.replace(uuid, Channel.STAFF);
	}

	public static void joinGlobal(UUID uuid) {
		tracker.replace(uuid, Channel.GLOBAL);
	}

	public static boolean isInStaffChat(UUID uuid) {
		return tracker.get(uuid) == Channel.STAFF;
	}

	public static ComponentBuilder getChatComponent(Player player, final String message) {
		String prefix = Vault.getChat().getPlayerPrefix(player);

		BaseComponent[] prefixComponent = new ComponentBuilder("")
				.append(Utilities.format(prefix))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Rank" + ChatColor.GRAY + ": " + Utilities.format(prefix.trim()))))
				.create();
		BaseComponent[] playerComponent = new ComponentBuilder("")
				.append(player.getDisplayName())
				.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
						ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + player.getName() + "\n" +
								ChatColor.GOLD + "Nickname" + ChatColor.GRAY + ": " + player.getDisplayName() + "\n" +
								ChatColor.GOLD + "Rank" + ChatColor.GRAY + ": " + Utilities.format(prefix.trim()) + "\n" +
								ChatColor.GOLD + "Is Staff" + ChatColor.GRAY + ": " + ChatColor.DARK_PURPLE + player.hasPermission(Permissions.IS_STAFF) + "\n" +
								ChatColor.GOLD + "Balance" + ChatColor.GRAY + ": " + ChatColor.GREEN + Vault.getEcon().getBalance(player) + ChatColor.YELLOW + " $"
				))).create();

		boolean hasItem = message.contains("@item") && player.hasPermission(Permissions.HIGHLIGHT_ITEM);

		ComponentBuilder com = new ComponentBuilder("");
		com.append(prefixComponent);
		com.append(" ").event((HoverEvent) null).event((ClickEvent) null);
		com.append(playerComponent);
		com.append(ChatColor.GRAY + ": " + ChatColor.RESET).event((HoverEvent) null).event((ClickEvent) null);

		if (!hasItem) {
			com.append(
					player.hasPermission(Permissions.COLOR_CHAT) ?
							Utilities.format(message) :
							ChatColor.stripColor(Utilities.format(message))
			).event((HoverEvent) null).event((ClickEvent) null);

			return com;
		}

		ItemStack itemStack = player.getInventory().getItemInMainHand();
		String item = ItemStackToJson.convertItemStackToJson(itemStack);
		List<String> messageParts = Arrays.asList(message.split("((?<= )|(?= ))"));

		BaseComponent[] itemPartComponent = new ComponentBuilder("")
				.append(ChatColor.AQUA + "[" + ChatColor.GRAY + Utilities.getFriendlyName(itemStack) + ChatColor.AQUA + "]")
				.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(item)}))
				.create();

		if (messageParts.size() == 0) {
			com.append(itemPartComponent);
		}

		for (String messagePart : messageParts) {
			BaseComponent[] messagePartComponent = new ComponentBuilder()
					.append(
							player.hasPermission(Permissions.COLOR_CHAT) ?
									Utilities.format(messagePart) :
									ChatColor.stripColor(Utilities.format(messagePart))
					).create();

			if (messagePart.equals("@item"))
				com.append(itemPartComponent);
			else
				com.append(messagePartComponent).event((HoverEvent) null).event((ClickEvent) null);
		}

		return com;
	}

	public static ComponentBuilder getStaffComponent(Player player, final String message) {
		String prefix = Vault.getChat().getPlayerPrefix(player);

		BaseComponent[] prefixComponent = new ComponentBuilder("")
				.append(Utilities.format(prefix))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Rank" + ChatColor.GRAY + ": " + Utilities.format(prefix.trim()))))
				.create();
		BaseComponent[] playerComponent = new ComponentBuilder("")
				.append(player.getDisplayName())
				.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
						ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + player.getName() + "\n" +
								ChatColor.GOLD + "Nickname" + ChatColor.GRAY + ": " + player.getDisplayName() + "\n" +
								ChatColor.GOLD + "Rank" + ChatColor.GRAY + ": " + Utilities.format(prefix.trim()) + "\n" +
								ChatColor.GOLD + "Is Staff" + ChatColor.GRAY + ": " + ChatColor.DARK_PURPLE + player.hasPermission(Permissions.IS_STAFF) + "\n" +
								ChatColor.GOLD + "Balance" + ChatColor.GRAY + ": " + ChatColor.GREEN + Vault.getEcon().getBalance(player) + ChatColor.YELLOW + " $"
				))).create();

		BaseComponent[] staffComponent = new ComponentBuilder("")
				.append(ChatColor.GRAY + "" + ChatColor.MAGIC + "||" + ChatColor.RESET + " " + ChatColor.DARK_PURPLE + "Staff" + " " + ChatColor.MAGIC + "||" + ChatColor.RESET)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "This message is only being displayed in the staff chat.")))
				.create();

		boolean hasItem = message.contains("@item") && player.hasPermission(Permissions.HIGHLIGHT_ITEM);

		ComponentBuilder com = new ComponentBuilder("");
		com.append(staffComponent);
		com.append(" ").event((HoverEvent) null).event((ClickEvent) null);
		com.append(prefixComponent);
		com.append(" ").event((HoverEvent) null).event((ClickEvent) null);
		com.append(playerComponent);
		com.append(ChatColor.GRAY + ": " + ChatColor.RESET).event((HoverEvent) null).event((ClickEvent) null);

		if (!hasItem) {
			com.append(
					player.hasPermission(Permissions.COLOR_CHAT) ?
							Utilities.format(message) :
							ChatColor.stripColor(Utilities.format(message))
			).event((HoverEvent) null).event((ClickEvent) null);

			return com;
		}

		ItemStack itemStack = player.getInventory().getItemInMainHand();
		String item = ItemStackToJson.convertItemStackToJson(itemStack);
		List<String> messageParts = Arrays.asList(message.split("((?<= )|(?= ))"));

		BaseComponent[] itemPartComponent = new ComponentBuilder("")
				.append(ChatColor.AQUA + "[" + ChatColor.GRAY + Utilities.getFriendlyName(itemStack) + ChatColor.AQUA + "]")
				.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(item)}))
				.create();

		if (messageParts.size() == 0) {
			com.append(itemPartComponent);
		}

		for (String messagePart : messageParts) {
			BaseComponent[] messagePartComponent = new ComponentBuilder()
					.append(
							player.hasPermission(Permissions.COLOR_CHAT) ?
									Utilities.format(messagePart) :
									ChatColor.stripColor(Utilities.format(messagePart))
					).create();

			if (messagePart.equals("@item"))
				com.append(itemPartComponent);
			else
				com.append(messagePartComponent).event((HoverEvent) null).event((ClickEvent) null);
		}

		return com;
	}
}
