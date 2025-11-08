# TokenApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**checkOAuthToken**](#checkoauthtoken) | **GET** /v1/token/validity | 检查令牌有效性|
|[**checkOAuthTokenPost**](#checkoauthtokenpost) | **POST** /v1/token/validity | 检查令牌有效性|
|[**deleteToken**](#deletetoken) | **DELETE** /v1/token | 删除令牌|
|[**getJwk**](#getjwk) | **GET** /v1/jwk | 获取 JWT 公钥（JWK）|
|[**getJws**](#getjws) | **GET** /v1/jws | 获取签名 JWT（JWS）|
|[**grantJws**](#grantjws) | **POST** /v1/jws | 颁发签名 JWT（JWS）|
|[**grantOAuthToken**](#grantoauthtoken) | **POST** /v1/oauth/token | 颁发 OAuth2 令牌|
|[**grantToken**](#granttoken) | **POST** /v1/token | 颁发默认令牌|

# **checkOAuthToken**
> { [key: string]: object; } checkOAuthToken()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

let token: string; // (default to undefined)

const { status, data } = await apiInstance.checkOAuthToken(
    token
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **token** | [**string**] |  | defaults to undefined|


### Return type

**{ [key: string]: object; }**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **checkOAuthTokenPost**
> { [key: string]: object; } checkOAuthTokenPost()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

let token: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.checkOAuthTokenPost(
    token
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **token** | [**string**] |  | (optional) defaults to undefined|


### Return type

**{ [key: string]: object; }**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/x-www-form-urlencoded
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteToken**
> deleteToken()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

const { status, data } = await apiInstance.deleteToken();
```

### Parameters
This endpoint does not have any parameters.


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

# **getJwk**
> object getJwk()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

const { status, data } = await apiInstance.getJwk();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/jwk-set+json; charset=UTF-8, application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getJws**
> OAuth2AccessToken getJws()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

const { status, data } = await apiInstance.getJws();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**OAuth2AccessToken**

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

# **grantJws**
> OAuth2AccessToken grantJws()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

let code: string; // (optional) (default to undefined)
let grantType: 'authorization_code' | 'refresh_token' | 'implicit' | 'client_credentials' | 'password'; // (optional) (default to 'authorization_code')
let redirectUri: string; // (optional) (default to undefined)
let username: string; // (optional) (default to undefined)
let password: string; // (optional) (default to undefined)
let refreshToken: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.grantJws(
    code,
    grantType,
    redirectUri,
    username,
    password,
    refreshToken
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | (optional) defaults to undefined|
| **grantType** | [**&#39;authorization_code&#39; | &#39;refresh_token&#39; | &#39;implicit&#39; | &#39;client_credentials&#39; | &#39;password&#39;**]**Array<&#39;authorization_code&#39; &#124; &#39;refresh_token&#39; &#124; &#39;implicit&#39; &#124; &#39;client_credentials&#39; &#124; &#39;password&#39;>** |  | (optional) defaults to 'authorization_code'|
| **redirectUri** | [**string**] |  | (optional) defaults to undefined|
| **username** | [**string**] |  | (optional) defaults to undefined|
| **password** | [**string**] |  | (optional) defaults to undefined|
| **refreshToken** | [**string**] |  | (optional) defaults to undefined|


### Return type

**OAuth2AccessToken**

### Authorization

[ClientCredentials](../README.md#ClientCredentials)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **grantOAuthToken**
> OAuth2AccessToken grantOAuthToken()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

let code: string; // (optional) (default to undefined)
let grantType: 'authorization_code' | 'refresh_token' | 'implicit' | 'client_credentials' | 'password'; // (optional) (default to 'authorization_code')
let redirectUri: string; // (optional) (default to undefined)
let username: string; // (optional) (default to undefined)
let password: string; // (optional) (default to undefined)
let refreshToken: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.grantOAuthToken(
    code,
    grantType,
    redirectUri,
    username,
    password,
    refreshToken
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | (optional) defaults to undefined|
| **grantType** | [**&#39;authorization_code&#39; | &#39;refresh_token&#39; | &#39;implicit&#39; | &#39;client_credentials&#39; | &#39;password&#39;**]**Array<&#39;authorization_code&#39; &#124; &#39;refresh_token&#39; &#124; &#39;implicit&#39; &#124; &#39;client_credentials&#39; &#124; &#39;password&#39;>** |  | (optional) defaults to 'authorization_code'|
| **redirectUri** | [**string**] |  | (optional) defaults to undefined|
| **username** | [**string**] |  | (optional) defaults to undefined|
| **password** | [**string**] |  | (optional) defaults to undefined|
| **refreshToken** | [**string**] |  | (optional) defaults to undefined|


### Return type

**OAuth2AccessToken**

### Authorization

[ClientCredentials](../README.md#ClientCredentials)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **grantToken**
> OAuth2AccessToken grantToken()


### Example

```typescript
import {
    TokenApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TokenApi(configuration);

let username: string; // (default to undefined)
let password: string; // (default to undefined)
let gRecaptchaResponse: string; // (default to undefined)

const { status, data } = await apiInstance.grantToken(
    username,
    password,
    gRecaptchaResponse
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **username** | [**string**] |  | defaults to undefined|
| **password** | [**string**] |  | defaults to undefined|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|


### Return type

**OAuth2AccessToken**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

