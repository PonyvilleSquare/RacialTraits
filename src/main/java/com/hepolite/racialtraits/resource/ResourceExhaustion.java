package com.hepolite.racialtraits.resource;

import org.bukkit.boss.BarColor;

public class ResourceExhaustion extends Resource
{
	public final static String ATTRIBUTE_MAX = "RESOURCE_EXHAUSTION_MAX";
	public final static String ATTRIBUTE_REGEN = "RESOURCE_EXHAUSTION_REGEN";

	public ResourceExhaustion()
	{
		super("Exhaustion", 1.0f, 0.0f, BarColor.BLUE, ATTRIBUTE_MAX, ATTRIBUTE_REGEN);
		setInverted(true);
	}
}
