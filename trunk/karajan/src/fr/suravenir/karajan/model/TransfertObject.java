package fr.suravenir.karajan.model;

public class TransfertObject {
	private Object transObjs;
	private boolean collection;
	private String name;

	public Object getTransObjs() {
		return transObjs;
	}

	public void setTransObjs(Object transObjs) {
		this.transObjs = transObjs;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
