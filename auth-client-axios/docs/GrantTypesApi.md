# GrantTypesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addClientGrantTypes**](#addclientgranttypes) | **PUT** /v1/clients/{cid}/types | 添加应用授权模式|
|[**addUserClientGrantTypes**](#adduserclientgranttypes) | **PUT** /v1/users/{uid}/clients/{cid}/types | 添加应用授权模式|
|[**deleteClientGrantTypes**](#deleteclientgranttypes) | **DELETE** /v1/clients/{cid}/types | 删除应用授权模式|
|[**deleteGrantTypes**](#deletegranttypes) | **DELETE** /v1/types | 删除授权模式|
|[**deleteUserClientGrantTypes**](#deleteuserclientgranttypes) | **DELETE** /v1/users/{uid}/clients/{cid}/types | 删除应用授权模式|
|[**getClientGrantTypes**](#getclientgranttypes) | **GET** /v1/clients/{cid}/types | 获取应用授权模式|
|[**getGrantTypes**](#getgranttypes) | **GET** /v1/types | 获取授权模式|
|[**getUserClientGrantTypes**](#getuserclientgranttypes) | **GET** /v1/users/{uid}/clients/{cid}/types | 获取应用授权模式|
|[**setGrantTypes**](#setgranttypes) | **PUT** /v1/types | 添加或修改授权模式|

# **addClientGrantTypes**
> addClientGrantTypes()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let cid: string; // (default to undefined)
let tid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.addClientGrantTypes(
    cid,
    tid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **tid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **addUserClientGrantTypes**
> addUserClientGrantTypes()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let tid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.addUserClientGrantTypes(
    uid,
    cid,
    tid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **tid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **deleteClientGrantTypes**
> deleteClientGrantTypes()

应用和用户需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let cid: string; // (default to undefined)
let tid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteClientGrantTypes(
    cid,
    tid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|
| **tid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **deleteGrantTypes**
> deleteGrantTypes()

应用和用户需要 WRITE_TYPE 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let tid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteGrantTypes(
    tid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **tid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **deleteUserClientGrantTypes**
> deleteUserClientGrantTypes()

应用和用户（uid 为当前用户除外）需要 WRITE_CLIENT 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)
let tid: Array<number>; // (default to undefined)

const { status, data } = await apiInstance.deleteUserClientGrantTypes(
    uid,
    cid,
    tid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **cid** | [**string**] |  | defaults to undefined|
| **tid** | **Array&lt;number&gt;** |  | defaults to undefined|


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

# **getClientGrantTypes**
> Array<GrantType> getClientGrantTypes()

应用和用户需要 READ_CLIENT 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getClientGrantTypes(
    cid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cid** | [**string**] |  | defaults to undefined|


### Return type

**Array<GrantType>**

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

# **getGrantTypes**
> Array<GrantType> getGrantTypes()


### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let tid: Array<number>; // (optional) (default to undefined)

const { status, data } = await apiInstance.getGrantTypes(
    tid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **tid** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|


### Return type

**Array<GrantType>**

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

# **getUserClientGrantTypes**
> Array<GrantType> getUserClientGrantTypes()

应用和用户（uid 为当前用户除外）需要 READ_CLIENT 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let uid: number; // (default to undefined)
let cid: string; // (default to undefined)

const { status, data } = await apiInstance.getUserClientGrantTypes(
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

**Array<GrantType>**

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

# **setGrantTypes**
> setGrantTypes(grantType)

应用和用户需要 WRITE_TYPE 权限。

### Example

```typescript
import {
    GrantTypesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GrantTypesApi(configuration);

let grantType: Array<GrantType>; //

const { status, data } = await apiInstance.setGrantTypes(
    grantType
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **grantType** | **Array<GrantType>**|  | |


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

