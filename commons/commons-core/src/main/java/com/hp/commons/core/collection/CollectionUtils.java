package com.hp.commons.core.collection;

import com.hp.commons.core.collection.aggregator.Aggregator;
import com.hp.commons.core.collection.aggregator.ListAggregator;
import com.hp.commons.core.collection.aggregator.MapAggregator;
import com.hp.commons.core.collection.aggregator.MaxAggregator;
import com.hp.commons.core.criteria.Criteria;
import com.hp.commons.core.handler.Handler;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: grunzwei
 * Date: 6/4/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 *
 * Utility class for handling collection related tasks.
 */
public class CollectionUtils {

    private CollectionUtils() {}

    /**
     * there's no point in being "type safe" in this method, as the returned value is not typed, and we would like to
     * support any type of iterable comparison.
     *
     * @param suspectedPrefix some iterable whose may be a prefix for container
     * @param container the iterable who may or may not start with suspectedPrefix
     * @return true iff suspectedPrefix is a prefix of container
     *
     */
    public static boolean isPrefix(Iterable suspectedPrefix, Iterable container) {

        final Iterator spi = suspectedPrefix.iterator();
        final Iterator ci = container.iterator();

        //the function consumes both iterators from left to right
        while (true) {

            //if we finished consuming the entire prefix - great!
            if (!spi.hasNext()) {
                return true;
            }

            //if prefix hasn't been consumed entirely, but the collection has - bad!
            if (!ci.hasNext()) {
                return false;
            }

            //if both still have not been completely consumed, but there's a difference - bad!
            if (areDifferent(spi.next(), ci.next())) {
                return false;
            }
        }
    }

    /**
     *
     * method that determines if all inputs are .equal().
     * the method handles null gracefully: if all elements are null, they are considered equal.
     *
     * the function assumes that equality is transitive, and doesn't all pair permutations.
     * it also assumes that no object .equals(null)
     *
     * @param elements to check if equal
     * @param <T> type of the elements
     * @return true if all elements are equal or all elements are null. base cases of empty and single element are
     * also considered "equal" in an empty fashion.
     */
    public static <T> boolean areEqual(T... elements) {

        //base cases, trivially "equal"
        if (elements.length < 2) {
           return true;
        }

        //TODO use aggregate instead of this loop
        T first = elements[0];
        for (int i = 1 ; i < elements.length ; i++) {
            T current = elements[i];

            //if both are null, then ok, but if only one is... bad.
            if (first == null) {
                if (current != null) {
                    return false;
                }
            }

            //if not null then safe to use equals method. assumes that it handles null correctly.
            else {
                if (!first.equals(current)) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     *
     * method that determines if any of the inputs is not .equal()
     * the method handles null gracefully: if all elements are null, they are considered equal.
     *
     * the function assumes that equality is transitive, and doesn't all pair permutations.
     * it also assumes that no object .equals(null)
     *
     * @param elements to check if different
     * @param <T> type of the elements
     * @return false if all elements are equal or all elements are null. base cases of empty and single element are
     * also considered "equal" in an empty fashion.
     */
    public static <T> boolean areDifferent(T... elements) {

        return !areEqual(elements);
    }


    /**
     *
     * @param i an iterator from which we want to retrieve the next element.
     * @return next element if iterator has more elements, null otherwise.
     *
     * TODO: typesafety Iterator<T> and return T
     */
    public static Object nextOrNull(Iterator i) {
        return i.hasNext() ? i.next() : null;
    }

    /**
     *
     * @param original an array we want to start with as prefix
     * @param additional an array we want to append as suffix
     * @return a new array consisting from original and afterwards additional
     *
     * TODO rename to arrayAppend
     */
    public static <T> T[] combineArrays(T[] original, T... additional) {

        final T[] ret = Arrays.copyOf(original, original.length + additional.length);

        for (int i = 0 ; i < additional.length ; i++) {

            ret[original.length + i] = additional[i];
        }

        return ret;
    }

    /**
     *
     * @param elements the collection we would like to aggregate
     * @param aggregator the aggregation strategy to use when aggregating the collection
     * @param <R> the resulting value of the aggregation
     * @param <T> the type of the aggregated collection
     * @return the result of aggregating elements with aggregator.
     * TODO: move to aggregator? make init,aggregate and finish protected?
     */
    public static <R, T> R aggregate(Collection<T> elements, Aggregator<R, T> aggregator) {

        aggregator.init(elements);

        for (T element : elements) {
            aggregator.aggregate(element);
        }

        return aggregator.finish();
    }

    /**
     *
     * @param elements the collection to iterate
     * @param handler the function to apply on each element of elements
     * @param <R> the result type of the application of the handler function on each of elements' elements
     * @param <T> the type of the elements collection and handler function input type
     * @return a list containing the results of applying handler on elements sequentially
     */
    public static <R, T> List<R> map(Collection<T> elements, Handler<R, T> handler) {

        return aggregate(elements, new ListAggregator<R,T>(handler));
    }

    /**
     *
     * @param elements the collection to iterate, each one will be a key in the resulting map
     * @param handler the function to apply on each element of elements in order to create a value
     * @param <R> the result type of the application of the handler function on each of elements' elements
     * @param <T> the type of the elements collection and handler function input type
     * @return a mapping from element -> handler(element)
     */

    public static <R, T> Map<T, R> mapToMap(Collection<T> elements, Handler<R, T> handler) {

        return aggregate(elements, new MapAggregator<T, R>(handler));
    }

    /**
     * sometimes when we want to compare objects according to something that is already comparable,
     * it's easier to write a handler that generates that comparable than to write a comparator.

     * has performance advantages over mapping and taking max, see MaxAggregator docs below for more details.
     *
     * @param elements on a transformation of which we wish to find the maximum
     * @param handler to transform each element into a comparable
     * @param <C> comparable type
     * @param <T> type of elements
     * @return element whose transformation is maximal among all others in elements input collection
     *
     * @see MaxAggregator
     */
    public static <C extends Comparable, T> Map.Entry<T,C> max(
            Collection<T> elements,
            Handler<C, T> handler) {

        return aggregate(elements, new MaxAggregator<C, T>(handler));
    }

    /**
     *
     * @param container the containing collection
     * @param contained the collection with a contained element
     * @return true if container contains any of the elements in contained.
     * this is the same as asking if the collections intersect.
     * the terminology is just because it's sometimes easier to think of it this way.
     */
    public static boolean containsAny(Collection container, Collection contained) {

        Collection copy = intersect(container, contained);

        return !copy.isEmpty();
    }

    /**
     *
     * @param container a collection
     * @param contained another collection
     * @return an intersection
     */
    public static Collection intersect(Collection container, Collection contained) {

        Collection copy = new ArrayList(container);
        copy.retainAll(contained);

        return copy;
    }

    /**
     *
     * @param items collection to filter according to criteria
     * @param criteria to use when filtering the items collection
     * @param <T> the type of the collection and input parameter of the criteria
     *
     * function returns nothing, works on the items collection in memory. iterator
     * of collection must support the remove method.
     */
    public static <T> void filter(Collection<T> items, Criteria<T> criteria) {

        final Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()){

            if (!criteria.isSuccessful(iterator.next())) {

                iterator.remove();
            }
        }
    }

    /**
     *
     * @param input map to reverse
     * @param <T> type of map and collection
     * @return reversed map with value sets switched with key sets.
     */
    public static <T> Map<T, HashSet<T>> reverseMap(Map<T, ? extends Collection<T>> input) {

        Map<T, HashSet<T>> ret = new HashMap<T, HashSet<T>>();

        for (Map.Entry<T, ? extends Collection<T>> entry : input.entrySet()) {

            final Collection<T> values = entry.getValue();
            final T key = entry.getKey();

            //foreach value in key->values mapping
            for (T value : values) {

                //get the reversed collection created when previously iterated keys
                // were associated with this value, value -> keys
                HashSet<T> newValues = ret.get(value);

                //if we've never processed a key that pointed to this value -
                // it's time to initialize the reversed collection from this value to keys that pointed to it
                if (newValues == null) {

                    newValues = new HashSet<T>();
                    ret.put(value, newValues);
                }

                //and obviously put the key in the reversed collection
                newValues.add(key);
            }
        }

        return ret;
    }

}


