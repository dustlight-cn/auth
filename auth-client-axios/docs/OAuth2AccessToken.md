# OAuth2AccessToken


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**value** | **string** |  | [optional] [default to undefined]
**expiration** | **string** |  | [optional] [default to undefined]
**expired** | **boolean** |  | [optional] [default to undefined]
**tokenType** | **string** |  | [optional] [default to undefined]
**refreshToken** | [**OAuth2RefreshToken**](OAuth2RefreshToken.md) |  | [optional] [default to undefined]
**additionalInformation** | **{ [key: string]: object; }** |  | [optional] [default to undefined]
**scope** | **Set&lt;string&gt;** |  | [optional] [default to undefined]
**expiresIn** | **number** |  | [optional] [default to undefined]

## Example

```typescript
import { OAuth2AccessToken } from './api';

const instance: OAuth2AccessToken = {
    value,
    expiration,
    expired,
    tokenType,
    refreshToken,
    additionalInformation,
    scope,
    expiresIn,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
