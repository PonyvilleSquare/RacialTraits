package com.hepolite.racialtraits.race.ability;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityAttitudeControl extends Ability
{
	public AbilityAttitudeControl(Race race)
	{
		super(race, "Attitude Control");
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (!player.getAllowFlight() || player.isFlying())
			return;
		if (!player.getLocation().getBlock().getRelative(0, -1, 0).getType().isSolid())
		{
			handleDeadlyFalls(player);
			handleFastFalls(player);
			handleDangerousBlocks(player);
		}
	}

	/** Handles the prevention of death by fall damage */
	private final void handleDeadlyFalls(Player player)
	{
		float damage = CoreUtility.getMovementHandler().getFallDamage(player);
		if (Math.max(0.01, player.getHealth() - 6.0) > damage)
			return;

		int X = (int) Math.floor(player.getLocation().getX() - 0.5);
		int Y = player.getLocation().getBlockY();
		int Z = (int) Math.floor(player.getLocation().getZ() - 0.5);
		for (int y = Y; y > Math.max(0, Y - 20); y--)
		{
			Block blockA = player.getWorld().getBlockAt(X, y, Z);
			Block blockB = player.getWorld().getBlockAt(X + 1, y, Z);
			Block blockC = player.getWorld().getBlockAt(X, y, Z + 1);
			Block blockD = player.getWorld().getBlockAt(X + 1, y, Z + 1);
			if (blockA.getType().isSolid() || blockB.getType().isSolid() || blockC.getType().isSolid() || blockD.getType().isSolid())
			{
				player.setFlying(true);
				break;
			}
		}
	}

	/** Handle too fast falls prevention */
	private final void handleFastFalls(Player player)
	{
		if (player.isSneaking() || player.isGliding())
			return;

		double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (CoreUtility.getMovementHandler().getFallDamage(player) > maxHealth)
			player.setFlying(true);
	}

	/** Handle lava and fire prevention */
	private final void handleDangerousBlocks(Player player)
	{
		if (player.isSneaking())
			return;

		int X = (int) Math.floor(player.getLocation().getX() - 0.5);
		int Y = player.getLocation().getBlockY();
		int Z = (int) Math.floor(player.getLocation().getZ() - 0.5);
		for (int y = Y; y > Math.max(0, Y - 10); y--)
		{
			Block blockA = player.getWorld().getBlockAt(X, y, Z);
			Block blockB = player.getWorld().getBlockAt(X + 1, y, Z);
			Block blockC = player.getWorld().getBlockAt(X, y, Z + 1);
			Block blockD = player.getWorld().getBlockAt(X + 1, y, Z + 1);
			if (isDangerous(blockA) || isDangerous(blockB) || isDangerous(blockC) || isDangerous(blockD))
			{
				player.setFlying(true);
				break;
			}
		}
	}

	/** Returns true if the block is a dangerous material */
	private final boolean isDangerous(Block block)
	{
		Material type = block.getType();
		return (type == Material.LAVA || type == Material.STATIONARY_LAVA || type == Material.FIRE || type == Material.CACTUS);
	}
}
