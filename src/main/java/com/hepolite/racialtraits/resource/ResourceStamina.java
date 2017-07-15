package com.hepolite.racialtraits.resource;

import org.bukkit.boss.BarColor;

public class ResourceStamina extends Resource
{
	public final static String ATTRIBUTE_MAX = "RESOURCE_STAMINA_MAX";
	public final static String ATTRIBUTE_REGEN = "RESOURCE_STAMINA_REGEN";

	public ResourceStamina()
	{
		super("Stamina", 100.0f, 0.0f, BarColor.GREEN, ATTRIBUTE_MAX, ATTRIBUTE_REGEN);
	}
}
