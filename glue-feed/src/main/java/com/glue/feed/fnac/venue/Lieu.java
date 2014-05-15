package com.glue.feed.fnac.venue;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Sample below.
 */

// <lieu geoAccuracy="6" geoStatus="2" geoLatDeg="49.032555"
// geoLongDeg="2.001526" typeLieu="STANDARD" petitLieu="false" alertemail="true"
// codlieu="FRCOU">
// <nomlieu>LE FOYER RURAL</nomlieu>
// <ad1lieu>Rue des Ecoles</ad1lieu>
// <ad2lieu></ad2lieu>
// <ad3lieu></ad3lieu>
// <ad4lieu></ad4lieu>
// <cptlieu>95800</cptlieu>
// <villieu>COURDIMANCHE</villieu>
// <paylieu>FR</paylieu>
// <reglieu>RPA</reglieu>
// <desclieu></desclieu>
// <infolieu></infolieu>
// <salle jautot="150" codsal="FRCOU">
// <nomsal>LE FOYER RURAL</nomsal>
// <useAdrLieu>true</useAdrLieu>
// <configsalle typplan="--" nbzsal="1" typsal="NN" codcsa="FRCOL"/>
// </salle>
// </lieu>

@XmlAccessorType(XmlAccessType.FIELD)
public class Lieu {

    @XmlAttribute(name = "geoLatDeg")
    double latitude;

    @XmlAttribute(name = "geoLongDeg")
    double longitude;

    String nomlieu;

    String ad1lieu;

    String ad2lieu;

    String ad3lieu;

    String ad4lieu;

    String cptlieu;

    String desclieu;

    String villieu;

    @Override
    public String toString() {
	return "Lieu [latitude=" + latitude + ", longitude=" + longitude
		+ ", nomlieu=" + nomlieu + ", ad1lieu=" + ad1lieu
		+ ", ad2lieu=" + ad2lieu + ", ad3lieu=" + ad3lieu
		+ ", ad4lieu=" + ad4lieu + ", cptlieu=" + cptlieu + "]";
    }
}
