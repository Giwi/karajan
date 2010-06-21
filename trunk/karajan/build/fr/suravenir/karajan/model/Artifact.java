package fr.suravenir.karajan.model;

import java.io.File;
import java.util.List;

public class Artifact {
	private File artifactFile;
	private MediatorContentType type;
	private String action;
	private String inputObject;
	private String outputObject;
	private List<Field> dataBinding;

	public String getInputObject() {
		return inputObject;
	}

	public void setInputObject(String inputObject) {
		this.inputObject = inputObject;
	}

	public String getOutputObject() {
		return outputObject;
	}

	public void setOutputObject(String outputObject) {
		this.outputObject = outputObject;
	}

	/**
	 * @return the artifactFile
	 */
	public File getArtifactFile() {
		return artifactFile;
	}

	/**
	 * @param artifactFile
	 *            the artifactFile to set
	 */
	public void setArtifactFile(final File artifactFile) {
		this.artifactFile = artifactFile;
	}

	/**
	 * @return the type
	 */
	public MediatorContentType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final MediatorContentType type) {
		this.type = type;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(final String action) {
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	public void setDataBinding(List<Field> dataBinding) {
		this.dataBinding = dataBinding;
	}

	public List<Field> getDataBinding() {
		return dataBinding;
	}
}
