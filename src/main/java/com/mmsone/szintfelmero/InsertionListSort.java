package com.mmsone.szintfelmero;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class InsertionListSort<T> implements IListSorter<T> {

	private final Comparator<T> _comparator;

	public InsertionListSort(Comparator<T> _comparator) {
		super();
		this._comparator = _comparator;
	}

	public List<T> sort(List<T> list) {
		final List<T> result = new LinkedList<T>();

		ListIterator<T> it = list.listIterator();

		while (it.hasNext()) {
			T currentPerson = it.next();
			int slot = result.size();
			while (slot > 0) {
				if (_comparator.compare(currentPerson, result.get(slot - 1)) >= 0) {
					break;
				}
				--slot;
			}
			result.add(slot, currentPerson);
		}
		list.clear();
		for(T t : result){
			list.add(t);
		}
		return result;
	}

}
