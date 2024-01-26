package dev.playo.cloudnet.report.listener;

import dev.playo.cloudnet.report.configuration.ReportConfiguration;
import dev.playo.cloudnet.report.creator.ReportContext;
import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.service.ServiceLifeCycle;
import eu.cloudnetservice.node.event.service.CloudServicePreForceStopEvent;
import eu.cloudnetservice.node.event.service.CloudServicePreLifecycleEvent;
import eu.cloudnetservice.node.service.CloudService;
import java.nio.file.Path;
import java.util.logging.Logger;
import lombok.NonNull;

public final class ReportListener {

  private static final Logger LOGGER = LogManager.logger(ReportListener.class);

  private final Path reportDestination;
  private final ReportConfiguration reportConfiguration;

  public ReportListener(
    @NonNull Path reportDestination,
    @NonNull ReportConfiguration reportConfiguration
  ) {
    this.reportDestination = reportDestination;
    this.reportConfiguration = reportConfiguration;
  }

  @EventListener
  public void handleServiceForceStop(@NonNull CloudServicePreForceStopEvent event) {
    this.reportAll(event.service());
  }

  @EventListener
  public void handleServiceStop(@NonNull CloudServicePreLifecycleEvent event) {
    if (this.reportConfiguration.reportForceStopOnly()) {
      return;
    }

    var lifecycle = event.targetLifecycle();
    if (lifecycle == ServiceLifeCycle.STOPPED || lifecycle == ServiceLifeCycle.DELETED) {
      this.reportAll(event.service());
    }
  }

  private void reportAll(@NonNull CloudService cloudService) {
    var context = ReportContext.create(this.reportDestination, cloudService);
    if (context != null) {
      context.reportLogs();
      context.reportCachedConsoleLog();
    }
  }
}
