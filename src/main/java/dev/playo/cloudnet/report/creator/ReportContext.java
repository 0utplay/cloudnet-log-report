package dev.playo.cloudnet.report.creator;

import eu.cloudnetservice.common.io.FileUtil;
import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.common.log.Logger;
import eu.cloudnetservice.driver.service.ServiceEnvironmentType;
import eu.cloudnetservice.node.service.CloudService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public record ReportContext(@NonNull Path directory, @NonNull CloudService service) {

  private static final Logger LOGGER = LogManager.logger(ReportContext.class);

  public static @Nullable ReportContext create(@NonNull Path baseDir, @NonNull CloudService service) {
    var directory = baseDir
      .resolve(service.serviceId().taskName())
      .resolve(service.serviceId().name() + '-' + service.serviceId().uniqueId())
      .normalize()
      .toAbsolutePath();

    if (Files.exists(directory)) {
      LOGGER.fine(
        "Unable to create log report directory for service %s the directory %s already exists",
        null,
        service.serviceId().name(),
        directory);
      return null;
    }

    FileUtil.createDirectory(directory);
    return new ReportContext(directory, service);
  }

  public void reportCachedConsoleLog() {
    try {
      Files.write(this.directory.resolve("consoleLog.txt"), this.service.cachedLogMessages());
    } catch (IOException exception) {
      LOGGER.severe("Unable to report cached console log for service %s", exception, this.service.serviceId().name());
    }
  }

  public void reportLogs() {
    var destination = this.directory.resolve("logs");
    FileUtil.createDirectory(destination);

    if (this.service.serviceId().environment().equals(ServiceEnvironmentType.BUNGEECORD)) {
      // bungeecord stores its logs in the root directory instead of "logs"
      FileUtil.walkFileTree(this.service.directory(),
        (root, current) -> FileUtil.copy(current, destination.resolve(root.relativize(current))), false,
        "proxy.log*");
    } else {
      FileUtil.copyDirectory(this.service.directory().resolve("logs"), destination);
    }
  }

}
