package com.hepolite.racialtraits.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.cmd.Instruction;
import com.hepolite.racialtraits.race.ability.AbilityFlight;

public class InstructionAbilityFlight extends Instruction
{
	public InstructionAbilityFlight()
	{
		super("Flight", 1);
		setPlayerOnly();
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("Custom");
		syntaxes.add("Vanilla");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Enables the custom takeoff system");
		descriptions.add("Disables the custom takeoff system");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the calling player to change the takeoff system, such that they may take off by simply jumping.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;

		final String system = arguments.get(0);
		if (system.equalsIgnoreCase("custom"))
		{
			AbilityFlight.setCustomTakeoffSystem(player, true);
			player.sendMessage(ChatColor.WHITE + "You can now start flying by simply jumping!");
		}
		else if (system.equalsIgnoreCase("vanilla"))
		{
			AbilityFlight.setCustomTakeoffSystem(player, false);
			player.sendMessage(ChatColor.WHITE + "You can take off by double jumping!");
		}
		else
			player.sendMessage(ChatColor.RED + "Not sure what you mean by " + ChatColor.WHITE
					+ system + ChatColor.RED + "...");
		return false;
	}
}
