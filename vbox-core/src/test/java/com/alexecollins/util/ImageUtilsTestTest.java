package com.alexecollins.util;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: alex.collins
 * Date: 04/01/13
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */
public class ImageUtilsTestTest {
    @Test
    public void testCreateImage() throws Exception {
        final File dest = new File("src.iso");
        ImageUtils.createImage(new File("target"), new File("src"), dest);
        assertTrue(dest.exists());
        assert dest.delete();
    }
}
