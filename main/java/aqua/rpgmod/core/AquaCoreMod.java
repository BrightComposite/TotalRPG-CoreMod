package aqua.rpgmod.core;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;

@Name("TotalRPGcore")
@MCVersion("1.7.10")
@DependsOn("forge")
@SortingIndex(2001)
public class AquaCoreMod extends DummyModContainer implements IFMLLoadingPlugin
{
	public File location;
    public static AquaCoreMod mod = null;
    public boolean obfuscatedEnvironment = true;
    
	public AquaCoreMod()
	{
		super(new ModMetadata());
		
		if(mod == null)
			mod = this;
		
		ModMetadata meta = getMetadata();
		meta.modId = "TotalRPGcore";
		meta.name = "Total RPG Core Mod";
		meta.version = "0.9.0";
		meta.authorList = Arrays.asList("Aqua");
		meta.description = "Core mod for Total RPG";
		meta.url = "";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	@SuppressWarnings("unused")
	@Subscribe
	public void modConstruction(FMLConstructionEvent evt) {}
	
	@SuppressWarnings({
		"unused", "static-method"
	})
    @Subscribe
	public void preInit(FMLPreInitializationEvent event)
	{
		if(!Transformer.instance.components.isEmpty())
		{
			log("Patching hasn't been completed successfully! Classes hasn't been found: ");
			
			for(ClassTransformer trans : Transformer.instance.components)
				log(trans.className);
		}
	}
	
	@SuppressWarnings("unused")
	@Subscribe
	public void init(FMLInitializationEvent event) {}
	
	@SuppressWarnings("unused")
	@Subscribe
	public void postInit(FMLPostInitializationEvent event) {}
	
    public static void log(String msg)
    {
    	CoreLogger.log(Level.INFO, msg);
    }
 
	@Override
	public String[] getASMTransformerClass() {
		return new String[]
		{
			Transformer.class.getName()
		};
	}

	@Override
	public String getModContainerClass() {
		return AquaCoreMod.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		this.location = (File) data.get("coremodLocation");
		this.obfuscatedEnvironment = ((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
		Transformer.init();
	}

	@Override
	public String getAccessTransformerClass() {
		return AccessTransformer.class.getName();
	}

}
