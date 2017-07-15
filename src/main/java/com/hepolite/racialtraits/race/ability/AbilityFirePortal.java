package com.hepolite.racialtraits.race.ability;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityFirePortal extends Ability
{
	public AbilityFirePortal(Race race)
	{
		super(race, "Fire Portal");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		Location target = findLocation(player, skill);
		if (target == null)
		{
			player.sendMessage(ChatColor.RED + "The spell failed to locate a destination");
			return false;
		}
		target.setDirection(player.getEyeLocation().getDirection());

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.7f, 0.0f);
		player.teleport(target);
		player.getWorld().playSound(target, Sound.ENTITY_ENDERMEN_TELEPORT, 0.7f, 0.0f);
		return true;
	}

	/** Finds a destination for the teleporting */
	private final Location findLocation(Player player, PlayerSkill skill)
	{
		int range = getSettings().getInt("Level " + skill.getLevel() + ".range");
		Location start = player.getEyeLocation();
		Location target = null;

		boolean foundSolidWall = false;
		BlockIterator it = new BlockIterator(start, 0.0, range);
		while (it.hasNext())
		{
			Block block = it.next();
			if (canTeleportThrough(block))
				foundSolidWall = true;
			else if (foundSolidWall)
			{
				Block neighbor = block.getRelative(BlockFace.DOWN);
				if ((block.isEmpty() || block.isLiquid()) && (neighbor.isEmpty() || neighbor.isLiquid()))
				{
					target = neighbor.getLocation();
					break;
				}
			}
			if (foundSolidWall && block.getType() != Material.AIR)
				break;
		}
		return target == null ? null : target.add(0.5, 0.0, 0.5);
	}

	/** Returns true if the player can teleport through the given block */
	private final boolean canTeleportThrough(Block block)
	{
		return block.getType().isOccluding();
	}
}
