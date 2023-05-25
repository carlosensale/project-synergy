package com.synergy.api.database;

import com.synergy.api.database.exception.DataHandlerException;
import java.io.Closeable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The base interface for executing CRUD operations on a database.
 *
 * @param <T> a data class that will be handled and mapped in this class
 */
public interface DataHandler<T> extends Closeable {

  /**
   * Fetches an object of the type T from the database.
   *
   * @param filter the filter that will search for the right result
   * @return an optional that holds the result or will be empty if there is
   * no result for the given
   * @throws DataHandlerException or a Sub-class of it when an error occurred while fetching
   */
  Optional<T> fetch(Filter filter) throws DataHandlerException;

  /**
   * Fetches an instance of the type T from the database asynchronously.
   *
   * @param filter the filter that will search for the right result
   * @param result a consumer that will be used when the fetching is done
   * @throws DataHandlerException or a Sub-class of it when an error occurred while fetching
   */
  void fetchAsync(Filter filter, Consumer<Optional<T>> result) throws DataHandlerException;

  /**
   * Stores an instance of the type t to the database.
   *
   * @param t the instance that will be stored.
   * @return the stored instance, maybe different due to "serial" data types for example.
   * @throws DataHandlerException or a Sub-class of it when an error occurred while storing
   */
  T store(T t) throws DataHandlerException;

  /**
   * Stores an instance of the type t to the database asynchronously.
   *
   * @param t the instance that will be stored.
   * @param result a consumer that will be used when the storing is done
   * @throws DataHandlerException or a Sub-class of it when an error occurred while storing
   */
  void storeAsync(T t, Consumer<T> result) throws DataHandlerException;

  /**
   * Delete a database entry with a fitting filter.
   *
   * @param filter the filter that will search for the right result
   * @return the deleted entry
   * @throws DataHandlerException or a Sub-class of it when an error occurred while deleting
   */
  T delete(Filter filter) throws DataHandlerException;

  /**
   * Delete a database entry with a fitting filter asynchronously.
   *
   * @param filter the filter that will search for the right result
   * @param result a consumer that will be used when the deleting is done
   * @throws DataHandlerException or a Sub-class of it when an error occurred while deleting
   */
  void deleteAsync(Filter filter, Consumer<T> result) throws DataHandlerException;

  /**
   * Update a database entry with a given instance of the type t.
   *
   * @param t the instance that will be updated.
   * @return the updated instance, maybe different due to "serial" data types for example.
   * @throws DataHandlerException the data handler exception
   */
  T update(T t) throws DataHandlerException;

  /**
   * Update a database entry asynchronously with a given instance of the type t.
   *
   * @param t the instance that will be updated.
   * @param result a consumer that will be used when the updating is done
   * @throws DataHandlerException or a Sub-class of it when an error occurred while updating
   */
  void updateAsync(T t, Consumer<T> result) throws DataHandlerException;

  /**
   * Check if an entry with a given filter exists in the database.
   *
   * @param filter the filter that will search for the right result
   * @return if the entry exists
   * @throws DataHandlerException or a Sub-class of it when an error occurred while updating
   */
  boolean checkExist(Filter filter) throws DataHandlerException;

  /**
   * Check if an entry with a given filter exists in the database asynchronously.
   *
   * @param filter the filter that will search for the right result
   * @param result a consumer that will be used when the checking for existence is done
   */
  void checkExistAsync(Filter filter, Consumer<Boolean> result);

  /**
   * Fetch a collection of the type t from the database.
   *
   * @param filter the filter that will search for the right result
   * @return all found entries in a collection
   * @throws DataHandlerException or a Sub-class of it when an error occurred while fetching
   */
  Collection<T> fetchCollection(Filter filter) throws DataHandlerException;

  /**
   * Fetch a collection of the type t from the database asynchronously.
   *
   * @param filter the filter that will search for the right result
   * @param result a consumer that will be used when the collection fetching is done
   * @throws DataHandlerException or a Sub-class of it when an error occurred while fetching
   */
  void fetchCollectionAsync(Filter filter, Consumer<Collection<T>> result) throws DataHandlerException;
}
