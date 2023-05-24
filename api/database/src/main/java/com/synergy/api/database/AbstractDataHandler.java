package com.synergy.api.database;

import com.synergy.api.database.exception.DataHandlerException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDataHandler<T> implements DataHandler<T> {

  protected final Executor executor;

  @Override
  public void fetchAsync(Filter filter, Consumer<Optional<T>> result) throws DataHandlerException {
    executor.execute(() -> result.accept(fetch(filter)));
  }

  @Override
  public void storeAsync(T t, Consumer<T> result) throws DataHandlerException{
    executor.execute(() -> result.accept(store(t)));
  }

  @Override
  public void updateAsync(T t, Consumer<T> result) throws DataHandlerException{
    executor.execute(() -> result.accept(update(t)));
  }

  @Override
  public void deleteAsync(Filter filter, Consumer<T> result) throws DataHandlerException{
    executor.execute(() -> result.accept(delete(filter)));
  }

  @Override
  public void checkExistAsync(Filter filter, Consumer<Boolean> result) throws DataHandlerException{
    executor.execute(() -> result.accept(checkExist(filter)));
  }

  @Override
  public void fetchCollectionAsync(Filter filter, Consumer<Collection<T>> result) throws DataHandlerException{
    executor.execute(() -> result.accept(fetchCollection(filter)));
  }
}
