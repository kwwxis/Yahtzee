package com.matthew0x40.yahtzee;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Various utility functions
 */
public class Util {
	
	/**
	 * Returns 'obj' or 'otherwise' if obj is null
	 */
	public static <T> T ifNull(T obj, T otherwise) {
		if (obj == null) {
			return otherwise;
		}
		return obj;
	}
	
	/** get the <int> at <key>, if exists, otherwise return 0 */
	public static <K> int getOrZero(Map<K,Integer> map, K key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return 0;
		}
	}
	
	/** add <amount> to the value at <key>, if exists, otherwise set <key> to <amount> */
	public static <K> void addOrSet(Map<K,Integer> map, K key, int amount) {
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + amount);
		} else {
			map.put(key, amount);
		}
	}
	
	/** increment the value at <key> by 1, if exists, otherwise set to 1 */
	public static <K> void incrementOrOne(Map<K,Integer> map, K key) {
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + 1);
		} else {
			map.put(key, 1);
		}
	}

	/** increment the value at <key> by 1, if exists, otherwise set to 0 */
	public static <K> void incrementOrZero(Map<K,Integer> map, K key) {
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + 1);
		} else {
			map.put(key, 0);
		}
	}
	
	/**
	 * Converts a int array to a Integer ArrayList
	 * @param arr
	 * @return
	 */
	public static List<Integer> asList(int[] arr) {
		List<Integer> list = new ArrayList<>(arr.length);
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}

	/**
	 * Converts a double array to a Double ArrayList
	 * @param arr
	 * @return
	 */
	public static List<Double> asList(double[] arr) {
		List<Double> list = new ArrayList<>(arr.length);
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}

	/**
	 * Converts a float array to a Float ArrayList
	 * @param arr
	 * @return
	 */
	public static List<Float> asList(float[] arr) {
		List<Float> list = new ArrayList<>(arr.length);
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}

	/**
	 * Converts a boolean array to a Boolean ArrayList
	 * @param arr
	 * @return
	 */
	public static List<Boolean> asList(boolean[] arr) {
		List<Boolean> list = new ArrayList<>(arr.length);
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}
	
	/**
	 * Converts a Integer ArrayList to a int array
	 * @param list
	 * @return
	 */
	public static int[] asIntegerArray(List<Integer> list) {
		int[] arr = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	/**
	 * Converts a Double ArrayList to a double array
	 * @param list
	 * @return
	 */
	public static double[] asDoubleArray(List<Double> list) {
		double[] arr = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	/**
	 * Converts a Float ArrayList to a float array
	 * @param list
	 * @return
	 */
	public static float[] asFloatArray(List<Float> list) {
		float[] arr = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	/**
	 * Converts a Boolean ArrayList to a boolean array
	 * @param list
	 * @return
	 */
	public static boolean[] asBooleanArray(List<Boolean> list) {
		boolean[] arr = new boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	/**
	 * Merges two lists, keeps duplicates
	 * @param a
	 * @param b
	 * @return merged list
	 */
	public static <T> List<T> list_merge(List<T> a, List<T> b) {
		List<T> merged = new ArrayList<>(a);
		merged.addAll(b);
		return merged;
	}
	
	/**
	 * Merges two lists, discards duplicates
	 * @param a
	 * @param b
	 * @return merged list
	 */
	public static <T> List<T> list_union(List<T> a, List<T> b) {
		Set<T> union = new HashSet<>(a);
		union.addAll(b);
		return new ArrayList<T>(union);
	}
	
	/**
	 * Intersects two lists
	 * @param a
	 * @param b
	 * @return the intersection of a and b
	 */
	public static <T> List<T> list_intersect(List<T> a, List<T> b) {
		List<T> intersection = new ArrayList<>(a);
		intersection.retainAll(b);
		return new ArrayList<T>(intersection);
	}

	/**
	 * Computes the difference of lists a and b
	 * @param a
	 * @param b
	 * @return a - b
	 */
	public static <T> List<T> list_diff(List<T> a, List<T> b) {
		List<T> diff = new ArrayList<>(a);
		diff.removeAll(b);
		return new ArrayList<T>(diff);
	}
	
}
