package com.glue.api.operations;

import com.glue.api.model.GlueException;
import com.glue.api.model.Stream;

public interface StreamOperations {

	Stream createStream(String title) throws GlueException;

}
