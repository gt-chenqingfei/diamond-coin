package com.lovecoin.ediamond;

import com.lovecoin.ediamond.utils.VersionCompare;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created on 2017/12/21.
 */

@RunWith(JUnit4.class)

public class VersionCompareTest {

    @Test
    public void testVersion() {
        Assert.assertEquals(0, VersionCompare.compare("1.0", "1.0"));
        Assert.assertEquals(0, VersionCompare.compare("1.0", "1.0.0"));
        Assert.assertEquals(0, VersionCompare.compare("1.0.0", "1.0.0"));
        Assert.assertEquals(0, VersionCompare.compare("1.0.0", "1.0"));
        Assert.assertEquals(-1, VersionCompare.compare("1.0.0", "1.0.1"));
        Assert.assertEquals(1, VersionCompare.compare("1.0.1", "1.0.0"));
        Assert.assertEquals(1, VersionCompare.compare("1.0.1", "1.0"));
        Assert.assertTrue(VersionCompare.needUpdate("1.0.1", "1.0.0"));
        Assert.assertFalse(VersionCompare.needUpdate("1.0.1", "1.0.1"));
        System.out.println(VersionCompare.needUpdate("2", "1.0.1"));
        System.out.println(VersionCompare.needUpdate("1", "1.0.1"));
    }
}
