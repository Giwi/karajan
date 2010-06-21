/**
 * 
 */
package fr.suravenir.karajan.helpers.readers;

import java.io.File;
import java.util.List;

import fr.suravenir.karajan.helpers.writers.Writable;
import fr.suravenir.karajan.model.UnitOfWork;

/**
 * @author b3605
 */
public class CREReader implements Readable {
	private File fileToRead;
	private String fullContent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.suravenir.ots.fileConverter.processors.readers.Readable#getFullContent
	 * ()
	 */
	@Override
	public String getFullContent() {
		return fullContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.suravenir.ots.fileConverter.processors.readers.Readable#getLines()
	 */
	@Override
	public List<String> getLines() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.suravenir.ots.fileConverter.processors.readers.Readable#setFile(java
	 * .io.File)
	 */
	@Override
	public boolean setFile(final File fileToRead) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String convert(final Writable writer, final UnitOfWork unitOfWork) {
		// TODO Auto-generated method stub
		return "";
	}

	/**
	 * @param fullContent
	 *            the fullContent to set
	 */
	public void setFullContent(final String fullContent) {
		this.fullContent = fullContent;
	}

}