package edu.kit.psegruppe3.mensax.data;

import android.test.AndroidTestCase;

/**
 * TestPractice class.
 * @author MensaX-group
 * @version 1.0
 */
public class TestPractice extends AndroidTestCase {

    /**
     * This gets run before every test.
     * @throws Exception if something goes wrong
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Check if assertions are working properly
     * @throws Throwable if not
     */
    public void testThatDemonstratesAssertions() throws Throwable {
        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("Y should be true", d > a);
        assertFalse("Z should be false", a == b);

        if (b > d) {
            fail("XX should never happen");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}