package dev.playo.cloudnet.report;

import dev.playo.cloudnet.report.configuration.ReportConfiguration;
import dev.playo.cloudnet.report.listener.ReportListener;
import eu.cloudnetservice.driver.document.DocumentFactory;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.module.ModuleLifeCycle;
import eu.cloudnetservice.driver.module.ModuleTask;
import eu.cloudnetservice.driver.module.driver.DriverModule;
import jakarta.inject.Singleton;
import lombok.NonNull;

@Singleton
public class CloudNetLogReportModule extends DriverModule {

  @ModuleTask(lifecycle = ModuleLifeCycle.STARTED)
  public void initModule(@NonNull EventManager eventManager) {
    var configuration = this.readConfig(
      ReportConfiguration.class,
      () -> ReportConfiguration.DEFAULT,
      DocumentFactory.json());

    var path = this.moduleWrapper.dataDirectory().resolve(configuration.reportDestination());
    eventManager.registerListener(new ReportListener(path, configuration));
  }
}
