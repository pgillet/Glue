package com.glue.feed.opennlp.generator;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import com.glue.feed.opennlp.Constants;

public class DateModelGenerator {

    private static Calendar calendar = Calendar.getInstance();
    private static Writer writer = null;

    // "C:\\Users\\Greg\\Downloads\\fr-ner-date.train"),

    /**
     * @param args
     */
    public static void main(String[] args) {

	DateModelGenerator generator = new DateModelGenerator();
	generator.launch();

    }

    public void launch() {

	try {
	    writer = new BufferedWriter(
		    new OutputStreamWriter(
			    new FileOutputStream(new File(this.getClass()
				    .getResource(Constants.DATE_TRAINING_FILE)
				    .toURI())),

			    "UTF-8"));

	    // // le samedi 17 avril à 18H15
	    generator1(100, 1, false, false, " ", null);
	    generator1(100, 1, false, false, " a ", null);

	    // jeudi 27 février à partir de 19h
	    generator1(100, 1, false, false, " a partir de ", null);

	    generator1(100, 1, true, false, " ", null);
	    generator1(100, 1, true, false, " a ", null);

	    // et=" "
	    generator1(100, 2, false, false, " ", " ");
	    generator1(100, 2, false, false, " a ", " ");
	    generator1(100, 3, false, false, " ", " ");
	    generator1(100, 3, false, false, " a ", " ");

	    // et = "et"
	    generator1(100, 2, false, false, " ", " et ");
	    generator1(100, 2, false, false, " a ", " et ");
	    generator1(100, 3, false, false, " ", " et ");
	    generator1(100, 3, false, false, " a ", " et ");

	    generator1(100, 1, false, true, " ", null);
	    generator1(100, 1, false, true, " a ", null);
	    generator1(100, 1, true, true, " ", null);
	    generator1(100, 1, true, true, " a ", null);

	    // et=" "
	    generator1(100, 2, false, true, " ", " ");
	    generator1(100, 2, false, true, " a ", " ");
	    generator1(100, 3, false, true, " ", " ");
	    generator1(100, 3, false, true, " a ", " ");

	    // et = "et"
	    generator1(100, 2, false, true, " ", " et ");
	    generator1(100, 2, false, true, " a ", " et ");
	    generator1(100, 3, false, true, " ", " et ");
	    generator1(100, 3, false, true, " a ", " et ");

	    // du samedi 17 au dimanche 19 18H15
	    generator2(100, 1, 0, " ", true, true);
	    // du samedi 17 au dimanche 19 mars à 18H15
	    generator2(100, 1, 0, " a ", true, true);
	    // du samedi 17 mars au dimanche 19 mars 18H15
	    generator2(100, 2, 0, " ", true, true);
	    // du samedi 17 mars au dimanche 19 mars a 18H15
	    generator2(100, 2, 0, " a ", true, true);
	    // du samedi 17 mars au dimanche 19 mars 2014 a 18H15
	    generator2(100, 2, 1, " a ", true, true);
	    // du samedi 17 décembre 2013 dimanche 19 mars 2013 a 18H15
	    generator2(100, 2, 2, " a ", true, true);
	    // du 17 mars au 19 mars 2014 a 18H15
	    generator2(200, 2, 1, " a ", false, true);
	    // du 17 mars au 19 mars 2014
	    generator2(200, 2, 1, "", false, false);
	    // du 17 décembre 2013 au 19 mars 2014 a 18H15
	    generator2(200, 2, 2, " a ", false, true);
	    // du 17 décembre 2013 au 19 mars 2014
	    generator2(200, 2, 2, "", false, false);

	    // le samedi 17 avril 2014 à 18H15

	    generator4(100, 1, false, " ");
	    generator4(100, 1, false, " a ");
	    generator4(100, 1, true, " ");
	    generator4(100, 1, true, " a ");
	    generator4(100, 2, false, " ");
	    generator4(100, 2, false, " a ");
	    generator4(100, 2, true, " ");
	    generator4(100, 2, true, " a ");

	    // samedi 22 mars 2014 à 23 :00
	    generator5(100, false, " ");
	    generator5(100, false, " a ");

	    // Mardi 4 Février – 20h – 8 euros
	    generator6(200, false);

	    createModel();

	} catch (IOException ex) {
	    // report
	} catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		if (writer != null)
		    writer.close();
	    } catch (Exception ex) {
	    }
	}

    }

    public void createModel() throws IOException, URISyntaxException {

	ObjectStream<String> lineStream = new PlainTextByLineStream(
		new FileInputStream(new File(this.getClass()
			.getResource(Constants.DATE_TRAINING_FILE).toURI())),
		"UTF-8");
	ObjectStream<NameSample> sampleStream = new NameSampleDataStream(
		lineStream);

	TokenNameFinderModel model = NameFinderME.train("fr", "date",
		sampleStream, Collections.<String, Object> emptyMap(), 100, 5);

	OutputStream modelOut = null;
	File modelFile = new File(this.getClass()
		.getResource(Constants.DATE_MODEL_FILE).toURI());
	try {
	    modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
	    model.serialize(modelOut);
	} finally {
	    if (modelOut != null)
		modelOut.close();
	}

    }

    private static void generator1(int number, int days, boolean withLe,
	    boolean withYear, String a, String et) throws IOException {

	int dayOfWeek, day, month, year, minutes, hours;

	StringBuilder builder = new StringBuilder();

	// samedi 5 et dimanche 4 septembre a 07H54
	for (int i = 0; i < number; i++) {
	    builder = new StringBuilder();

	    month = getRandomNumber(1, 12);
	    minutes = getRandomNumber(0, 59);
	    hours = getRandomNumber(0, 23);

	    dayOfWeek = getRandomNumber(1, 7);
	    day = getRandomNumber(1, 31);
	    builder.append(withLe ? "le " : "").append(getDayName(dayOfWeek))
		    .append(" <START:day> ").append(day).append(" <END>");

	    for (int j = 1; j < days - 1; j++) {
		dayOfWeek = getRandomNumber(1, 7);
		day = getRandomNumber(1, 31);
		builder.append(" ").append(getDayName(dayOfWeek))
			.append(" <START:day> ").append(day).append(" <END>");
	    }

	    if (days > 1) {
		dayOfWeek = getRandomNumber(1, 7);
		day = getRandomNumber(1, 31);
		builder.append(et).append(getDayName(dayOfWeek))
			.append(" <START:day> ").append(day).append(" <END>");
	    }

	    builder.append(" <START:month> ").append(getMonthName(month));

	    if (withYear) {
		year = getRandomNumber(2013, 2030);
		builder.append(" <END> <START:year> ").append(year);
	    }

	    builder.append(" <END>").append(a).append("<START:hour> ")
		    .append((hours < 10 ? "0" + hours : hours)).append("h")
		    .append((minutes < 10 ? "0" + minutes : minutes))
		    .append(" <END>\n");
	    writer.write(builder.toString());
	}
    }

    private static void generator2(int number, int months, int years, String a,
	    boolean dow, boolean withTime) throws IOException {

	int dayOfWeek, day, month, minutes, year, hours;

	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < number; i++) {
	    builder = new StringBuilder();

	    minutes = getRandomNumber(0, 59);
	    hours = getRandomNumber(0, 23);

	    dayOfWeek = getRandomNumber(1, 7);
	    day = getRandomNumber(1, 31);
	    builder.append("du ");

	    if (dow) {
		builder.append(getDayName(dayOfWeek)).append(" ");
	    }
	    builder.append("<START:day_from> ").append(day).append(" <END>");

	    if (months == 2) {
		month = getRandomNumber(1, 12);
		builder.append(" <START:month> ").append(getMonthName(month))
			.append(" <END>");
		if (years == 2) {
		    year = getRandomNumber(2013, 2050);
		    builder.append(" <START:year> ").append(year)
			    .append(" <END>");
		}
	    }

	    builder.append(" au ");

	    month = getRandomNumber(1, 12);
	    dayOfWeek = getRandomNumber(1, 7);
	    day = getRandomNumber(1, 31);

	    if (dow) {
		builder.append(getDayName(dayOfWeek)).append(" ");
	    }

	    builder.append("<START:day> ").append(day).append(" <END> ");

	    builder.append("<START:month> ").append(getMonthName(month))
		    .append(" <END>");

	    if (years > 0) {
		year = getRandomNumber(2013, 2050);
		builder.append(" <START:year> ").append(year).append(" <END>");
	    }

	    if (withTime) {

		builder.append(a).append("<START:hour> ")
			.append((hours < 10 ? "0" + hours : hours)).append("h")
			.append((minutes < 10 ? "0" + minutes : minutes))
			.append(" <END>");

	    }
	    writer.write(builder.toString() + "\n");
	}
    }

    private static void generate3(int number, int days) throws IOException {

	int dayOfWeek;

	StringBuilder builder = new StringBuilder();

	// du jeudi <START day>6<END> au dimanche <START day>9<END> <START
	// month>mars<END> à 10h30 et 16h45
	for (int i = 0; i < number; i++) {
	    builder = new StringBuilder();

	    dayOfWeek = getRandomNumber(1, 7);
	    builder.append("les <START:day_mult> ")
		    .append(getDayName(dayOfWeek)).append(" <END>");

	    for (int j = 1; j < days - 1; j++) {
		dayOfWeek = getRandomNumber(1, 7);
		builder.append(" <START:day_mult> ")
			.append(getDayName(dayOfWeek)).append(" <END>");
	    }
	    dayOfWeek = getRandomNumber(1, 7);
	    builder.append(" et <START:day_mult> ")
		    .append(getDayName(dayOfWeek)).append(" <END>\n");
	    writer.write(builder.toString());
	}
    }

    // dimanche 29 septembre 2013 à 15h00 jeudi 3 octobre 2013 à 20h00
    private static void generator4(int number, int days, boolean withLe,
	    String a) throws IOException {

	int dayOfWeek, day, month, year, minutes, hours;

	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < number; i++) {

	    builder = new StringBuilder();

	    for (int j = 0; j < days; j++) {

		month = getRandomNumber(1, 12);
		year = getRandomNumber(2013, 2050);
		minutes = getRandomNumber(0, 59);
		hours = getRandomNumber(0, 23);

		dayOfWeek = getRandomNumber(1, 7);
		day = getRandomNumber(1, 31);
		builder.append(withLe ? "le " : "")
			.append(getDayName(dayOfWeek)).append(" <START:day> ")
			.append(day).append(" <END> ");

		builder.append("<START:month> ").append(getMonthName(month))
			.append(" <END> <START:year> ").append(year)
			.append(" <END>").append(a).append("<START:hour> ")
			.append((hours < 10 ? "0" + hours : hours)).append("h")
			.append((minutes < 10 ? "0" + minutes : minutes))
			.append(" <END> ");
	    }
	    writer.write(builder.toString() + "\n");
	}
    }

    // samedi 22 mars 2014 a 23 :00
    private static void generator5(int number, boolean withLe, String a)
	    throws IOException {

	int dayOfWeek, day, month, year, minutes, hours;

	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < number; i++) {
	    builder = new StringBuilder();

	    month = getRandomNumber(1, 12);
	    year = getRandomNumber(2013, 2030);
	    minutes = getRandomNumber(0, 59);
	    hours = getRandomNumber(0, 23);

	    dayOfWeek = getRandomNumber(1, 7);
	    day = getRandomNumber(1, 31);
	    builder.append(withLe ? "le " : "").append(getDayName(dayOfWeek))
		    .append(" <START:day> ").append(day).append(" <END>");

	    builder.append(" <START:month> ").append(getMonthName(month))
		    .append(" <END> <START:year> ").append(year)
		    .append(" <END>").append(a).append("<START:hour> ")
		    .append((hours < 10 ? "0" + hours : hours)).append(" :")
		    .append((minutes < 10 ? "0" + minutes : minutes))
		    .append(" <END>\n");
	    writer.write(builder.toString());
	}
    }

    // Mardi 4 Février – 20h – 8 euros
    private static void generator6(int number, boolean withLe)
	    throws IOException {

	int dayOfWeek, day, month, minutes, hours;

	StringBuilder builder = new StringBuilder();

	for (int i = 0; i < number; i++) {
	    builder = new StringBuilder();

	    month = getRandomNumber(1, 12);
	    minutes = getRandomNumber(0, 59);
	    hours = getRandomNumber(0, 23);

	    dayOfWeek = getRandomNumber(1, 7);
	    day = getRandomNumber(1, 31);
	    builder.append(withLe ? "le " : "").append(getDayName(dayOfWeek))
		    .append(" <START:day> ").append(day).append(" <END>");

	    builder.append(" <START:month> ").append(getMonthName(month));

	    builder.append(" <END> - <START:hour> ")
		    .append((hours < 10 ? "0" + hours : hours)).append("h")
		    .append((minutes < 10 ? "0" + minutes : minutes))
		    .append(" <END>").append(" - ").append(getPrice())
		    .append(" \n");
	    writer.write(builder.toString());
	}
    }

    private static String getPrice() {
	int priceStrategy = getRandomNumber(0, 3);
	return priceStrategy == 0 ? generatePrice1()
		: (priceStrategy == 1 ? generatePrice2()
			: (priceStrategy == 2 ? generatePrice3()
				: generatePrice4()));
    }

    private static String generatePrice1() {
	return "gratuit";
    }

    private static String generatePrice2() {
	return getRandomNumber(1, 25) + " euros";
    }

    private static String generatePrice3() {
	return getRandomNumber(1, 15) + " /" + getRandomNumber(10, 20)
		+ " euros";
    }

    private static String generatePrice4() {
	return getRandomNumber(1, 15) + " /" + getRandomNumber(10, 20) + " /"
		+ getRandomNumber(15, 25) + " euros";
    }

    public static String getMonthName(int month) {
	// Calendar numbers months from 0
	calendar.set(Calendar.MONTH, month - 1);
	return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG,
		Locale.FRENCH);
    }

    public static String getDayName(int day) {
	calendar.set(Calendar.DAY_OF_WEEK, day - 1);
	return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
		Locale.FRENCH);
    }

    public static int getRandomNumber(int min, int max) {
	Random rnd = new Random();
	return rnd.nextInt((max + 1) - min) + min;
    }
}
