package com.jtomaszk.apps.myscale.importer;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.jtomaszk.apps.common.utils.DateUtil;
import com.jtomaszk.apps.myscale.entity.WeightEntry;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by jarema-user on 2016-01-20.
 */
public class ImporterTest {

    public static final ArrayList<String> STRING_DATES = Lists.newArrayList("2013-11-02", "2013-11-30", "2014-01-08", "2014-01-18", "2014-08-01", "2015-01-16", "2015-01-25", "2015-02-08", "2015-02-11", "2015-02-13", "2015-02-22", "2015-03-24");
    public static final ArrayList<String> STRING_DATES2 = Lists.newArrayList("2013-11-02", "2013-11-30", "2014-01-08", "2015-06-16", "2015-06-18", "2015-07-20", "2015-08-06", "2015-08-15", "2015-10-27", "2015-10-28", "2015-10-28", "2015-11-11", "2015-11-14");
    public static final ArrayList<String> STRING_WEIGHTS = Lists.newArrayList("70.9", "71.5", "74.8", "73.8", "76.0", "71.4", "72.8", "73.5", "74.3", "73.4", "72.8", "73.8");
    public static final ArrayList<String> STRING_WEIGHTS2 = Lists.newArrayList("70.9", "71.5", "74.8", "74.2", "74.7", "72.3", "74.6", "73.6", "72.6", "71.9", "72.4", "72.4", "72.5");

    private Importer importer = new Importer();

    @Test
    public void parseFile() throws Exception {
        //given
        File file = new File(this.getClass().getResource("input.csv").getFile());

        //when
        List<WeightEntry> list = importer.parseCsvData(file);

        //then
        Assert.assertEquals(12, list.size());

        List<String> dates = new ArrayList<>(Collections2.transform(list, new WeightEntryStringDateFunction()));
        assertThat(dates, CoreMatchers.<List<String>>equalTo(STRING_DATES));

        List<String> weights = new ArrayList<>(Collections2.transform(list, new WeightEntryStringWeightFunction()));
        assertThat(weights, CoreMatchers.<List<String>>equalTo(STRING_WEIGHTS));
    }

    @Test
    public void parseFileLibra2() throws Exception {
        //given
        File file = new File(this.getClass().getResource("libra2.csv").getFile());

        //when
        List<WeightEntry> list = importer.parseCsvData(file);

        //then
        Assert.assertEquals(13, list.size());

        List<String> dates = new ArrayList<>(Collections2.transform(list, new WeightEntryStringDateFunction()));
        assertThat(dates, CoreMatchers.<List<String>>equalTo(STRING_DATES2));

        List<String> weights = new ArrayList<>(Collections2.transform(list, new WeightEntryStringWeightFunction()));
        assertThat(weights, CoreMatchers.<List<String>>equalTo(STRING_WEIGHTS2));
    }

    private static class WeightEntryStringDateFunction implements Function<WeightEntry, String> {
        @Nullable
        @Override
        public String apply(WeightEntry input) {
            return DateUtil.millisecondsToString(input.getDateTimeMilliseconds(), "YYYY-MM-dd");
        }
    }

    private static class WeightEntryStringWeightFunction implements Function<WeightEntry, String> {
        @Nullable
        @Override
        public String apply(WeightEntry input) {
            return input.getWeight().toString();
        }
    }
}