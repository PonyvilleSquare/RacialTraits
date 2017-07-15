package com.hepolite.racialtraits.race;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.log.Log;
import com.hepolite.racialtraits.race.ability.Ability;
import com.hepolite.racialtraits.race.races.RaceBatPony;
import com.hepolite.racialtraits.race.races.RaceChangeling;
import com.hepolite.racialtraits.race.races.RaceEarthPony;
import com.hepolite.racialtraits.race.races.RacePegasus;
import com.hepolite.racialtraits.race.races.RaceSeaPony;
import com.hepolite.racialtraits.race.races.RaceUnicorn;
import com.hepolite.racialtraits.resource.Resource;
import com.hepolite.racialtraits.skillapi.SkillAPIHelper;
import com.sucy.skill.api.classes.RPGClass;
import com.sucy.skill.api.event.PlayerAccountChangeEvent;
import com.sucy.skill.api.event.PlayerCastSkillEvent;
import com.sucy.skill.api.event.PlayerClassChangeEvent;
import com.sucy.skill.api.event.PlayerSkillDowngradeEvent;
import com.sucy.skill.api.event.PlayerSkillUpgradeEvent;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerSkill;

public class RaceHandler extends CoreHandler
{
	private final Map<String, Race> races = new HashMap<String, Race>();
	private final Map<UUID, Race> players = new HashMap<UUID, Race>();
	private final Collection<UUID> playersToInitialize = new ArrayList<UUID>();

	public RaceHandler()
	{
		addRace(new RaceBatPony());
		addRace(new RaceChangeling());
		addRace(new RaceEarthPony());
		addRace(new RacePegasus());
		addRace(new RaceSeaPony());
		addRace(new RaceUnicorn());
	}

	@Override
	public final void onTick(int tick)
	{
		for (UUID uuid : playersToInitialize)
		{
			Player player = Bukkit.getPlayer(uuid);
			Race race = getPlayerRace(player);
			if (player == null || race == null)
			{
				Log.debug("Failed to initialize race for player " + uuid);
				continue;
			}

			for (Ability ability : race.getAbilities())
			{
				PlayerSkill skill = ability.getSkill(player);
				if (skill != null)
					ability.onInitialize(player, skill.getLevel());
			}
		}
		playersToInitialize.clear();

		for (Entry<UUID, Race> entry : players.entrySet())
		{
			Race race = entry.getValue();
			Player player = Bukkit.getPlayer(entry.getKey());
			if (player == null)
				continue;
			for (Ability ability : race.getAbilities())
			{
				PlayerSkill skill = SkillAPIHelper.getSkill(player, ability.getName());
				if (skill != null)
					ability.onTick(player, skill, tick);
			}

			Resource resource = race.getResource();
			if (resource != null)
				resource.onTick(player, tick);
		}

		for (Race race : races.values())
			for (Ability ability : race.getAbilities())
				ability.onTick(tick);
	}

	@Override
	public final void onRestart()
	{
		for (Race race : races.values())
			for (Ability ability : race.getAbilities())
				ability.getSettings().load();
	}

	// //////////////////////////////////////////////////////////////////////

	/** Adds a race to the system */
	private final Race addRace(Race race)
	{
		races.put(race.getName().toLowerCase(), race);
		return race;
	}

	/** Returns the race with the given name, or null if no races with that name exists */
	public final Race getRace(String race)
	{
		return races.get(race.toLowerCase());
	}

	/** Returns the race with the given SkillAPI class, or null if no races associated with that class exists */
	public final Race getRace(PlayerClass race)
	{
		return race == null ? null : getRace(race.getData());
	}

	/** Returns the race with the given SkillAPI class, or null if no races associated with that class exists */
	public final Race getRace(RPGClass race)
	{
		return race == null ? null : getRace(race.getName());
	}

	// //////////////////////////////////////////////////////////////////////

	/** Sets the given player's race */
	private final void setPlayerRace(Player player, Race race)
	{
		Race prevRace = getPlayerRace(player);
		if (prevRace == race)
			return;
		if (prevRace != null)
			removePlayerRace(player);
		if (race == null)
			return;

		race.addPlayer(player);
		players.put(player.getUniqueId(), race);
		playersToInitialize.add(player.getUniqueId());
		Log.debug("Added player " + player.getName() + " to race " + race.getName());
	}

	/** Removes the race from the given player */
	private final void removePlayerRace(Player player)
	{
		Race race = getPlayerRace(player);
		if (race == null)
			return;

		for (Ability ability : race.getAbilities())
			ability.onDeinitialize(player);

		players.remove(player.getUniqueId());
		race.removePlayer(player);
		Log.debug("Removed player " + player.getName() + " from race " + race.getName());
	}

	/** Returns the race the player current has, or null if the player has no race */
	private final Race getPlayerRace(Player player)
	{
		return player == null ? null : players.get(player.getUniqueId());
	}

	// //////////////////////////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJoinServer(PlayerJoinEvent event)
	{
		setPlayerRace(event.getPlayer(), getRace(SkillAPIHelper.getRaceClass(event.getPlayer())));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerLeaveServer(PlayerQuitEvent event)
	{
		removePlayerRace(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerChangeClass(PlayerClassChangeEvent event)
	{
		setPlayerRace(event.getPlayerData().getPlayer(), getRace(event.getNewClass()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerChangeAccount(PlayerAccountChangeEvent event)
	{
		setPlayerRace(event.getAccountData().getPlayer(),
				getRace(SkillAPIHelper.getRaceClass(event.getNewAccount())));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerUpgradeSkill(PlayerSkillUpgradeEvent event)
	{
		Player player = event.getPlayerData().getPlayer();
		Race race = getPlayerRace(player);
		if (race == null)
			return;
		Ability ability = race.getAbility(event.getUpgradedSkill().getData().getName());
		if (ability == null)
			return;
		int level = event.getUpgradedSkill().getLevel();
		if (level > 0)
			ability.onDeinitialize(player);
		ability.onInitialize(player, level + 1);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerDowngradeSkill(PlayerSkillDowngradeEvent event)
	{
		Player player = event.getPlayerData().getPlayer();
		Race race = getPlayerRace(player);
		if (race == null)
			return;
		Ability ability = race.getAbility(event.getDowngradedSkill().getData().getName());
		if (ability == null)
			return;
		int level = event.getDowngradedSkill().getLevel();
		ability.onDeinitialize(player);
		if (level > 1)
			ability.onInitialize(player, level - 1);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerCastSkill(PlayerCastSkillEvent event)
	{
		Race race = getRace(SkillAPIHelper.getRaceClass(event.getPlayerData()));
		if (race == null)
			return;
		Ability ability = race.getAbility(event.getSkill().getData().getName());
		if (ability != null)
		{
			if (!ability.onCast(event.getPlayer(), event.getSkill()))
				event.setCancelled(true);
		}
	}
}
