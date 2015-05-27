package aqua.rpgmod.core;

abstract public class InstructionClassPatcher extends InstructionPatcher
{
	public final String oldClassName, className;
	
	public InstructionClassPatcher(MethodPatcher owner, String oldClassName, String className)
	{
		super(owner);
		
		this.oldClassName = oldClassName;
		this.className = className;
	}
	
	public InstructionClassPatcher(String oldClassName, String className)
	{
		this.oldClassName = oldClassName;
		this.className = className;
	}
}
