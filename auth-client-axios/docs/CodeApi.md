# CodeApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createRegistrationCode**](#createregistrationcode) | **POST** /v1/code/registration | 获取注册邮箱验证码|
|[**createRegistrationEmailCode**](#createregistrationemailcode) | **POST** /v1/code/registration/email | 获取注册邮箱验证码|
|[**createRegistrationPhoneCode**](#createregistrationphonecode) | **POST** /v1/code/registration/phone | 获取注册手机验证码|
|[**createUpdateEmailCode**](#createupdateemailcode) | **POST** /v1/code/email | 获取更换邮箱验证码|
|[**createUpdatePasswordEmailCode**](#createupdatepasswordemailcode) | **POST** /v1/code/password/email | 获取重置密码邮箱验证码|
|[**createUpdatePasswordPhoneCode**](#createupdatepasswordphonecode) | **POST** /v1/code/password/phone | 获取重置密码手机验证码|
|[**createUpdatePhoneCode**](#createupdatephonecode) | **POST** /v1/code/phone | 获取更换手机号码验证码|

# **createRegistrationCode**
> createRegistrationCode()

发送验证码到邮箱，用于注册。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let email: string; // (default to undefined)

const { status, data } = await apiInstance.createRegistrationCode(
    gRecaptchaResponse,
    email
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **email** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createRegistrationEmailCode**
> createRegistrationEmailCode()

发送验证码到邮箱，用于注册。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let email: string; // (default to undefined)

const { status, data } = await apiInstance.createRegistrationEmailCode(
    gRecaptchaResponse,
    email
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **email** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createRegistrationPhoneCode**
> createRegistrationPhoneCode()

发送验证码到手机，用于注册。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let phone: string; // (default to undefined)

const { status, data } = await apiInstance.createRegistrationPhoneCode(
    gRecaptchaResponse,
    phone
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **phone** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createUpdateEmailCode**
> createUpdateEmailCode()

发送验证码到邮箱，用于更改邮箱。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let email: string; // (default to undefined)

const { status, data } = await apiInstance.createUpdateEmailCode(
    gRecaptchaResponse,
    email
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **email** | [**string**] |  | defaults to undefined|


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

# **createUpdatePasswordEmailCode**
> createUpdatePasswordEmailCode()

发送验证码到邮箱，用于更改密码。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let email: string; // (default to undefined)

const { status, data } = await apiInstance.createUpdatePasswordEmailCode(
    gRecaptchaResponse,
    email
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **email** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createUpdatePasswordPhoneCode**
> createUpdatePasswordPhoneCode()

发送验证码到手机，用于更改密码。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let phone: string; // (default to undefined)

const { status, data } = await apiInstance.createUpdatePasswordPhoneCode(
    gRecaptchaResponse,
    phone
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **phone** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createUpdatePhoneCode**
> createUpdatePhoneCode()

发送验证码到手机，用于更改手机号码。

### Example

```typescript
import {
    CodeApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CodeApi(configuration);

let gRecaptchaResponse: string; // (default to undefined)
let phone: string; // (default to undefined)

const { status, data } = await apiInstance.createUpdatePhoneCode(
    gRecaptchaResponse,
    phone
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gRecaptchaResponse** | [**string**] |  | defaults to undefined|
| **phone** | [**string**] |  | defaults to undefined|


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

