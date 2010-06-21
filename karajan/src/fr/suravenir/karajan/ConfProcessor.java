package fr.suravenir.karajan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.suravenir.karajan.helpers.ConfigurationHelper;
import fr.suravenir.karajan.helpers.readers.XMLReader;
import fr.suravenir.karajan.model.UnitOfWork;

public class ConfProcessor {
	public static Map<String, UnitOfWork> unitsOfWork = new HashMap<String, UnitOfWork>();

	/**
	 * @param confDirectory
	 * @return
	 */
	public static boolean configure(File confDirectory) {
		if (confDirectory.exists()) {
			for (final File xmlConfigurationFile : confDirectory.listFiles()) {
				if (xmlConfigurationFile.getName().endsWith(".xml")) {
					final XMLReader confReader = new XMLReader();
					if (confReader.setFile(xmlConfigurationFile)) {
						try {
							ConfProcessor.unitsOfWork.putAll(ConfigurationHelper.loadConfig(confReader.getFullContent()));
						} catch (final ClassNotFoundException e) {
							e.printStackTrace();
							return false;
						} catch (final InstantiationException e) {
							e.printStackTrace();
							return false;
						} catch (final IllegalAccessException e) {
							e.printStackTrace();
							return false;
						}
					}
				}
			}
			return true;
		}
		return false;
	}
}
