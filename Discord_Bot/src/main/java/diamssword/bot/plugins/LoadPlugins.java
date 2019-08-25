package diamssword.bot.plugins;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.diamssword.bot.api.IPlugin;
import com.diamssword.bot.api.References;
import com.diamssword.bot.api.utils.ELoggerControl;

import diamssword.bot.InternalPlugin;

public class LoadPlugins {

	public static List<IPlugin> plugins = new ArrayList<IPlugin>();
	public static final Logger LOG = References.getLogger("PluginLoader", ELoggerControl.REDUCED_CLASS_NAME);
	@SuppressWarnings("rawtypes")
	public static void Find()
	{
		plugins.add(new InternalPlugin());
		LOG.log(Level.INFO, "Loading internal plugin!   ID: "+plugins.get(0).id()+ "    Name:"+plugins.get(0).name());
		try {
			File root = new File("botdata/plugins/");
			root.mkdirs();

			File[] pluginsJar = root.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					return (pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".zip") );
				}});

			for(File plugFile : pluginsJar)
			{
				JarFile jarFile = new JarFile(plugFile.getPath());
				//JarFile jarFile = new JarFile("botdata/plugins/plug.jar");
				Enumeration<JarEntry> e = jarFile.entries();
				URLClassLoader cl =   new URLClassLoader(new URL[] {plugFile.toURI().toURL()},
						Thread.currentThread().getContextClassLoader());

				while (e.hasMoreElements()) {
					JarEntry je = e.nextElement();

					if(je.isDirectory() || !je.getName().endsWith(".class")){
						continue;
					}
					// -6 because of '.class'
					String className = je.getName().substring(0,je.getName().length()-6);
					className = className.replace('/', '.');
					Class c = cl.loadClass(className);
					for(Class cI :c.getInterfaces())
					{
						if(cI.getName().equals(IPlugin.class.getName()))
						{
							IPlugin plug = (IPlugin) c.newInstance();
							int flag = -1;
							for(IPlugin p1 : plugins)
							{
								if(p1.id().equals(plug.id()))
								{
									flag = plugins.indexOf(p1);
									break;
								}
							}
							if(flag == -1)
							{
								LOG.log(Level.INFO, "Found a Plugin!   ID: "+plug.id()+ "    Name:"+plug.name());
								plugins.add(plug);
							}
							else
							{
								LOG.log(Level.SEVERE, "Found a Duplicated Plugin ID!   ID: "+plug.id()+ "    Names:"+plug.name()+"/"+plugins.get(flag).name());
								LOG.log(Level.SEVERE, "Keeping the plugin instance from: "+plugins.get(flag).getClass()+"   The new one : "+plug.getClass()+" (In the jar file '"+jarFile.getName()+"') won't be loaded!");
							}
						}
					}
				}
				jarFile.close();
				cl.close();
			}

		} catch (ClassNotFoundException | IOException e1){
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

	}

	public static void preinit()
	{
		for(IPlugin p : plugins)
		{
			p.preInit();
		}
	}

	public static void init()
	{
		for(IPlugin p : plugins)
		{
			p.init();
		}
	}
	public static void postinit()
	{
		for(IPlugin p : plugins)
		{
			p.postInit();
		}
	}

}

