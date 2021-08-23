// This is just an example,
// so you can safely delete all default props below

export default {
  "siteName": "Account",
  "gender": {
    "undefined": "null",
    "0": "Unknown",
    "1": "Male",
    "2": "Female",
    "3": "Other"
  },
  "error": "Error",
  "needPermission": "Insufficient Permissions",
  "done": "Done",
  "create": "Create",
  "createSuccess": "Created successfully",
  "delete": "Delete",
  "deleteSuccess": "Deleted successfully",
  "noResults": "No results.",
  "search": "Search",
  "noSearchResults": "No search results.",
  "update": "Update",
  "updateSuccess": "Updated successfully",
  "cancel": "Cancel",
  "name": "Name",
  "title": "Title",
  "description": "Description",
  "seconds": "Seconds",
  "day": "Day",
  "goBack": "Go Back",
  "Error404": {
    "notFound": "Oops. Nothing here..."
  },
  "AvatarButton": {
    "signIn": "Sign In",
    "signOut": "Sign Out",
    "settings": "Settings",
    "signOutSuccess": "Signed out successfully"
  },
  "RequireAuthorization": {
    "tips": "Please Sign In to View This Content",
    "signIn": "Sign In"
  },
  "SignIn": {
    "title": "Sign In",
    "signIn": "Sign In",
    "success": "Signed in successfully",
    "signUp": "Sign Up",
    "forgotPassword": "forgot password?",
    "account": "Account",
    "accountHint": "Your username or email",
    "password": "Password",
    "passwordHint": "Your password",
    "accountRule": "Please enter your username or E-mail, and empty characters are not allowed.",
    "passwordRule": "Please enter your password, which is between 6 and 20 in length."
  },
  "SignUp": {
    "title": "Sign Up",
    "continue": "Next",
    "back": "Back",
    "done": "Verify And Sign In",
    "signIn": "Sign In",
    "success": "Signed Up successfully",
    "step1": {
      "title": "Enter Information",
      "caption": "Enter your username, password and E-mail.",
      "username": "Username",
      "usernameHint": "Your username, cannot be changed after registration.",
      "usernameRule": "The user name consists of letters, numbers, '-', '_' and must begin with a letter and be between 6 and 20 lengths long.",
      "password": "Password",
      "passwordHint": "Your password.",
      "passwordRule": "The password is between 6 and 20 characters long.",
      "confirmPassword": "Confirm Password",
      "confirmPasswordHint": "Confirm the password.",
      "confirmPasswordRule": "The password entered twice is inconsistent.",
      "email": "Email Address",
      "emailHint": "Your email address, which is used for website login and receive verification codes.",
      "emailRule": "Please enter a valid email address.",
      "usernameExists": "The username is registered, please select a different username.",
      "emailExists": "This email address has been registered, please select a different email address or retrieve your password.",
      "phone": "Phone",
      "phoneHint": "Your mobile phone number, used to log in to the website and receive a verification code.",
      "phoneRule": "Please enter a valid mobile phone number with an international area code.",
      "phoneExists": "The mobile phone number has been registered, please select another mobile phone number or retrieve the password."
    },
    "step2": {
      "title": "Verify Email",
      "caption": "You need to verify your ownership of the email address before you sign up.",
      "code": "Code",
      "codeHint": "The email verification code you receive, the message may be blocked as spam, please check your email carefully.",
      "codeRule": "Please enter verification code, the message may be blocked as spam, please check your email carefully."
    }
  },
  "ResetPassword": {
    "title": " Retrieve Password",
    "continue": "Next",
    "back": "Back",
    "done": "Reset Password",
    "success": "Password reset successfully",
    "step1": {
      "title": "Enter your email address",
      "caption": "Please enter your account email address to receive the verification code.",
      "email": "Email",
      "emailHint": "Your email address, which is used to receive the verification code.",
      "emailRule": "Please enter a valid email address.",
      "emailNotExists": "The email is not bound to any accounts."
    },
    "step2": {
      "title": "Reset Password",
      "caption": "Set your new password.",
      "password": "New Password",
      "passwordHint": "Your new password to sign in to the website.",
      "passwordRule": "The password is between 6 and 20 characters long.",
      "confirmPassword": "Confirm Password",
      "confirmPasswordHint": "Confirm the password.",
      "confirmPasswordRule": "The password entered twice is inconsistent.",
      "code": "Code",
      "codeHint": "The email verification code you receive, the message may be blocked as spam, please check your email carefully.",
      "codeRule": "Please enter verification code, the message may be blocked as spam, please check your email carefully."
    }
  },
  "Authorize": {
    "require": "wants to access your account: ",
    "approved": "Approved",
    "redirect": "Authorizing will redirect to ",
    "approve": "Approve",
    "cancel": "Cancel",
    "developer": "Developer",
    "createdAt": "Created At",
    "count": "Users"
  },
  "PersonalInfo": {
    "title": "Personal Information",
    "subtitle": "Your personal information, such as nickname and avatar",

    "baseInfo": "Basic information",
    "baseInfoDesc": "Your avatar, nickname and password.",
    "avatar": "Avatar",
    "nickname": "Nickname",
    "gender": "Gender",
    "password": "Password",

    "contact": "Contact",
    "contactDesc": "Your contact details.",
    "email": "E-mail",
    "phone": "Phone",

    "other": "Other",
    "otherDesc": "Additional information.",
    "username": "Username",
    "roles": "Roles",
    "regtime": "Join At"
  },
  "Nickname": {
    "title": "Nickname",
    "operator": "Update Nickname",
    "nickname": "Nickname",
    "nicknameHint": "User's nickname, anyone can see this information.",
    "nicknameRule": "Please enter a valid nickname."
  },
  "Gender": {
    "title": "Gender",
    "operator": "Update Gender",
    "gender": "Gender"
  },
  "Password": {
    "title": "Password",
    "operator": "Update Password",
    "oldPassword": "The Original Password",
    "oldPasswordHint": "Your original password.",
    "newPassword": "The New Password",
    "newPasswordHint": "Your new password.",
    "confirmPassword": "Confirm New Password",
    "confirmPasswordHint": "Confirm your new password.",
    "passwordRule": "The password is between 6 and 20 characters long.",
    "confirmPasswordRule": "The password entered twice is inconsistent.",
    "passwordRuleOriginal": "The new password must not be the same as the original password."
  },
  "Avatar": {
    "title": "Avatar",
    "operator": "Update Avatar"
  },
  "Email": {
    "title": "Email Address",
    "operator": "Update Email Address",
    "step1": {
      "title": "Your Email Address",
      "caption": "Please enter your new email address."
    },
    "step2": {
      "title": "Your Password And Verification Code",
      "caption": "Please enter your account password and email verification code.",
      "titleWithoutPassword": "Your Verification Code",
      "captionWithoutPassword": "Please enter your email verification code."
    }
  },
  "Phone": {
    "title": "Phone",
    "operator": "Update Phone Number",
    "step1": {
      "title": "Your Phone Number",
      "caption": "Please enter your new phone number."
    },
    "step2": {
      "title": "Your Password And Verification Code",
      "titleWithoutPassword": "Your Verification Code",
      "caption": "Please enter your account password and SMS verification code.",
      "captionWithoutPassword": "Please enter your SMS verification code."
    }
  },
  "Clients": {
    "title": "Apps",
    "subtitle": "Manage OAuth2 clients, including app name, details, and more.",
    "yourClients": "Your Apps",
    "yourClientsDesc": "OAuth2 apps you created or managed.",
    "allClients": "All Clients",
    "allClientsDesc": "Apps that you and other users created."
  },
  "Client": {
    "title": "App Details",
    "createdAt": "created this app at",
    "clientId": "Client ID",
    "clientSecret": "Client Secret",
    "regenerateSecret": "Regenerate",
    "regenerateSecretMsg": "Are you sure to regenerate the client secret?",
    "clientLogo": "Application Logo",
    "upload": "Upload new logo",
    "clientName": "Name",
    "clientNameRule": "The app name cannot be empty and can be up to 64 characters long.",
    "clientDescription": "Description",
    "clientDescriptionRule": "The app description must not be empty and can be up to 256 characters long.",
    "clientRedirectUri": "Application Callback URLs",
    "clientScopes": "Authorization Scopes",
    "clientAuthorities": "Application Authorities",
    "clientGrantTypes": "Authorization Grant Types",
    "clientMembers": "Members",
    "accessTokenValidity": "Access Token Validity",
    "accessTokenValidityHint": "The validity of the access token, in seconds.",
    "refreshTokenValidity": "Refresh Token Validity",
    "refreshTokenValidityHint": "The validity of the refresh token, in seconds.",
    "AdvancedSettings": "Advanced Settings",
    "deleteClient": "Delete",
    "deleteClientTitle": "Delete Application",
    "deleteClientMsg": "Are you sure to delete this app?",
    "deleted": "Application Deleted"
  },
  "ClientAdvancedSettings": {
    "title": "Client Advanced Settings",
    "authorities": "Authorities",
    "authoritiesTips": "Application Custom Authorities",
    "roles": "Roles",
    "rolesTips": "Application Custom Roles"
  },
  "NewClient": {
    "title": "New OAuth Application",
    "ownerUid": "Application Owner",
    "ownerUidHint": "UID of application owner.",
    "accessTokenValidity": "Access Token Validity",
    "accessTokenValidityHint": "The validity of the access token, in seconds.",
    "refreshTokenValidity": "Refresh Token Validity",
    "refreshTokenValidityHint": "The validity of the refresh token, in seconds.",
    "selectUser": "Select User"
  },
  "Users": {
    "title": "Users",
    "subtitle": "Search for users to see their nicknames, genders, and more.",
    "userList": "Users",
    "userListDesc": "Search for all users of this site."
  },
  "User": {
    "title": "User Details",
    "uid": "UID",
    "username": "Username",
    "nickname": "Nickname",
    "gender": "Gender",
    "email": "E-mail",
    "phone": "Phone",
    "password": "Password",
    "roles": "Roles",

    "deleteUser": "Delete",
    "deleteUserTitle": "Delete User",
    "deleteUserMsg": "Are you sure to delete this user?",
    "deleted": "This user has been deleted",

    "banUser": "Ban",
    "unbanUser": "Unban",
    "banUserTitle": "Ban User",
    "banUserMsg": "Are you sure to ban this user?",
    "unbanUserTitle": "Unban User",
    "unbanUserMsg": "Are you sure to unban this user?",
    "userBanned": "This user has been banned"
  },
  "NewUser": {
    "title": "New User",
    "username": "Username",
    "usernameHint": "Please enter the username of the new user.",
    "password": "Password",
    "passwordHint": "Please enter the password of the new user.",
    "confirmPassword": "Confirm Password",
    "confirmPasswordHint": "Confirm the new user's password.",
    "email": "E-mail",
    "emailHint": "Please enter the new user's email address.",
    "phone": "Phone",
    "phoneHint": "Please enter the mobile phone number of the new user."
  },
  "SystemSettings": {
    "title": "System Settings",
    "subtitle": "Manage roles, authorities, authorization scopes, and so on.",
    "grantTypes": "Grant Types",
    "grantTypesDesc": "Manage the OAuth2 authorization grant types.",
    "deleteGrantType": "Delete Grant Type",
    "deleteGrantTypeMsg": "Are you sure to delete this authorization grant type?",
    "createGrantType": "Create Grant Type",
    "createGrantTypeMsg": "Please enter the name of the new authorization grant type.",
    "scopes": "Authorization Scopes",
    "scopesDesc": "Manage the OAuth2 authorization scopes.",
    "deleteScope": "Delete Scope",
    "deleteScopeMsg": "Are you sure to delete this authorization scope?",
    "createScope": "Create Scope",
    "createScopeMsg": "Please enter the name of the new authorization scope.",
    "authorities": "Authorities",
    "authoritiesDesc": "Management system authorities.",
    "deleteAuthority": "Delete Authority",
    "deleteAuthorityMsg": "Are you sure to delete this authority?",
    "createAuthority": "Create Authority",
    "createAuthorityMsg": "Please enter the name of the new authority.",
    "roles": "Roles",
    "rolesDesc": "Manage roles and authorities for roles.",
    "deleteRole": "Delete Role",
    "deleteRoleMsg": "Are you sure to delete this role?",
    "createRole": "Create Role",
    "createRoleMsg": "Please enter the name of the new role.",
    "grantRole": "Grant Role"
  },
  "UserClientRoles": {
    "expiredAt": "Expired At",
  },
  "UserRoleSettings": {
    "expiredAt": "Expired At",
    "expiredNull": "No Expiration",
    "expiredSimple": "Simple Setup",
    "expiredDetails": "Detailed Settings",
    "expiredNullDesc": "The role has no expiration time.",
  },
  "menus": {
    "home": "Home",
    "account": "Account",
    "clients": "Apps",
    "users": "Users",
    "systemSettings": "System Settings"
  },
  "errors": {
    // 0-10
    "-1": "No errors",
    "0": "Unknown error",
    "1": "Unauthorized",
    "2": "Access denied",
    "3": "Sign in failed",
    "4": "Register failed",
    "5": "Verify failed",
    "10": "OAuth2 Error",
    // 1000-1999
    "1000": "Input invalid",
    "1001": "Email invalid",
    "1002": "Username invalid",
    "1003": "Password invalid",
    "1004": "Phone invalid",
    "1005": "Code invalid",
    // 2000-2999
    "2000": "Resource not found",
    "2001": "Email not found",
    "2002": "User not found",
    "2003": "Client not found",
    // 3000-3999
    "3000": "Resource already exists",
    "3001": "Email already exists",
    "3002": "User already exists",
    "3003": "Client already exists",
    // 4000-4999
    "4000": "Fail to create resource",
    "4001": "Fail to create user",
    "4002": "Fail to create role",
    "4003": "Fail to create authority",
    "4004": "Fail to create scope",
    "4005": "Fail to create client",
    "4006": "Fail to create grant type",
    // 5000-5999
    "5000": "Fail to update resource",
    "5001": "Fail to update user",
    "5002": "Fail to update role",
    "5003": "Fail to update authority",
    "5004": "Fail to update scope",
    "5005": "Fail to update client",
    "5006": "Fail to update grant type",
    "5007": "Fail to update password",
    "5008": "Fail to update password",
    // 6000-6999
    "6000": "Fail to delete resource",
    "6001": "Fail to delete user",
    "6002": "Fail to delete role",
    "6003": "Fail to delete authority",
    "6004": "Fail to delete scope",
    "6005": "Fail to delete client",
    "6006": "Fail to delete grant type",
    "6007": "Fail to delete user's avatar",
    "6008": "Fail to delete user's token",
    "6009": "Fail to delete client's logo"
  }
};
