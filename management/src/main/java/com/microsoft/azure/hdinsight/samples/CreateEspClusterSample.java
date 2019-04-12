package com.microsoft.azure.hdinsight.samples;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.*;
import com.microsoft.azure.management.hdinsight.v2018_06_01_preview.implementation.HDInsightManager;

import java.util.Collections;

import static com.microsoft.azure.hdinsight.samples.Configurations.*;

public class CreateEspClusterSample {

    public static void main(String[] args){

        // Authentication
        ApplicationTokenCredentials credentials = new ApplicationTokenCredentials(
            CLIENT_ID,
            TENANT_ID,
            CLIENT_SECRET,
            AzureEnvironment.AZURE);

        HDInsightManager manager = HDInsightManager.authenticate(credentials, SUBSCRIPTION_ID);

        String aaddsDnsDomainName = AADDS_RESOURCE_ID.substring(AADDS_RESOURCE_ID.lastIndexOf("/") + 1);

        // Prepare cluster create parameters
        ClusterCreateParametersExtended createParams = new ClusterCreateParametersExtended()
            .withLocation(LOCATION)
            .withProperties(new ClusterCreateProperties()
                .withClusterVersion("3.6")
                .withOsType(OSType.LINUX)
                .withTier(Tier.PREMIUM)
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
                            )
                            .withVirtualNetworkProfile(new VirtualNetworkProfile()
                                .withId(VIRTUAL_NETWORK_RESOURCE_ID)
                                .withSubnet(String.format("%s/subnets/%s", VIRTUAL_NETWORK_RESOURCE_ID, SUBNET_NAME))
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
                            .withVirtualNetworkProfile(new VirtualNetworkProfile()
                                .withId(VIRTUAL_NETWORK_RESOURCE_ID)
                                .withSubnet(String.format("%s/subnets/%s", VIRTUAL_NETWORK_RESOURCE_ID, SUBNET_NAME))
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
                .withSecurityProfile(new SecurityProfile()
                    .withDirectoryType(DirectoryType.ACTIVE_DIRECTORY)
                    .withLdapsUrls(Collections.singletonList(LDAPS_URL))
                    .withDomainUsername(DOMAIN_USER_NAME)
                    .withDomain(aaddsDnsDomainName)
                    .withClusterUsersGroupDNs(Collections.singletonList(CLUSTER_ACCESS_GROUP))
                    .withAaddsResourceId(AADDS_RESOURCE_ID)
                    .withMsiResourceId(MANAGED_IDENTITY_RESOURCE_ID)
                )
            )
            .withIdentity(new ClusterIdentity()
                .withType(ResourceIdentityType.USER_ASSIGNED)
                .withUserAssignedIdentities(ImmutableMap.of(
                    MANAGED_IDENTITY_RESOURCE_ID, new ClusterIdentityUserAssignedIdentitiesValue()
                ))
            );

        System.out.printf("Starting to create HDInsight Spark cluster %s\n", CLUSTER_NAME);
        manager.clusters().inner().create(RESOURCE_GROUP_NAME, CLUSTER_NAME, createParams);
        System.out.printf("Finished creating HDInsight Spark cluster %s\n", CLUSTER_NAME);
    }
}
