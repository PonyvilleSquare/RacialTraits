package com.hepolite.racialtraits.cmd;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.cmd.Instruction;
import com.hepolite.racialtraits.race.ability.AbilityCarry;
import com.hepolite.racialtraits.race.ability.AbilityCarry.Request;

public class InstructionAbilityCarry extends Instruction
{
	public InstructionAbilityCarry()
	{
		super("Carry", 0);
		setPlayerOnly();
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Allows you to be carried by the requester");
	}

	@Override
	protected String getExplanation()
	{
		return "Whenever a player requests to carry you around, you may use this instruction to allow them to do so.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;
		Request request = AbilityCarry.getRequest(player);
		if (request == null)
			player.sendMessage(ChatColor.RED
					+ "No player has requested to pick you up, or request timed out");
		else
		{
			Player carrier = Bukkit.getPlayer(request.uuid);
			if (carrier == null)
				player.sendMessage(ChatColor.WHITE
						+ "The one who wanted to carry you left you all alone :c");
			else if (carrier.getName().equalsIgnoreCase(player.getName()))
				player.sendMessage(ChatColor.WHITE
						+ "How did you manage to send a request to yourself...?");
			else if (carrier.getPassenger() != null)
			{
				Entity passenger = carrier.getPassenger();
				player.sendMessage(ChatColor.WHITE + carrier.getDisplayName() + ChatColor.RED
						+ " is already carrying someone!");
				carrier.sendMessage(ChatColor.RED
						+ "You are unable to carry "
						+ ChatColor.WHITE
						+ player.getDisplayName()
						+ ChatColor.RED
						+ " while carrying "
						+ ChatColor.WHITE
						+ (passenger instanceof Player ? ((Player) passenger).getDisplayName()
								: passenger.getName()));
			}
			else if (carrier.getLocation().distance(player.getLocation()) < 5.0)
			{
				carrier.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA
						+ " accepted your request");
				carrier.setPassenger(player);
			}
			else
			{
				carrier.sendMessage(ChatColor.RED + "You are too far away from " + ChatColor.WHITE
						+ player.getDisplayName() + ChatColor.RED + "!");
				player.sendMessage(ChatColor.RED + "You are too far away from " + ChatColor.WHITE
						+ carrier.getDisplayName() + ChatColor.RED + "!");
			}
		}
		return false;
	}
}
