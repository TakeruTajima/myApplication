package com.mr2.zaiko.domain.inhouse.orderSlip;

import androidx.annotation.NonNull;

import com.mr2.zaiko.domain.inhouse.user.UserId;
import com.mr2.zaiko.domain.outside.commodity.CommodityId;

import java.time.ZonedDateTime;

public final class Order extends BuyRequest {
    public Order(@NonNull CommodityId commodityId, int quantity, @NonNull UserId userId, @NonNull String note) {
        super(commodityId, quantity, userId, note);
    }

    public Order(@NonNull CommodityId commodityId, int quantity, @NonNull UserId userId, @NonNull ZonedDateTime createdAt, @NonNull String note) {
        super(commodityId, quantity, userId, createdAt, note);
    }
}
