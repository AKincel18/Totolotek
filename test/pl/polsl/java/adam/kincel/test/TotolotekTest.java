package pl.polsl.java.adam.kincel.test;

import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.polsl.java.adam.kincel.model.MyException;
import pl.polsl.java.adam.kincel.model.Totolotek;

/**
 * @author Adam Kincel
 * @version 5.0
 */
public class TotolotekTest {

    public TotolotekTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    Totolotek totolotek;

    @Before
    public void setUp() {
        totolotek = new Totolotek();
    }

    /**
     * Method which check if number is in range 1 and 49
     *
     * @param arg number
     * @return true if beetwen 1 and 49, false if not
     */
    private Boolean isInRange(int arg) {
        return arg >= 1 && arg <= 49;
    }

    /**
     * Method which check if points in in range 0 and 6
     *
     * @param points points scored by user
     * @return true if beetwen 0 and 6, false if not
     */
    private Boolean isInRange2(int points) {
        return points >= 0 && points <= 6;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of drawing method, of class Totolotek, check if drawn numbers are in
     * correct range.
     */
    @Test
    public void Test1() {
        totolotek.drawing(); //drawing numbers

        assertTrue(isInRange(totolotek.getDrawnNumber(0)));
        assertTrue(isInRange(totolotek.getDrawnNumber(1)));
        assertTrue(isInRange(totolotek.getDrawnNumber(2)));
        assertTrue(isInRange(totolotek.getDrawnNumber(3)));
        assertTrue(isInRange(totolotek.getDrawnNumber(4)));
        assertTrue(isInRange(totolotek.getDrawnNumber(5)));
    }

    /**
     * Test of check method, of class Totolotek, check if scored points are in
     * correct range.
     */
    @Test
    public void Test2() {
        totolotek.check();
        assertTrue(isInRange2(totolotek.getPoints()));
    }

    /**
     * Test of check method, of class Totolotek, set user number, set drawn
     * number and call 'check' method to check if points are correct value.
     */
    @Test
    public void Test3() {

        totolotek.setNumber(0, 5);
        totolotek.setNumber(1, 11);
        totolotek.setNumber(2, 18);
        totolotek.setNumber(3, 25);
        totolotek.setNumber(4, 31);
        totolotek.setNumber(5, 44);

        Vector<Integer> drawnNumber = new Vector<>(6);
        drawnNumber.add(5);
        drawnNumber.add(11);
        drawnNumber.add(18);
        drawnNumber.add(25);
        drawnNumber.add(30);
        drawnNumber.add(44);
        totolotek.setDrawnNumber(drawnNumber);

        totolotek.check();
        //should be correct value
        assertEquals(totolotek.getPoints(), 5);

        //wrong value ('neighboring' value)
        assertNotSame(totolotek.getPoints(), 4);
        assertNotSame(totolotek.getPoints(), 6);

        //wrong value
        assertNotSame(totolotek.getPoints(), 0);
        assertNotSame(totolotek.getPoints(), 12);
    }

    /**
     * Test of isGoodNumber method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test
    public void Test4() throws MyException {
        //testing correct limit value
        totolotek.isGoodNumber(1);
        totolotek.isGoodNumber(49);

        //testing correct value
        totolotek.isGoodNumber(24);
        totolotek.isGoodNumber(39);
    }

    /**
     * Test of isGoodNumber method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test(expected = MyException.class)
    public void Test5() throws MyException {
        //testing 'neighboring' value out of left interval
        totolotek.isGoodNumber(0);
    }

    /**
     * Test of isGoodNumber method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test(expected = MyException.class)
    public void Test6() throws MyException {
        //testing 'neighboring' value out of right interval
        totolotek.isGoodNumber(50);
    }

    /**
     * Test of isGoodNumber method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test(expected = MyException.class)
    public void Test7() throws MyException {
        //testing wrong value, out of interval
        totolotek.isGoodNumber(-10);
    }

    /**
     * Test of isGoodNumber method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test(expected = MyException.class)
    public void Test8() throws MyException {
        //testing wrong value, out of interval
        totolotek.isGoodNumber(86);
    }

    /**
     * Test of isRepeatable method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test(expected = MyException.class)
    public void Test9() throws MyException {
        //wrong array (4 is repeated)
        int t[] = {1, 4, 3, 4, 6, 5};
        totolotek.isRepeatable(t);
    }

    /**
     * Test of isRepeatable method, of class Totolotek.
     *
     * @throws MyException
     */
    @Test(expected = MyException.class)
    public void Test10() throws MyException {
        //wrong array (1 is repeated in extreme position)
        int t[] = {1, 2, 3, 4, 5, 1};
        totolotek.isRepeatable(t);
    }

}
