package com.hepolite.racialtraits.race.ability;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hepolite.coreutility.event.events.PlayerFlightAllowEvent;
import com.hepolite.coreutility.event.events.PlayerJumpEvent;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentToggle;

public class AbilityFlight extends Ability
{
	private static final ComponentToggle toggle = new ComponentToggle(false);

	private final boolean invertWaterCheck;

	public AbilityFlight(Race race, boolean invertWaterCheck)
	{
		super(race, "Flight");
		this.invertWaterCheck = invertWaterCheck;
	}

	/** Sets whether the given player should use the custom takeoff system or not */
	public final static void setCustomTakeoffSystem(Player player, boolean custom)
	{
		toggle.set(player, custom);
	}

	/** Returns true if the given player use the custom takeoff system */
	public final static boolean usesCustomTakeoffSystem(Player player)
	{
		return toggle.get(player);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJump(PlayerJumpEvent event)
	{
		Player player = event.getPlayer();
		if (player.getAllowFlight() && usesCustomTakeoffSystem(player))
			player.setFlying(true);
	}

	// ////////////////////////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerLogin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (!player.getAllowFlight())
			return;
		Block block = player.getLocation().getBlock();
		if (!block.getType().isSolid() && !block.getRelative(0, -1, 0).getType().isSolid())
			player.setFlying(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public final void onPlayerFlightCheck(PlayerFlightAllowEvent event)
	{
		Player player = event.getPlayer();
		if (isPlayerEnclosed(player) || isPlayerInFluids(player))
			event.setFligthAllowed(false);
	}

	/** Checks if the player is in some fluid or not */
	private final boolean isPlayerInFluids(Player player)
	{
		// Magic check; if in water deny flight, unless the invert flag is true
		return invertWaterCheck != player.getEyeLocation().getBlock().isLiquid();
	}

	/** Checks if the player is in an enclosed space */
	private final boolean isPlayerEnclosed(Player player)
	{
		Block block = player.getLocation().add(0.0, -0.05, 0.0).getBlock();
		boolean mask[] = new boolean[27];
		for (int x = -1; x <= 1; x++)
			for (int y = 0; y <= 2; y++)
				for (int z = -1; z <= 1; z++)
					mask[index(x + 1, y, z + 1)] = !block.getRelative(x, y, z).getType().isSolid();

		// Figure out if the area is open or not
		for (int X = 0; X <= 1; X++)
			for (int Y = 0; Y <= 1; Y++)
				for (int Z = 0; Z <= 1; Z++)
				{
					if (mask[index(X, Y, Z)] && mask[index(X, Y, Z + 1)] && mask[index(X, Y + 1, Z)] && mask[index(X, Y + 1, Z + 1)] && mask[index(X + 1, Y, Z)] && mask[index(X + 1, Y, Z + 1)] && mask[index(X + 1, Y + 1, Z)] && mask[index(X + 1, Y + 1, Z + 1)])
						return false;
				}
		return true;
	}

	/** Used for the enclosed space check */
	private final int index(int x, int y, int z)
	{
		return z + 3 * (y + 3 * x);
	}
}
