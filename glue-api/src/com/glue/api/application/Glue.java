package com.glue.api.application;

import java.io.Serializable;

import com.glue.api.operations.StreamOperations;
import com.glue.api.operations.UserOperations;

public interface Glue extends Serializable, StreamOperations, UserOperations {

}
