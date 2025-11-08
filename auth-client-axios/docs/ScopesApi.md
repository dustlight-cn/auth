# ScopesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addClientScopes**](#addclientscopes) | **PUT** /v1/clients/{cid}/scopes | 添加应用授权作用域|
|[**addUserClientScopes**](#adduserclientscopes) | **PUT** /v1/users/{uid}/clients/{cid}/scopes | 添加应用授权作用域|
|[**deleteScopes**](#deletescopes) | **DELETE** /v1/scopes | 删除授权作用域|
|[**getClientScopes**](#getclientscopes) | **GET** /v1/clients/{cid}/scopes | 获取应用授权作用域|
|[**getScopes**](#getscopes) | **GET** /v1/scopes | 获取授权作用域|
|[**getUserClientScopes**](#getuserclientscopes) | **GET** /v1/users/{uid}/clients/{cid}/scopes | 获取应用授权作用域|
|[**removeClientScopes**](#removeclientscopes) | **DELETE** /v1/clients/{cid}/scopes | 删除应用授权作用域|
|[**removeUserClientScopes**](#removeuserclientscopes) | **DELETE** /v1/users/{uid}/clients/{cid}/scopes | 删除应用授权作用域|
|[**setScopes**](#setscopes) | **PUT** /v1/scopes | 修改或添加授权作用域|

# **addClientScopes**
> addClientScopes()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let cid: string; // (default to undefined)
let sid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.addClientScopes(
    cid,
    sid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **sid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **addUserClientScopes**
> addUserClientScopes()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let sid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.addUserClientScopes(
    uid,
    cid,
    sid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **sid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **deleteScopes**
> deleteScopes()

应用和用户需要 WRITE_SCOPE 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let id: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteScopes(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
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

# **getClientScopes**
> Array<Scope> getClientScopes()

应用和用户需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getClientScopes(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**Array<Scope>**

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

# **getScopes**
> Array<Scope> getScopes()

应用和用户需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let id: Array<number>; // (optional) (default to undefined)

const { status, data } = await apiInstance.getScopes(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|


### Return type

**Array<Scope>**

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

# **getUserClientScopes**
> Array<Scope> getUserClientScopes()

应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getUserClientScopes(
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

**Array<Scope>**

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

# **removeClientScopes**
> removeClientScopes()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let cid: string; // (default to undefined)
let sid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.removeClientScopes(
    cid,
    sid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **sid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **removeUserClientScopes**
> removeUserClientScopes()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let sid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.removeUserClientScopes(
    uid,
    cid,
    sid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **sid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **setScopes**
> setScopes(scope)

应用和用户需要 WRITE_SCOPE 权限。

### Example

```typescript
import {
    ScopesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ScopesApi(configuration);

let scope: Array<Scope>; //

const { status, data } = await apiInstance.setScopes(
    scope
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **scope** | **Array<Scope>**|  | |


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

