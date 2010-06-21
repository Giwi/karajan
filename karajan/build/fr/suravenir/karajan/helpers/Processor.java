package fr.suravenir.karajan.helpers;

import java.lang.reflect.InvocationTargetException;

import fr.suravenir.karajan.model.Action;
import fr.suravenir.karajan.model.UnitOfWork;

public class Processor {

	public static Object processAction(final UnitOfWork unitOfWork, final Object target) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException, InvocationTargetException {

		final Action action = (Action) Class.forName(unitOfWork.getTargetArtifact().getAction()).newInstance();

		return action.getClass().getMethod("process", Object.class).invoke(action, target);
	}
}
