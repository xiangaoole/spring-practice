package com.haroldgao.configuration.microprofile.config.source;

import java.util.Map;
import java.util.prefs.Preferences;

public class JavaSystemPropertiesConfigSource extends MapBasedConfigSource {

    public JavaSystemPropertiesConfigSource() {
        super("Java System Properties", 400);
    }

    /**
     * Java 系统属性通常在启动前确定，最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * @param configData
     * @throws Throwable
     */
    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(System.getProperties());
    }
}
