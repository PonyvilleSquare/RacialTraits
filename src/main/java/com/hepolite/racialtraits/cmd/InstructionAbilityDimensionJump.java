package com.hepolite.racialtraits.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.cmd.Instruction;
import com.hepolite.racialtraits.ability.AbilityDimensionJump;

public class InstructionAbilityDimensionJump extends Instruction
{
	private static final Map<UUID, AbilityDimensionJump> invokers = new HashMap<UUID, AbilityDimensionJump>();

	public InstructionAbilityDimensionJump()
	{
		super("DimensionJump", 1);
		setPlayerOnly();
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<dimension>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Teleports you to the given dimension");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows you to teleport to an available dimension of your choice";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;
		String current = player.getWorld().getName().toLowerCase();
		String target = arguments.get(0).toLowerCase();

		AbilityDimensionJump ability = getAbility(player);
		if (ability == null || !ability.performValidationTest(player))
		{
			sender.sendMessage(ChatColor.RED + "You are unable to do this right now");
			return true;
		}
		if (current.equals(target))
		{
			sender.sendMessage(ChatColor.RED + "You are already in that dimension");
			return false;
		}

		World world = Bukkit.getWorld(target);
		if (world == null)
		{
			sender.sendMessage(ChatColor.RED + "Could not recognize dimension " + ChatColor.WHITE
					+ target);
			return false;
		}

		ability.performAbility(player, world.getName());
		return false;
	}

	/** Links the invoking player to a given ability */
	public static final void addInvoker(Player player, AbilityDimensionJump ability)
	{
		invokers.put(player.getUniqueId(), ability);
	}

	/** Returns the ability that has been linked to the given player */
	private static AbilityDimensionJump getAbility(Player player)
	{
		return invokers.get(player.getUniqueId());
	}
}
