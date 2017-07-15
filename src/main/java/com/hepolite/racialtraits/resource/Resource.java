package com.hepolite.racialtraits.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;

public class Resource
{
	private final Map<UUID, Float> currentAmounts = new HashMap<UUID, Float>();
	private final Map<UUID, BossBar> resourceBars = new HashMap<UUID, BossBar>();

	private final String maxAmountAttribute;
	private final String regenerateAttribute;

	private final String name;
	private final BarColor color;
	private final float baseMaxValue;
	private final float baseRegenValue;
	private boolean inverted = false;

	public Resource(String name, float baseMaxValue, float baseRegenValue, BarColor color, String maxAmountAttribute, String regenerateAttribute)
	{
		this.maxAmountAttribute = maxAmountAttribute;
		this.regenerateAttribute = regenerateAttribute;

		this.name = name;
		this.color = color;
		this.baseMaxValue = baseMaxValue;
		this.baseRegenValue = baseRegenValue;
	}

	/** Inverts the bar, so that if the resource is full, the bar is empty */
	protected void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}

	/** Initializes the resource for the given player */
	public final void addPlayer(Player player)
	{
		UUID uuid = player.getUniqueId();
		if (!currentAmounts.containsKey(uuid))
			currentAmounts.put(uuid, 0.0f);
		if (!resourceBars.containsKey(uuid))
			resourceBars.put(uuid, Bukkit.createBossBar(name, color, BarStyle.SEGMENTED_10));

		AttributeAPI.get(player, maxAmountAttribute).setBaseValue(baseMaxValue).setMinValue(0.0f);
		AttributeAPI.get(player, regenerateAttribute).setBaseValue(baseRegenValue);
	}

	/** Deinitializes the resource for the given player */
	public final void removePlayer(Player player)
	{
		UUID uuid = player.getUniqueId();
		BossBar bar = resourceBars.get(uuid);
		if (bar != null)
			bar.removePlayer(player);

		AttributeAPI.remove(player, maxAmountAttribute);
		AttributeAPI.remove(player, regenerateAttribute);
	}

	/** Invoked every tick */
	public final void onTick(Player player, int tick)
	{
		Attribute regen = AttributeAPI.get(player, regenerateAttribute);
		addResource(player, regen.getValue());
	}

	// //////////////////////////////////////////////////////////////

	/** Returns the boss bar associated with the given player; returns null if the resource is not applicable to the given player */
	public final BossBar getBossBar(Player player)
	{
		if (player == null || !isApplicable(player))
			return null;
		return resourceBars.get(player.getUniqueId());
	}

	// //////////////////////////////////////////////////////////////

	/** Returns true if the resource is applicable to the given player */
	public final boolean isApplicable(Player player)
	{
		return currentAmounts.containsKey(player.getUniqueId());
	}

	/** Returns the current resource amount the player have left */
	public final float getRemaining(Player player)
	{
		return getMaximum(player) - getCurrent(player);
	}

	/** Returns the current resource level for the player */
	public final float getCurrent(Player player)
	{
		return currentAmounts.get(player.getUniqueId());
	}

	/** Returns the maximum amount for the given player */
	public final float getMaximum(Player player)
	{
		return AttributeAPI.get(player, maxAmountAttribute).getValue();
	}

	/** Adds the given amount to the resource for the given player */
	public final void addResource(Player player, float amount)
	{
		setResource(player, getCurrent(player) - amount);
	}

	/** Sets the current amount of the resource for the given player */
	public final void setResource(Player player, float amount)
	{
		float max = getMaximum(player);
		amount = Math.max(0.0f, Math.min(max, amount));
		float ratio = Math.min(1.0f, Math.max(0.0f, amount / max));

		UUID uuid = player.getUniqueId();
		float old = currentAmounts.get(uuid);
		if (old != amount)
		{
			currentAmounts.put(uuid, amount);
			BossBar bar = resourceBars.get(uuid);
			if (ratio == 0.0f)
				bar.removePlayer(player);
			else
			{
				bar.addPlayer(player);
				bar.setProgress(inverted ? ratio : 1.0f - ratio);
			}
		}
	}

	/** Sets how much should remain of the resource for the given player */
	public final void setRemaining(Player player, float amount)
	{
		setResource(player, getMaximum(player) - amount);
	}
}
