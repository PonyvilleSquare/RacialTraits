package com.hepolite.racialtraits.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.cmd.Instruction;
import com.hepolite.racialtraits.race.ability.Ability;

public class InstructionAbilityChangeWeather extends Instruction
{
	private static final Map<UUID, Ability> invokers = new HashMap<UUID, Ability>();

	public InstructionAbilityChangeWeather()
	{
		super("ChangeWeather", 1);
		setPlayerOnly();
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("sun");
		syntaxes.add("rain");
		syntaxes.add("storm");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Changes the weather to a sunny day");
		descriptions.add("Changes the weather to a rainy day");
		descriptions.add("Changes the weather to a stormy day");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows you to change the weather in the world you are currently in";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;
		World world = player.getWorld();
		String weather = arguments.get(0);

		Ability ability = getAbility(player);
		if (ability == null || !ability.validate(player))
		{
			sender.sendMessage(ChatColor.RED + "You are unable to do this right now");
			return true;
		}

		if (weather.equalsIgnoreCase("sun"))
		{
			world.setStorm(false);
			world.setThundering(false);
			sender.sendMessage(ChatColor.AQUA + "Changed the weather to sun");
		}
		else if (weather.equalsIgnoreCase("rain"))
		{
			world.setStorm(true);
			world.setThundering(false);
			sender.sendMessage(ChatColor.AQUA + "Changed the weather to rain");
		}
		else if (weather.equalsIgnoreCase("storm"))
		{
			world.setStorm(true);
			world.setThundering(true);
			sender.sendMessage(ChatColor.AQUA + "Changed the weather to storm");
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Could not recognize weather " + ChatColor.WHITE
					+ weather);
			return true;
		}
		ability.cast(player);
		return false;
	}

	/** Links the invoking player to a given ability */
	public static final void addInvoker(Player player, Ability ability)
	{
		invokers.put(player.getUniqueId(), ability);
	}

	/** Returns the ability that has been linked to the given player */
	private static Ability getAbility(Player player)
	{
		return invokers.get(player.getUniqueId());
	}
}
