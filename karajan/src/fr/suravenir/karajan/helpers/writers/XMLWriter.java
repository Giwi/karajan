package fr.suravenir.karajan.helpers.writers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import fr.suravenir.karajan.model.Field;
import fr.suravenir.karajan.model.TransfertObject;
import fr.suravenir.karajan.model.UnitOfWork;

public class XMLWriter implements Writable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.suravenir.karajan.helpers.writers.Writable#write(fr.suravenir .ots.imediator.model.UnitOfWork, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String write(UnitOfWork unitOfWork, final Object content) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setRootName(unitOfWork.getTargetArtifact().getOutputObject());
		xmlSerializer.setTypeHintsEnabled(false);
		JSON json = null;
		TransfertObject transObj = unitOfWork.getDataObjects().get(unitOfWork.getTargetArtifact().getOutputObject());
		if (transObj.isCollection()) {
			JSONArray targetList = new JSONArray();
			for (Object item : (List<Object>) content) {
				targetList.add(processFields(unitOfWork, item, false));
			}
			json = targetList;
		} else {
			json = processFields(unitOfWork, content, false);
		}
		return xmlSerializer.write(json);
	}

	/**
	 * @param unitOfWork
	 * @param source
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private JSONObject processFields(UnitOfWork unitOfWork, Object source, boolean inList) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		final JSONObject jsonResponse = new JSONObject();
		for (final Field f : unitOfWork.getTargetArtifact().getDataBinding()) {
			if (f.getSource().equals(unitOfWork.getTargetArtifact().getInputObject()) || inList) {
				if (source instanceof JSONObject) {
					jsonResponse.element(f.getTarget(), ((JSONObject) source).get(f.getSource()));
				} else {

					String[] methods = f.getSourceName().split("\\.");
					List<Object> res = new ArrayList<Object>();
					res.add(source);
					for (int im = 0; im < methods.length; im++) {
						Method m = getTargetMethod(methods[im], res.get(im));
						if (m != null)
							res.add(m.invoke(res.get(im)));
						else
							res.add(null);
					}
					switch (f.getTargetType()) {
					case LIST:
						if (!inList)
							jsonResponse.element(f.getTargetName(), releaseMapping(unitOfWork, res.get(res.size() - 1)));
						break;
					case DATE:
						Object result = res.get(res.size() - 1);
						if (result != null) {
							Date d = new Date();
							if (result instanceof XMLGregorianCalendar) {
								d = ((XMLGregorianCalendar) result).toGregorianCalendar().getTime();
							} else {
								d = (Date) result;
							}
							// TODO : faire plus propre...
							jsonResponse.element(f.getTargetName(), DateFormat.getDateInstance(DateFormat.MEDIUM).format(d));
						}
						break;
					case ENUMTYPE:
						jsonResponse.element(f.getTargetName(), ((Enum<?>) res.get(res.size() - 1)).toString());
						break;
					default:
						jsonResponse.element(f.getTargetName(), res.get(res.size() - 1));
					}

				}
			}
		}
		return jsonResponse;
	}

	/**
	 * @param unitOfWork
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	private JSONArray releaseMapping(UnitOfWork unitOfWork, Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (object != null) {
			JSONArray jsonArray = new JSONArray();
			for (Object o : (List<Object>) object) {
				jsonArray.add(processFields(unitOfWork, o, true));
			}
			return jsonArray;
		}
		return null;
	}

	/**
	 * @param string
	 * @param object
	 * @return
	 */
	private Method getTargetMethod(String string, Object object) {
		if (object != null)
			for (final java.lang.reflect.Method m : object.getClass().getDeclaredMethods()) {
				if (m.getName().equalsIgnoreCase("get" + string)) {
					return m;
				}
			}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.suravenir.ots.fileConverter.processors.writers.Writable#setFile(java .io.File)
	 */
	@Override
	public boolean setFile(final File fileToWrite) {
		// TODO Auto-generated method stub
		return false;
	}

}