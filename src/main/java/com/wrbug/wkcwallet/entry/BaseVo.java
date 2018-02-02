package com.wrbug.wkcwallet.entry;

import com.wrbug.wkcwallet.util.JsonHelper;

public class BaseVo {
    public String toJson() {
        return JsonHelper.toJson(this);
    }
}
