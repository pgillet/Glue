package com.glue.feed.opennlp.test;

import java.io.IOException;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.opennlp.dates.DateAnalyzer;
import com.glue.feed.opennlp.dates.EventDate;

public class TestDateExtraction {

    static final Logger LOG = LoggerFactory.getLogger(TestDateExtraction.class);

    private static DateAnalyzer dateAnalyzer = DateAnalyzer.getInstance();

    /**
     * @param args
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void main(String[] args) throws InvalidFormatException,
	    IOException {

	analyze("mercredi 18 août à 23h15");

	analyze("mecredi 18 août 23h15");

	analyze("le jeudi 15 septembre 23h45");

	analyze("vendredi 17 juillet à 7h12");

	analyze("vendredi 17 et jeudi 25 mars à 7h12");

	analyze("vendredi 14 jeudi 17 mai à 7h12");

	analyze("samedi 17 jeudi 18 et dimanche 17 mai à 07h12");

	analyze("samedi 17 juin et jeudi 18 mars 0h17");

	analyze("samedi 1 dimanche 2 lundi 3 mardi 4 et mecredi 5 mars 10h30 et 0h17");

	analyze("du mercredi 18 au jeudi 27 août à 23h15");

	analyze("du samedi 15 mars au dimanche 27 avril à 23h15 et 11h47");

	analyze("à 17h et 11h47");

	analyze("le 23 mars à 15h");

	analyze("jeudi 03 avril à 19H30 vendredi 04 et samedi 05 avril à 20h30");

	analyze("lundi 3 mardi 4 et jeudi 6 mars à 12h45");

	analyze("les lundis et mardis");

	analyze("les lundi mardi et jeudi");

	analyze("le mardi 9 de 12h à 17h30");

	analyze("jeudi 24 avril à 19h30 vendredi 25 et samedi 26 avril à 20h30");

	analyze("le dimanche 29 septembre 2014 à 15h00");

	analyze("dimanche 29 septembre 2014 à 15h00 jeudi 3 octobre 2013 à 20h00");

	analyze("mardi 4  & mercredi 5 février à 21h30");

	analyze("Jeudi 20 février 2014    20 :30");

	analyze("vendredi 28 février 2014 à 20 :00");

	analyze("mercredi 5 février - 20h54 - 8 euros");

	analyze("mercredi 5 février - 20h54 - 12 /17 euros");

	analyze("jeudi 15 mars - 20h - gratuit");

	analyze("Dimanche 9 Février - 19h30 - 14 /17 /20 euros");

	analyze("samedi 22 février 2014  - 22h00");

	analyze("jeudi 24 avril à 19h30 vendredi 25 avril à 20h30 dimanche 5 août 2015 à 19H05");

	analyze("   Jeudi 20 février 2014    20 :30");

	analyze("jeudi 27 février à partir de 19h");

	analyze("jeudi 13 mars à à partir de 19h");

	analyze("- Du 18 février au 27 mars 2014");

	analyze("Conte clownesque en duo, une création 2012 en coproduction des Compagnies la Caravole et kOikadi. du jeudi 6 au dimanche 9 mars à 10h30 et 16h45. Une tanière sous une tempête de neige en plein été, un \"Loup\" devenu végétarien, un \"Rouge\" petit Chaperon et pot de colle sont les ingrédients de cette rencontre où les questions se posent : la peur aide-t-elle à grandir, le nem nourrit il son Loup, les médias sont-ils neutres, que fera \"Rouge\" à sa retraite, que vient faire Pinocchio dans cette histoire, comment devenir soi-même, est-ce la fin des contes et pourquoi toute cette neige un 02 juillet !");

    }

    private static void analyze(String str) {
	List<EventDate> dates = dateAnalyzer.analyze(str);
	for (EventDate e : dates) {
	    LOG.debug(e.toString() + "\n");
	}
    }

}
