package com.hepolite.racialtraits.skillapi;

import org.bukkit.OfflinePlayer;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;

public class SkillAPIHelper
{
	/** Returns the active SkillAPI account associated with the given player */
	public final static PlayerData getActiveAccount(OfflinePlayer player)
	{
		return SkillAPI.getPlayerData(player);
	}

	/** Returns the race associated with the given player, or null if the player has no race */
	public final static PlayerClass getRaceClass(OfflinePlayer player)
	{
		return getRaceClass(getActiveAccount(player));
	}

	/** Returns the race from the given player data, or null if the player has no race */
	public final static PlayerClass getRaceClass(PlayerData data)
	{
		return data == null ? null : data.getClass("race");
	}

	/** Returns the skill of the given name, if the player has it unlocked. Returns null otherwise */
	public static PlayerSkill getSkill(OfflinePlayer player, String name)
	{
		return player == null ? null : getSkill(SkillAPI.getPlayerData(player), name);
	}

	/** Returns the skill of the given name, if the player has it unlocked. Returns null otherwise */
	public static PlayerSkill getSkill(PlayerData data, String name)
	{
		if (data == null || name == null)
			return null;
		PlayerSkill skill = data.getSkill(name);
		if (skill == null)
			return null;
		return (skill.getLevel() <= 0 && skill.getData().getMaxLevel() > 0) ? null : skill;
	}

	// ///////////////////////////////////////////////////////////////

	/** Consumes the given mana */
	public final static void useMana(OfflinePlayer player, double amount)
	{
		PlayerData data = SkillAPI.getPlayerData(player);
		if (data != null)
			data.useMana(amount);
	}

	/** Grants the given player some mana */
	public final static void giveMana(OfflinePlayer player, double amount)
	{
		PlayerData data = SkillAPI.getPlayerData(player);
		if (data != null)
			data.giveMana(amount);
	}

	// ///////////////////////////////////////////////////////////////

	/** Returns the frequency at which mana regenerates, in ticks */
	public final static int getManaRegenFrequency()
	{
		return SkillAPI.getSettings().getGainFreq();
	}
}
