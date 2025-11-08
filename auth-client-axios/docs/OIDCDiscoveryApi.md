# OIDCDiscoveryApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getConfiguration**](#getconfiguration) | **GET** /.well-known/openid-configuration | OpenID Connect Discovery|

# **getConfiguration**
> { [key: string]: object; } getConfiguration()

返回 OpenID Provider 配置元数据

### Example

```typescript
import {
    OIDCDiscoveryApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new OIDCDiscoveryApi(configuration);

const { status, data } = await apiInstance.getConfiguration();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**{ [key: string]: object; }**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

