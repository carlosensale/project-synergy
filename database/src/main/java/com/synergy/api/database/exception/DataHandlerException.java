package com.synergy.api.database.exception;

/**
 * An exception super class for creating new exceptions that will be thrown while working with
 * a database.
 */
public class DataHandlerException extends RuntimeException{

  /**
   * Instantiates a new Data handler exception only with a message.
   *
   * @param message the message that will be passed for more information
   */
public DataHandlerException(String message) {
    super("DataHandlerException occurred: "+message);
  }

  /**
   * Instantiates a new Data handler exception with a message and a throwable from another exception.
   *
   * @param message the message that will be passed for more information
   * @param cause the cause from an exception that happened before
   */
  public DataHandlerException(String message, Throwable cause) {
    super("DataHandlerException occurred: "+message, cause);
  }
}
