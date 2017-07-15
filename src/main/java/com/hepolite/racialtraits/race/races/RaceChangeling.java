package com.hepolite.racialtraits.race.races;

import com.hepolite.racialtraits.ability.AbilityAttitudeControl;
import com.hepolite.racialtraits.ability.AbilityBug;
import com.hepolite.racialtraits.ability.AbilityDisguise;
import com.hepolite.racialtraits.ability.AbilityFirePortal;
import com.hepolite.racialtraits.ability.AbilityFlight;
import com.hepolite.racialtraits.ability.AbilityFlyer;
import com.hepolite.racialtraits.ability.AbilityGlide;
import com.hepolite.racialtraits.ability.AbilityHollowLeg;
import com.hepolite.racialtraits.ability.AbilityNightVision;
import com.hepolite.racialtraits.ability.generic.AbilityHungerModifierFlight;
import com.hepolite.racialtraits.ability.generic.AbilityProduction;
import com.hepolite.racialtraits.race.Race;

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
