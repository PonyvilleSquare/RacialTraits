package com.hepolite.racialtraits.race.races;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.AbilityDeflectorShield;
import com.hepolite.racialtraits.race.ability.AbilityDimensionJump;
import com.hepolite.racialtraits.race.ability.AbilityDisarm;
import com.hepolite.racialtraits.race.ability.AbilityDispel;
import com.hepolite.racialtraits.race.ability.AbilityDistantInteraction;
import com.hepolite.racialtraits.race.ability.AbilityDistantReach;
import com.hepolite.racialtraits.race.ability.AbilityEnchanter;
import com.hepolite.racialtraits.race.ability.AbilityHold;
import com.hepolite.racialtraits.race.ability.AbilityMagicalGrasp;
import com.hepolite.racialtraits.race.ability.AbilityPull;
import com.hepolite.racialtraits.race.ability.AbilityRepel;
import com.hepolite.racialtraits.race.ability.AbilityResourceful;
import com.hepolite.racialtraits.race.ability.AbilityScrollScribe;
import com.hepolite.racialtraits.race.ability.AbilityTeleport;
import com.hepolite.racialtraits.race.ability.AbilityThaumicBlast;
import com.hepolite.racialtraits.race.ability.AbilityThaumicBolt;
import com.hepolite.racialtraits.race.ability.generic.AbilityProduction;

public class RaceUnicorn extends Race
{
	public RaceUnicorn()
	{
		super("Unicorn", null);
		addAbility(new AbilityDeflectorShield(this));
		addAbility(new AbilityDimensionJump(this));
		addAbility(new AbilityDisarm(this));
		addAbility(new AbilityDispel(this));
		addAbility(new AbilityDistantInteraction(this));
		addAbility(new AbilityDistantReach(this));
		addAbility(new AbilityEnchanter(this));
		addAbility(new AbilityHold(this));
		addAbility(new AbilityProduction(this, "Infuse Stone"));
		addAbility(new AbilityMagicalGrasp(this));
		addAbility(new AbilityPull(this));
		addAbility(new AbilityRepel(this));
		addAbility(new AbilityResourceful(this));
		addAbility(new AbilityScrollScribe(this));
		addAbility(new AbilityTeleport(this, "Teleport"));
		addAbility(new AbilityTeleport(this, "Teleport Group"));
		addAbility(new AbilityThaumicBlast(this));
		addAbility(new AbilityThaumicBolt(this));
	}
}
