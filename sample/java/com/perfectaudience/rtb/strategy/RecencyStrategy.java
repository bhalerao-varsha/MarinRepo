package com.perfectaudience.rtb.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.perfectaudience.rtb.kv.dynamodb.table.user.UserObject;
import com.perfectaudience.rtb.model.PaAd;
import com.perfectaudience.rtb.model.PaBidRequest;
import com.perfectaudience.rtb.model.PaCampaign;
import com.perfectaudience.rtb.model.PaSegment;
import com.perfectaudience.rtb.model.enums.BiddingStrategyType;

public final class RecencyStrategy implements BidStrategy<CampaignRecencyStrategyInfo, AdRecencyStrategyInfo> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public BiddingStrategyType type() {
        return BiddingStrategyType.RECENCY;
    }

    @Override
    public Class<CampaignRecencyStrategyInfo> campaignBidStrategyInfoType() {
        return CampaignRecencyStrategyInfo.class;
    }

    @Override
    public Class<AdRecencyStrategyInfo> adBidStrategyInfoType() {
        return AdRecencyStrategyInfo.class;
    }

    @Override
    public long calculateCpm(PaBidRequest bidRequest, UserObject<?> userObject, PaSegment segment, PaCampaign campaign,
            PaAd ad, CampaignRecencyStrategyInfo campaignBidStrategyInfo, AdRecencyStrategyInfo adBidStrategyInfo) {
        if (segment == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("C{} segment was null, could not set recency bid, returning 0 bid", campaign.getId());
            }
            return 0L;
        }

        long lastSeenUserTs = segment.getTs();

        if (lastSeenUserTs == 0L) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not find lastSeenUserTs on user_id=" + userObject.userId()
                        + ", returning campaign max bid.");
            }
            return campaign.getMaxBid();
        }

        final int adjustment = getAdjustment(campaignBidStrategyInfo, lastSeenUserTs);
        if (adjustment > CampaignRecencyStrategyInfo.MAX_BID_ADJUST
                || adjustment < CampaignRecencyStrategyInfo.MIN_BID_ADJUST) {
            logger.warn("Found adjustment=" + adjustment + " outside min=" + CampaignRecencyStrategyInfo.MIN_BID_ADJUST
                    + " max=" + CampaignRecencyStrategyInfo.MAX_BID_ADJUST + ", returning campaign max bid");
            return campaign.getMaxBid();
        }

        final long cpm = (long) ((adjustment / 100.0d) * (campaign.getMaxBid()));
        if (logger.isDebugEnabled()) {
            logger.debug("RecencyStrategy C{} got cpm={} from campaign max bid={}, adjustment={}", new Object[] {
                    campaign.getId(), cpm, campaign.getMaxBid(), adjustment });
        }

        return cpm;
    }

    private int getAdjustment(final CampaignRecencyStrategyInfo campaignBidStrategyInfo, final long lastSeenUserTs) {
        final long segmentElapsedSecs = (System.currentTimeMillis() - lastSeenUserTs) / DateUtils.MILLIS_PER_SECOND;

        List<Long> adjustmentIntervals = new ArrayList<>(campaignBidStrategyInfo.getAdjustments().keySet());
        Collections.sort(adjustmentIntervals);
        int adjustment = -1;
        for (long adjustmentInterval : adjustmentIntervals) {
            if (adjustmentInterval > segmentElapsedSecs) {
                break;
            }

            adjustment = campaignBidStrategyInfo.getAdjustments().get(adjustmentInterval);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("RecencyStrategy calculated adjustment=" + adjustment + " from segmentElapsedSecs="
                    + segmentElapsedSecs);
        }

        return adjustment;
    }
}
