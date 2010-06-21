package fr.suravenir.karajan.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import fr.suravenir.karajan.model.Artifact;
import fr.suravenir.karajan.model.Field;
import fr.suravenir.karajan.model.FieldTypes;
import fr.suravenir.karajan.model.MediatorContentType;
import fr.suravenir.karajan.model.TransfertObject;
import fr.suravenir.karajan.model.UnitOfWork;

public class ConfigurationHelper {
	/**
	 * @param xmlConfigurationFile
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Map<String, UnitOfWork> loadConfig(final String xmlContent) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		final XMLSerializer xmlSer = new XMLSerializer();
		final JSONArray json = (JSONArray) xmlSer.read(xmlContent);

		final Map<String, UnitOfWork> units = new HashMap<String, UnitOfWork>();

		for (int i = 0; i < json.size(); i++) {
			final UnitOfWork uow = new UnitOfWork();
			uow.setName(json.getJSONObject(i).getString("@name"));
			final Artifact sourceArtifact = new Artifact();
			sourceArtifact.setAction(json.getJSONObject(i).getJSONObject("input").getString("@action"));
			sourceArtifact.setType(MediatorContentType.valueOf(json.getJSONObject(i).getJSONObject("input").getString("@format").toUpperCase()));
			sourceArtifact.setInputObject(json.getJSONObject(i).getJSONObject("input").getString("@inputObject"));
			sourceArtifact.setOutputObject(json.getJSONObject(i).getJSONObject("input").getString("@outputObject"));

			List<Field> lof = new ArrayList<Field>();
			// truc à la con quand jsonlib jsonifie du xml, s'il n'y a q'une
			// balise fille, c'est un jsonobject, sinon, c'est du jsonarray ...
			try {
				JSONArray dataBindings = json.getJSONObject(i).getJSONObject("input").getJSONArray("field");
				for (int j = 0; j < dataBindings.size(); j++) {
					lof.add(getDatabinding(dataBindings.getJSONObject(j)));
				}
			} catch (JSONException e) {
				lof.add(getDatabinding(json.getJSONObject(i).getJSONObject("input").getJSONObject("field")));
			}
			sourceArtifact.setDataBinding(lof);
			uow.setSourceArtifact(sourceArtifact);

			final Artifact targetArtifact = new Artifact();
			targetArtifact.setType(MediatorContentType.valueOf(json.getJSONObject(i).getJSONObject("output").getString("@format").toUpperCase()));
			targetArtifact.setAction(json.getJSONObject(i).getJSONObject("output").getString("@action"));
			targetArtifact.setInputObject(json.getJSONObject(i).getJSONObject("output").getString("@inputObject"));
			targetArtifact.setOutputObject(json.getJSONObject(i).getJSONObject("output").getString("@outputObject"));

			lof = new ArrayList<Field>();
			// truc à la con quand jsonlib jsonifie du xml, s'il n'y a q'une
			// balise fille, c'est un jsonobject, sinon, c'est du jsonarray ...
			try {
				JSONArray dataBindings = json.getJSONObject(i).getJSONObject("output").getJSONArray("field");
				for (int j = 0; j < dataBindings.size(); j++) {
					lof.add(getDatabinding(dataBindings.getJSONObject(j)));
				}
			} catch (JSONException e) {
				lof.add(getDatabinding(json.getJSONObject(i).getJSONObject("output").getJSONObject("field")));
			}
			targetArtifact.setDataBinding(lof);
			uow.setTargetArtifact(targetArtifact);

			final Map<String, TransfertObject> dataObjects = new HashMap<String, TransfertObject>();
			for (int j = 0; j < json.getJSONObject(i).getJSONArray("dataObjects").size(); j++) {
				final JSONObject dataObject = json.getJSONObject(i).getJSONArray("dataObjects").getJSONObject(j);
				TransfertObject transObj = new TransfertObject();
				transObj.setCollection(dataObject.getBoolean("@isCollection"));
				transObj.setName(dataObject.getString("@name"));
				transObj.setTransObjs(Class.forName(dataObject.getString("@className")).newInstance());
				dataObjects.put(dataObject.getString("@name"), transObj);
			}
			uow.setDataObjects(dataObjects);
			units.put(uow.getSourceArtifact().getAction(), uow);
		}
		return units;
	}

	/**
	 * @param dataBinding
	 * @return
	 */
	private static Field getDatabinding(JSONObject dataBinding) {
		final Field f = new Field();
		f.setSourceName(dataBinding.getString("@sourceName"));
		f.setTargetName(dataBinding.getString("@targetName"));
		f.setSource(dataBinding.getString("@source"));
		f.setSourceType(FieldTypes.valueOf(dataBinding.getString("@sourceType").toUpperCase()));
		f.setTarget(dataBinding.getString("@target"));
		f.setTargetType(FieldTypes.valueOf(dataBinding.getString("@targetType").toUpperCase()));
		return f;
	}
}
