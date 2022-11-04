package net.dirtcraft.plugins.dirtchat.data;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtchat.utils.Permissions;
import net.dirtcraft.plugins.dirtchat.utils.Utilities;
import net.dirtcraft.plugins.dirtchat.utils.Vault;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.validator.routines.UrlValidator;
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

	public static ComponentBuilder getChatComponent(Player player, final String message, boolean isStaff) {
		String prefix = Vault.getChat().getPlayerPrefix(player);

		BaseComponent[] staffComponent = new ComponentBuilder("")
				.append(ChatColor.GRAY + "" + ChatColor.BOLD + "\u25c6 " + ChatColor.GOLD + "" + ChatColor.BOLD + "Staff" + ChatColor.GRAY + "" + ChatColor.BOLD + " \u25c6" + ChatColor.RESET)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "This message is only being displayed in the staff chat.")))
				.create();
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

		ComponentBuilder messageBuilder = new ComponentBuilder("");
		if (isStaff) {
			messageBuilder.append(staffComponent);
			messageBuilder.append(" ").event((HoverEvent) null).event((ClickEvent) null);
		}
		messageBuilder.append(prefixComponent);
		messageBuilder.append(" ").event((HoverEvent) null).event((ClickEvent) null);
		messageBuilder.append(playerComponent);
		messageBuilder.append(ChatColor.GRAY + ": " + ChatColor.RESET).event((HoverEvent) null).event((ClickEvent) null);

		String[] messageParts = message.split(" ");
		ChatColor color = ChatColor.WHITE;
		for (String part : messageParts) {
			if (part.equalsIgnoreCase("@item") && player.hasPermission(Permissions.HIGHLIGHT_ITEM)) {
				ItemStack itemStack = player.getInventory().getItemInMainHand();
				NBTCompound itemData = NBTItem.convertItemtoNBT(itemStack);
				String item = itemData.toString();

				BaseComponent[] itemPartComponent = new ComponentBuilder("")
						.append(ChatColor.AQUA + "[" + ChatColor.GRAY + Utilities.getFriendlyName(itemStack) + ChatColor.AQUA + "]")
						.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(item)}))
						.create();

				messageBuilder.append(itemPartComponent);
			} else if (UrlValidator.getInstance().isValid(part) && player.hasPermission(Permissions.POST_LINK)) {
				BaseComponent[] linkComponent = new ComponentBuilder("")
						.append(ChatColor.BLUE + "" + ChatColor.ITALIC + part)
						.event(new ClickEvent(ClickEvent.Action.OPEN_URL, part))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "\u2139 " + ChatColor.RESET + ChatColor.RED + "Click to open the link.")))
						.create();

				messageBuilder.append(linkComponent);
			} else {
				if (player.hasPermission(Permissions.COLOR_CHAT)) {
					messageBuilder.append(Utilities.format(part)).event((HoverEvent) null).event((ClickEvent) null);
				} else {
					messageBuilder.append(ChatColor.stripColor(Utilities.format(part))).event((HoverEvent) null).event((ClickEvent) null);
				}
			}

			messageBuilder.append(" ").event((HoverEvent) null).event((ClickEvent) null);
		}

		return messageBuilder;
	}
}
