package com.hepolite.racialtraits.ability.components;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.AttributeType;

public class ComponentHungerModifier
{
	private final String name;

	public ComponentHungerModifier(String name)
	{
		this.name = name;
	}

	/** Sets the max hunger modifier represented by this component */
	public final void setMax(Player player, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_MAX).getModifier(name).setScale(scale).setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the max hunger modifier represented by this component; the lifetime is measured in seconds */
	public final void setMax(Player player, float scale, float multiplier, float flat, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_MAX).getModifier(name).setScale(scale).setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the max hunger modifier represented by this component; the lifetime is measured in seconds */
	public final void setMaxLifetime(Player player, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_MAX).getModifier(name).setLifetime(lifetime);
	}

	/** Sets the hunger decrease modifier represented by this component */
	public final void setDecrease(Player player, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_DECREASE).getModifier(name).setScale(scale).setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the hunger decrease modifier represented by this component; the lifetime is measured in seconds */
	public final void setDecrease(Player player, float scale, float multiplier, float flat, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_DECREASE).getModifier(name).setScale(scale).setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the hunger decrease modifier represented by this component; the lifetime is measured in seconds */
	public final void setDecreaseLifetime(Player player, int lifetime)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_DECREASE).getModifier(name).setLifetime(lifetime);
	}

	/** Wipes the component, removing the data for the given player */
	public final void wipe(Player player)
	{
		AttributeAPI.get(player, AttributeType.HUNGER_MAX).removeModifier(name);
		AttributeAPI.get(player, AttributeType.HUNGER_DECREASE).removeModifier(name);
	}
}
