# RolesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteRoles**](#deleteroles) | **DELETE** /v1/roles | 删除角色|
|[**deleteUserClientRoles**](#deleteuserclientroles) | **DELETE** /v1/users/{uid}/clients/{cid}/roles | 删除用户的角色|
|[**deleteUserRoles**](#deleteuserroles) | **DELETE** /v1/users/{uid}/roles | 删除用户的角色|
|[**getRoles**](#getroles) | **GET** /v1/roles | 获取角色|
|[**getUserClientRoles**](#getuserclientroles) | **GET** /v1/users/{uid}/clients/{cid}/roles | 获取用户角色|
|[**getUserRoleClients**](#getuserroleclients) | **GET** /v1/users/{uid}/role-clients | 获取用户的角色应用|
|[**getUserRoles**](#getuserroles) | **GET** /v1/users/{uid}/roles | 获取用户角色|
|[**setRoles**](#setroles) | **PUT** /v1/roles | 修改或添加角色|
|[**setUserClientRoles**](#setuserclientroles) | **PUT** /v1/users/{uid}/clients/{cid}/roles | 为用户添加角色|
|[**setUserRoles**](#setuserroles) | **PUT** /v1/users/{uid}/roles | 为用户添加角色|

# **deleteRoles**
> deleteRoles()

应用和用户需要 WRITE_ROLE 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let id: Array<number>; // (default to undefined)
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.deleteRoles(
    id,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | **Array&lt;number&gt;** |  | defaults to undefined|
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

# **deleteUserClientRoles**
> deleteUserClientRoles()

应用和用户需要 GRANT_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let id: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteUserClientRoles(
    uid,
    cid,
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **id** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **deleteUserRoles**
> deleteUserRoles()

应用和用户需要 GRANT_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)
let id: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteUserRoles(
    uid,
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **id** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **getRoles**
> Array<Role> getRoles()


### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let id: Array<number>; // (optional) (default to undefined)
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getRoles(
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

**Array<Role>**

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

# **getUserClientRoles**
> Array<Role> getUserClientRoles()

应用需要 READ_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getUserClientRoles(
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

**Array<Role>**

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

# **getUserRoleClients**
> Array<UserRoleClient> getUserRoleClients()

应用需要 READ_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)
let managed: boolean; // (optional) (default to false)

const { status, data } = await apiInstance.getUserRoleClients(
    uid,
    managed
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **managed** | [**boolean**] |  | (optional) defaults to false|


### Return type

**Array<UserRoleClient>**

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

# **getUserRoles**
> Array<Role> getUserRoles()

应用和用户（uid 为当前用户除外）需要 READ_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)

const { status, data } = await apiInstance.getUserRoles(
    uid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|


### Return type

**Array<Role>**

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

# **setRoles**
> Array<Role> setRoles(role)

应用和用户需要 WRITE_ROLE 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let role: Array<Role>; //
let clientId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.setRoles(
    role,
    clientId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **role** | **Array<Role>**|  | |
| **clientId** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<Role>**

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

# **setUserClientRoles**
> setUserClientRoles(userRole)

应用和用户需要 GRANT_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let userRole: Array<UserRole>; //

const { status, data } = await apiInstance.setUserClientRoles(
    uid,
    cid,
    userRole
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userRole** | **Array<UserRole>**|  | |
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|


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

# **setUserRoles**
> setUserRoles(userRole)

应用和用户需要 GRANT_USER 权限。

### Example

```typescript
import {
    RolesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RolesApi(configuration);

let uid: number; // (default to undefined)
let userRole: Array<UserRole>; //

const { status, data } = await apiInstance.setUserRoles(
    uid,
    userRole
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userRole** | **Array<UserRole>**|  | |
| **uid** | [**number**] |  | defaults to undefined|


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

