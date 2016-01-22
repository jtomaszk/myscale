package com.jtomaszk.apps.myscale.importer

import com.google.common.collect.Lists
import com.jtomaszk.apps.myscale.dao.WeightEntryDao
import com.jtomaszk.apps.myscale.entity.WeightEntry
import spock.lang.Specification
import spock.lang.Subject

/**
 * Created by jtomaszk on 22.01.16.
 */
class ImporterServiceTest extends Specification {

    public static final int TIME1 = 1000
    @Subject
    private ImporterService service

    private WeightEntryDao dao = Mock(WeightEntryDao)

    public void setup() {
        service = new ImporterService();
        service.setDao(dao)
    }

    def "when empty list provided no exception is thrown"() {
        when:
            service.mergeFromImport(Lists.newArrayList())
        then:
            noExceptionThrown()
    }

    def "when no matched record found entry is saved"() {
        given:
            WeightEntry entry = Mock(WeightEntry)
            entry.getDateTimeMilliseconds() >> TIME1
        when:
            service.mergeFromImport(Lists.asList(entry))
        then:
            1 * entry.save()
    }

    def "when matched record found entry is not saved"() {
        given:
            WeightEntry entry = Mock(WeightEntry)
            entry.getDateTimeMilliseconds() >> TIME1
            dao.findByDateTimeMillisecons(TIME1) >> Mock(WeightEntry)
        when:
            service.mergeFromImport(Lists.asList(entry))
        then:
            0 * entry.save()
    }
}
