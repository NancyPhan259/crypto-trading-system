package com.aquariux.crypto_trading_system.service.contract;


import com.aquariux.crypto_trading_system.entity.Asset;

import java.util.Collection;
import java.util.UUID;

public interface AssetService {

    Collection<Asset> getAssets(UUID userId);
}
