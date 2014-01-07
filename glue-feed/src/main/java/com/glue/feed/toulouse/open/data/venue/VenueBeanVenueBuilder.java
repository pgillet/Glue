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

		StringBuilder address = new StringBuilder();

		// Is there a number?
		String number = StringUtils.defaultString(bean.getNumero());
		if (!"".equals(number)) {
			number = number.replaceAll("^0+", "");
			address.append(number).append(" ");
		}
		address.append(StringUtils.defaultString(bean.getLibOff())).append(" ")
				.append(StringUtils.defaultString(bean.getIdSecteurPostal())).append(" ")
				.append(StringUtils.defaultString(bean.getEqVille())).append(" ");
		venue.setAddress(address.toString().trim());
		venue.setCity(bean.getEqVille().trim());
		venue.setLatitude(Double.parseDouble(bean.getYWgs84()));
		venue.setLongitude(Double.parseDouble(bean.getXWgs84()));
		return venue;
	}
}
