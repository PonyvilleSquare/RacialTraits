package com.hepolite.racialtraits.ability.components;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.resource.Resource;

public class ComponentResourceCost
{
	private final Resource resource;

	public ComponentResourceCost(Resource resource)
	{
		this.resource = resource;
	}

	/** Returns true if the given player has at least the given amount available */
	public final boolean has(Player player, float amount)
	{
		return resource.getRemaining(player) >= amount;
	}

	/** Consumes the given amount of the resource for the given player */
	public final void consume(Player player, float amount)
	{
		resource.addResource(player, -amount);
	}

	/** Restores the given amount of the resource for the given player */
	public final void restore(Player player, float amount)
	{
		resource.addResource(player, amount);
	}
}
