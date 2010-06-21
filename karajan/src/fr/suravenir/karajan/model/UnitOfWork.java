package fr.suravenir.karajan.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class UnitOfWork {
	private Artifact sourceArtifact;
	private Artifact targetArtifact;
	private Map<String, TransfertObject> dataObjects;
	private String name;

	/**
	 * @return the sourceArtifact
	 */
	public Artifact getSourceArtifact() {
		return sourceArtifact;
	}

	/**
	 * @param sourceArtifact
	 *            the sourceArtifact to set
	 */
	public void setSourceArtifact(final Artifact sourceArtifact) {
		this.sourceArtifact = sourceArtifact;
	}

	/**
	 * @return the targetArtifact
	 */
	public Artifact getTargetArtifact() {
		return targetArtifact;
	}

	/**
	 * @param targetArtifact
	 *            the targetArtifact to set
	 */
	public void setTargetArtifact(final Artifact targetArtifact) {
		this.targetArtifact = targetArtifact;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param jsonContent
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public String processFromString(final String jsonContent) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException,
			SecurityException, NoSuchMethodException {
		sourceArtifact.getType().getReader().setFullContent(jsonContent);
		return sourceArtifact.getType().getReader().convert(targetArtifact.getType().getWriter(), this);
	}

	/**
	 * @param dataObjects
	 *            the dataObjects to set
	 */
	public void setDataObjects(final Map<String, TransfertObject> dataObjects) {
		this.dataObjects = dataObjects;
	}

	/**
	 * @return the dataObjects
	 */
	public Map<String, TransfertObject> getDataObjects() {
		return dataObjects;
	}
}
