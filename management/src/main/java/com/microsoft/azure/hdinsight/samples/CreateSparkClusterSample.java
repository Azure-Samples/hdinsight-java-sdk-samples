package com.microsoft.azure.hdinsight.samples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.*;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.implementation.HDInsightManager;

import static com.microsoft.azure.hdinsight.samples.Configurations.*;

public class CreateSparkClusterSample {

    public static void main(String[] args){

        // Authentication
        ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
            CLIENT_ID,
            TENANT_ID,
            CLIENT_SECRET,
            AzureEnvironment.AZURE);

        HDInsightManager manager = HDInsightManager.authenticate(credentials, SUBSCRIPTION_ID);

        // Prepare cluster create parameters
        ClusterCreateParametersExtended createParams = new ClusterCreateParametersExtended()
            .withLocation(LOCATION)
            .withProperties(new ClusterCreateProperties()
                .withClusterVersion("3.6")
                .withOsType(OSType.LINUX)
                .withTier(Tier.STANDARD)
                .withClusterDefinition(new ClusterDefinition()
                    .withKind("Spark")
                    .withConfigurations(ImmutableMap.of(
                        "gateway", ImmutableMap.of(
                            "restAuthCredential.isEnabled", "true",
                            "restAuthCredential.username", CLUSTER_LOGIN_USER_NAME,
                            "restAuthCredential.password", PASSWORD
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
                                        .withUsername(SSH_USER_NAME)
                                        .withPassword(PASSWORD)
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
                                        .withUsername(SSH_USER_NAME)
                                        .withPassword(PASSWORD)
                                )
                            )
                    ))
                )
                .withStorageProfile(new StorageProfile()
                    .withStorageaccounts(ImmutableList.of(
                        new StorageAccount()
                            .withName(STORAGE_ACCOUNT_NAME + BLOB_ENDPOINT_SUFFIX)
                            .withKey(STORAGE_ACCOUNT_KEY)
                            .withContainer(CONTAINER_NAME.toLowerCase())
                            .withIsDefault(true)

                    ))
                )
            );

        System.out.printf("Starting to create HDInsight Spark cluster %s\n", CLUSTER_NAME);
        manager.clusters().inner().create(RESOURCE_GROUP_NAME, CLUSTER_NAME, createParams);
        System.out.printf("Finished creating HDInsight Spark cluster %s\n", CLUSTER_NAME);
    }
}
