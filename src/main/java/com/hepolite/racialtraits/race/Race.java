package com.hepolite.racialtraits.race;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.racialtraits.RacialTraits;
import com.hepolite.racialtraits.ability.Ability;
import com.hepolite.racialtraits.resource.Resource;

public class Race
{
	private final String name;
	private final Resource resource;

	private final Map<String, Ability> abilities = new HashMap<String, Ability>();

	public Race(String name, Resource resource)
	{
		this.name = name;
		this.resource = resource;

		CoreUtility.getHungerHandler().addGroup(name);
	}

	/** Returns the name of the race */
	public final String getName()
	{
		return name;
	}

	// ///////////////////////////////////////////////////////////////////////////

	/** Returns the resource associated with this race */
	public final Resource getResource()
	{
		return resource;
	}

	// ///////////////////////////////////////////////////////////////////////////

	/** Adds an ability to the race */
	protected final void addAbility(Ability ability)
	{
		if (ability == null || abilities.containsKey(ability.getName()))
			return;
		abilities.put(ability.getName(), ability);
		Bukkit.getPluginManager().registerEvents(ability, RacialTraits.getInstance());
	}

	/** Returns the ability with the given name, if the race has it */
	public final Ability getAbility(String ability)
	{
		for (Ability a : abilities.values())
		{
			if (a.getName().equals(ability))
				return a;
		}
		return null;
	}

	/** Returns the abilities for this race */
	public final Collection<Ability> getAbilities()
	{
		return Collections.unmodifiableCollection(abilities.values());
	}

	// ///////////////////////////////////////////////////////////////////////////

	/** Adds the specified player to this race */
	public final void addPlayer(Player player)
	{
		if (resource != null)
			resource.addPlayer(player);
		CoreUtility.getHungerHandler().setPlayerGroup(player, getName());
	}

	/** Removes the specified player from this race */
	public final void removePlayer(Player player)
	{
		if (resource != null)
			resource.removePlayer(player);
		CoreUtility.getHungerHandler().setPlayerGroup(player, null);
	}
}
