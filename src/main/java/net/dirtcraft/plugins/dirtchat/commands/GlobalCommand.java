package net.dirtcraft.plugins.dirtchat.commands;

import net.dirtcraft.plugins.dirtchat.data.ChatManager;
import net.dirtcraft.plugins.dirtchat.utils.Permissions;
import net.dirtcraft.plugins.dirtchat.utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GlobalCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		Player player = (Player) sender;

		if (sender.hasPermission(Permissions.WRITE_GLOBAL)) {
			if (args.length == 0) {
				ChatManager.joinGlobal(player.getUniqueId());
				sender.sendMessage(Strings.NOW_GLOBAL);
				return true;
			}

			StringBuilder stringBuilder = new StringBuilder();
			for (String arg : args) {
				stringBuilder.append(arg).append(" ");
			}
			player.chat("###global " + stringBuilder);
		} else {
			sender.sendMessage(Strings.NO_PERMISSION);
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (args.length != 0) {
			return Collections.singletonList("[Message]");
		}

		return null;
	}
}
