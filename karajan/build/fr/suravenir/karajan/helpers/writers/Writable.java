package fr.suravenir.karajan.helpers.writers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import fr.suravenir.karajan.model.UnitOfWork;

public interface Writable {
	public boolean setFile(File fileToWrite);

	public String write(UnitOfWork unitOfWork, Object jsonResponse) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

}