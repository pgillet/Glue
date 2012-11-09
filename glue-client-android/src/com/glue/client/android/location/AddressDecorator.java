package com.glue.client.android.location;

import java.util.Locale;

import android.location.Address;
import android.os.Bundle;
import android.os.Parcel;

public class AddressDecorator extends Address {

	private Address delegate;

	private AddressDecorator(Locale locale) {
		super(locale);
	}

	public AddressDecorator(Address address) {
		super(address.getLocale());
		delegate = address;
	}

	/**
	 * 
	 * @see android.location.Address#clearLatitude()
	 */
	public void clearLatitude() {
		delegate.clearLatitude();
	}

	/**
	 * 
	 * @see android.location.Address#clearLongitude()
	 */
	public void clearLongitude() {
		delegate.clearLongitude();
	}

	/**
	 * @return
	 * @see android.location.Address#describeContents()
	 */
	public int describeContents() {
		return delegate.describeContents();
	}

	/**
	 * @param o
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	/**
	 * @param index
	 * @return
	 * @see android.location.Address#getAddressLine(int)
	 */
	public String getAddressLine(int index) {
		return delegate.getAddressLine(index);
	}

	/**
	 * @return
	 * @see android.location.Address#getAdminArea()
	 */
	public String getAdminArea() {
		return delegate.getAdminArea();
	}

	/**
	 * @return
	 * @see android.location.Address#getCountryCode()
	 */
	public String getCountryCode() {
		return delegate.getCountryCode();
	}

	/**
	 * @return
	 * @see android.location.Address#getCountryName()
	 */
	public String getCountryName() {
		return delegate.getCountryName();
	}

	/**
	 * @return
	 * @see android.location.Address#getExtras()
	 */
	public Bundle getExtras() {
		return delegate.getExtras();
	}

	/**
	 * @return
	 * @see android.location.Address#getFeatureName()
	 */
	public String getFeatureName() {
		return delegate.getFeatureName();
	}

	/**
	 * @return
	 * @see android.location.Address#getLatitude()
	 */
	public double getLatitude() {
		return delegate.getLatitude();
	}

	/**
	 * @return
	 * @see android.location.Address#getLocale()
	 */
	public Locale getLocale() {
		return delegate.getLocale();
	}

	/**
	 * @return
	 * @see android.location.Address#getLocality()
	 */
	public String getLocality() {
		return delegate.getLocality();
	}

	/**
	 * @return
	 * @see android.location.Address#getLongitude()
	 */
	public double getLongitude() {
		return delegate.getLongitude();
	}

	/**
	 * @return
	 * @see android.location.Address#getMaxAddressLineIndex()
	 */
	public int getMaxAddressLineIndex() {
		return delegate.getMaxAddressLineIndex();
	}

	/**
	 * @return
	 * @see android.location.Address#getPhone()
	 */
	public String getPhone() {
		return delegate.getPhone();
	}

	/**
	 * @return
	 * @see android.location.Address#getPostalCode()
	 */
	public String getPostalCode() {
		return delegate.getPostalCode();
	}

	/**
	 * @return
	 * @see android.location.Address#getPremises()
	 */
	public String getPremises() {
		return delegate.getPremises();
	}

	/**
	 * @return
	 * @see android.location.Address#getSubAdminArea()
	 */
	public String getSubAdminArea() {
		return delegate.getSubAdminArea();
	}

	/**
	 * @return
	 * @see android.location.Address#getSubLocality()
	 */
	public String getSubLocality() {
		return delegate.getSubLocality();
	}

	/**
	 * @return
	 * @see android.location.Address#getSubThoroughfare()
	 */
	public String getSubThoroughfare() {
		return delegate.getSubThoroughfare();
	}

	/**
	 * @return
	 * @see android.location.Address#getThoroughfare()
	 */
	public String getThoroughfare() {
		return delegate.getThoroughfare();
	}

	/**
	 * @return
	 * @see android.location.Address#getUrl()
	 */
	public String getUrl() {
		return delegate.getUrl();
	}

	/**
	 * @return
	 * @see android.location.Address#hasLatitude()
	 */
	public boolean hasLatitude() {
		return delegate.hasLatitude();
	}

	/**
	 * @return
	 * @see android.location.Address#hasLongitude()
	 */
	public boolean hasLongitude() {
		return delegate.hasLongitude();
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return delegate.hashCode();
	}

	/**
	 * @param index
	 * @param line
	 * @see android.location.Address#setAddressLine(int, java.lang.String)
	 */
	public void setAddressLine(int index, String line) {
		delegate.setAddressLine(index, line);
	}

	/**
	 * @param adminArea
	 * @see android.location.Address#setAdminArea(java.lang.String)
	 */
	public void setAdminArea(String adminArea) {
		delegate.setAdminArea(adminArea);
	}

	/**
	 * @param countryCode
	 * @see android.location.Address#setCountryCode(java.lang.String)
	 */
	public void setCountryCode(String countryCode) {
		delegate.setCountryCode(countryCode);
	}

	/**
	 * @param countryName
	 * @see android.location.Address#setCountryName(java.lang.String)
	 */
	public void setCountryName(String countryName) {
		delegate.setCountryName(countryName);
	}

	/**
	 * @param extras
	 * @see android.location.Address#setExtras(android.os.Bundle)
	 */
	public void setExtras(Bundle extras) {
		delegate.setExtras(extras);
	}

	/**
	 * @param featureName
	 * @see android.location.Address#setFeatureName(java.lang.String)
	 */
	public void setFeatureName(String featureName) {
		delegate.setFeatureName(featureName);
	}

	/**
	 * @param latitude
	 * @see android.location.Address#setLatitude(double)
	 */
	public void setLatitude(double latitude) {
		delegate.setLatitude(latitude);
	}

	/**
	 * @param locality
	 * @see android.location.Address#setLocality(java.lang.String)
	 */
	public void setLocality(String locality) {
		delegate.setLocality(locality);
	}

	/**
	 * @param longitude
	 * @see android.location.Address#setLongitude(double)
	 */
	public void setLongitude(double longitude) {
		delegate.setLongitude(longitude);
	}

	/**
	 * @param phone
	 * @see android.location.Address#setPhone(java.lang.String)
	 */
	public void setPhone(String phone) {
		delegate.setPhone(phone);
	}

	/**
	 * @param postalCode
	 * @see android.location.Address#setPostalCode(java.lang.String)
	 */
	public void setPostalCode(String postalCode) {
		delegate.setPostalCode(postalCode);
	}

	/**
	 * @param premises
	 * @see android.location.Address#setPremises(java.lang.String)
	 */
	public void setPremises(String premises) {
		delegate.setPremises(premises);
	}

	/**
	 * @param subAdminArea
	 * @see android.location.Address#setSubAdminArea(java.lang.String)
	 */
	public void setSubAdminArea(String subAdminArea) {
		delegate.setSubAdminArea(subAdminArea);
	}

	/**
	 * @param sublocality
	 * @see android.location.Address#setSubLocality(java.lang.String)
	 */
	public void setSubLocality(String sublocality) {
		delegate.setSubLocality(sublocality);
	}

	/**
	 * @param subthoroughfare
	 * @see android.location.Address#setSubThoroughfare(java.lang.String)
	 */
	public void setSubThoroughfare(String subthoroughfare) {
		delegate.setSubThoroughfare(subthoroughfare);
	}

	/**
	 * @param thoroughfare
	 * @see android.location.Address#setThoroughfare(java.lang.String)
	 */
	public void setThoroughfare(String thoroughfare) {
		delegate.setThoroughfare(thoroughfare);
	}

	/**
	 * @param Url
	 * @see android.location.Address#setUrl(java.lang.String)
	 */
	public void setUrl(String Url) {
		delegate.setUrl(Url);
	}

	/**
	 * @return
	 * @see android.location.Address#toString()
	 */
	public String toString() {
		return String.format(
				"%s, %s, %s",
				delegate.getMaxAddressLineIndex() > 0 ? delegate
						.getAddressLine(0) : "", delegate.getLocality(),
				delegate.getCountryName());
	}

	/**
	 * @param parcel
	 * @param flags
	 * @see android.location.Address#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel parcel, int flags) {
		delegate.writeToParcel(parcel, flags);
	}

}
