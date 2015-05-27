package aqua.rpgmod.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassPatcher extends ClassTransformer
{
	private ArrayList<MethodPatcher> components = new ArrayList<MethodPatcher>();
	
	public ClassPatcher(Transformer owner, String className)
	{
		super(owner, className);
	}

	public void add(MethodPatcher patcher)
	{
		this.components.add(patcher);
	}

	public void remove(MethodPatcher patcher)
	{
		this.components.remove(patcher);
	}

	@Override
	public byte[] transform(String className, byte[] bytes)
	{
		if(!this.className.equals(className))
			return bytes;

		AquaCoreMod.log("Class '" + className + "' has been found!");
		
		ClassReader classReader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		
		for(Iterator<MethodNode> i = classNode.methods.iterator(); i.hasNext();)
		{
			MethodNode method = i.next();
			
			for(MethodPatcher patcher : this.components)
			{
				if(patcher.transform(this, method.name, method.desc, method.instructions))
					break;
			}
			
			if(this.components.isEmpty())
				break;
		}

		if(!this.components.isEmpty())
			AquaCoreMod.log("Non-used method patchers: " + this.components.size());
		else
			this.owner.components.remove(this);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
