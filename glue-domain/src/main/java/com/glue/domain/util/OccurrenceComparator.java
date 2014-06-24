package com.glue.domain.util;

import java.io.Serializable;
import java.util.Comparator;

import com.glue.domain.Occurrence;

public class OccurrenceComparator implements Comparator<Occurrence>,
	Serializable {

    @Override
    public int compare(Occurrence o1, Occurrence o2) {
	int dateCmp = o1.getStartTime().compareTo(o2.getStartTime());
	return dateCmp;
    }

}
