# AuthorizationApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createAuthorization**](#createauthorization) | **POST** /v1/oauth/authorization | 应用授权|
|[**getAuthorization**](#getauthorization) | **GET** /v1/oauth/authorization | 获取应用授权|

# **createAuthorization**
> AuthorizationResponse createAuthorization()

应用需要 AUTHORIZE 权限。

### Example

```typescript
import {
    AuthorizationApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationApi(configuration);

let approved: boolean; // (default to undefined)
let scope: Set<string>; // (optional) (default to undefined)
let jwt: boolean; // (optional) (default to undefined)

const { status, data } = await apiInstance.createAuthorization(
    approved,
    scope,
    jwt
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **approved** | [**boolean**] |  | defaults to undefined|
| **scope** | **Set&lt;string&gt;** |  | (optional) defaults to undefined|
| **jwt** | [**boolean**] |  | (optional) defaults to undefined|


### Return type

**AuthorizationResponse**

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

# **getAuthorization**
> AuthorizationResponse getAuthorization()

获取包含应用信息、所属用户信息、回调地址以及是否已授权。应用需要 AUTHORIZE 权限。

### Example

```typescript
import {
    AuthorizationApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationApi(configuration);

let clientId: string; // (default to undefined)
let responseType: 'code' | 'token'; // (optional) (default to 'code')
let redirectUri: string; // (optional) (default to undefined)
let scope: Array<string>; // (optional) (default to undefined)
let state: string; // (optional) (default to undefined)
let jwt: boolean; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAuthorization(
    clientId,
    responseType,
    redirectUri,
    scope,
    state,
    jwt
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **clientId** | [**string**] |  | defaults to undefined|
| **responseType** | [**&#39;code&#39; | &#39;token&#39;**]**Array<&#39;code&#39; &#124; &#39;token&#39;>** |  | (optional) defaults to 'code'|
| **redirectUri** | [**string**] |  | (optional) defaults to undefined|
| **scope** | **Array&lt;string&gt;** |  | (optional) defaults to undefined|
| **state** | [**string**] |  | (optional) defaults to undefined|
| **jwt** | [**boolean**] |  | (optional) defaults to undefined|


### Return type

**AuthorizationResponse**

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

