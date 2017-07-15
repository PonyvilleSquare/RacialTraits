package com.hepolite.racialtraits.ability;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.util.BlockIterator;

import com.hepolite.racialtraits.RacialTraits;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDistantInteraction extends Ability
{
	private final Map<Location, Integer> timers = new HashMap<Location, Integer>();

	public AbilityDistantInteraction(Race race)
	{
		super(race, "Distant Interaction");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int range = getSettings().getInt("range");
		int duration = (int) (20.0f * getSettings().getFloat("duration"));

		BlockIterator it = new BlockIterator(player.getEyeLocation(), 0.0, range);
		while (it.hasNext())
		{
			Block block = it.next();
			Material type = block.getType();
			if (type.isOccluding())
			{
				player.sendMessage(ChatColor.RED + "Can not interact with " + ChatColor.WHITE
						+ type.toString().toLowerCase());
				return false;
			}
			BlockState state = block.getState();

			switch (type)
			{
			case WOOD_BUTTON:
			case STONE_BUTTON:
				Button button = (Button) state.getData();
				if (button.isPowered())
					return false;
				button.setPowered(true);
				state.setData(button);
				state.update(true);
				timers.put(block.getLocation(), RacialTraits.getCurrentTick() + duration);
				return true;

			case LEVER:
				Lever lever = (Lever) state.getData();
				lever.setPowered(!lever.isPowered());
				state.setData(lever);
				state.update(true);
				return true;
			default:
			}
		}
		player.sendMessage(ChatColor.RED + "Could not find anything to interact with");
		return false;
	}

	@Override
	public final void onTick(int tick)
	{
		Collection<Location> locations = new HashSet<Location>();
		for (Entry<Location, Integer> entry : timers.entrySet())
		{
			if (entry.getValue() <= tick)
				locations.add(entry.getKey());
		}
		for (Location location : locations)
		{
			Block block = location.getBlock();
			BlockState state = block.getState();
			Button button = (Button) state.getData();
			button.setPowered(false);
			state.setData(button);
			state.update(true);
			timers.remove(location);
		}
	}
}
