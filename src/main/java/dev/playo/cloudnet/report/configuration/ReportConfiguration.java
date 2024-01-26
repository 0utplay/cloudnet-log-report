package dev.playo.cloudnet.report.configuration;

import java.nio.file.Path;
import lombok.NonNull;

public record ReportConfiguration(
  boolean reportForceStopOnly,
  @NonNull Path reportDestination
) {

  public static final ReportConfiguration DEFAULT = new ReportConfiguration(
    false,
    Path.of("reports"));
}
