package com.hepolite.racialtraits.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.coreutility.cmd.Instruction;

public class InstructionAbility extends Instruction
{
	public InstructionAbility()
	{
		super("Ability", -1);
		registerSubInstruction(new InstructionAbilityCarry());
		registerSubInstruction(new InstructionAbilityDimensionJump());
		registerSubInstruction(new InstructionAbilityDisguise());
		registerSubInstruction(new InstructionAbilityChangeWeather());
		registerSubInstruction(new InstructionAbilityFlight());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{}

	@Override
	protected void addDescription(List<String> descriptions)
	{}

	@Override
	protected String getExplanation()
	{
		return "This is the base for all ability instructions and will do nothing if invoked.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Invoked the 'Ability' instruction. Did you mistype anything?");
		return true;
	}
}
