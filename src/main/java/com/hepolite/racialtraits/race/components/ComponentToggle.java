package com.hepolite.racialtraits.race.components;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ComponentToggle
{
	private final Set<UUID> activePlayers = new HashSet<UUID>();

	private boolean inverted = false;

	public ComponentToggle(boolean inverted)
	{
		this.inverted = inverted;
	}

	/** Toggles the state for the given player */
	public final void toggle(Player player)
	{
		UUID uuid = player.getUniqueId();
		if (activePlayers.contains(uuid))
			activePlayers.remove(uuid);
		else
			activePlayers.add(uuid);
	}

	/** Sets the state for the given player */
	public final void set(Player player, boolean active)
	{
		if (active)
			activePlayers.add(player.getUniqueId());
		else
			activePlayers.remove(player.getUniqueId());
	}

	/** Returns true if the state is active for the player */
	public final boolean get(Player player)
	{
		return inverted ^ activePlayers.contains(player.getUniqueId());
	}
}
