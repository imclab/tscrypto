/*
    Copyright 2013 NIC Chile Research Labs
    This file is part of TsCrypto.

    TsCrypto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TsCrypto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TsCrypto.  If not, see <http://www.gnu.org/licenses/>.
 */

package cl.niclabs.tscrypto.testing.datatypes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cl.niclabs.tscrypto.common.datatypes.KeyMetaInfo;
import cl.niclabs.tscrypto.common.datatypes.Stats;

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
