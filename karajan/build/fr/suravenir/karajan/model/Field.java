package fr.suravenir.karajan.model;

import java.io.Serializable;

public class Field implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5715665718676499384L;
	private String sourceName;
	private String targetName;
	private String source;
	private String target;
	private String targetItemName;
	private FieldTypes sourceType;
	private FieldTypes targetType;

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public FieldTypes getSourceType() {
		return sourceType;
	}

	public void setSourceType(final FieldTypes sourceType) {
		this.sourceType = sourceType;
	}

	public FieldTypes getTargetType() {
		return targetType;
	}

	public void setTargetType(final FieldTypes targetType) {
		this.targetType = targetType;
	}

	/**
	 * @param targetName
	 *            the targetName to set
	 */
	public void setTargetName(final String targetName) {
		this.targetName = targetName;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param sourceNname
	 *            the sourceNname to set
	 */
	public void setSourceName(final String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @return the sourceNname
	 */
	public String getSourceName() {
		return sourceName;
	}

	public void setTargetItemName(String targetItemName) {
		this.targetItemName = targetItemName;
	}

	public String getTargetItemName() {
		return targetItemName;
	}
}
