package com.hepolite.racialtraits.ability;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.skillapi.SkillAPIHelper;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityResourceful extends Ability
{
	public AbilityResourceful(Race race)
	{
		super(race, "Resourceful");
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (tick % SkillAPIHelper.getManaRegenFrequency() != 0)
			return;

		double modifier = getSettings().getDouble("Level " + skill.getLevel() + ".modifier");

		PlayerClass race = SkillAPIHelper.getRaceClass(player);
		SkillAPIHelper.giveMana(player, modifier * race.getData().getManaRegen());
	}
}
