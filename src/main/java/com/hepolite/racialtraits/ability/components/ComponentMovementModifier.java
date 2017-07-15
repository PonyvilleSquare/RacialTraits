package com.hepolite.racialtraits.ability.components;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.AttributeType;

public class ComponentMovementModifier
{
	private final String name;

	public ComponentMovementModifier(String name)
	{
		this.name = name;
	}

	/** Sets the flight speed modifier represented by this component */
	public final void setFly(Player player, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(player, AttributeType.SPEED_FLY).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the flight speed modifier represented by this component; the lifetime is measured in seconds */
	public final void setFly(Player player, float scale, float multiplier, float flat, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.SPEED_FLY).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the flight speed modifier represented by this component; the lifetime is measured in seconds */
	public final void setFlyLifetime(Player player, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.SPEED_FLY).getModifier(name).setLifetime(lifetime);
	}

	/** Sets the walk speed modifier represented by this component */
	public final void setWalk(Player player, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(player, AttributeType.SPEED_WALK).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the walk speed modifier represented by this component; the lifetime is measured in seconds */
	public final void setWalk(Player player, float scale, float multiplier, float flat, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.SPEED_WALK).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the walk speed modifier represented by this component; the lifetime is measured in seconds */
	public final void setWalkLifetime(Player player, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.SPEED_WALK).getModifier(name).setLifetime(lifetime);
	}

	/** Wipes the component, removing the data for the given player */
	public final void wipe(Player player)
	{
		AttributeAPI.get(player, AttributeType.SPEED_FLY).removeModifier(name);
		AttributeAPI.get(player, AttributeType.SPEED_WALK).removeModifier(name);
	}
}
