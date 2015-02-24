package edu.mit.voicesurvey.androidapplication;

import junit.framework.TestCase;

import edu.mit.voicesurvey.androidapplication.model.DataHolder;

/**
 * Created by Ashley on 2/24/2015.
 */
public class DataHolderTest extends TestCase {

    /**
     * Test parsing in the campaign data from the json file
     */
    public final void testCampaignParsing() {
        assertEquals(TestData.getCampaign(),DataHolder.getInstance().getCampaign());
    }

}
