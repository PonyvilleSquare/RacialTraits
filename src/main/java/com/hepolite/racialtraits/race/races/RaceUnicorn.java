package com.hepolite.racialtraits.race.races;

import com.hepolite.racialtraits.ability.AbilityDeflectorShield;
import com.hepolite.racialtraits.ability.AbilityDimensionJump;
import com.hepolite.racialtraits.ability.AbilityDisarm;
import com.hepolite.racialtraits.ability.AbilityDispel;
import com.hepolite.racialtraits.ability.AbilityDistantInteraction;
import com.hepolite.racialtraits.ability.AbilityDistantReach;
import com.hepolite.racialtraits.ability.AbilityEnchanter;
import com.hepolite.racialtraits.ability.AbilityHold;
import com.hepolite.racialtraits.ability.AbilityMagicalGrasp;
import com.hepolite.racialtraits.ability.AbilityPull;
import com.hepolite.racialtraits.ability.AbilityRepel;
import com.hepolite.racialtraits.ability.AbilityResourceful;
import com.hepolite.racialtraits.ability.AbilityScrollScribe;
import com.hepolite.racialtraits.ability.AbilityTeleport;
import com.hepolite.racialtraits.ability.AbilityThaumicBlast;
import com.hepolite.racialtraits.ability.AbilityThaumicBolt;
import com.hepolite.racialtraits.ability.generic.AbilityProduction;
import com.hepolite.racialtraits.race.Race;

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
