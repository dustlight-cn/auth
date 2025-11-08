# UserInfoApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getUserInfo**](#getuserinfo) | **GET** /v1/userinfo | 获取用户信息 (OpenID Connect UserInfo Endpoint)|
|[**getUserInfoPost**](#getuserinfopost) | **POST** /v1/userinfo | 获取用户信息 (POST)|

# **getUserInfo**
> { [key: string]: object; } getUserInfo()

返回已认证用户的标准 OpenID Connect 用户信息声明

### Example

```typescript
import {
    UserInfoApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserInfoApi(configuration);

const { status, data } = await apiInstance.getUserInfo();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**{ [key: string]: object; }**

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

# **getUserInfoPost**
> { [key: string]: object; } getUserInfoPost()

通过 POST 方法返回已认证用户的标准 OpenID Connect 用户信息声明

### Example

```typescript
import {
    UserInfoApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserInfoApi(configuration);

const { status, data } = await apiInstance.getUserInfoPost();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**{ [key: string]: object; }**

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

