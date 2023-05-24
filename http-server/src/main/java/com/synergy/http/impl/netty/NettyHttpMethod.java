package com.synergy.http.impl.netty;

import java.lang.reflect.Method;

/** A data class to hold all information needed to invoke the method that's mapped to a path. */
record NettyHttpMethod(
    String httpMethod, Object service, Method methodToCall, boolean needSession) {}
