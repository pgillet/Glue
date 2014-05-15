package com.glue.feed.fnac.venue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Venue;
import com.glue.feed.GlueObjectBuilder;

public class FnacVenueBeanBuilder implements GlueObjectBuilder<Lieu, Venue> {

    static final Logger LOG = LoggerFactory
	    .getLogger(FnacVenueBeanBuilder.class);

    @Override
    public Venue build(Lieu bean) throws Exception {
	Venue venue = new Venue();
	venue.setName(bean.nomlieu);
	venue.setLatitude(bean.latitude);
	venue.setLongitude(bean.longitude);
	venue.setCity(bean.villieu);
	venue.setDescription(StringUtils.defaultString(bean.desclieu).trim());
	venue.setReference(true);

	StringBuilder address = new StringBuilder();
	address.append(StringUtils.defaultString(bean.ad1lieu)).append(" ")
		.append(StringUtils.defaultString(bean.ad2lieu)).append(" ")
		.append(StringUtils.defaultString(bean.ad3lieu)).append(" ")
		.append(StringUtils.defaultString(bean.ad4lieu)).append(" ")
		.append(StringUtils.defaultString(bean.cptlieu)).append(" ")
		.append(StringUtils.defaultString(bean.villieu));
	venue.setAddress(address.toString().trim());
	return venue;
    }
}
