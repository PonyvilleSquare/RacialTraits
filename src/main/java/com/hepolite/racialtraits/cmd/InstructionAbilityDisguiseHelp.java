package com.hepolite.racialtraits.cmd;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;

import com.hepolite.coreutility.cmd.Instruction;

public class InstructionAbilityDisguiseHelp extends Instruction
{
	public InstructionAbilityDisguiseHelp()
	{
		super("Help", 0);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Displays the disguise help menu");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the user to view detailed help for how disguises work";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		String[] messages = new String[] {
				"",
				"&n&9Disguise command helper&r&9:",
				"&b-h        &fHides your disguise from yourself",
				"&b-b        &fSets the disguise to be a baby",
				"&b-c:color  &fSets the color of your disguise",
				"&b-t:type   &fSets the type of your disguise",
				"",
				"&n&9Examples&r&9:",
				"&bHiding disguise &f/dis Cow -h",
				"&bBlue baby sheep &f/dis Sheep -b -c:blue",
				"&bUnique horse    &f/dis Horse -c:chestnut -t:white_dots",
				"&bVillager        &f/dis Villager -t:blacksmith",
				"",
				"&n&9More help&r&9:",
				"&b/dhelp         &fLibsDisguises help menu",
				"&b/dhelp <topic> &fLibsDisguises help menu",
				"",
				"&fAll &bLibsDisguises&f settings will also work",
				""
		};
		for (int i = 0; i < messages.length; ++i)
			messages[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
		sender.sendMessage(messages);
		return false;
	}
}
