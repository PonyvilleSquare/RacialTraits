package com.hepolite.racialtraits.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.coreutility.cmd.Instruction;
import com.hepolite.coreutility.cmd.InstructionDebugHunger;

public class InstructionRace extends Instruction
{
	public InstructionRace()
	{
		super("Race", -1);
		registerSubInstruction(new InstructionDebugHunger());
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
		return "This is the base for all race instructions and will do nothing if invoked.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Invoked the 'Race' instruction. Did you mistype anything?");
		return false;
	}
}
