package com.hepolite.racialtraits.race.races;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.AbilityAttitudeControl;
import com.hepolite.racialtraits.race.ability.AbilityBug;
import com.hepolite.racialtraits.race.ability.AbilityDisguise;
import com.hepolite.racialtraits.race.ability.AbilityFirePortal;
import com.hepolite.racialtraits.race.ability.AbilityFlight;
import com.hepolite.racialtraits.race.ability.AbilityFlyer;
import com.hepolite.racialtraits.race.ability.AbilityGlide;
import com.hepolite.racialtraits.race.ability.AbilityHollowLeg;
import com.hepolite.racialtraits.race.ability.AbilityNightVision;
import com.hepolite.racialtraits.race.ability.generic.AbilityHungerModifierFlight;
import com.hepolite.racialtraits.race.ability.generic.AbilityProduction;

public class RaceChangeling extends Race
{
	public RaceChangeling()
	{
		super("Changeling", null);
		addAbility(new AbilityAttitudeControl(this));
		addAbility(new AbilityBug(this));
		addAbility(new AbilityProduction(this, "Clear Resin"));
		addAbility(new AbilityDisguise(this));
		addAbility(new AbilityHungerModifierFlight(this, "Efficient Flyer"));
		addAbility(new AbilityFirePortal(this));
		addAbility(new AbilityFlight(this, false));
		addAbility(new AbilityFlyer(this));
		addAbility(new AbilityGlide(this));
		addAbility(new AbilityHollowLeg(this));
		addAbility(new AbilityNightVision(this));
		addAbility(new AbilityProduction(this, "Solid Resin"));
		addAbility(new AbilityProduction(this, "Sticky Resin"));
	}
}
