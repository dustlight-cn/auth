# AuthoritiesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteAuthorities**](#deleteauthorities) | **DELETE** /v1/authorities | 删除权限|
|[**deleteClientAuthorities**](#deleteclientauthorities) | **DELETE** /v1/clients/{cid}/authorities | 删除应用权限|
|[**deleteRoleAuthorities**](#deleteroleauthorities) | **DELETE** /v1/roles/{rid}/authorities | 删除角色权限|
|[**getAuthorities**](#getauthorities) | **GET** /v1/authorities | 获取权限|
|[**getClientAuthorities**](#getclientauthorities) | **GET** /v1/clients/{cid}/authorities | 获取应用权限|
|[**getRoleAuthorities**](#getroleauthorities) | **GET** /v1/roles/{rid}/authorities | 获取角色权限|
|[**getUserClientAuthorities**](#getuserclientauthorities) | **GET** /v1/users/{uid}/clients/{cid}/authorities | 获取应用权限|
|[**setAuthorities**](#setauthorities) | **PUT** /v1/authorities | 修改或添加权限|
|[**setClientAuthorities**](#setclientauthorities) | **PUT** /v1/clients/{cid}/authorities | 添加应用权限|
|[**setRoleAuthorities**](#setroleauthorities) | **PUT** /v1/roles/{rid}/authorities | 添加角色权限|

# **deleteAuthorities**
> deleteAuthorities(requestBody)

应用和用户需要 WRITE_AUTHORITY 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let requestBody: Array<number>; //
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.deleteAuthorities(
    requestBody,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **requestBody** | **Array<number>**|  | |
| **clientId** | [**string**] |  | (optional) defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteClientAuthorities**
> deleteClientAuthorities()

应用和用户需要 GRANT_CLIENT 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let cid: string; // (default to undefined)
let authorityId: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteClientAuthorities(
    cid,
    authorityId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **authorityId** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **deleteRoleAuthorities**
> deleteRoleAuthorities()

应用和用户需要 GRANT_ROLE 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let rid: number; // (default to undefined)
let authorityId: Array<number>; // (default to undefined)
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.deleteRoleAuthorities(
    rid,
    authorityId,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rid** | [**number**] |  | defaults to undefined|
| **authorityId** | **Array&lt;number&gt;** |  | defaults to undefined|
| **clientId** | [**string**] |  | (optional) defaults to undefined|


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

# **getAuthorities**
> Array<Authority> getAuthorities()


### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let id: Array<number>; // (optional) (default to undefined)
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAuthorities(
    id,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|
| **clientId** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<Authority>**

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

# **getClientAuthorities**
> Array<string> getClientAuthorities()

应用和用户需要 READ_CLIENT 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getClientAuthorities(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**Array<string>**

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

# **getRoleAuthorities**
> Array<string> getRoleAuthorities()


### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let rid: number; // (default to undefined)

const { status, data } = await apiInstance.getRoleAuthorities(
    rid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rid** | [**number**] |  | defaults to undefined|


### Return type

**Array<string>**

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

# **getUserClientAuthorities**
> Array<string> getUserClientAuthorities()

应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getUserClientAuthorities(
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

**Array<string>**

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

# **setAuthorities**
> Array<Authority> setAuthorities(authority)

应用和用户需要 WRITE_AUTHORITY 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let authority: Array<Authority>; //
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.setAuthorities(
    authority,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **authority** | **Array<Authority>**|  | |
| **clientId** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<Authority>**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **setClientAuthorities**
> setClientAuthorities()

应用和用户需要 GRANT_CLIENT 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let cid: string; // (default to undefined)
let authorityId: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.setClientAuthorities(
    cid,
    authorityId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **authorityId** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **setRoleAuthorities**
> setRoleAuthorities()

应用和用户需要 GRANT_ROLE 权限。

### Example

```typescript
import {
    AuthoritiesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthoritiesApi(configuration);

let rid: number; // (default to undefined)
let authorityId: Array<number>; // (default to undefined)
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.setRoleAuthorities(
    rid,
    authorityId,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rid** | [**number**] |  | defaults to undefined|
| **authorityId** | **Array&lt;number&gt;** |  | defaults to undefined|
| **clientId** | [**string**] |  | (optional) defaults to undefined|


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

