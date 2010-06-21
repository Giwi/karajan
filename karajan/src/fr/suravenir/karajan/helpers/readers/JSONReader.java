/**
 * 
 */
package fr.suravenir.karajan.helpers.readers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import fr.suravenir.karajan.helpers.Processor;
import fr.suravenir.karajan.helpers.writers.Writable;
import fr.suravenir.karajan.model.Field;
import fr.suravenir.karajan.model.TransfertObject;
import fr.suravenir.karajan.model.UnitOfWork;

/**
 * @author b3605
 */
public class JSONReader implements Readable {
	private String fullContent;

	/*
	 * (non-Javadoc)
	 * 
	 * Lit le flux source et le transforme en target
	 * 
	 * @see fr.suravenir.ots.fileConverter.processors.readers.Readable#convert(fr .suravenir.ots.fileConverter.processors.writers.Writable)
	 */
	@Override
	public String convert(final Writable writer, final UnitOfWork unitOfWork) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException,
			ClassNotFoundException, SecurityException, NoSuchMethodException {
		final JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(fullContent);
		TransfertObject transObj = unitOfWork.getDataObjects().get(unitOfWork.getSourceArtifact().getOutputObject());
		Object target = transObj.getTransObjs();
		if (transObj.isCollection()) {
			return writer.write(unitOfWork, Processor.processAction(unitOfWork, processBindingOnCollection(unitOfWork, jsonObject, target)));
		}
		return writer.write(unitOfWork, Processor.processAction(unitOfWork, processBindingOnObject(unitOfWork, jsonObject, target)));
	}

	/**
	 * @param unitOfWork
	 * @param jsonObject
	 * @param target
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Object processBindingOnObject(UnitOfWork unitOfWork, JSON jsonObject, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
			NoSuchMethodException {
		for (final Field f : unitOfWork.getSourceArtifact().getDataBinding()) {
			if (target instanceof JSONObject) {
				((JSONObject) target).element(f.getTarget(), getSourceValue(f, jsonObject, unitOfWork));
			} else {
				target = processBinding(target, f, jsonObject, unitOfWork);
			}
		}
		return target;
	}

	/**
	 * @param unitOfWork
	 * @param jsonObject
	 * @param target
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private List<Object> processBindingOnCollection(UnitOfWork unitOfWork, JSON jsonObject, Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		List<Object> targetList = new ArrayList<Object>();
		for (final Field f : unitOfWork.getSourceArtifact().getDataBinding()) {
			if (target instanceof JSONObject) {
				((JSONObject) target).element(f.getTarget(), getSourceValue(f, jsonObject, unitOfWork));
			} else {
				targetList.add(processBinding(target, f, jsonObject, unitOfWork));
			}
		}
		return targetList;
	}

	/**
	 * @param target
	 * @param f
	 * @param jsonObject
	 * @param unitOfWork
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	private Object processBinding(Object target, Field f, JSON jsonObject, UnitOfWork unitOfWork) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		for (final Method m : target.getClass().getDeclaredMethods()) {

			// TODO : gérer un type complexe du type du writer
			if (m.getName().equalsIgnoreCase("set" + f.getTargetName())) {
				switch (f.getTargetType()) {
				case DOUBLE:
					m.invoke(target, Double.valueOf(getSourceValue(f, jsonObject, unitOfWork)));
					break;
				case INT:
					m.invoke(target, Integer.valueOf(getSourceValue(f, jsonObject, unitOfWork)));
					break;
				case STRING:
					m.invoke(target, getSourceValue(f, jsonObject, unitOfWork));
					break;
				case DATE:
					// TODO : parser les dates JSON
					m.invoke(target, new Date(getSourceValue(f, jsonObject, unitOfWork)));
					break;
				case BOOLEAN:
					m.invoke(target, Boolean.valueOf(getSourceValue(f, jsonObject, unitOfWork)));
					break;
				case LIST:
					List<Object> output = new ArrayList<Object>();
					Type[] genericParameterTypes = m.getGenericParameterTypes();
					for (Type genericParameterType : genericParameterTypes) {
						if (genericParameterType instanceof ParameterizedType) {
							ParameterizedType aType = (ParameterizedType) genericParameterType;
							Type[] parameterArgTypes = aType.getActualTypeArguments();
							for (int i = 0; i < ((JSONObject) jsonObject).getJSONArray(f.getSourceName()).size(); i++) {
								JSONObject json = ((JSONObject) jsonObject).getJSONArray(f.getSourceName()).getJSONObject(i);
								JsonConfig jsonConfig = new JsonConfig();
								jsonConfig.setRootClass(((Class) parameterArgTypes[0]));
								Object item = processBindingOnObject(unitOfWork, json, JSONSerializer.toJava(json, jsonConfig));
								output.add(item);
							}
						}
					}
					m.invoke(target, output);
					break;
				case ENUMTYPE:
					Enum[] e = (Enum[]) m.getParameterTypes()[0].getEnumConstants();
					String enumName = getSourceValue(f, jsonObject, unitOfWork);
					for (Enum item : e) {
						if (enumName.equals(item.name())) {
							m.invoke(target, item);
						}
					}
					break;
				case MAP:
					String[] map = f.getTargetName().split(".");
					Method getter = target.getClass().getDeclaredMethod("get" + StringUtils.capitalize(map[0]), null);

					Map<String, String> mapObj = (Map<String, String>) getter.invoke(target, null);
					if (mapObj == null) {
						mapObj = new HashMap<String, String>();
						m.invoke(target, mapObj);
					}
					mapObj.put(map[1], getSourceValue(f, jsonObject, unitOfWork));
					break;
				}
			}
		}
		return target;
	}

	/**
	 * @param f
	 * @param jsonObject
	 * @param unitOfWork
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private String getSourceValue(final Field f, final JSON jsonObject, final UnitOfWork unitOfWork) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object source = unitOfWork.getDataObjects().get(f.getSource()).getTransObjs();
		if (source instanceof JSONObject) {
			return ((JSONObject) jsonObject).getString(f.getSourceName());
		}
		final JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(source.getClass());
		source = JSONSerializer.toJava(jsonObject, jsonConfig);
		for (final java.lang.reflect.Method m : source.getClass().getDeclaredMethods()) {
			if (m.getName().equalsIgnoreCase("get" + f.getTargetName())) {
				return String.valueOf(m.invoke(source));
			}
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.suravenir.ots.fileConverter.processors.readers.Readable#getFullContent ()
	 */
	@Override
	public String getFullContent() {
		return fullContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.suravenir.ots.fileConverter.processors.readers.Readable#getLines()
	 */
	@Override
	public List<String> getLines() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.suravenir.ots.fileConverter.processors.readers.Readable#setFile(java .io.File)
	 */
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
