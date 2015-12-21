package com.jtomaszk.apps.common

import android.test.mock.MockContext
import com.jtomaszk.apps.common.prefs.ApplicationPref
import com.jtomaszk.apps.common.prefs.PreferenceUtil
import spock.lang.Specification

/**
 * Created by jarema-user on 2015-12-09.
 */
class PreferenceUtilTest extends Specification {
    def pref = new ApplicationPref() {
        @Override
        String getPrefName() {
            return "TEST"
        }
    }

    def "should ReadInt return default value"() {
        when:
        def val = PreferenceUtil.readInt(pref, 0, new MockContext());
        then:
        val == 0
    }

    def "ReadString"() {

    }

    def "ReadTimeInMillis"() {

    }

    def "WriteTimeInMillis"() {

    }

    def "WriteInt"() {

    }

    def "WriteString"() {

    }
}
