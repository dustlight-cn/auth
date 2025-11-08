# AuthorizationClient


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **string** |  | [optional] [default to undefined]
**description** | **string** |  | [optional] [default to undefined]
**logo** | **string** |  | [optional] [default to undefined]
**additionalInformation** | **{ [key: string]: object; }** |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]
**status** | **number** |  | [optional] [default to undefined]
**uid** | **number** |  | [optional] [default to undefined]
**resources** | [**Array&lt;Resource&gt;**](Resource.md) |  | [optional] [default to undefined]
**members** | **Array&lt;string&gt;** |  | [optional] [default to undefined]
**scopes** | [**Array&lt;AuthorizationClientScope&gt;**](AuthorizationClientScope.md) |  | [optional] [default to undefined]
**types** | [**Array&lt;GrantType&gt;**](GrantType.md) |  | [optional] [default to undefined]
**authorities** | **Array&lt;string&gt;** |  | [optional] [default to undefined]
**clientSecret** | **string** |  | [optional] [default to undefined]
**refreshTokenValiditySeconds** | **number** |  | [optional] [default to undefined]
**accessTokenValiditySeconds** | **number** |  | [optional] [default to undefined]
**registeredRedirectUri** | **Set&lt;string&gt;** |  | [optional] [default to undefined]
**cid** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { AuthorizationClient } from './api';

const instance: AuthorizationClient = {
    name,
    description,
    logo,
    additionalInformation,
    createdAt,
    updatedAt,
    status,
    uid,
    resources,
    members,
    scopes,
    types,
    authorities,
    clientSecret,
    refreshTokenValiditySeconds,
    accessTokenValiditySeconds,
    registeredRedirectUri,
    cid,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
