package com.microsoft.azure.hdinsight.samples.Configurations;

import com.microsoft.azure.hdinsight.samples.CreateEspClusterSample;
import com.microsoft.azure.hdinsight.samples.CreateHadoopClusterWithAdlsGen2Sample;
import com.microsoft.azure.hdinsight.samples.autoscale.CreateLoadBasedAutoscaleEnabledHadoopCluster;
import com.microsoft.azure.hdinsight.samples.autoscale.CreateScheduleBasedAutoscaleEnabledHadoopCluster;
import java.util.Random;

public class AzureConfig {

  /**
   * Tenant ID for your Azure Subscription Required by all the samples.
   */
  public static final String TENANT_ID = "00000000-0000-0000-0000-000000000000";

  /**
   * Your Service Principal App Client ID Required by all the samples.
   */
  public static final String CLIENT_ID = "00000000-0000-0000-0000-000000000000";

  /**
   * Your Service Principal Client Secret Required by all the samples.
   */
  public static final String CLIENT_SECRET = "";

  /**
   * Azure Subscription ID Required by all the samples.
   */
  public static final String SUBSCRIPTION_ID = "00000000-0000-0000-0000-000000000000";

  /**
   * The name for the cluster you are creating. The name must be unique, 59 characters or less, and
   * can contain letters, numbers, and hyphens (but the first and last character must be a letter or
   * number). Required by all the samples.
   */
  public static final String CLUSTER_NAME =
      "hdisamplecluster" + (new Random().nextInt(9000) + 1000);

  /**
   * The version of the cluster you want to create. The current supported versions are 3.6 and 4.0
   */
  public static final String CLUSTER_VERSION = "3.6";

  /**
   * Choose a region. i.e. "East US 2". Required by all the samples.
   */
  public static final String LOCATION = "";

  /**
   * The name of your existing Resource Group Required by all the samples.
   */
  public static final String RESOURCE_GROUP_NAME = "";

  /**
   * Choose a cluster login username. The username must be at least two characters in length and can
   * only consist of digits, upper or lowercase letters, and/or the following special characters: !
   * # $ % & ' ( ) - ^ _ ` { } ~ Required by all the samples.
   */
  public static final String CLUSTER_LOGIN_USER_NAME = "admin";

  /**
   * Choose a Secure Shell (SSH) user username. The SSH username must be at least two characters in
   * length and can only consist of digits, upper or lowercase letters, and/or the following special
   * characters: % & ' - ^ _ ` {} ~ Required by all the samples.
   */
  public static final String SSH_USER_NAME = "sshuser";

  /**
   * Choose a cluster admin password. The password must be at least 10 characters in length and must
   * contain at least one digit, one uppercase and one lower case letter, one non-alphanumeric
   * character (except characters ' " ` \). Required by all the samples.
   */
  public static final String PASSWORD = "";

  /**
   * The name of blob storage account Required by all the samples except {@link
   * CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String STORAGE_ACCOUNT_NAME = "";

  /**
   * Blob storage account key Required by all the samples except {@link
   * CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String STORAGE_ACCOUNT_KEY = "";

  /**
   * Blob Storage endpoint suffix. Required by all the samples except {@link
   * CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String BLOB_ENDPOINT_SUFFIX = ".blob.core.windows.net";

  /**
   * Blob storage account container name Required by all the samples except {@link
   * CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String CONTAINER_NAME = "container" + (new Random().nextInt(9000) + 1000);

  /**
   * Dfs Storage endpoint suffix. Required by {@link CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String DFS_ENDPOINT_SUFFIX = ".dfs.core.windows.net";

  /**
   * ADLS Gen2 storage account filesystem name Required by {@link CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String ADLS_GEN2_FILE_SYSTEM_NAME = CONTAINER_NAME;

  /**
   * ADLS Gen2 storage account recource id Required by {@link CreateHadoopClusterWithAdlsGen2Sample}.
   */
  public static final String ADLS_GEN2_RESOURCE_ID = "/subscriptions/00000000-0000-0000-0000-000000000000/resourceGroups/samplerg/providers/Microsoft.Storage/storageAccounts/sampleadlsgen2";

  /**
   * User-assigned managed identity resource id Required by {@link CreateHadoopClusterWithAdlsGen2Sample}
   * and {@link CreateEspClusterSample}.
   */
  public static final String MANAGED_IDENTITY_RESOURCE_ID = "/subscriptions/00000000-0000-0000-0000-000000000000/resourcegroups/samplerg/providers/Microsoft.ManagedIdentity/userAssignedIdentities/samplemsi";

  /**
   * Azure AD Domain Service resource id Required by {@link CreateEspClusterSample}.
   */
  public static final String AADDS_RESOURCE_ID = "/subscriptions/00000000-0000-0000-0000-000000000000/resourceGroups/samplerg/providers/Microsoft.AAD/domainServices/sampledomain.onmicrosoft.com";

  /**
   * Virtual network resource id Required by {@link CreateEspClusterSample}.
   */
  public static final String VIRTUAL_NETWORK_RESOURCE_ID = "/subscriptions/00000000-0000-0000-0000-000000000000/resourceGroups/samplerg/providers/Microsoft.Network/virtualNetworks/samplevnet";

  /**
   * Subnet name Required by {@link CreateEspClusterSample}.
   */
  public static final String SUBNET_NAME = "";

  /**
   * Secure LDAP protocol URL to communicate with the Active Directory Required by {@link
   * CreateEspClusterSample}.
   */
  public static final String LDAPS_URL = "ldaps://sampledomain.onmicrosoft.com:636";

  /**
   * Cluster Admin AD Account Username Required by {@link CreateEspClusterSample}.
   */
  public static final String DOMAIN_USER_NAME = "sample.user@sampledomain.onmicrosoft.com";

  /**
   * Cluster access group Required by {@link CreateEspClusterSample}.
   */
  public static final String CLUSTER_ACCESS_GROUP = "Sample Group";

  /**
   * Minimum Instance count for Load Based Autoscale Required by {@link
   * CreateLoadBasedAutoscaleEnabledHadoopCluster}
   */
  public static final int LOAD_BASED_AUTOSCALE_MIN_INSTANCE_COUNT = 3;

  /**
   * Maximum Instance count for Load Based Autoscale Required by {@link
   * CreateLoadBasedAutoscaleEnabledHadoopCluster}
   */
  public static final int LOAD_BASED_AUTOSCALE_MAX_INSTANCE_COUNT = 5;

  /**
   * Timezone for Schedule Based Autoscale Required by {@link CreateScheduleBasedAutoscaleEnabledHadoopCluster}
   */
  public static final String SCHEDULE_BASED_AUTOSCALE_TIMEZONE = "UTC";

  /**
   * Target Instance Count for Schedule Based Autoscale Required by {@link
   * CreateScheduleBasedAutoscaleEnabledHadoopCluster}
   */
  public static final int SCHEDULE_BASED_AUTOSCALE_TARGET_INSTANCE_COUNT = 5;

  /**
   * List of time in HH:mm format when the autoscale should be triggered Required by {@link
   * CreateScheduleBasedAutoscaleEnabledHadoopCluster}
   */
  public static final String SCHEDULE_BASED_AUTOSCALE_TIME = "09:00";
}
