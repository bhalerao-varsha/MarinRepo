package com.perfectaudience.rtb.strategy;

import com.perfectaudience.rtb.kv.dynamodb.table.user.UserObject;
import com.perfectaudience.rtb.model.PaAd;
import com.perfectaudience.rtb.model.PaBidRequest;
import com.perfectaudience.rtb.model.PaCampaign;
import com.perfectaudience.rtb.model.PaSegment;
import com.perfectaudience.rtb.model.enums.BiddingStrategyType;

public class FlatBidStrategy implements BidStrategy<CampaignFlatBidStrategyInfo, AdFlatBidStrategyInfo> {

    @Override
    public BiddingStrategyType type() {
        return BiddingStrategyType.FLATBID;
    }

    @Override
    public Class<CampaignFlatBidStrategyInfo> campaignBidStrategyInfoType() {
        return CampaignFlatBidStrategyInfo.class;
    }

    @Override
    public Class<AdFlatBidStrategyInfo> adBidStrategyInfoType() {
        return AdFlatBidStrategyInfo.class;
    }

    @Override
    public long calculateCpm(PaBidRequest bidRequest, UserObject<?> userObject, PaSegment segment, PaCampaign campaign,
            PaAd ad, CampaignFlatBidStrategyInfo campaignBidStrategyInfo, AdFlatBidStrategyInfo adBidStrategyInfo) {
        return campaign.getMaxBid();
    }

}
