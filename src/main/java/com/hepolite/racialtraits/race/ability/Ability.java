package com.hepolite.racialtraits.race.ability;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.settings.SettingsSimple;
import com.hepolite.racialtraits.RacialTraits;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.skillapi.SkillAPIHelper;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerSkill;

public class Ability implements Listener
{
	protected static final Random random = new Random();

	private final Race race;
	private final String name;
	private final Settings settings;

	public Ability(Race race, String name)
	{
		this.race = race;
		this.name = name;
		this.settings = new SettingsSimple(RacialTraits.getInstance(), "Races/" + race.getName(), name);
	}

	/** Returns the race owning this ability */
	protected final Race getRace()
	{
		return race;
	}

	/** Returns the name of the ability */
	public final String getName()
	{
		return name;
	}

	/** Returns the settings associated with the ability */
	public final Settings getSettings()
	{
		return settings;
	}

	// ////////////////////////////////////////////////////////////////////

	/** Invoked whenever the skill is initialized for the given player */
	public void onInitialize(Player player, int newLevel)
	{}

	/** Invoked whenever the skill is deinitialized for the given player */
	public void onDeinitialize(Player player)
	{}

	/** Invoked whenever the skill is ticked */
	public void onTick(int tick)
	{}

	/** Invoked whenever the skill is ticked */
	public void onTick(Player player, PlayerSkill skill, int tick)
	{}

	/** Invoked when the skill is cast; returns true if the skill was performed and should go on cooldown */
	public boolean onCast(Player player, PlayerSkill skill)
	{
		return true;
	}

	// ////////////////////////////////////////////////////////////////////

	/** Performs a cast of the ability, if possible */
	public final void cast(Player player)
	{
		PlayerSkill skill = getSkill(player);
		if (skill == null)
			return;
		skill.startCooldown();
	}

	/** Returns true if the player is able to use this ability */
	public final boolean validate(Player player)
	{
		PlayerClass race = SkillAPIHelper.getRaceClass(player);
		if (race == null || !race.getData().getName().equalsIgnoreCase(this.race.getName()))
			return false;
		PlayerSkill skill = getSkill(player);
		return skill != null && !skill.isOnCooldown();
	}

	/** Returns the SkillAPI skill associated with this ability, if the given player can use this ability. Returns null otherwise */
	public final PlayerSkill getSkill(Player player)
	{
		return SkillAPIHelper.getSkill(player, getName());
	}
}
