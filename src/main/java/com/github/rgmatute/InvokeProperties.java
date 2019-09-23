package com.github.rgmatute;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.util.ResourceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Email: rgmatute91@gmail.com
 * Whatsapp: +593 981851214
 * @Autor ING. Ronny Gabriel Matute Granizo 
 **/
public class InvokeProperties {

	private static final Log log = LogFactory.getLog(InvokeProperties.class.getName());
	// static final Logger logger = (Logger) LogManager.getLogger(InvokeProperties.class.getName());
	private static final Properties configProp = new Properties();
	private static InvokeProperties INSTANCE = null;
	private static String PATHFILE = null;

	private InvokeProperties() {
		try {
			FileInputStream fis = new FileInputStream(PATHFILE);
			log.info("Read all properties from file");
			configProp.load(fis);
		} catch (IOException e) {
			log.error(e);
		}
	}

	private static InvokeProperties getInstance() {
		log.info("getInstance executed....");
		if (INSTANCE == null) {
			if (PATHFILE == null) {
				// EN CASO QUE NO SE CONFIGURE EL FilePath se coloca el por defecto (16092019)
				try {
					setPathFile(ResourceUtils.getFile("classpath:application.properties").getPath(), false);
					log.warn("The properties were charged by default, since an External one was not specified");
					return getInstance();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				INSTANCE = new InvokeProperties();
			}
		}
		return INSTANCE;
		/*
		log.info("getInstance executed....");
		if (INSTANCE == null) {
			if (PATHFILE == null) { // (13092019)
				com.conecel.anotations.InvokePropertiesConfig anotationObtenida = null;
				try {
					ProductOrdeningApplication productOrdeningApplication = new ProductOrdeningApplication();
					Class<?> in = productOrdeningApplication.getClass();
					anotationObtenida = (com.conecel.anotations.InvokePropertiesConfig) in.getAnnotation(com.conecel.anotations.InvokePropertiesConfig.class);
				} catch (NullPointerException e) {
					anotationObtenida = null;
				}
				if(anotationObtenida != null) {
					if(!anotationObtenida.methodHandler().equals("")) {
						setPathFile(anotationObtenida.pathFile(), anotationObtenida.classHandler(), anotationObtenida.methodHandler());
					}else {
						setPathFile(!anotationObtenida.value().equals("")?anotationObtenida.value():anotationObtenida.pathFile(), anotationObtenida.autorefresh());
					}
					log.warn("Properties charge, located in " + anotationObtenida.pathFile());
					return getInstance();
				}else {
					// EN CASO QUE NO SE CONFIGURE EL FilePath se coloca el por defecto (16092019)
					try {
						setPathFile(ResourceUtils.getFile("classpath:application.properties").getAbsolutePath(), false);
						log.warn("The properties were charged by default, since an External one was not specified");
						return getInstance();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					//log.info("It has not assigned the origin of the file [PATHFILE=null]");
				}
			} else {
				INSTANCE = new InvokeProperties();
			}
		}
		return INSTANCE;
		*/
	}

	public static String getProperty(String key) {
		if (INSTANCE == null) {
			getInstance();
		}
		log.info("Key: " + key + " - Value: " + configProp.getProperty(key).toString());
		return configProp.getProperty(key).toString();
	}

	public static Set<String> getAllPropertyNames() {
		if (INSTANCE == null) {
			getInstance();
		}
		return configProp.stringPropertyNames();
	}

	public static boolean containsKey(String key) {
		if (INSTANCE == null) {
			getInstance();
		}
		return configProp.containsKey(key);
	}

	public static boolean setPathFile(String pathfile) {
		if (PATHFILE != null) {
			log.info("PATHFILE OLD: " + PATHFILE + "  |  PATHFILE NEW: " + pathfile);
		} else {
		}
		PATHFILE = pathfile;
		return true;
	}

	public static boolean setPathFile(String pathfile, Class<?> yourClass, String methodCall) {
		if (PATHFILE != null) {
			log.info("PATHFILE OLD: " + PATHFILE + "  |  PATHFILE NEW: " + pathfile);
		} else {
			// WATCHER
			FileWatcher watch = new FileWatcher(new File(pathfile));
			watch.start();
			try {
				// INVOCA AL METHODO CUANDO EL FICHERO SEA ACTUALIZADO
				watch.react(yourClass, methodCall);
			} catch (Exception e) {
				log.error(e);
			}
		}
		PATHFILE = pathfile;
		return true;
	}

	public static boolean setPathFile(String pathfile, boolean watcherRefreshAutomatic) {
		if (PATHFILE != null) {
			log.info("PATHFILE OLD: " + PATHFILE + "  |  PATHFILE NEW: " + pathfile);
		} else {
			if (watcherRefreshAutomatic) {
				// WATCHER
				FileWatcher watch = new FileWatcher(new File(pathfile));
				watch.start();
				try {
					// INVOCA AL METHODO CUANDO EL FICHERO SEA ACTUALIZADO
					watch.react(InvokeProperties.class, "refresh");
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		PATHFILE = pathfile;
		return true;
	}

	public static synchronized void refresh() {
		try {
			INSTANCE = new InvokeProperties();
			log.info("INSTANCE PROPERTIES -> refresh");
		} catch (Exception e) {
			log.error(e);
		}
	}
	

	public static class FileWatcher extends Thread {
		//static final Logger logger = (Logger) LogManager.getLogger(FileWatcher.class.getName());
		// private static final Log logger = LogFactory.getLog(FileWatcher.class.getName());
		private final File file;
		private AtomicBoolean stop = new AtomicBoolean(false);
		private static Class<?> CLASS = null;
		private static String METHOD = null;

		public FileWatcher(File file) {
			this.file = file;
		}

		public boolean isStopped() {
			return stop.get();
		}

		public void stopThread() {
			stop.set(true);
		}

		public void doOnChange() {
			try {
				react(CLASS, METHOD);
			} catch (Exception e) {
				log.error(e);
			}
		}

		public void react(Class<?> yourClass, String methodCall) throws Exception {
			if (CLASS != null & METHOD != null) {
				Method call = CLASS.getDeclaredMethod(METHOD);
				call.invoke(null);
			} else {
				CLASS = yourClass;
				METHOD = methodCall;
			}
		}

		@Override
		public void run() {
			try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
				Path path = file.toPath().getParent();
				path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
				while (!isStopped()) {
					WatchKey key;
					try {
						key = watcher.poll(25, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						return;
					}
					if (key == null) {
						Thread.yield();
						continue;
					}
					// No permite eventos repetidos
					//Thread.sleep(50);
					Thread.sleep(100);
					
					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						@SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path filename = ev.context();

						if (kind == StandardWatchEventKinds.OVERFLOW) {
							Thread.yield();
							continue;
						} else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
								&& filename.toString().equals(file.getName())) {
							doOnChange();
						}
						boolean valid = key.reset();
						if (!valid) {
							break;
						}
					}
					Thread.yield();
				}
			} catch (Throwable e) {
				log.error(e);
			}
		}
	}
}
