package com.mmsone.szintfelmero;

import java.util.Comparator;
import java.util.List;

public class BubbleListSort<T> implements IListSorter<T> {

	private final Comparator<T> _comparator;

	public BubbleListSort(Comparator<T> _comparator) {
		super();
		this._comparator = _comparator;
	}

	public List<T> sort(List<T> list) {
		int size = list.size();

		for (int pass = 1; pass < size; pass++) {
			for (int left = 0; left < (size - pass); left++) {
				int right = left + 1;
				if (_comparator.compare(list.get(left), list.get(right)) > 0) {
					swap(list, left, right);
				}
			}
		}

		return list;
	}

	private void swap(List<T> list, int left, int right) {
		T temp = list.get(left);
		list.set(left, list.get(right));
		list.set(right, temp);
	}

}
