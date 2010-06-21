package fr.suravenir.karajan.model;

import fr.suravenir.karajan.helpers.readers.CREReader;
import fr.suravenir.karajan.helpers.readers.CSVReader;
import fr.suravenir.karajan.helpers.readers.JSONReader;
import fr.suravenir.karajan.helpers.readers.Readable;
import fr.suravenir.karajan.helpers.readers.XMLReader;
import fr.suravenir.karajan.helpers.writers.CREWriter;
import fr.suravenir.karajan.helpers.writers.CSVWriter;
import fr.suravenir.karajan.helpers.writers.JSONWriter;
import fr.suravenir.karajan.helpers.writers.Writable;
import fr.suravenir.karajan.helpers.writers.XMLWriter;

public enum MediatorContentType {

	XML(new XMLReader(), new XMLWriter()), JSON(new JSONReader(), new JSONWriter()), CSV(new CSVReader(), new CSVWriter()), CRE(new CREReader(), new CREWriter());

	private Writable writer;
	private Readable reader;

	MediatorContentType(final Readable reader, final Writable writer) {
		setReader(reader);
		setWriter(writer);
	}

	public Writable getWriter() {
		return writer;
	}

	public void setWriter(final Writable writer) {
		this.writer = writer;
	}

	public Readable getReader() {
		return reader;
	}

	public void setReader(final Readable reader) {
		this.reader = reader;
	}
}
