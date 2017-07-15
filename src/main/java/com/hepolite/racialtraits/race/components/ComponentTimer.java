package com.hepolite.racialtraits.race.components;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.RacialTraits;

public class ComponentTimer
{
	private final Map<UUID, Integer> endTimes = new HashMap<UUID, Integer>();

	/** Starts the timer for the given player; the duration is measured in seconds */
	public final void start(Player player, float duration)
	{
		if (player != null)
			endTimes.put(player.getUniqueId(), RacialTraits.getCurrentTick()
					+ (int) (20.0f * duration));
	}

	/** Stops the timer for the given player */
	public final void stop(Player player)
	{
		if (player != null)
			endTimes.remove(player.getUniqueId());
	}

	/** Returns true if the timer is still ticking for the given player */
	public final boolean isActive(Player player)
	{
		return getRemainingTimeAsTicks(player) > 0;
	}

	/** Returns true if the timer is at its last tick */
	public final boolean isLastTick(Player player)
	{
		return getRemainingTimeAsTicks(player) == 1;
	}

	/** Returns the remaining time (as ticks) for the given player */
	public final int getRemainingTimeAsTicks(Player player)
	{
		if (player == null)
			return 0;
		UUID uuid = player.getUniqueId();
		return endTimes.containsKey(uuid) ? Math.max(0,
				endTimes.get(uuid) - RacialTraits.getCurrentTick()) : 0;
	}

	/** Returns the remaining time (as seconds) for the given player */
	public final float getRemainingTime(Player player)
	{
		return 0.05f * (float) getRemainingTimeAsTicks(player);
	}

	/** Returns the remaining time (as milliseconds) for the given player */
	public final int getRemainingTimeAsMS(Player player)
	{
		return 50 * getRemainingTimeAsTicks(player);
	}
}
