# ClientsApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addClientMembers**](#addclientmembers) | **PUT** /v1/clients/{cid}/members | 添加应用成员|
|[**addUserClientMembers**](#adduserclientmembers) | **PUT** /v1/users/{uid}/clients/{cid}/members | 添加应用成员|
|[**createClient**](#createclient) | **POST** /v1/clients | 创建应用|
|[**createUserClient**](#createuserclient) | **POST** /v1/users/{uid}/clients | 创建用户应用|
|[**getClient**](#getclient) | **GET** /v1/clients/{cid} | 获取应用|
|[**getClientLogo**](#getclientlogo) | **GET** /v1/clients/{cid}/logo | 获取应用 Logo|
|[**getClients**](#getclients) | **GET** /v1/clients | 查询应用|
|[**getUserClient**](#getuserclient) | **GET** /v1/users/{uid}/clients/{cid} | 获取用户应用|
|[**getUserClientLogo**](#getuserclientlogo) | **GET** /v1/users/{uid}/clients/{cid}/logo | 获取用户应用 Logo|
|[**getUserClients**](#getuserclients) | **GET** /v1/users/{uid}/clients | 查询用户应用|
|[**removeClient**](#removeclient) | **DELETE** /v1/clients/{cid} | 删除应用|
|[**removeClientMembers**](#removeclientmembers) | **DELETE** /v1/clients/{cid}/members | 移除应用成员|
|[**removeClients**](#removeclients) | **DELETE** /v1/clients | 删除应用|
|[**removeUserClient**](#removeuserclient) | **DELETE** /v1/users/{uid}/clients/{cid} | 删除用户应用|
|[**removeUserClientMembers**](#removeuserclientmembers) | **DELETE** /v1/users/{uid}/clients/{cid}/members | 移除应用成员|
|[**removeUserClients**](#removeuserclients) | **DELETE** /v1/users/{uid}/clients | 删除用户应用|
|[**updateClientAccessTokenValidity**](#updateclientaccesstokenvalidity) | **PUT** /v1/clients/{cid}/access-token-validity | 更新应用AccessToken有效期|
|[**updateClientDescription**](#updateclientdescription) | **PUT** /v1/clients/{cid}/description | 更新应用描述|
|[**updateClientLogo**](#updateclientlogo) | **PUT** /v1/clients/{cid}/logo | 更新应用 Logo|
|[**updateClientName**](#updateclientname) | **PUT** /v1/clients/{cid}/name | 更新应用名称|
|[**updateClientRedirectUri**](#updateclientredirecturi) | **PUT** /v1/clients/{cid}/redirect | 更新应用回调地址|
|[**updateClientRefreshTokenValidity**](#updateclientrefreshtokenvalidity) | **PUT** /v1/clients/{cid}/refresh-token-validity | 更新应用RefreshToken有效期|
|[**updateClientSecret**](#updateclientsecret) | **PUT** /v1/clients/{cid}/secret | 更新应用密钥|
|[**updateClientStatus**](#updateclientstatus) | **PUT** /v1/clients/{cid}/status | 更新应用状态|
|[**updateUserClientDescription**](#updateuserclientdescription) | **PUT** /v1/users/{uid}/clients/{cid}/description | 更新用户应用描述|
|[**updateUserClientLogo**](#updateuserclientlogo) | **PUT** /v1/users/{uid}/clients/{cid}/logo | 更新用户应用 Logo|
|[**updateUserClientName**](#updateuserclientname) | **PUT** /v1/users/{uid}/clients/{cid}/name | 更新用户应用名称|
|[**updateUserClientRedirectUri**](#updateuserclientredirecturi) | **PUT** /v1/users/{uid}/clients/{cid}/redirect | 更新用户应用回调地址|
|[**updateUserClientSecret**](#updateuserclientsecret) | **PUT** /v1/users/{uid}/clients/{cid}/secret | 更新用户应用密钥|

# **addClientMembers**
> addClientMembers()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let uids: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.addClientMembers(
    cid,
    uids
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **uids** | **Array&lt;number&gt;** |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **addUserClientMembers**
> addUserClientMembers()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let uids: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.addUserClientMembers(
    uid,
    cid,
    uids
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **uids** | **Array&lt;number&gt;** |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createClient**
> Client createClient()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let name: string; // (default to undefined)
let description: string; // (default to undefined)
let redirectUri: string; // (default to undefined)
let scopes: Array<number>; // (optional) (default to undefined)
let grantTypes: Array<number>; // (optional) (default to undefined)
let accessTokenValidity: number; // (optional) (default to 7200)
let refreshTokenValidity: number; // (optional) (default to 86400)
let additionalInformation: string; // (optional) (default to undefined)
let status: number; // (optional) (default to 0)

const { status, data } = await apiInstance.createClient(
    uid,
    name,
    description,
    redirectUri,
    scopes,
    grantTypes,
    accessTokenValidity,
    refreshTokenValidity,
    additionalInformation,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **name** | [**string**] |  | defaults to undefined|
| **description** | [**string**] |  | defaults to undefined|
| **redirectUri** | [**string**] |  | defaults to undefined|
| **scopes** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|
| **grantTypes** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|
| **accessTokenValidity** | [**number**] |  | (optional) defaults to 7200|
| **refreshTokenValidity** | [**number**] |  | (optional) defaults to 86400|
| **additionalInformation** | [**string**] |  | (optional) defaults to undefined|
| **status** | [**number**] |  | (optional) defaults to 0|


### Return type

**Client**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createUserClient**
> Client createUserClient()

应用和用户需要 WRITE_CLIENT 权限，或者应用拥有 WRITE_CLIENT 权限且 uid 为当前用户并拥有 CREATE_CLIENT 权限）

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let name: string; // (default to undefined)
let description: string; // (default to undefined)
let redirectUri: string; // (default to undefined)
let scopes: Array<number>; // (optional) (default to undefined)
let grantTypes: Array<number>; // (optional) (default to undefined)

const { status, data } = await apiInstance.createUserClient(
    uid,
    name,
    description,
    redirectUri,
    scopes,
    grantTypes
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **name** | [**string**] |  | defaults to undefined|
| **description** | [**string**] |  | defaults to undefined|
| **redirectUri** | [**string**] |  | defaults to undefined|
| **scopes** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|
| **grantTypes** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|


### Return type

**Client**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getClient**
> Client getClient()

应用和用户需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getClient(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**Client**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getClientLogo**
> getClientLogo()


### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getClientLogo(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getClients**
> QueryResultsClient getClients()

应用和用户需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let q: string; // (optional) (default to undefined)
let order: Array<string>; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let limit: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.getClients(
    q,
    order,
    offset,
    limit
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **q** | [**string**] |  | (optional) defaults to undefined|
| **order** | **Array&lt;string&gt;** |  | (optional) defaults to undefined|
| **offset** | [**number**] |  | (optional) defaults to undefined|
| **limit** | [**number**] |  | (optional) defaults to undefined|


### Return type

**QueryResultsClient**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUserClient**
> Client getUserClient()

应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getUserClient(
    uid,
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**Client**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUserClientLogo**
> getUserClientLogo()

应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getUserClientLogo(
    uid,
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUserClients**
> QueryResultsClient getUserClients()

应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let q: string; // (optional) (default to undefined)
let order: Array<string>; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let limit: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.getUserClients(
    uid,
    q,
    order,
    offset,
    limit
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **q** | [**string**] |  | (optional) defaults to undefined|
| **order** | **Array&lt;string&gt;** |  | (optional) defaults to undefined|
| **offset** | [**number**] |  | (optional) defaults to undefined|
| **limit** | [**number**] |  | (optional) defaults to undefined|


### Return type

**QueryResultsClient**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeClient**
> removeClient()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.removeClient(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeClientMembers**
> removeClientMembers()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let uids: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.removeClientMembers(
    cid,
    uids
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **uids** | **Array&lt;number&gt;** |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeClients**
> removeClients()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cids: Array<string>; // (default to undefined)

const { status, data } = await apiInstance.removeClients(
    cids
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cids** | **Array&lt;string&gt;** |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeUserClient**
> removeUserClient()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.removeUserClient(
    uid,
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeUserClientMembers**
> removeUserClientMembers()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let uids: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.removeUserClientMembers(
    uid,
    cid,
    uids
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **uids** | **Array&lt;number&gt;** |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeUserClients**
> removeUserClients()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cids: Array<string>; // (default to undefined)

const { status, data } = await apiInstance.removeUserClients(
    uid,
    cids
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cids** | **Array&lt;string&gt;** |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientAccessTokenValidity**
> updateClientAccessTokenValidity()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let accessTokenValidity: number; // (default to undefined)

const { status, data } = await apiInstance.updateClientAccessTokenValidity(
    cid,
    accessTokenValidity
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **accessTokenValidity** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientDescription**
> updateClientDescription()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let description: string; // (default to undefined)

const { status, data } = await apiInstance.updateClientDescription(
    cid,
    description
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **description** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientLogo**
> updateClientLogo(body)

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let body: File; //

const { status, data } = await apiInstance.updateClientLogo(
    cid,
    body
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **body** | **File**|  | |
| **cid** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: image/*
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientName**
> updateClientName()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let name: string; // (default to undefined)

const { status, data } = await apiInstance.updateClientName(
    cid,
    name
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **name** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientRedirectUri**
> updateClientRedirectUri()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let redirectUri: string; // (default to undefined)

const { status, data } = await apiInstance.updateClientRedirectUri(
    cid,
    redirectUri
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **redirectUri** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientRefreshTokenValidity**
> updateClientRefreshTokenValidity()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let refreshTokenValidity: number; // (default to undefined)

const { status, data } = await apiInstance.updateClientRefreshTokenValidity(
    cid,
    refreshTokenValidity
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **refreshTokenValidity** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientSecret**
> string updateClientSecret()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.updateClientSecret(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**string**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateClientStatus**
> updateClientStatus()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let cid: string; // (default to undefined)
let status: number; // (default to undefined)

const { status, data } = await apiInstance.updateClientStatus(
    cid,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **status** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserClientDescription**
> updateUserClientDescription()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let description: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserClientDescription(
    uid,
    cid,
    description
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **description** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserClientLogo**
> updateUserClientLogo(body)

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let body: File; //

const { status, data } = await apiInstance.updateUserClientLogo(
    uid,
    cid,
    body
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **body** | **File**|  | |
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: image/*
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserClientName**
> updateUserClientName()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let name: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserClientName(
    uid,
    cid,
    name
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **name** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserClientRedirectUri**
> updateUserClientRedirectUri()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let redirectUri: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserClientRedirectUri(
    uid,
    cid,
    redirectUri
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **redirectUri** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserClientSecret**
> string updateUserClientSecret()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ClientsApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ClientsApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserClientSecret(
    uid,
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**string**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

