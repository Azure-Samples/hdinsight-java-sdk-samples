package com.microsoft.azure.hdinsight.samples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.hdinsight.samples.Configurations.AzureConfig;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.Autoscale;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.AutoscaleRecurrence;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.AutoscaleSchedule;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.AutoscaleTimeAndCapacity;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ClusterCreateParametersExtended;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ClusterCreateProperties;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ClusterDefinition;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ComputeProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.DaysOfWeek;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.HardwareProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.LinuxOperatingSystemProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.OSType;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.OsProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.Role;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.StorageAccount;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.StorageProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.Tier;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.implementation.HDInsightManager;
import java.util.ArrayList;
import java.util.List;

public class CreateScheduleBasedAutoscaleEnabledHadoopCluster {

  public static void main(String[] args) {

    // Authentication
    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
        AzureConfig.CLIENT_ID,
        AzureConfig.TENANT_ID,
        AzureConfig.CLIENT_SECRET,
        AzureEnvironment.AZURE);

    HDInsightManager manager = HDInsightManager
        .authenticate(credentials, AzureConfig.SUBSCRIPTION_ID);

    // Schedule Based Autoscale Configuration
    AutoscaleSchedule autoscaleSchedule = new AutoscaleSchedule();

    // Days of Week when the Autoscale needs to be triggered
    List<DaysOfWeek> daysOfWeek = new ArrayList<DaysOfWeek>();
    daysOfWeek.add(DaysOfWeek.MONDAY);
    autoscaleSchedule.withDays(daysOfWeek);

    // List of Time when the Autoscale needs to be triggered
    AutoscaleTimeAndCapacity autoscaleTimeAndCapacity = new AutoscaleTimeAndCapacity();
    autoscaleTimeAndCapacity.withTime(AzureConfig.SCHEDULE_BASED_AUTOSCALE_TIME);
    autoscaleTimeAndCapacity
        .withMinInstanceCount(AzureConfig.SCHEDULE_BASED_AUTOSCALE_TARGET_INSTANCE_COUNT);
    autoscaleTimeAndCapacity
        .withMaxInstanceCount(AzureConfig.SCHEDULE_BASED_AUTOSCALE_TARGET_INSTANCE_COUNT);
    autoscaleSchedule.withTimeAndCapacity(autoscaleTimeAndCapacity);

    // List of schedule. A schedule contains a list of time and days of the week.
    // example:
    // [Monday, Tuesday] - [09:00, 14:00]
    // [Friday] -[08:00]
    List<AutoscaleSchedule> autoscaleScheduleList = new ArrayList<AutoscaleSchedule>();
    autoscaleScheduleList.add(autoscaleSchedule);

    // Autoscale Recurrence is a list of Schedules and the Timezone of the Schedules
    AutoscaleRecurrence autoscaleRecurrence = new AutoscaleRecurrence();
    autoscaleRecurrence.withTimeZone(AzureConfig.SCHEDULE_BASED_AUTOSCALE_TIMEZONE);
    autoscaleRecurrence.withSchedule(autoscaleScheduleList);

    // Prepare cluster create parameters
    ClusterCreateParametersExtended createParams = new ClusterCreateParametersExtended()
        .withLocation(AzureConfig.LOCATION)
        .withProperties(new ClusterCreateProperties()
            .withClusterVersion(AzureConfig.CLUSTER_VERSION)
            .withOsType(OSType.LINUX)
            .withTier(Tier.STANDARD)
            .withClusterDefinition(new ClusterDefinition()
                .withKind("Hadoop")
                .withConfigurations(ImmutableMap.of(
                    "gateway", ImmutableMap.of(
                        "restAuthCredential.isEnabled", "true",
                        "restAuthCredential.username", AzureConfig.CLUSTER_LOGIN_USER_NAME,
                        "restAuthCredential.password", AzureConfig.PASSWORD
                    )))
            )
            .withComputeProfile(new ComputeProfile()
                .withRoles(ImmutableList.of(
                    new Role().withName("headnode")
                        .withTargetInstanceCount(2)
                        .withHardwareProfile(new HardwareProfile()
                            .withVmSize("Large")
                        )
                        .withOsProfile(new OsProfile()
                            .withLinuxOperatingSystemProfile(
                                new LinuxOperatingSystemProfile()
                                    .withUsername(AzureConfig.SSH_USER_NAME)
                                    .withPassword(AzureConfig.PASSWORD)
                            )
                        ),
                    new Role().withName("workernode")
                        .withTargetInstanceCount(3)
                        .withHardwareProfile(new HardwareProfile()
                            .withVmSize("Large")
                        )
                        .withOsProfile(new OsProfile()
                            .withLinuxOperatingSystemProfile(
                                new LinuxOperatingSystemProfile()
                                    .withUsername(AzureConfig.SSH_USER_NAME)
                                    .withPassword(AzureConfig.PASSWORD)
                            )
                        )
                        .withAutoscaleConfiguration(
                            new Autoscale().withRecurrence(autoscaleRecurrence))
                ))
            )
            .withStorageProfile(new StorageProfile()
                .withStorageaccounts(ImmutableList.of(
                    new StorageAccount()
                        .withName(
                            AzureConfig.STORAGE_ACCOUNT_NAME + AzureConfig.BLOB_ENDPOINT_SUFFIX)
                        .withKey(AzureConfig.STORAGE_ACCOUNT_KEY)
                        .withContainer(AzureConfig.CONTAINER_NAME.toLowerCase())
                        .withIsDefault(true)

                ))
            )
        );

    System.out
        .printf("Starting to create Schedule based autoscale enabled HDInsight Hadoop cluster %s\n",
            AzureConfig.CLUSTER_NAME);
    manager.clusters().inner()
        .create(AzureConfig.RESOURCE_GROUP_NAME, AzureConfig.CLUSTER_NAME, createParams);
    System.out
        .printf("Finished creating Scedhule based autoscale enabled HDInsight Hadoop cluster %s\n",
            AzureConfig.CLUSTER_NAME);
  }
}
