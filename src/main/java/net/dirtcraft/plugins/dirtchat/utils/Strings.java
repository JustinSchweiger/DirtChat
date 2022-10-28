package net.dirtcraft.plugins.dirtchat.utils;


import org.bukkit.ChatColor;

public class Strings {
	public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "Dirt" + ChatColor.BLUE + "Chat" + ChatColor.GRAY + "] ";
	public static final String INTERNAL_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "Dirt" + ChatColor.BLUE + "Chat" + ChatColor.GRAY + "] ";
	public static final String NO_PERMISSION = PREFIX + ChatColor.RED + "You do not have permission to use this command.";
	public static final String NO_CONSOLE = PREFIX + ChatColor.RED + "You must be a player to use this command.";
	public static final String NOW_GLOBAL = PREFIX + ChatColor.GRAY + "You are now in the channel " + ChatColor.BLUE + "GLOBAL" + ChatColor.GRAY + ".";
	public static final String NOW_STAFF = PREFIX + ChatColor.GRAY + "You are now in the channel " + ChatColor.BLUE + "STAFF" + ChatColor.GRAY + ".";
}
