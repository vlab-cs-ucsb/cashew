

import gov.nasa.jpf.Config;

import java.lang.reflect.InvocationTargetException;

public class RunQuantifier extends gov.nasa.jpf.tool.Run {

	public static final int HELP = 1;
	public static final int SHOW = 2;
	public static final int LOG = 4;
	public static final int TIMING = 8;
	public static final int MEMORY = 16;

	static final String JPF_CLASSNAME = "gov.nasa.jpf.JPF";

	public static void main(String[] args) {
		try {
			int options = getOptions(args);

			if (args.length == 0 || isOptionEnabled(HELP, options)) {
				showUsage();
				return;
			}

			if (isOptionEnabled(LOG, options)) {
				Config.enableLogging(true);
			}

			Config conf = new Config(args);

			if (isOptionEnabled(SHOW, options)) {
				conf.printEntries();
			}
			
			// configure the side-channel quantifier
			configSCQ(conf);
			// conf.printEntries();
			
			// setting option in parameters have higher priority than in .jpf file
			if (isOptionEnabled(TIMING, options)) {
				conf.setProperty("listener", "sidechannel.TimingChannelQuantifier");
			}
			
			if (isOptionEnabled(MEMORY, options)) {
				conf.setProperty("listener", "sidechannel.MemoryChannelQuantifier");
			}

			ClassLoader cl = conf
					.initClassLoader(RunQuantifier.class.getClassLoader());

			Class<?> jpfCls = cl.loadClass(JPF_CLASSNAME);
			if (!call(jpfCls, "start", new Object[] { conf, args })) {
				error("cannot find 'public static start(Config,String[])' in "
						+ JPF_CLASSNAME);
			}
		} catch (NoClassDefFoundError ncfx) {
			ncfx.printStackTrace();
		} catch (ClassNotFoundException cnfx) {
			error("cannot find " + JPF_CLASSNAME);
		} catch (InvocationTargetException ix) {
			// should already be handled by JPF
			ix.getCause().printStackTrace();
		}
	}

	static void configSCQ(Config conf) {
		String sidechannel = conf.getProperty("sidechannel");
		if(sidechannel == null) {
			return;
		}
		switch(sidechannel){
		case "timing":
			conf.setProperty("listener", "sidechannel.TimingChannelQuantifier");
			break;
		case "memory":
			conf.setProperty("listener", "sidechannel.MemoryChannelQuantifier");
			break;
		default:
			throw new RuntimeException("Invalid sidechannel");
		}
	}

	public static int getOptions(String[] args) {
		int mask = 0;

		if (args != null) {

			for (int i = 0; i < args.length; i++) {
				String a = args[i];
				if ("-help".equals(a)) {
					args[i] = null;
					mask |= HELP;

				} else if ("-show".equals(a)) {
					args[i] = null;
					mask |= SHOW;

				} else if ("-log".equals(a)) {
					args[i] = null;
					mask |= LOG;
				} else if ("-timing".equals(a)) {
					args[i] = null;
					mask |= TIMING;
				} else if ("-memory".equals(a)) {
					args[i] = null;
					mask |= MEMORY;
				}
			}
		}

		return mask;
	}

	public static boolean isOptionEnabled(int option, int mask) {
		return ((mask & option) != 0);
	}

	public static void showUsage() {
		// TODO: show usage
		System.out
				.println("Usage: \"java [<vm-option>..] -jar ...RunJPF.jar [<jpf-option>..] [<app> [<app-arg>..]]");
		System.out
				.println("  <jpf-option> : -help : print usage information and exit");
		System.out
				.println("               | -version : print JPF version information");
		System.out
				.println("               | -log : print configuration initialization steps");
		System.out
				.println("               | -show : print configuration dictionary contents");
		System.out
				.println("               | +<key>=<value>  : add or override key/value pair to config dictionary");
		System.out
				.println("  <app>        : *.jpf application properties file pathname | fully qualified application class name");
		System.out
				.println("  <app-arg>    : arguments passed into main() method of application class");
	}
}
