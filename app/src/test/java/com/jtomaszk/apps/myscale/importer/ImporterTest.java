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

    public static final ArrayList<String> STRING_DATES = Lists.newArrayList("2013-11-02", "2013-11-30", "2014-01-08", "2014-01-18", "2014-08-01", "2015-01-16", "2015-01-25", "2015-02-08", "2015-02-12", "2015-02-13", "2015-02-24", "2015-03-25");
    public static final ArrayList<String> STRING_WEIGHTS = Lists.newArrayList("70.9", "71.5", "74.8", "73.8", "76.0", "71.4", "72.8", "73.5", "74.3", "73.4", "72.8", "73.8");

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