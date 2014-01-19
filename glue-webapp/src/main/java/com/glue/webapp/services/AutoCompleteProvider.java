package com.glue.webapp.services;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.glue.struct.IStream;
import com.glue.webapp.search.SearchEngine;
import com.glue.webapp.search.SolrSearchServer;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
public class AutoCompleteProvider implements InjectableProvider<Context, Type>{

    public AutoCompleteProvider(){

    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

	@Override
	public Injectable<SearchEngine<IStream>> getInjectable(ComponentContext componentContext, Context context, Type type) {

		return new AbstractHttpContextInjectable<SearchEngine<IStream>>() {
			@Override
			public SearchEngine<IStream> getValue(HttpContext arg0) {
				return new SolrSearchServer();
			}
		};
	}
}
