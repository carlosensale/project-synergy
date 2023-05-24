package com.synergy.api.database;

/** A simple data class to hold database credentials */
public record Credentials(String host, int port, String user, String password, String database) {}
