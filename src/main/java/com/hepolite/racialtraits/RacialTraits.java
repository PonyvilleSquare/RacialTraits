package com.hepolite.racialtraits;

import com.hepolite.coreutility.cmd.CommandHandler;
import com.hepolite.coreutility.cmd.InstructionReload;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.racialtraits.cmd.InstructionAbility;
import com.hepolite.racialtraits.cmd.InstructionRace;
import com.hepolite.racialtraits.race.RaceHandler;

public class RacialTraits extends CorePlugin
{
	private static RacialTraits INSTANCE;
	private RaceHandler raceHandler;
	private int currentTick;

	@Override
	protected final void onInitialize()
	{
		INSTANCE = this;

		CommandHandler commandHandler = setCommandHandler(new CommandHandler(this, "race"));
		commandHandler.registerInstruction(new InstructionReload(this, "racialtraits.race.admin"));
		commandHandler.registerInstruction(new InstructionAbility());
		commandHandler.registerInstruction(new InstructionRace());

		raceHandler = (RaceHandler) addHandler(new RaceHandler());
	}

	@Override
	protected final void onDeinitialize()
	{}

	@Override
	protected final void onTick(int tick)
	{
		currentTick = tick;
	}

	@Override
	protected void onRestart()
	{}

	// ////////////////////////////////////////////////////////////////

	/** Returns the plugin instance */
	public static final RacialTraits getInstance()
	{
		return INSTANCE;
	}

	/** Returns the race handler */
	public static final RaceHandler getRaceHandler()
	{
		return INSTANCE.raceHandler;
	}

	/** Returns the current game tick */
	public static final int getCurrentTick()
	{
		return INSTANCE.currentTick;
	}
}
