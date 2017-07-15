package com.hepolite.racialtraits.ability.components;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.racialtraits.resource.ResourceStamina;

public class ComponentStaminaModifier
{
	private final String name;

	public ComponentStaminaModifier(String name)
	{
		this.name = name;
	}

	/** Sets the stamina regen modifier represented by this component */
	public final void setRegen(Player player, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_REGEN).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the stamina regen modifier represented by this component */
	public final void setRegen(Player player, float scale, float multiplier, float flat,
			int lifetime)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_REGEN).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the regen modifier represented by this component */
	public final void setRegenLifetime(Player player, int lifetime)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_REGEN).getModifier(name)
				.setLifetime(lifetime);
	}

	/** Sets the maximum stamina modifier represented by this component */
	public final void setMax(Player player, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_MAX).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the maximum stamina modifier represented by this component */
	public final void setMax(Player player, float scale, float multiplier, float flat, int lifetime)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_MAX).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the maximum stamina modifier represented by this component */
	public final void setMaxLifetime(Player player, int lifetime)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_MAX).getModifier(name)
				.setLifetime(lifetime);
	}

	/** Wipes the component, removing the data for the given player */
	public final void wipe(Player player)
	{
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_REGEN).removeModifier(name);
		AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_MAX).removeModifier(name);
	}
}
