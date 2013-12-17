package cl.inria.tscrypto.testing.datatypes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cl.inria.tscrypto.common.datatypes.KeyMetaInfo;
import cl.inria.tscrypto.common.datatypes.Stats;

public class StatsTest {

	Stats stats;
	
	@Before
	public void setUp() {
		stats = new Stats(new KeyMetaInfo(3,4,512));
	}
	
	@Test
    @Ignore
	public void test_isValid() {
		stats.markValid(1);
        Assert.assertEquals(false, stats.isValid(0));
        Assert.assertEquals(true, stats.isValid(1));
        Assert.assertEquals(false, stats.isValid(2));
        Assert.assertEquals(false, stats.isValid(3));

		stats.markInvalid(1);
		stats.markValid(3);

        Assert.assertEquals(false, stats.isValid(0));
        Assert.assertEquals(false, stats.isValid(1));
        Assert.assertEquals(false, stats.isValid(2));
        Assert.assertEquals(true, stats.isValid(3));
	}
	
	@Test
	public void test_totalTime() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stats.finished();
        Assert.assertTrue(stats.getFinalTime() > stats.getInitialTime());
	}
	
	@Test
	public void test_firstkvalidnodes() {
		stats.markValid(1);
		stats.markValid(3);
		
		int[] valids = stats.getFirstKValidNodes();
        Assert.assertTrue(valids[0]==1);
        Assert.assertTrue(valids[1]==3);
	}
}
