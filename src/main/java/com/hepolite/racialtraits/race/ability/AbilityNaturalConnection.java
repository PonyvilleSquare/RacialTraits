package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.skillapi.SkillAPIHelper;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityNaturalConnection extends Ability
{
	public AbilityNaturalConnection(Race race)
	{
		super(race, "Natural Connection");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMobSpawn(CreatureSpawnEvent event)
	{
		if (event.getSpawnReason() != SpawnReason.BREEDING)
			return;

		float range = getSettings().getFloat("range");
		SphereLocater locater = new SphereLocater(event.getLocation(), range);
		for (Player player : locater.get(Player.class))
		{
			PlayerSkill skill = SkillAPIHelper.getSkill(player, getName());
			if (skill == null)
				continue;

			float chance = getSettings().getFloat("chance");
			int attempts = getSettings().getInt("attempts");
			spawn(event, chance, attempts);
		}
	}

	/** Spawns a few animals at the given location */
	private final void spawn(CreatureSpawnEvent event, float chance, int attempts)
	{
		for (int i = 0; i < attempts; i++)
		{
			if (random.nextFloat() < chance)
			{
				Entity child = event.getLocation().getWorld()
						.spawnEntity(event.getLocation(), event.getEntityType());
				if (child instanceof Ageable)
					((Ageable) child).setBaby();
			}
		}
	}
}
