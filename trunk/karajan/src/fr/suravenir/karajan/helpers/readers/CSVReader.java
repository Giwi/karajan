package fr.suravenir.karajan.helpers.readers;

import java.io.File;
import java.util.List;

import fr.suravenir.karajan.helpers.writers.Writable;
import fr.suravenir.karajan.model.UnitOfWork;

public class CSVReader implements Readable {
	private File fileToRead;
	private String fullContent;

	@Override
	public String convert(final Writable writer, final UnitOfWork unitOfWork) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getFullContent() {
		return fullContent;
	}

	@Override
	public List<String> getLines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setFile(final File fileToRead) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param fullContent
	 *            the fullContent to set
	 */
	public void setFullContent(final String fullContent) {
		this.fullContent = fullContent;
	}

}