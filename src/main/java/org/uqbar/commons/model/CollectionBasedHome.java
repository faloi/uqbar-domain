package org.uqbar.commons.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

/**
 * Implementación de {@link Home} que mantiene los objetos en memoria, dentro 
 * de una collection.
 * 
 * Esta implementacion NO PERSISTE!
 * Es decir que, cada vez que se baja la VM (java), todos los datos se pierden.
 * 
 * @see Home
 * 
 * @author npasserini
 */
public abstract class CollectionBasedHome<T extends Entity> extends AbstractAutogeneratedIdHome<T> {
	private List<T> objects = new ArrayList<T>();

	// ********************************************************
	// ** Altas, bajas y modificaciones.
	// ********************************************************

	@Override
	protected void effectiveCreate(T object) {
		this.objects.add(object);
	}

	@Override
	public void update(T object) {
		this.objects.remove(object);
		this.objects.add(object);
	}

	@Override
	protected void effectiveDelete(T object) {
		this.objects.remove(object);
	}
	
	// ********************************************************
	// ** Búsquedas
	// ********************************************************
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> searchByExample(final T example) {
		return (List<T>) CollectionUtils.select(this.objects, this.getCriterio(example));
	}

	protected abstract Predicate getCriterio(T example);

	@Override
	public T searchById(int id) {
		for (T candidate : this.allInstances()) {
			if (candidate.getId().equals(id)) {
				return candidate;
			}
		}

		// TODO Mejorar el mensaje de error
		throw new RuntimeException("No se encontró el objeto con el id: " + id);
	}

	@Override
	public List<T> allInstances() {
		return this.objects;
	}

	// ********************************************************
	// ** Criterios de búsqueda
	// ********************************************************

	protected Predicate<T> getCriterioTodas() {
		return new Predicate<T>() {
			@Override
			public boolean evaluate(T arg) {
				return true;
			}
		};
	}

	protected Predicate<T> getCriterioPorId(final Integer id) {
		return new Predicate<T>() {
			@Override
			public boolean evaluate(T arg) {
				return arg.getId().equals(id);
			}
		};
	}

}