package com.hepolite.racialtraits.resource;

import org.bukkit.boss.BarColor;

public class ResourceOxygen extends Resource
{
	public final static String ATTRIBUTE_MAX = "RESOURCE_OXYGEN_MAX";
	public final static String ATTRIBUTE_REGEN = "RESOURCE_OXYGEN_REGEN";

	public ResourceOxygen()
	{
		super("Oxygen", 100.0f, 1.0f, BarColor.BLUE, ATTRIBUTE_MAX, ATTRIBUTE_REGEN);
	}
}
