package io.github.linyimin0812.profiler.core.monitor.check;


import io.github.linyimin0812.profiler.common.logger.LogFactory;
import io.github.linyimin0812.profiler.common.settings.ProfilerSettings;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;

/**
 * @author linyimin
 * @date 2023/04/20 17:56
 * @description 启动超时检查
 **/
@MetaInfServices
public class TimeoutCheckService implements AppStatusCheckService {

    private final Logger logger = LogFactory.getStartupLogger();
    /**
     * 超时时间: 默认20分钟
     */
    private Duration duration;
    private Instant startInstant;

    @Override
    public void init() {
        long minutes = Long.parseLong(ProfilerSettings.getProperty("java-profiler.app.status.check.timeout", "20"));
        duration = Duration.ofMinutes(minutes);
        startInstant = Instant.now();
        logger.info("timeout duration: {} minutes", minutes);
    }

    @Override
    public AppStatus check() {
        Duration runDuration = Duration.between(startInstant, Instant.now());

        // 超时, 默认应用以启动成功
        if (duration.compareTo(runDuration) <= 0) {
            return AppStatus.running;
        }

        return AppStatus.initializing;
    }
}
