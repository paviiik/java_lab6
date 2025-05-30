package com.phone.cache;

import com.phone.dto.PrefixDtoResponse;
import org.springframework.stereotype.Component;

@Component
public class PrefixCache extends LfuCache<PrefixDtoResponse> {
    public PrefixCache() {
        super(10);
    }
}
