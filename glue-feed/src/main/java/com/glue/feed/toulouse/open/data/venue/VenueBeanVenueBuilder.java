package com.glue.feed.toulouse.open.data.venue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.GlueObjectBuilder;
import com.glue.struct.IVenue;
import com.glue.struct.impl.Venue;

public class VenueBeanVenueBuilder implements GlueObjectBuilder<VenueBean, IVenue> {

	static final Logger LOG = LoggerFactory.getLogger(VenueBeanVenueBuilder.class);

	public VenueBeanVenueBuilder() {
	}

	@Override
	public IVenue build(VenueBean bean) throws Exception {

		IVenue venue = new Venue();
		venue.setName(bean.getEqNomEquipement().trim());

		// Construct venue address
		StringBuilder address = new StringBuilder().append(StringUtils.defaultString(bean.getNumero())).append(" ")
				.append(StringUtils.defaultString(bean.getLibOff())).append(" ")
				.append(StringUtils.defaultString(bean.getEqSecteur())).append(" ")
				.append(StringUtils.defaultString(bean.getEqVille())).append(" ");
		venue.setAddress(address.toString().trim());
		venue.setCity(bean.getEqVille().trim());
		return venue;
	}
}
