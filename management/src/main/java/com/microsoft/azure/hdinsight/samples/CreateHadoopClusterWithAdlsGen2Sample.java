package com.microsoft.azure.hdinsight.samples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.hdinsight.samples.Configurations.AzureConfig;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ClusterCreateParametersExtended;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ClusterCreateProperties;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ClusterDefinition;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.ComputeProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.HardwareProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.LinuxOperatingSystemProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.OSType;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.OsProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.Role;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.StorageAccount;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.StorageProfile;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.Tier;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.implementation.HDInsightManager;

public class CreateHadoopClusterWithAdlsGen2Sample {

  public static void main(String[] args) {

    // Authentication
    ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
        AzureConfig.CLIENT_ID,
        AzureConfig.TENANT_ID,
        AzureConfig.CLIENT_SECRET,
        AzureEnvironment.AZURE);

    HDInsightManager manager = HDInsightManager
        .authenticate(credentials, AzureConfig.SUBSCRIPTION_ID);

    String adlsGen2AccountName = AzureConfig.ADLS_GEN2_RESOURCE_ID
        .substring(AzureConfig.ADLS_GEN2_RESOURCE_ID.lastIndexOf("/") + 1);

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
                ))
            )
            .withStorageProfile(new StorageProfile()
                .withStorageaccounts(ImmutableList.of(
                    new StorageAccount()
                        .withName(adlsGen2AccountName + AzureConfig.DFS_ENDPOINT_SUFFIX)
                        .withIsDefault(true)
                        .withFileSystem(AzureConfig.ADLS_GEN2_FILE_SYSTEM_NAME)
                        .withResourceId(AzureConfig.ADLS_GEN2_RESOURCE_ID)
                        .withMsiResourceId(AzureConfig.MANAGED_IDENTITY_RESOURCE_ID)
                ))
            )
        );

    System.out.printf(
        "Starting to create HDInsight Hadoop cluster %s with Azure Data Lake Storage Gen2\n",
        AzureConfig.CLUSTER_NAME);
    manager.clusters().inner()
        .create(AzureConfig.RESOURCE_GROUP_NAME, AzureConfig.CLUSTER_NAME, createParams);
    System.out
        .printf("Finished creating HDInsight Hadoop cluster %s with Azure Data Lake Storage Gen2\n",
            AzureConfig.CLUSTER_NAME);
  }
}
