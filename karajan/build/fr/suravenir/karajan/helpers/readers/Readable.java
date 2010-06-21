package fr.suravenir.karajan.helpers.readers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import fr.suravenir.karajan.helpers.writers.Writable;
import fr.suravenir.karajan.model.UnitOfWork;

public interface Readable {

	public boolean setFile(File fileToRead);

	public String convert(Writable writer, UnitOfWork unitOfWork) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException,
			SecurityException, NoSuchMethodException;

	public String getFullContent();

	public void setFullContent(String fullContent);

	public List<String> getLines();

}