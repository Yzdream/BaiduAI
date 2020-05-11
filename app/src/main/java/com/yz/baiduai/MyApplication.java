package com.yz.baiduai;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class MyApplication extends TinkerApplication {

    public MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.yz.baiduai.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
